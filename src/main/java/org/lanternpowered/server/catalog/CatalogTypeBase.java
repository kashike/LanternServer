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
package org.lanternpowered.server.catalog;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.lanternpowered.server.util.Conditions.checkNotNullOrEmpty;

import org.lanternpowered.server.game.Lantern;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.text.translation.Translation;

public abstract class CatalogTypeBase extends AbstractCatalogType implements CatalogType {

    protected final String identifier;
    protected final String name;

    public CatalogTypeBase(String identifier, String name) {
        this.identifier = checkNotNullOrEmpty(identifier, "identifier");
        this.name = checkNotNullOrEmpty(name, "name");
    }

    @Override
    public String getId() {
        return this.identifier;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public static abstract class Translatable extends CatalogTypeBase implements org.spongepowered.api.text.translation.Translatable {

        private final Translation translation;

        public Translatable(String identifier, String name, String translation) {
            this(identifier, name, Lantern.getRegistry().getTranslationManager().get(translation));
        }

        public Translatable(String identifier, String name, Translation translation) {
            super(identifier, name);
            this.translation = checkNotNull(translation, "translation");
        }

        @Override
        public Translation getTranslation() {
            return this.translation;
        }
    }
}
