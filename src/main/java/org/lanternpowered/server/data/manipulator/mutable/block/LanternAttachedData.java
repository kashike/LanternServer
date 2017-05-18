package org.lanternpowered.server.data.manipulator.mutable.block;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.block.AttachedData;
import org.spongepowered.api.data.value.mutable.Value;

public interface LanternAttachedData extends AttachedData {

    @Override
    default Value<Boolean> attached() {
        return getValue(Keys.ATTACHED).get();
    }
}
