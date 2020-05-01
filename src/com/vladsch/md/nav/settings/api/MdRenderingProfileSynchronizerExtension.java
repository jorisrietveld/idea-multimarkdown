// Copyright (c) 2015-2020 Vladimir Schneider <vladimir.schneider@gmail.com> Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.vladsch.md.nav.settings.api;

import com.intellij.openapi.extensions.ExtensionPointName;
import com.vladsch.md.nav.settings.RenderingProfileSynchronizer;
import com.vladsch.md.nav.util.MdExtensions;
import org.jetbrains.annotations.NotNull;

public interface MdRenderingProfileSynchronizerExtension {
    ExtensionPointName<MdRenderingProfileSynchronizerExtension> EP_NAME = ExtensionPointName.create("com.vladsch.idea.multimarkdown.renderingProfileSynchronizerExtension");
    MdExtensions<MdRenderingProfileSynchronizerExtension> EXTENSIONS = new MdExtensions<>(EP_NAME, new MdRenderingProfileSynchronizerExtension[0]);

    /**
     * Create an instance of the extension data
     *
     * @param profileSynchronizer instance of profile synchronizer
     *
     * @return instance of extension data
     */
    @NotNull
    Object createExtensionData(@NotNull RenderingProfileSynchronizer profileSynchronizer);
}
