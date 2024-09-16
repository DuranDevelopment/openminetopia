package nl.openminetopia.api.player;

import com.craftmend.storm.api.enums.Where;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.FitnessModel;

import java.util.concurrent.CompletableFuture;

public class FitnessManager {

    private static FitnessManager instance;

    public static FitnessManager getInstance() {
        if (instance == null) {
            instance = new FitnessManager();
        }
        return instance;
    }

    public void setFitness(MinetopiaPlayer player, int amount) {
        StormDatabase.getExecutorService().submit(() -> {
            try {
                FitnessModel fitnessModel = StormDatabase.getInstance().getStorm().buildQuery(FitnessModel.class)
                        .where("uuid", Where.EQUAL, player.getUuid().toString())
                        .execute()
                        .join()
                        .stream()
                        .findFirst()
                        .orElse(null);

                if (fitnessModel == null) {
                    fitnessModel = new FitnessModel();
                    fitnessModel.setUniqueId(player.getUuid());
                }
                fitnessModel.setTotal(amount);
                StormDatabase.getInstance().saveStormModel(fitnessModel);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public void setFitnessGainedByDrinking(MinetopiaPlayer player, int amount) {
        StormDatabase.getExecutorService().submit(() -> {
            try {
                FitnessModel fitnessModel = StormDatabase.getInstance().getStorm().buildQuery(FitnessModel.class)
                        .where("uuid", Where.EQUAL, player.getUuid().toString())
                        .execute()
                        .join()
                        .stream()
                        .findFirst()
                        .orElse(null);


                if (fitnessModel == null) {
                    fitnessModel = new FitnessModel();
                    fitnessModel.setUniqueId(player.getUuid());
                }
                fitnessModel.setFitnessGainedByDrinking(amount);
                StormDatabase.getInstance().saveStormModel(fitnessModel);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public CompletableFuture<Integer> getFitnessGainedByDrinking(MinetopiaPlayer player) {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();

        StormDatabase.getExecutorService().submit(() -> {
            try {
                FitnessModel fitnessModel = StormDatabase.getInstance().getStorm().buildQuery(FitnessModel.class)
                        .where("uuid", Where.EQUAL, player.getUuid().toString())
                        .execute()
                        .join()
                        .stream()
                        .findFirst()
                        .orElse(null);

                if (fitnessModel != null) {
                    completableFuture.complete(fitnessModel.getFitnessGainedByDrinking());
                } else {
                    completableFuture.complete(0);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                completableFuture.completeExceptionally(exception);
            }
        });

        return completableFuture;
    }

    public CompletableFuture<Double> getDrinkingPoints(MinetopiaPlayer player) {
        CompletableFuture<Double> completableFuture = new CompletableFuture<>();

        StormDatabase.getExecutorService().submit(() -> {
            try {
                FitnessModel fitnessModel = StormDatabase.getInstance().getStorm().buildQuery(FitnessModel.class)
                        .where("uuid", Where.EQUAL, player.getUuid().toString())
                        .execute()
                        .join()
                        .stream()
                        .findFirst()
                        .orElse(null);

                if (fitnessModel != null) {
                    completableFuture.complete(fitnessModel.getDrinkingPoints());
                } else {
                    completableFuture.complete(0.0);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                completableFuture.completeExceptionally(exception);
            }
        });

        return completableFuture;
    }

    public void setDrinkingPoints(MinetopiaPlayer player, double amount) {
        StormDatabase.getExecutorService().submit(() -> {
            try {
                FitnessModel fitnessModel = StormDatabase.getInstance().getStorm().buildQuery(FitnessModel.class)
                        .where("uuid", Where.EQUAL, player.getUuid().toString())
                        .execute()
                        .join()
                        .stream()
                        .findFirst()
                        .orElse(null);

                if (fitnessModel == null) {
                    fitnessModel = new FitnessModel();
                    fitnessModel.setUniqueId(player.getUuid());
                }
                fitnessModel.setDrinkingPoints(amount);
                StormDatabase.getInstance().saveStormModel(fitnessModel);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public void setFitnessGainedByClimbing(MinetopiaPlayer player, int amount) {
        StormDatabase.getExecutorService().submit(() -> {
            try {
                FitnessModel fitnessModel = StormDatabase.getInstance().getStorm().buildQuery(FitnessModel.class)
                        .where("uuid", Where.EQUAL, player.getUuid().toString())
                        .execute()
                        .join()
                        .stream()
                        .findFirst()
                        .orElse(null);

                if (fitnessModel == null) {
                    fitnessModel = new FitnessModel();
                    fitnessModel.setUniqueId(player.getUuid());
                }
                fitnessModel.setFitnessGainedByClimbing(amount);
                StormDatabase.getInstance().saveStormModel(fitnessModel);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public void setFitnessGainedBySprinting(MinetopiaPlayer player, int amount) {
        StormDatabase.getExecutorService().submit(() -> {
            try {
                FitnessModel fitnessModel = StormDatabase.getInstance().getStorm().buildQuery(FitnessModel.class)
                        .where("uuid", Where.EQUAL, player.getUuid().toString())
                        .execute()
                        .join()
                        .stream()
                        .findFirst()
                        .orElse(null);

                if (fitnessModel == null) {
                    fitnessModel = new FitnessModel();
                    fitnessModel.setUniqueId(player.getUuid());
                }
                fitnessModel.setFitnessGainedBySprinting(amount);
                StormDatabase.getInstance().saveStormModel(fitnessModel);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public void setFitnessGainedByFlying(MinetopiaPlayer player, int amount) {
        StormDatabase.getExecutorService().submit(() -> {
            try {
                FitnessModel fitnessModel = StormDatabase.getInstance().getStorm().buildQuery(FitnessModel.class)
                        .where("uuid", Where.EQUAL, player.getUuid().toString())
                        .execute()
                        .join()
                        .stream()
                        .findFirst()
                        .orElse(null);

                if (fitnessModel == null) {
                    fitnessModel = new FitnessModel();
                    fitnessModel.setUniqueId(player.getUuid());
                }
                fitnessModel.setFitnessGainedByFlying(amount);
                StormDatabase.getInstance().saveStormModel(fitnessModel);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public void setFitnessGainedBySwimming(MinetopiaPlayer player, int amount) {
        StormDatabase.getExecutorService().submit(() -> {
            try {
                FitnessModel fitnessModel = StormDatabase.getInstance().getStorm().buildQuery(FitnessModel.class)
                        .where("uuid", Where.EQUAL, player.getUuid().toString())
                        .execute()
                        .join()
                        .stream()
                        .findFirst()
                        .orElse(null);

                if (fitnessModel == null) {
                    fitnessModel = new FitnessModel();
                    fitnessModel.setUniqueId(player.getUuid());
                }
                fitnessModel.setFitnessGainedBySwimming(amount);
                StormDatabase.getInstance().saveStormModel(fitnessModel);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public void setFitnessGainedByWalking(MinetopiaPlayer player, int amount) {
        StormDatabase.getExecutorService().submit(() -> {
            try {
                FitnessModel fitnessModel = StormDatabase.getInstance().getStorm().buildQuery(FitnessModel.class)
                        .where("uuid", Where.EQUAL, player.getUuid().toString())
                        .execute()
                        .join()
                        .stream()
                        .findFirst()
                        .orElse(null);

                if (fitnessModel == null) {
                    fitnessModel = new FitnessModel();
                    fitnessModel.setUniqueId(player.getUuid());
                }
                fitnessModel.setFitnessGainedByWalking(amount);
                StormDatabase.getInstance().saveStormModel(fitnessModel);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }
}
