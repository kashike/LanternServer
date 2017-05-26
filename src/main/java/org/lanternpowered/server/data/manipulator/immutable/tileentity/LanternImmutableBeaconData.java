package org.lanternpowered.server.data.manipulator.immutable.tileentity;

import org.spongepowered.api.data.manipulator.immutable.tileentity.ImmutableBeaconData;

public interface LanternImmutableBeaconData extends ImmutableBeaconData {

    @Override
    default ImmutableBeaconData clearEffects() {
        return asMutable().clearEffects().asImmutable();
    }
}
