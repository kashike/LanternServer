package org.lanternpowered.server.network.entity.vanilla;

import org.lanternpowered.server.entity.LanternEntityLiving;
import org.lanternpowered.server.network.entity.EntityProtocolType;
import org.lanternpowered.server.network.entity.ParameterType;
import org.lanternpowered.server.network.entity.ParameterValueTypes;

public class LanternLivingProtocol<E extends LanternEntityLiving> extends LanternEntityProtocol<E> {

    public static final EntityProtocolType TYPE = LanternEntityProtocol.TYPE.copy();

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
}
