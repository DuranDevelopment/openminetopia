package nl.openminetopia.utils.nms;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import lombok.experimental.UtilityClass;
import net.minecraft.SharedConstants;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.IdentityHashMap;
import java.util.Map;

@UtilityClass
public class EntityUtils {
    public void injectEntity(String entityId, EntityType<?> originalType, EntityType.EntityFactory<? extends Entity> factory,
                              MobCategory category) throws NoSuchFieldException, IllegalAccessException {
        ResourceLocation mcKey = ResourceLocation.parse(entityId);
        try {
            Class.forName("com.destroystokyo.paper.ParticleBuilder");
            @SuppressWarnings("unchecked")
            Map<String, Type<?>> types = (Map<String, Type<?>>) DataFixers.getDataFixer().getSchema(
                            DataFixUtils.makeKey(SharedConstants.getCurrentVersion().getDataVersion().getVersion()))
                    .findChoiceType(References.ENTITY).types();
            types.put(mcKey.toString(), types.get(BuiltInRegistries.ENTITY_TYPE.getKey(originalType).toString()));
        } catch (ClassNotFoundException ignored){}

        WritableRegistry<EntityType<?>> entities = (WritableRegistry<EntityType<?>>) DedicatedServer.getServer().registryAccess().registryOrThrow(Registries.ENTITY_TYPE);
        ReflectionUtils.setField("unregisteredIntrusiveHolders", MappedRegistry.class, entities, new IdentityHashMap<EntityType<?>, Holder.Reference<EntityType<?>>>());
        ReflectionUtils.setField("frozen", MappedRegistry.class, entities, false);

        ResourceKey<EntityType<?>> customKey = ResourceKey.create(Registries.ENTITY_TYPE, mcKey);
        EntityType<?> entityType = EntityType.Builder.of(factory, category).build(entityId);
        entities.register(customKey, entityType, RegistrationInfo.BUILT_IN);
    }

    public boolean isInjected(String entityId) {
        ResourceLocation mcKey = ResourceLocation.parse(entityId);
        return BuiltInRegistries.ENTITY_TYPE.getOptional(mcKey).isPresent();
    }

}
