package nl.openminetopia.api.player.objects;

import lombok.Getter;
import lombok.Setter;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.places.objects.MTPlace;
import nl.openminetopia.api.places.objects.MTWorld;
import nl.openminetopia.api.player.fitness.FitnessManager;
import nl.openminetopia.api.player.fitness.objects.Fitness;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.modules.color.enums.OwnableColorType;
import nl.openminetopia.modules.color.objects.*;
import nl.openminetopia.modules.data.DataModule;
import nl.openminetopia.modules.data.storm.models.PlayerModel;
import nl.openminetopia.modules.prefix.objects.Prefix;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

@Getter
public class OfflineMinetopiaPlayer implements MinetopiaPlayer {
    private final UUID uuid;

    private @Setter Fitness fitness;

    private int playtime;

    private int level;
    private @Setter int calculatedLevel;

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

    private final DataModule dataModule = OpenMinetopia.getModuleManager().getModule(DataModule.class);

    public OfflineMinetopiaPlayer(UUID uuid) {
        this.uuid = uuid;
        this.fitness = FitnessManager.getInstance().getFitness(uuid);
        fitness.load().whenComplete((unused, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }
        });

        dataModule.getAdapter().getPlaytime(this).whenComplete((playtime, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }
            this.playtime = playtime;
        });

        dataModule.getAdapter().getLevel(this).whenComplete((level, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }
            this.level = level;
        });

        dataModule.getAdapter().getPrefixes(this).whenComplete((prefixes, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }
            this.prefixes = prefixes;
        });

        dataModule.getAdapter().getActivePrefix(this).whenComplete((activePrefix, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }
            this.activePrefix = activePrefix;
        });

        dataModule.getAdapter().getColors(this).whenComplete((colors, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }
            this.colors = colors;
        });

        dataModule.getAdapter().getActiveColor(this, OwnableColorType.PREFIX).whenComplete((activePrefixColor, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }
            this.activePrefixColor = (PrefixColor) activePrefixColor;
        });

        dataModule.getAdapter().getActiveColor(this, OwnableColorType.NAME).whenComplete((activeNameColor, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }
            this.activeNameColor = (NameColor) activeNameColor;
        });

        dataModule.getAdapter().getActiveColor(this, OwnableColorType.CHAT).whenComplete((activeChatColor, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }
            this.activeChatColor = (ChatColor) activeChatColor;
        });

        dataModule.getAdapter().getActiveColor(this, OwnableColorType.LEVEL).whenComplete((activeLevelColor, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }
            this.activeLevelColor = (LevelColor) activeLevelColor;
        });
    }

    @Override
    public PlayerModel getPlayerModel() {
        return null;
    }

    @Override
    public OfflinePlayer getBukkit() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    @Override
    public void setPlaytime(int playtime, boolean updateDatabase) {
        this.playtime = playtime;
        dataModule.getAdapter().setPlaytime(this, playtime);
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
            activePrefix = new Prefix(-1, getConfig().getDefaultPrefix(), -1);
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
            activePrefix = new Prefix(-1, getConfig().getDefaultPrefix(), -1);
        }

        if (activePrefix.isExpired()) {
            removePrefix(activePrefix);
            setActivePrefix(new Prefix(-1, getConfig().getDefaultPrefix(), -1));
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
                case PREFIX -> colors.add(new PrefixColor(id, color.getColor(), color.getExpiresAt()));
                case NAME -> colors.add(new NameColor(id, color.getColor(), color.getExpiresAt()));
                case CHAT -> colors.add(new ChatColor(id, color.getColor(), color.getExpiresAt()));
                case LEVEL -> colors.add(new LevelColor(id, color.getColor(), color.getExpiresAt()));
            }
        });
    }

    @Override
    public void removeColor(OwnableColor color) {
        colors.remove(color);
        dataModule.getAdapter().removeColor(this, color);
    }

    @Override
    public boolean isInPlace() {
        return false;
    }

    @Override
    public MTPlace getPlace() {
        return null;
    }

    @Override
    public MTWorld getWorld() {
        return null;
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
            removeColor(color);
            setActiveColor(getDefaultColor(type), type);
        }
        return color;
    }

    private OwnableColor getDefaultColor(OwnableColorType type) {
        return switch (type) {
            case PREFIX -> new PrefixColor(-1, getConfig().getDefaultPrefixColor(), -1);
            case NAME -> new NameColor(-1, getConfig().getDefaultNameColor(), -1);
            case CHAT -> new ChatColor(-1, getConfig().getDefaultChatColor(), -1);
            case LEVEL -> new LevelColor(-1, getConfig().getDefaultLevelColor(), -1);
        };
    }
    
    private DefaultConfiguration getConfig() {
        return OpenMinetopia.getDefaultConfiguration();
    }
}
