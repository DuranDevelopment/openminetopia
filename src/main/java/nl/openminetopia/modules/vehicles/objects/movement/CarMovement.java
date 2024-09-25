package nl.openminetopia.modules.vehicles.objects.movement;

import nl.openminetopia.modules.vehicles.objects.Vehicle;
import nl.openminetopia.modules.vehicles.wrappers.WrappedPlayerInputPacket;
import org.bukkit.util.Vector;

public class CarMovement extends Movement {

    public CarMovement(Vehicle vehicle) {
        super(vehicle);
    }

    private double speed = 0;

    @Override
    public void move(WrappedPlayerInputPacket packet) {
        float ACCELERATION = 0.1F;
        float MAX_SPEED = 1.5F;
        float DECELERATION = 0.05F;

        if (packet.isForward()) speed = Math.min(speed + ACCELERATION, MAX_SPEED);
        else if (packet.isBackward()) speed = Math.max(speed - ACCELERATION, -MAX_SPEED);
        else {
            if (speed > 0) speed = Math.max(speed - DECELERATION, 0);
            else speed = Math.min(speed + DECELERATION, 0);
        }

        if (packet.isLeft()) internalEntity.setRot(internalEntity.yRotO - 5, 0);
        else if (packet.isRight()) internalEntity.setRot(internalEntity.yRotO + 5, 0);

        entity.setVelocity(vector(speed));
    }

    private Vector vector(double speed) {
        Vector vector = entity.getLocation().getDirection();
        vector.multiply(speed);
        vector.setY(-1);

        return vector;
    }

    public static CarMovement movement(Vehicle vehicle) {
        return new CarMovement(vehicle);
    }

}
