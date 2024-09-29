package nl.openminetopia.modules.vehicles.objects.movement;

import lombok.Getter;
import lombok.Setter;
import nl.openminetopia.modules.vehicles.objects.Vehicle;
import nl.openminetopia.modules.vehicles.wrappers.WrappedPlayerInputPacket;
import org.bukkit.util.Vector;

public class CarMovement extends Movement {

    public CarMovement(Vehicle vehicle) {
        super(vehicle);
    }

    @Getter
    @Setter
    private double speed = 0;

    @Override
    public void move(WrappedPlayerInputPacket packet) {
        float MAX_SPEED = 1.5F;
        float MIN_SPEED = -1F;
        float ACCELERATION = 0.1F;
        float DECELERATION = 0.1F; /* Brake Speed & Reverse Speed */
        float ROLL_RATE = 0.05F; /* Forced Deceleration */
        float TURN_RATE = 5F;

        if (packet.isForward()) speed = Math.min(speed + ACCELERATION, MAX_SPEED);
        else if (packet.isBackward()) speed = Math.max(speed - DECELERATION, MIN_SPEED);
        else {
            if (speed > 0) speed = Math.max(speed - ROLL_RATE, 0);
            else speed = Math.min(speed + ROLL_RATE, 0);
        }

        if (packet.isLeft()) internalEntity.setRot(internalEntity.yRotO - TURN_RATE, 0);
        else if (packet.isRight()) internalEntity.setRot(internalEntity.yRotO + TURN_RATE, 0);

        entity.setVelocity(vector(speed));
    }

    private Vector vector(double speed) {
        Vector vector = entity.getLocation().getDirection();
        vector.multiply(speed);
        vector.setY(-1);

        return vector;
    }

    public static CarMovement inst(Vehicle vehicle) {
        return new CarMovement(vehicle);
    }

}
