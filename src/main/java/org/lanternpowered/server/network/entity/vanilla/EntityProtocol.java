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
import org.lanternpowered.server.entity.LanternEntity;
import org.lanternpowered.server.network.buffer.ByteBuffer;
import org.lanternpowered.server.network.buffer.ByteBufferAllocator;
import org.lanternpowered.server.network.entity.AbstractEntityProtocol;
import org.lanternpowered.server.network.entity.ByteBufParameterList;
import org.lanternpowered.server.network.entity.EntityUpdateContext;
import org.lanternpowered.server.network.entity.ParameterList;
import org.lanternpowered.server.network.entity.ParameterType;
import org.lanternpowered.server.network.entity.ParameterTypeCollection;
import org.lanternpowered.server.network.entity.ParameterValueTypes;
import org.lanternpowered.server.network.vanilla.message.type.play.MessagePlayOutDestroyEntities;
import org.lanternpowered.server.network.vanilla.message.type.play.MessagePlayOutEntityHeadLook;
import org.lanternpowered.server.network.vanilla.message.type.play.MessagePlayOutEntityLook;
import org.lanternpowered.server.network.vanilla.message.type.play.MessagePlayOutEntityLookAndRelativeMove;
import org.lanternpowered.server.network.vanilla.message.type.play.MessagePlayOutEntityMetadata;
import org.lanternpowered.server.network.vanilla.message.type.play.MessagePlayOutEntityRelativeMove;
import org.lanternpowered.server.network.vanilla.message.type.play.MessagePlayOutEntityTeleport;
import org.lanternpowered.server.network.vanilla.message.type.play.MessagePlayOutEntityVelocity;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;

public abstract class EntityProtocol<E extends LanternEntity> extends AbstractEntityProtocol<E> {

    public static final ParameterTypeCollection TYPE = new ParameterTypeCollection();

    /**
     * Bit mask Meaning
     * 0x01	    On Fire
     * 0x02	    Crouched
     * 0x08	    Sprinting
     * 0x10	    Eating/drinking/blocking
     * 0x20	    Invisible
     * 0x40	    Glowing effect
     * 0x80	    Flying with elytra
     */
    public static final ParameterType<Byte> BASE_DATA = TYPE.newParameterType(ParameterValueTypes.BYTE);

    /**
     * The air level of the entity.
     */
    public static final ParameterType<Integer> AIR_LEVEL = TYPE.newParameterType(ParameterValueTypes.INTEGER);

    /**
     * The custom name of the entity.
     */
    public static final ParameterType<String> CUSTOM_NAME = TYPE.newParameterType(ParameterValueTypes.STRING);

    /**
     * Whether the custom name is always visible.
     */
    public static final ParameterType<Boolean> CUSTOM_NAME_VISIBLE = TYPE.newParameterType(ParameterValueTypes.BOOLEAN);

    /**
     * Whether the entity is silent.
     */
    public static final ParameterType<Boolean> IS_SILENT = TYPE.newParameterType(ParameterValueTypes.BOOLEAN);

    /**
     * Whether the entity has no gravity.
     */
    public static final ParameterType<Boolean> NO_GRAVITY = TYPE.newParameterType(ParameterValueTypes.BOOLEAN);

    private double lastX;
    private double lastY;
    private double lastZ;

    private double lastYaw;
    private double lastPitch;

    private double lastHeadYaw;

    private double lastVelX;
    private double lastVelY;
    private double lastVelZ;

    public EntityProtocol(E entity) {
        super(entity);
    }

    @Override
    protected void destroy(EntityUpdateContext context) {
        context.sendToAll(new MessagePlayOutDestroyEntities(new int[] { this.entity.getEntityId() }));
    }

    @Override
    public void update(EntityUpdateContext context) {
        final Vector3d rot = this.entity.getRotation();
        final Vector3d headRot = this.entity instanceof Living ? ((Living) this.entity).getHeadRotation() : null;
        final Vector3d pos = this.entity.getPosition();

        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();

        double yaw = rot.getY();
        // All living entities have a head rotation and changing the pitch
        // would only affect the head pitch.
        double pitch = (headRot != null ? headRot : rot).getX();

        boolean dirtyPos = x != this.lastX || y != this.lastY || z != this.lastZ;
        boolean dirtyRot = yaw != this.lastYaw || z != this.lastPitch;

        // TODO: On ground state

        final int entityId = this.entity.getEntityId();

        if (dirtyPos) {
            double dx = x - this.lastX;
            double dy = y - this.lastY;
            double dz = z - this.lastZ;

            if (dirtyRot) {
                this.lastYaw = yaw;
                this.lastPitch = pitch;
            }
            if (Math.abs(dx) < 8 && Math.abs(dy) < 8 && Math.abs(dz) < 8) {
                int dxu = (int) (dx * 4096);
                int dyu = (int) (dy * 4096);
                int dzu = (int) (dz * 4096);

                if (dirtyRot) {
                    context.sendToAllExceptSelf(new MessagePlayOutEntityLookAndRelativeMove(entityId,
                            dxu, dyu, dzu, wrapAngle(yaw), wrapAngle(pitch), false));
                    // The rotation is already send
                    dirtyRot = false;
                } else {
                    context.sendToAllExceptSelf(new MessagePlayOutEntityRelativeMove(entityId,
                            dxu, dyu, dzu, false));
                }
            } else {
                context.sendToAllExceptSelf(new MessagePlayOutEntityTeleport(entityId,
                        x, y, z, wrapAngle(yaw), wrapAngle(pitch), false));
                // The rotation is already send
                dirtyRot = false;
            }
            this.lastX = x;
            this.lastY = y;
            this.lastZ = z;
        }
        if (dirtyRot) {
            context.sendToAllExceptSelf(new MessagePlayOutEntityLook(entityId, wrapAngle(yaw), wrapAngle(pitch), false));
        }
        if (headRot != null) {
            double headYaw = headRot.getY();
            if (headYaw != this.lastHeadYaw) {
                context.sendToAllExceptSelf(new MessagePlayOutEntityHeadLook(entityId, wrapAngle(headYaw)));
            }
            this.lastHeadYaw = yaw;
        }
        final Vector3d velocity = this.entity.getVelocity();
        x = velocity.getX();
        y = velocity.getY();
        z = velocity.getZ();
        if (x != this.lastVelX || y != this.lastVelY || z != this.lastVelZ) {
            context.sendToAll(new MessagePlayOutEntityVelocity(entityId, x, y, z));
            this.lastVelX = x;
            this.lastVelY = y;
            this.lastVelZ = z;
        }
        final ParameterList parameterList = this.fillParameters(false);
        // There were parameters applied
        if (!parameterList.isEmpty()) {
            context.sendToAll(new MessagePlayOutEntityMetadata(entityId, parameterList));
        }
        // TODO: Update attributes
    }

    /**
     * Fills a {@link ByteBuffer} with parameters to spawn or update the {@link Entity}.
     *
     * @param initial Whether the entity is being spawned, the byte buffer can be null if this is false
     * @return The byte buffer
     */
    ParameterList fillParameters(boolean initial) {
        final ByteBufParameterList parameterList = new ByteBufParameterList(ByteBufferAllocator.unpooled());
        this.fill(parameterList, initial);
        return parameterList;
    }

    /**
     * Fills the {@link ParameterList} with parameters to update the {@link Entity} on
     * the client. If {@code initial} is {@code true} then is being initially spawned
     * on the client.
     *
     * @param parameterList The parameter list to fill
     * @param initial Whether the entity is being spawned
     */
    public void fill(ParameterList parameterList, boolean initial) {
        if (initial) {
            parameterList.add(AIR_LEVEL, this.getInitialAirLevel());
            // Always disable gravity, we will handle our own physics
            parameterList.add(NO_GRAVITY, true);
        }
    }

    /**
     * Gets the air level of the entity.
     *
     * The air is by default 100 because the entities don't even use the air level,
     * except for the players. This method can be overridden if needed.
     *
     * @return The air level
     */
    protected short getInitialAirLevel() {
        return 100;
    }
}
