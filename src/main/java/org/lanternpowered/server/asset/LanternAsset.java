/*
 * This file is part of LanternServer, licensed under the MIT License (MIT).
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
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
package org.lanternpowered.server.asset;

import com.google.common.base.MoreObjects;
import org.lanternpowered.api.asset.Asset;
import org.spongepowered.api.plugin.PluginContainer;

import java.net.URL;
import java.nio.file.Path;

final class LanternAsset implements Asset {

    private final PluginContainer plugin;
    private final String id;
    final Path path;
    private final URL url;

    LanternAsset(PluginContainer plugin, String id, Path path, URL url) {
        this.plugin = plugin;
        this.path = path;
        this.url = url;
        this.id = id;
    }

    @Override
    public PluginContainer getOwner() {
        return this.plugin;
    }

    @Override
    public URL getUrl() {
        return this.url;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("plugin", this.plugin.getId())
                .add("id", this.id)
                .add("url", this.url.toString())
                .toString();
    }
}
