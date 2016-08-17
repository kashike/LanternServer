/*
 * This file is part of LanternServer, licensed under the MIT License (MIT).
 *
 * Copyright (c) LanternPowered <https://github.com/LanternPowered>
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the Software), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED AS IS, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.lanternpowered.server.network.entity.vanilla;

import static org.lanternpowered.server.network.vanilla.message.codec.play.CodecUtils.wrapAngle;

import com.flowpowered.math.vector.Vector3d;
import org.lanternpowered.server.entity.LanternEntityLiving;
import org.lanternpowered.server.network.entity.ParameterTypeCollection;
import org.lanternpowered.server.network.entity.EntityUpdateContext;
import org.lanternpowered.server.network.entity.ParameterType;
import org.lanternpowered.server.network.entity.ParameterValueTypes;
import org.lanternpowered.server.network.vanilla.message.type.play.MessagePlayOutSpawnMob;

public abstract class LivingEntityProtocol<E extends LanternEntityLiving> extends EntityProtocol<E> {

    public static final ParameterTypeCollection TYPE = EntityProtocol.TYPE.copy();

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

    public LivingEntityProtocol(E entity) {
        super(entity);
    }

    /**
     * Gets the mob type.
     *
     * @return The mob type
     */
    protected abstract int getMobType();

    @Override
    public void spawn(EntityUpdateContext context) {
        final Vector3d rot = this.entity.getRotation();
        final Vector3d headRot = this.entity.getHeadRotation();
        final Vector3d pos = this.entity.getPosition();
        final Vector3d vel = this.entity.getVelocity();

        double yaw = rot.getY();
        double headPitch = headRot.getX();
        double headYaw = headRot.getY();

        context.sendToAllExceptSelf(() -> new MessagePlayOutSpawnMob(this.entity.getEntityId(), this.entity.getUniqueId(), this.getMobType(),
                pos, wrapAngle(yaw), wrapAngle(headPitch), wrapAngle(headYaw), vel, this.fillParameters(true)));
    }
}
