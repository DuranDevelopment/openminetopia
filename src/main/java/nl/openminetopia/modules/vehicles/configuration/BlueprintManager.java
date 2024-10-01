package nl.openminetopia.modules.vehicles.configuration;

import lombok.*;
import nl.openminetopia.modules.vehicles.configuration.components.FuelComponent;
import nl.openminetopia.modules.vehicles.configuration.components.MovementComponent;
import nl.openminetopia.modules.vehicles.configuration.components.PartComponent;
import nl.openminetopia.modules.vehicles.configuration.components.SeatComponent;

import java.io.File;
import java.util.*;

/* Should probably do singleton, but who cares */
public class BlueprintManager {

    private static final Map<String, Blueprint> blueprints = new HashMap<>();

    public static void load(File folder) {
        blueprints().clear();

        if (folder.listFiles() == null) return;
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.isDirectory()) {
                load(file);
            } else if (file.getName().endsWith(".yml") || file.getName().endsWith(".yaml")) {
                /* holy java garbage collector please be my saviour */
                new VehicleConfiguration(file.getParentFile(), file.getName());
            }
        }
    }

    public static Blueprint get(String identifier) {
        Blueprint blueprint = blueprints.get(identifier);
        if (blueprint == null) {
            blueprint = new Blueprint();
            blueprints.put(identifier, blueprint);
        }

        return blueprint;
    }

    public static Map<String, Blueprint> blueprints() {
        return blueprints;
    }

    @Data
    @NoArgsConstructor
    public static class Blueprint {

        private String displayName;

        /* Fuel Settings */
        private FuelComponent fuelComponent;

        /* Default Movement */
        private MovementComponent movementComponent;

        /* All seats */
        private List<SeatComponent> seats = new ArrayList<>();

        /* Part related */
        private final Map<String, PartComponent> parts = new HashMap<>();
        private List<String> defaultParts;

        public PartComponent obtainPart(String identifier) {
            return parts.get(identifier);
        }

        public void addPart(PartComponent part) {
            parts.put(part.identifier(), part);
        }

    }

}
