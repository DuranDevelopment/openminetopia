package nl.openminetopia.modules.vehicles.objects.movement;

import nl.openminetopia.modules.vehicles.objects.Vehicle;
import nl.openminetopia.modules.vehicles.wrappers.WrappedPlayerInputPacket;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class BoatMovement extends Movement {

    public BoatMovement(Vehicle vehicle) {
        super(vehicle);
    }

    private double speed = 0;

    @Override
    public void move(WrappedPlayerInputPacket packet) {
        float ACCELERATION = 0.1F, MAX_SPEED = 1.5F, DECELERATION = 0.05F;

        if (!onWater()) {
            speed = 0;
            return;
        }

        if (packet.isForward()) speed = Math.min(speed + ACCELERATION, MAX_SPEED);
        else if (packet.isBackward()) speed = Math.max(speed - ACCELERATION, -MAX_SPEED);
        else speed = speed > 0 ? Math.max(speed - DECELERATION, 0) : Math.min(speed + DECELERATION, 0);

        if (packet.isLeft()) internalEntity.setRot(internalEntity.yRotO - 5, 0);
        else if (packet.isRight()) internalEntity.setRot(internalEntity.yRotO + 5, 0);

        Vector vector = vector(speed);
        if (entity.isUnderWater() || entity.isInWater()) {
            vector.setY(0.2);
        }

        entity.setVelocity(vector);
    }

    private Vector vector(double speed) {
        Vector vector = entity.getLocation().getDirection();
        vector.multiply(speed);
        vector.setY(-0.1);

        return vector;
    }

    private boolean onWater() {
        return entity.getLocation().subtract(0, 1, 0).getBlock().getType() == Material.WATER || entity.isInWater();
    }

    public static BoatMovement movement(Vehicle vehicle) {
        return new BoatMovement(vehicle);
    }
}
