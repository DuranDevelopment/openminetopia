package nl.openminetopia.modules.vehicles.configuration.components;

/**
 * @param type
 * @param maxSpeed
 * @param minSpeed
 * @param acceleration
 * @param deceleration
 * @param rollRate
 * @param turnRate
 */
public record MovementComponent(String type, float maxSpeed, float minSpeed, float acceleration, float deceleration,
                                float rollRate, float turnRate) {}
