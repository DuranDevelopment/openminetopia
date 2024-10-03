package nl.openminetopia.api.player.objects;

import lombok.Getter;
import lombok.Setter;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.places.MTPlaceManager;
import nl.openminetopia.api.places.MTWorldManager;
import nl.openminetopia.api.places.objects.MTPlace;
import nl.openminetopia.api.places.objects.MTWorld;
import nl.openminetopia.api.player.fitness.FitnessManager;
import nl.openminetopia.api.player.fitness.objects.Fitness;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.modules.color.enums.OwnableColorType;
import nl.openminetopia.modules.color.objects.*;
import nl.openminetopia.modules.data.DataModule;
import nl.openminetopia.modules.data.storm.models.PlayerModel;
import nl.openminetopia.modules.fitness.runnables.HealthStatisticRunnable;
import nl.openminetopia.modules.player.runnables.LevelcheckRunnable;
import nl.openminetopia.modules.player.runnables.PlaytimeRunnable;
import nl.openminetopia.modules.prefix.objects.Prefix;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
public class OnlineMinetopiaPlayer implements MinetopiaPlayer {

    private final UUID uuid;
    private final PlayerModel playerModel;

    private @Setter boolean scoreboardVisible;

    private int playtime;
    private PlaytimeRunnable playtimeRunnable;

    private HealthStatisticRunnable healthStatisticRunnable;

    private int level;
    private @Setter int calculatedLevel;
    private LevelcheckRunnable levelcheckRunnable;

    private boolean staffchatEnabled;
    private boolean commandSpyEnabled;
    private boolean chatSpyEnabled;

    private List<Prefix> prefixes;
    private Prefix activePrefix;

    private List<OwnableColor> colors;
    private PrefixColor activePrefixColor;
    private NameColor activeNameColor;
    private ChatColor activeChatColor;
    private LevelColor activeLevelColor;

    private @Setter Fitness fitness;

    private final DefaultConfiguration configuration = OpenMinetopia.getDefaultConfiguration();
    private final DataModule dataModule = OpenMinetopia.getModuleManager().getModule(DataModule.class);

    public OnlineMinetopiaPlayer(UUID uuid, PlayerModel playerModel) {
        this.uuid = uuid;
        this.playerModel = playerModel;
    }

    public CompletableFuture<Void> load() {
        CompletableFuture<Void> loadFuture = new CompletableFuture<>();

        getBukkit().sendMessage(ChatUtils.color("<red>Je data wordt geladen..."));

        this.fitness = FitnessManager.getInstance().getFitness(uuid);
        fitness.load().thenAccept((unused) -> {
            fitness.getRunnable().runTaskTimer(OpenMinetopia.getInstance(), 0, 60 * 20L);
            fitness.apply();
        });

        dataModule.getAdapter().getStaffchatEnabled(this).whenComplete((staffchatEnabled, throwable) -> {
            if (staffchatEnabled == null) {
                this.staffchatEnabled = false;
                return;
            }
            this.staffchatEnabled = staffchatEnabled;
        });
  
        dataModule.getAdapter().getCommandSpyEnabled(this).whenComplete((spy, throwable) -> {
            if (spy == null) {
                this.commandSpyEnabled = false;
                return;
            }

            this.commandSpyEnabled = spy;
        });

        dataModule.getAdapter().getChatSpyEnabled(this).whenComplete((spy, throwable) -> {
            if (spy == null) {
                this.chatSpyEnabled = false;
                return;
            }

            this.chatSpyEnabled = spy;
        });

        dataModule.getAdapter().getPrefixes(this).whenComplete((prefixes, throwable) -> {
            if (prefixes == null) {
                this.prefixes = new ArrayList<>();
                return;
            }
            this.prefixes = prefixes;
        });

        dataModule.getAdapter().getColors(this).whenComplete((colors, throwable) -> {
            if (colors == null) {
                this.colors = new ArrayList<>();
                return;
            }
            this.colors = colors;
        });

        this.calculatedLevel = configuration.getDefaultLevel();
        dataModule.getAdapter().getLevel(this).whenComplete((level, throwable) -> {
            if (level == null) {
                this.level = configuration.getDefaultLevel();
                return;
            }
            this.level = level;
        });

        dataModule.getAdapter().getActivePrefix(this).whenComplete((prefix, throwable) -> {
            if (prefix == null) {
                this.activePrefix = new Prefix(-1, configuration.getDefaultPrefix(), -1);
                return;
            }
            this.activePrefix = prefix;
        });

        dataModule.getAdapter().getActiveColor(this, OwnableColorType.NAME).whenComplete((color, throwable) -> {
            if (color == null) {
                this.activeNameColor = (NameColor) getDefaultColor(OwnableColorType.NAME);
                return;
            }
            this.activeNameColor = (NameColor) color;
        });
        dataModule.getAdapter().getActiveColor(this, OwnableColorType.NAME).whenComplete((color, throwable) -> {
            if (color == null) {
                this.activeChatColor = (ChatColor) getDefaultColor(OwnableColorType.CHAT);
                return;
            }
            this.activeChatColor = (ChatColor) color;
        });
        dataModule.getAdapter().getActiveColor(this, OwnableColorType.PREFIX).whenComplete((color, throwable) -> {
            if (color == null) {
                this.activePrefixColor = (PrefixColor) getDefaultColor(OwnableColorType.PREFIX);
                return;
            }
            this.activePrefixColor = (PrefixColor) color;
        });
        dataModule.getAdapter().getActiveColor(this, OwnableColorType.LEVEL).whenComplete((color, throwable) -> {
            if (color == null) {
                this.activeLevelColor = (LevelColor) getDefaultColor(OwnableColorType.LEVEL);
                return;
            }
            this.activeLevelColor = (LevelColor) color;
        });

        dataModule.getAdapter().getPlaytime(this).whenComplete((playtime, throwable) -> {
            if (playtime == null) {
                this.playtime = 0;
                return;
            }
            this.playtime = playtime;
        });

        this.playtimeRunnable = new PlaytimeRunnable(getBukkit());
        playtimeRunnable.runTaskTimer(OpenMinetopia.getInstance(), 0, 20L);

        this.levelcheckRunnable = new LevelcheckRunnable(this);
        levelcheckRunnable.runTaskTimer(OpenMinetopia.getInstance(), 0, 20L * 30);

        this.healthStatisticRunnable = new HealthStatisticRunnable(this);
        healthStatisticRunnable.runTaskTimer(OpenMinetopia.getInstance(), 0, 20L);

        loadFuture.complete(null);
        return loadFuture;
    }

    public CompletableFuture<Void> save() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        dataModule.getAdapter().savePlayer(this).whenComplete((unused, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
            }
        });
        fitness.save().whenComplete((unused, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
            }
        });

        future.complete(null);
        return future;
    }

    @Override
    public Player getBukkit() {
        return Bukkit.getPlayer(uuid);
    }

    /* Playtime */

    /**
     * Sets the playtime in seconds
     *
     * @param seconds        The amount of seconds
     * @param updateDatabase If true, the playtime will be pushed to the database, otherwise it will only be set in the object
     *                       This should be set to false by default, and only set to true when the player logs out to prevent unnecessary database calls
     */
    @Override
    public void setPlaytime(int seconds, boolean updateDatabase) {
        this.playtime = seconds;
        if (updateDatabase) dataModule.getAdapter().setPlaytime(this, seconds);
    }

    /* Places */
    public boolean isInPlace() {
        return getPlace() != null;
    }

    public MTPlace getPlace() {
        return MTPlaceManager.getInstance().getPlace(getBukkit().getLocation());
    }

    public MTWorld getWorld() {
        return MTWorldManager.getInstance().getWorld(getBukkit().getLocation());
    }

    /* Level */

    @Override
    public void setLevel(int level) {
        this.level = level;
        dataModule.getAdapter().setLevel(this, level);
    }

    /* Staffchat */

    public void setStaffchatEnabled(boolean staffchatEnabled) {
        this.staffchatEnabled = staffchatEnabled;
        dataModule.getAdapter().setStaffchatEnabled(this, staffchatEnabled);
    }

    /* Spy */

    public void setCommandSpyEnabled(boolean commandSpyEnabled) {
        this.commandSpyEnabled = commandSpyEnabled;
        dataModule.getAdapter().setCommandSpyEnabled(this, commandSpyEnabled);
    }

    public void setChatSpyEnabled(boolean chatSpyEnabled) {
        this.chatSpyEnabled = chatSpyEnabled;
        dataModule.getAdapter().setChatSpyEnabled(this, chatSpyEnabled);
    }

    /* Prefix */

    @Override
    public void addPrefix(Prefix prefix) {
        dataModule.getAdapter().addPrefix(this, prefix).whenComplete((id, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }
            prefixes.add(new Prefix(id, prefix.getPrefix(), prefix.getExpiresAt()));
        });
    }

    @Override
    public void removePrefix(Prefix prefix) {
        prefixes.remove(prefix);

        if (activePrefix == prefix) {
            activePrefix = new Prefix(-1, configuration.getDefaultPrefix(), -1);
            setActivePrefix(activePrefix);
        }

        dataModule.getAdapter().removePrefix(this, prefix);
    }

    @Override
    public void setActivePrefix(Prefix prefix) {
        this.activePrefix = prefix;
        dataModule.getAdapter().setActivePrefix(this, prefix);
    }

    @Override
    public Prefix getActivePrefix() {
        if (activePrefix == null) {
            activePrefix = new Prefix(-1, configuration.getDefaultPrefix(), -1);
        }

        if (activePrefix.isExpired()) {
            getBukkit().sendMessage(ChatUtils.color("<red>Je prefix <dark_red>" + activePrefix.getPrefix() + " <red>is verlopen!"));
            removePrefix(activePrefix);
            setActivePrefix(new Prefix(-1, configuration.getDefaultPrefix(), -1));
        }

        return activePrefix;
    }

    /* Colors */
    @Override
    public void addColor(OwnableColor color) {
        dataModule.getAdapter().addColor(this, color).whenComplete((id, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }

            switch (color.getType()) {
                case PREFIX -> colors.add(new PrefixColor(id, color.getColorId(), color.getExpiresAt()));
                case NAME -> colors.add(new NameColor(id, color.getColorId(), color.getExpiresAt()));
                case CHAT -> colors.add(new ChatColor(id, color.getColorId(), color.getExpiresAt()));
                case LEVEL -> colors.add(new LevelColor(id, color.getColorId(), color.getExpiresAt()));
            }
        });
    }

    @Override
    public void removeColor(OwnableColor color) {
        colors.remove(color);
        dataModule.getAdapter().removeColor(this, color);
    }

    @Override
    public void setActiveColor(OwnableColor color, OwnableColorType type) {
        switch (type) {
            case PREFIX:
                this.activePrefixColor = (PrefixColor) color;
                break;
            case NAME:
                this.activeNameColor = (NameColor) color;
                break;
            case CHAT:
                this.activeChatColor = (ChatColor) color;
                break;
            case LEVEL:
                this.activeLevelColor = (LevelColor) color;
                break;
        }
        dataModule.getAdapter().setActiveColor(this, color, type);
    }

    @Override
    public OwnableColor getActiveColor(OwnableColorType type) {
        OwnableColor color = switch (type) {
            case PREFIX -> activePrefixColor;
            case NAME -> activeNameColor;
            case CHAT -> activeChatColor;
            case LEVEL -> activeLevelColor;
        };

        if (color == null || color.getId() == 0) {
            color = getDefaultColor(type);
        }

        if (color.isExpired()) {
            getBukkit().sendMessage(ChatUtils.color("<red>Je " + type.name().toLowerCase() + " kleur <dark_red>" + color.getColorId() + " is verlopen!"));
            removeColor(color);
            setActiveColor(getDefaultColor(type), type);
        }
        return color;
    }

    private OwnableColor getDefaultColor(OwnableColorType type) {
        return switch (type) {
            case PREFIX -> new PrefixColor(-1, configuration.getDefaultPrefixColor(), -1);
            case NAME -> new NameColor(-1, configuration.getDefaultNameColor(), -1);
            case CHAT -> new ChatColor(-1, configuration.getDefaultChatColor(), -1);
            case LEVEL -> new LevelColor(-1, configuration.getDefaultLevelColor(), -1);
        };
    }
}