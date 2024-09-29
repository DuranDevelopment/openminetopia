package nl.openminetopia.modules.data.utils;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.builders.QueryBuilder;
import lombok.experimental.UtilityClass;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.FitnessModel;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@UtilityClass
public class StormUtils {

    // Create model

    // Read model

    /**
     * This method is used to fetch data from a model in the database.
     * It will return a CompletableFuture that will be completed with the data from the model.
     *
     * @param modelClass      The class of the model to fetch
     * @param filterBuilder   A consumer that can be used to apply additional filters to the query
     *                        (e.g. query -> query.where("some_column", Where.EQUAL, someValue))
     * @param postQueryFilter (Optional) A predicate to filter the stream of results after the query execution.
     *                        If null, no filtering will be applied.
     * @param dataExtractor   A function that extracts the data from the model
     *                        (e.g. model -> model.getSomeData())
     * @param defaultValue    The default value to return if the model is not found
     */
    public <T, M extends StormModel> CompletableFuture<T> getModelData(
            Class<M> modelClass,
            Consumer<QueryBuilder<M>> filterBuilder,
            @Nullable Predicate<M> postQueryFilter,
            Function<M, T> dataExtractor,
            T defaultValue) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();

        StormDatabase.getExecutorService().submit(() -> {
            try {
                QueryBuilder<M> query = StormDatabase.getInstance().getStorm().buildQuery(modelClass);

                // Apply additional query filters via filterBuilder
                filterBuilder.accept(query);

                M model = query.execute()
                        .join()
                        .stream()
                        .filter(postQueryFilter != null ? postQueryFilter : filteredModel -> true)  // Apply post-query filter if not null
                        .findFirst()
                        .orElse(null);

                if (model != null) {
                    completableFuture.complete(dataExtractor.apply(model));
                } else {
                    completableFuture.complete(defaultValue);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                completableFuture.completeExceptionally(exception);
            }
        });

        return completableFuture;
    }

    // Update model

    /**
     * This method is used to update data in a model in the database.
     * It will return a CompletableFuture that will be completed when the update is done.
     *
     * @param modelClass    The class of the model to update
     * @param filterBuilder A consumer that applies filters to identify which models to update
     *                      (e.g. query -> query.where("some_column", Where.EQUAL, someValue))
     * @param updateAction  A consumer that defines how the model should be updated
     *                      (e.g. model -> model.setSomeField(newValue))
     */
    public <M extends StormModel> CompletableFuture<Void> updateModelData(
            Class<M> modelClass,
            Consumer<QueryBuilder<M>> filterBuilder,
            Consumer<M> updateAction) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        StormDatabase.getExecutorService().submit(() -> {
            try {
                QueryBuilder<M> query = StormDatabase.getInstance().getStorm().buildQuery(modelClass);

                // Apply filters to select the models to update
                filterBuilder.accept(query);

                // Fetch the models that match the query
                query.execute().whenComplete((models, throwable) -> {
                    if (throwable != null) {
                        throwable.printStackTrace();
                        return;
                    }

                    // Apply updates to each model
                    for (M model : models) {
                        updateAction.accept(model);  // Apply the update logic
                        StormDatabase.getInstance().saveStormModel(model).whenComplete((integer, throwable2) -> {
                            if (throwable2 != null) {
                                throwable2.printStackTrace();
                            }
                        });  // Save the updated model back to the database
                    }
                });

                completableFuture.complete(null);  // Mark the future as complete
            } catch (Exception exception) {
                exception.printStackTrace();
                completableFuture.completeExceptionally(exception);  // Handle errors
            }
        });

        return completableFuture;
    }

    // Delete model

    /**
     * This method is used to delete models from the database based on the provided filter.
     * It will return a CompletableFuture that will be completed when the deletion is done.
     *
     * @param modelClass    The class of the model to delete
     * @param filterBuilder A consumer that applies filters to identify which models to delete
     *                      (e.g. query -> query.where("some_column", Where.EQUAL, someValue))
     */
    public <M extends StormModel> CompletableFuture<Void> deleteModelData(
            Class<M> modelClass,
            Consumer<QueryBuilder<M>> filterBuilder) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        StormDatabase.getExecutorService().submit(() -> {
            try {
                QueryBuilder<M> query = StormDatabase.getInstance().getStorm().buildQuery(modelClass);

                // Apply filters to select the models to delete
                filterBuilder.accept(query);

                // Fetch the models that match the query
                Collection<M> models = query.execute().join();

                // Delete each model
                for (M model : models) {
                    StormDatabase.getInstance().getStorm().delete(model);  // Delete the model from the database
                }

                completableFuture.complete(null);  // Mark the future as complete
            } catch (Exception exception) {
                exception.printStackTrace();
                completableFuture.completeExceptionally(exception);  // Handle errors
            }
        });

        return completableFuture;
    }


    public <M extends StormModel> CompletableFuture<Integer> getNextId(Class<M> modelClass, Function<M, Integer> idGetter) {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();

        StormDatabase.getExecutorService().submit(() -> {
            try {
                // Fetch all models of the given class
                Collection<M> models = StormDatabase.getInstance().getStorm().buildQuery(modelClass)
                        .execute()
                        .join();

                // Use the stream to find the maximum ID using the idGetter function
                int nextId = models.stream()
                        .mapToInt(idGetter::apply) // Use the idGetter to get the ID
                        .max()
                        .orElse(0) + 1;

                completableFuture.complete(nextId);
            } catch (Exception exception) {
                exception.printStackTrace();
                completableFuture.completeExceptionally(exception);
            }
        });

        return completableFuture;
    }
}
