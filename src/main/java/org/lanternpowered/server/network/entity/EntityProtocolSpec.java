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
package org.lanternpowered.server.network.entity;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.lanternpowered.server.network.entity.vanilla.EntityProtocol;
import org.spongepowered.api.entity.Entity;

import java.util.function.Function;

public class EntityProtocolSpec {

    @Expose
    @SerializedName("entity-protocol-type")
    private EntityProtocolType<?> entityProtocolType;

    @Expose
    @SerializedName("tracking-range")
    private double trackingRange = 64;

    @Expose
    @SerializedName("tracking-update-rate")
    private int trackingUpdateRate = 4;

    public EntityProtocolSpec(EntityProtocolType<?> entityProtocolType) {
        this.entityProtocolType = entityProtocolType;
    }

    private EntityProtocolSpec() {
    }

    /**
     * Constructs a new {@link EntityProtocol} instance from this spec.
     *
     * @param entity The entity to construct the protocol for
     * @return The entity protocol instance
     */
    public EntityProtocol<?> construct(Entity entity) {
        checkArgument(this.entityProtocolType.getEntityType().isInstance(entity),
                "The protocol type %s specified by this spec isn't applicable for %s",
                this.entityProtocolType.getId(), entity.getType().getId());
        //noinspection unchecked
        final EntityProtocol entityProtocol = (EntityProtocol) ((Function) this.entityProtocolType.getSupplier()).apply(entity);
        entityProtocol.setTrackingRange(this.trackingRange);
        entityProtocol.setTickRate(this.trackingUpdateRate);
        return entityProtocol;
    }

    /**
     * Gets the {@link EntityProtocolType} that should be used.
     *
     * @return The entity protocol type
     */
    public EntityProtocolType<?> getEntityProtocolType() {
        return this.entityProtocolType;
    }

    /**
     * Gets the default tracking range of the spec.
     *
     * @return The tracking range
     */
    public double getTrackingRange() {
        return this.trackingRange;
    }

    /**
     * Gets the default tracking update rate of the spec.
     *
     * @return The tracking update rate
     */
    public int getTrackingUpdateRate() {
        return this.trackingUpdateRate;
    }
}
