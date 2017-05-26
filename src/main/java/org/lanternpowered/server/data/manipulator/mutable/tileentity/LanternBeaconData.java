package org.lanternpowered.server.data.manipulator.mutable.tileentity;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.tileentity.BeaconData;

import java.util.Optional;

public interface LanternBeaconData extends BeaconData {

    @Override
    default BeaconData clearEffects() {
        set(Keys.BEACON_PRIMARY_EFFECT, Optional.empty());
        set(Keys.BEACON_SECONDARY_EFFECT, Optional.empty());
        return this;
    }
}
