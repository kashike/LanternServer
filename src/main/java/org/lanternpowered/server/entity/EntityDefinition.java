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
package org.lanternpowered.server.entity;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.lanternpowered.server.entity.living.player.LanternPlayer;
import org.lanternpowered.server.network.entity.EntityProtocolType;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.entity.EntityType;

/**
 * A configuration file which represents a {@link EntityType}.
 */
@ConfigSerializable
public class EntityDefinition {

    /**
     * The parent of the definition.
     *
     * <p>If fields of the entity definition are left out
     * then it will default to the parent definition.</p>
     */
    @Setting("parent")
    private String parent = "generic";

    /**
     * The base class of the entity.
     */
    @Setting("base-class")
    private String baseClass = LanternEntity.class.getName();

    /**
     * Properties for the protocol of the entity.
     */
    @ConfigSerializable
    public static final class Protocol {

        /**
         * The protocol type of the entity. This will affect the
         * appearance of the entity.
         *
         * <p>Certain protocol types will require a specific set of
         * {@link Key}s of data or a specific {@link LanternEntity}
         * base class.<p/>
         */
        @Setting("protocol-type")
        private EntityProtocolType<?> entityProtocolType;

        /**
         * The maximum range that a {@link LanternPlayer} can track a
         * the {@link LanternEntity}. The view distance of the tracker
         * will also affect the actual distance.
         */
        @Setting("tracking-range")
        private int trackingRange = 64;

        /**
         * The rate (in ticks) that the tracked entity will be checked
         * for updates.
         */
        @Setting("tracking-update-rate")
        private int trackingUpdateRate = 4;

    }
}
