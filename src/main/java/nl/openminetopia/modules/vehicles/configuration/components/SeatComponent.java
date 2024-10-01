package nl.openminetopia.modules.vehicles.configuration.components;

import org.joml.Vector3f;

/**
 * @param identifier
 * @param x
 * @param y
 * @param z
 * @param isDriver
 */
public record SeatComponent(String identifier, float x, float y, float z, boolean isDriver) {

    public Vector3f vector() {
        return new Vector3f(x, y, z);
    }

}
