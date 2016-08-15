package org.lanternpowered.server.network.entity;

import static org.lanternpowered.server.network.vanilla.message.codec.play.CodecUtils.wrapAngle;

import com.flowpowered.math.vector.Vector3d;
import org.lanternpowered.server.entity.LanternEntityLiving;
import org.lanternpowered.server.network.message.Message;
import org.lanternpowered.server.network.vanilla.message.type.play.MessagePlayOutSpawnMob;

public abstract class LivingEntityProtocol<E extends LanternEntityLiving> extends EntityProtocol<E> {

    public static final EntityProtocolType TYPE = EntityProtocol.TYPE.copy();

    /**
     * Bit mask Meaning
     * 0x01	    Is hand active
     * 0x02	    Active hand (0 = main hand, 1 = offhand)
     */
    public static final ParameterType<Byte> HAND_DATA = TYPE.newParameterType(ParameterValueTypes.BYTE);

    /**
     * The health of the entity.
     */
    public static final ParameterType<Float> HEALTH = TYPE.newParameterType(ParameterValueTypes.FLOAT);

    /**
     * The potion effect color of the particles that spawn around the player.
     */
    public static final ParameterType<Integer> POTION_EFFECT_COLOR = TYPE.newParameterType(ParameterValueTypes.INTEGER);

    /**
     * Whether the potion effect particles are ambient.
     */
    public static final ParameterType<Boolean> POTION_EFFECT_AMBIENT = TYPE.newParameterType(ParameterValueTypes.BOOLEAN);

    /**
     * The amount of arrows that are in the entity.
     */
    public static final ParameterType<Integer> ARROWS_IN_ENTITY = TYPE.newParameterType(ParameterValueTypes.INTEGER);

    /**
     * Gets the mob type.
     *
     * @return The mob type
     */
    protected abstract int getMobType();

    @Override
    public Message newSpawnMessage() {
        final Vector3d rot = this.entity.getRotation();
        final Vector3d headRot = this.entity.getHeadRotation();
        final Vector3d pos = this.entity.getPosition();

        double yaw = rot.getY();
        double headPitch = headRot.getX();
        double headYaw = headRot.getY();

        return new MessagePlayOutSpawnMob(this.entity.getEntityId(), this.entity.getUniqueId(), this.getMobType(),
                pos, wrapAngle(yaw), wrapAngle(headPitch), wrapAngle(headYaw), this.entity.getVelocity(), this.fillParameters(true));
    }
}
