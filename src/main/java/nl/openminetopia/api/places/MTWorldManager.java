package nl.openminetopia.api.places;

import com.craftmend.storm.api.enums.Where;
import lombok.Getter;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.places.objects.MTWorld;
import nl.openminetopia.modules.data.DataModule;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.WorldModel;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Getter
public class MTWorldManager {
    private static MTWorldManager instance;

    public static MTWorldManager getInstance() {
        if (instance == null) {
            instance = new MTWorldManager();
        }
        return instance;
    }

    private final DataModule dataModule = OpenMinetopia.getModuleManager().getModule(DataModule.class);

    public List<MTWorld> worlds = new ArrayList<>();

    public CompletableFuture<List<MTWorld>> loadWorlds() {
        CompletableFuture<List<MTWorld>> completableFuture = new CompletableFuture<>();

        StormDatabase.getExecutorService().submit(() -> {
            try {
                StormDatabase.getInstance().getStorm().buildQuery(WorldModel.class)
                        .execute()
                        .whenComplete((worldModels, throwable) -> {
                            if (throwable != null) {
                                throwable.printStackTrace();
                                return;
                            }

                            List<MTWorld> worlds = new ArrayList<>();
                            for (WorldModel model : worldModels) {
                                MTWorld world = new MTWorld(model.getWorldName(), model.getTitle(), model.getColor(), model.getTemperature(), model.getLoadingName());
                                worlds.add(world);
                            }

                            worlds.forEach(world -> {
                                if (this.worlds.contains(world)) return;
                                this.worlds.add(world);
                            });
                            completableFuture.complete(worlds);
                        }
                );
            } catch (Exception exception) {
                exception.printStackTrace();
                completableFuture.completeExceptionally(exception);
            }
        });

        return completableFuture;
    }

    public void setTitle(MTWorld world, String title) {
        worlds.forEach(mtWorld -> {
            if (!mtWorld.getName().equals(world.getName())) return;
            mtWorld.setTitle(title);
        });
        dataModule.getAdapter().setTitle(world, title);
    }

    public void setTemperature(MTWorld world, double temperature) {
        worlds.forEach(mtWorld -> {
            if (!mtWorld.getName().equals(world.getName())) return;
            mtWorld.setTemperature(temperature);
        });
        dataModule.getAdapter().setTemperature(world, temperature);
    }

    public void setLoadingName(MTWorld world, String loadingName) {
        worlds.forEach(mtWorld -> {
            if (!mtWorld.getName().equals(world.getName())) return;
            mtWorld.setLoadingName(loadingName);
        });
        dataModule.getAdapter().setLoadingName(world, loadingName);
    }

    public void setColor(MTWorld world, String color) {
        worlds.forEach(mtWorld -> {
            if (!mtWorld.getName().equals(world.getName())) return;
            mtWorld.setColor(color);
        });
        dataModule.getAdapter().setColor(world, color);
    }

    public void createWorld(MTWorld world) {
        dataModule.getAdapter().createWorld(world);
        worlds.add(world);
    }

    public void deleteWorld(MTWorld world) {
        dataModule.getAdapter().deleteWorld(world);
        worlds.remove(world);
    }

    public MTWorld getWorld(String worldName) {
        for (MTWorld world : worlds) {
            if (!world.getName().equals(worldName)) continue;
            return world;
        }
        return null;
    }

    public MTWorld getWorld(Location location) {
        for (MTWorld world : worlds) {
            if (!world.getName().equals(location.getWorld().getName())) continue;
            return world;
        }
        return null;
    }
}
