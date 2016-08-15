package org.lanternpowered.server.entity;

import org.spongepowered.api.entity.weather.Lightning;

public class LanternLightning extends LanternEntity implements Lightning {

    @Override
    public boolean isEffect() {
        return false;
    }

    @Override
    public void setEffect(boolean effect) {
    }
}
