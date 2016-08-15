package org.lanternpowered.server.network.entity;

import org.lanternpowered.server.entity.LanternLightning;
import org.lanternpowered.server.network.message.Message;
import org.lanternpowered.server.network.vanilla.message.type.play.MessagePlayOutSpawnThunderbolt;

public class LightningProtocol extends EntityProtocol<LanternLightning> {

    @Override
    public Message newSpawnMessage() {
        return new MessagePlayOutSpawnThunderbolt(this.entity.getEntityId(), this.entity.getPosition());
    }

    @Override
    public void fill(ParameterList parameterList, boolean initial) {
    }
}
