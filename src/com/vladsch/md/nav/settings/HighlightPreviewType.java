// Copyright (c) 2015-2020 Vladimir Schneider <vladimir.schneider@gmail.com> Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.vladsch.md.nav.settings;

import com.vladsch.md.nav.MdBundle;
import com.vladsch.plugin.util.ui.ComboBoxAdaptable;
import com.vladsch.plugin.util.ui.ComboBoxAdapter;
import com.vladsch.plugin.util.ui.ComboBoxAdapterImpl;
import org.jetbrains.annotations.NotNull;

public enum HighlightPreviewType implements ComboBoxAdaptable<HighlightPreviewType> {
    NONE(0, MdBundle.message("highlight.preview.none")),
    BLOCK(1, MdBundle.message("highlight.preview.block")),
    LINE(2, MdBundle.message("highlight.preview.line"));

    @NotNull public final String displayName;
    public final int intValue;

    HighlightPreviewType(int intValue, @NotNull String displayName) {
        this.intValue = intValue;
        this.displayName = displayName;
    }

    public static ComboBoxAdaptable.Static<HighlightPreviewType> ADAPTER = new Static<>(new ComboBoxAdapterImpl<>(LINE));

    @NotNull
    @Override
    public ComboBoxAdapter<HighlightPreviewType> getAdapter() {
        return ADAPTER;
    }

    @Override
    public int getIntValue() { return intValue; }

    @NotNull
    public String getDisplayName() { return displayName; }

    @NotNull
    public HighlightPreviewType[] getValues() { return values(); }
}
