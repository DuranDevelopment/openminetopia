package nl.openminetopia.modules.vehicles.configuration;

import com.google.gson.Gson;
import nl.openminetopia.modules.vehicles.configuration.components.FuelComponent;
import nl.openminetopia.modules.vehicles.configuration.components.MovementComponent;
import nl.openminetopia.modules.vehicles.configuration.components.PartComponent;
import nl.openminetopia.modules.vehicles.configuration.components.SeatComponent;
import nl.openminetopia.utils.ConfigurateConfig;
import org.bukkit.Material;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VehicleConfiguration extends ConfigurateConfig {

    public VehicleConfiguration(File file, String name) {
        super(file, name);

        this.vehicle();
        this.parts();
    }

    private void parts() {
        /* Clean this up someday */
        ConfigurationNode partsNode = rootNode.node("parts");
        for (Map.Entry<Object, ? extends ConfigurationNode> vehicleEntry : partsNode.childrenMap().entrySet()) {
            ConfigurationNode vehicleNode = vehicleEntry.getValue();
            String vehicleIdentifier = vehicleEntry.getKey().toString();

            for (Map.Entry<Object, ? extends ConfigurationNode> partEntry : vehicleNode.childrenMap().entrySet()) {
                ConfigurationNode partNode = partEntry.getValue();

                String partIdentifier = partEntry.getKey().toString();
                String displayName = partNode.node("display_name").getString("");
                String namespacedNBT = partNode.node("item", "nbt").getString(); /* TODO create a PartItemComponent */
                String hexColor = partNode.node("item", "hex_color").getString();
                int customModelData = partNode.node("item", "custom_model_data").getInt(-1);
                boolean visible = partNode.node("visible").getBoolean(true);

                Material material;
                try {
                    material = Material.valueOf(partNode.node("item", "material").getString("IRON_INGOT"));
                } catch (IllegalArgumentException e) {
                    material = Material.IRON_INGOT;
                }

                PartComponent part = new PartComponent(partIdentifier, displayName, material,
                        namespacedNBT, hexColor, customModelData, visible);

                BlueprintManager.get(vehicleIdentifier).addPart(part);
            }
        }
    }

    private void vehicle() {
        ConfigurationNode vehiclesNode = rootNode.node("vehicles");
        for (Map.Entry<Object, ? extends ConfigurationNode> vehicleEntry : vehiclesNode.childrenMap().entrySet()) {
            ConfigurationNode vehicleNode = vehicleEntry.getValue();

            String vehicleIdentifier = vehicleEntry.getKey().toString();
            String displayName = vehicleNode.node("display_name").getString();

            BlueprintManager.Blueprint blueprint = BlueprintManager.get(vehicleIdentifier);
            blueprint.setDisplayName(displayName);
            blueprint.setFuelComponent(fuel(vehicleNode));
            blueprint.setMovementComponent(movement(vehicleNode));
            blueprint.setDefaultParts(defaultParts(vehicleNode));
            blueprint.setSeats(seats(vehicleNode));
        }
    }

    private List<String> defaultParts(ConfigurationNode vehicleNode) {
        List<String> defaultParts;
        try {
            defaultParts = vehicleNode.node("default_parts").getList(String.class);
        } catch (SerializationException e) {
            defaultParts = new ArrayList<>();
        }

        return defaultParts;
    }

    private FuelComponent fuel(ConfigurationNode vehicleNode) {
        boolean fuelEnabled = vehicleNode.node("fuel", "enabled").getBoolean(true);
        float fuelUsage = vehicleNode.node("fuel", "usage").getFloat(0.0f);

        return new FuelComponent(fuelEnabled, fuelUsage);
    }

    private MovementComponent movement(ConfigurationNode vehicleNode) {
        String movementType = vehicleNode.node("movement", "type").getString("CAR");
        float maxSpeed = vehicleNode.node("movement", "max_speed").getFloat(0.0f);
        float minSpeed = vehicleNode.node("movement", "min_speed").getFloat(0.0f);
        float acceleration = vehicleNode.node("movement", "acceleration").getFloat(0.0f);
        float deceleration = vehicleNode.node("movement", "deceleration").getFloat(0.0f);
        float rollRate = vehicleNode.node("movement", "roll_rate").getFloat(0.0f);
        float turnRate = vehicleNode.node("movement", "turn_rate").getFloat(0.0f);

        return new MovementComponent(movementType, maxSpeed, minSpeed, acceleration, deceleration, rollRate, turnRate);
    }

    private List<SeatComponent> seats(ConfigurationNode vehicleNode) {
        List<SeatComponent> seats = new ArrayList<>();

        ConfigurationNode seatsNode = vehicleNode.node("seats");
        for (Map.Entry<Object, ? extends ConfigurationNode> seatEntry : seatsNode.childrenMap().entrySet()) {
            ConfigurationNode seatNode = seatEntry.getValue();

            String seatIdentifier = seatEntry.getKey().toString();
            float x = seatNode.node("x").getFloat(0.0f);
            float y = seatNode.node("y").getFloat(0.0f);
            float z = seatNode.node("z").getFloat(0.0f);
            boolean isDriver = seatNode.node("driver").getBoolean(false);

            seats.add(new SeatComponent(seatIdentifier, x, y, z, isDriver));
        }

        return seats;
    }

}
