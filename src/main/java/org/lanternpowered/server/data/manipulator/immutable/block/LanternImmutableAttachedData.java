package org.lanternpowered.server.data.manipulator.immutable.block;

import org.lanternpowered.server.data.manipulator.IImmutableValueHolder;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableAttachedData;
import org.spongepowered.api.data.manipulator.mutable.block.AttachedData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;

public interface LanternImmutableAttachedData extends IImmutableValueHolder, ImmutableAttachedData {

    @Override
    default ImmutableValue<Boolean> attached() {
        return getImmutableValue(Keys.ATTACHED).get();
    }
}
