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

import org.lanternpowered.server.entity.LanternEntity;
import org.lanternpowered.server.network.entity.EntityProtocol;
import org.lanternpowered.server.network.entity.EntityProtocolType;
import org.lanternpowered.server.network.entity.ParameterList;
import org.lanternpowered.server.network.entity.ParameterType;
import org.lanternpowered.server.network.entity.ParameterValueTypes;

public class LanternEntityProtocol<E extends LanternEntity> extends EntityProtocol<E> {

    public static final EntityProtocolType TYPE = new EntityProtocolType();

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

    @Override
    public void fill(E entity, ParameterList parameterList, boolean initial) {
        super.fill(entity, parameterList, initial);

        if (initial) {
            parameterList.add(AIR_LEVEL, this.getInitialAirLevel(entity));
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
     * @param entity The target entity
     * @return The air level
     */
    protected short getInitialAirLevel(E entity) {
        return 100;
    }
}
