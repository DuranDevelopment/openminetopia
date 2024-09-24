package nl.openminetopia.api.places;

import com.craftmend.storm.api.enums.Where;
import lombok.Getter;
import nl.openminetopia.api.places.objects.MTWorld;
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

        getWorldModel(world).whenComplete((model, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }
            StormDatabase.getExecutorService().submit(() -> {
                try {
                    model.setTitle(title);
                    StormDatabase.getInstance().saveStormModel(model);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });
        });
    }

    public void setTemperature(MTWorld world, double temperature) {
        worlds.forEach(mtWorld -> {
            if (!mtWorld.getName().equals(world.getName())) return;
            mtWorld.setTemperature(temperature);
        });

        getWorldModel(world).whenComplete((model, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }
            StormDatabase.getExecutorService().submit(() -> {
                try {
                    model.setTemperature(temperature);
                    StormDatabase.getInstance().saveStormModel(model);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });
        });
    }

    public void setLoadingName(MTWorld world, String loadingName) {
        worlds.forEach(mtWorld -> {
            if (!mtWorld.getName().equals(world.getName())) return;
            mtWorld.setLoadingName(loadingName);
        });

        getWorldModel(world).whenComplete((model, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }
            StormDatabase.getExecutorService().submit(() -> {
                try {
                    model.setLoadingName(loadingName);
                    StormDatabase.getInstance().saveStormModel(model);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });
        });
    }

    public void setColor(MTWorld world, String color) {
        worlds.forEach(mtWorld -> {
            if (!mtWorld.getName().equals(world.getName())) return;
            mtWorld.setColor(color);
        });

        getWorldModel(world).whenComplete((model, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }
            StormDatabase.getExecutorService().submit(() -> {
                try {
                    model.setColor(color);
                    StormDatabase.getInstance().saveStormModel(model);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });
        });
    }

    public void createWorld(MTWorld world) {
        StormDatabase.getExecutorService().submit(() -> {
            try {
                WorldModel worldModel = new WorldModel();
                worldModel.setWorldName(world.getName());
                worldModel.setTitle(world.getTitle());
                worldModel.setColor(world.getColor());
                worldModel.setTemperature(world.getTemperature());
                worldModel.setLoadingName(world.getLoadingName());

                StormDatabase.getInstance().saveStormModel(worldModel);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        worlds.add(world);
    }

    public void deleteWorld(MTWorld world) {
        getWorldModel(world).whenComplete((model, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }
            StormDatabase.getExecutorService().submit(() -> {
                try {
                    StormDatabase.getInstance().getStorm().delete(model);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });
        });
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

    public CompletableFuture<WorldModel> getWorldModel(MTWorld world) {
        CompletableFuture<WorldModel> completableFuture = new CompletableFuture<>();

        StormDatabase.getExecutorService().submit(() -> {
            try {
                WorldModel model = StormDatabase.getInstance().getStorm().buildQuery(WorldModel.class)
                        .where("world_name", Where.EQUAL, world.getName())
                        .execute()
                        .join()
                        .stream()
                        .findFirst()
                        .orElse(null);

                completableFuture.complete(model);
            } catch (Exception exception) {
                exception.printStackTrace();
                completableFuture.completeExceptionally(exception);
            }
        });
        return completableFuture;
    }
}
