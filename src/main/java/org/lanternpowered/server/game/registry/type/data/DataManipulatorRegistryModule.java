package org.lanternpowered.server.game.registry.type.data;

import org.lanternpowered.server.data.manipulator.DataManipulatorRegistration;
import org.lanternpowered.server.game.registry.AdditionalPluginCatalogRegistryModule;
import org.spongepowered.api.data.DataRegistration;

public final class DataManipulatorRegistryModule extends AdditionalPluginCatalogRegistryModule<DataRegistration> {

    private static final DataManipulatorRegistryModule instance = new DataManipulatorRegistryModule();

    public static DataManipulatorRegistryModule get() {
        return instance;
    }

    private DataManipulatorRegistryModule() {
        super(null);
    }

    @Override
    public void registerAdditionalCatalog(DataRegistration catalogType) {
        if (catalogType instanceof DataManipulatorRegistration) {
            super.register(catalogType);
        } else {
            super.registerAdditionalCatalog(catalogType);
        }
    }
}
