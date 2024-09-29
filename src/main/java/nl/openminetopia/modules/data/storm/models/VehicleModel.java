package nl.openminetopia.modules.data.storm.models;

import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "vehicles")
public class VehicleModel {

    @Column(name = "uuid", unique = true)
    private UUID uniqueId;

    /* Movement */
    @Column(name = "max_speed")
    private Float maxSpeed;

    @Column(name = "min_speed")
    private float minSpeed;

    @Column(name = "acceleration")
    private float acceleration;

    @Column(name = "deceleration")
    private float deceleration; /* Brake Speed & Reverse Speed */

    @Column(name = "roll_rate")
    private float rollRate; /* Forced Deceleration */

    @Column(name = "turn_rate")
    private float turnRate;

    /* Parts */
    /* Riders */
    /* Passengers */

}
