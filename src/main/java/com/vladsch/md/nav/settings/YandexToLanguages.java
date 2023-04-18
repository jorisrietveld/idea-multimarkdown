// Copyright (c) 2015-2023 Vladimir Schneider <vladimir.schneider@gmail.com> Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.vladsch.md.nav.settings;

import com.vladsch.plugin.util.ui.ComboBoxAdapter;
import com.vladsch.plugin.util.ui.ComboBoxAdapterImpl;
import com.vladsch.plugin.util.ui.DynamicListAdaptable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComboBox;
import java.util.List;

public class YandexToLanguages extends DynamicListAdaptable<YandexToLanguages> {
    public YandexToLanguages(final int intValue, @NotNull final String displayName) {
        super(intValue, displayName);
    }

    final public static YandexToLanguages EMPTY = new YandexToLanguages(0, "");
    @SuppressWarnings("rawtypes")
    public static DynamicListAdaptable[] values = new DynamicListAdaptable[0];
    final public static Static<DynamicListAdaptable<YandexToLanguages>> ADAPTER = new Static<>(new ComboBoxAdapterImpl<>(EMPTY));

    public static void updateValues(@NotNull String[] valueList, final boolean addEmpty, @Nullable JComboBox<String> comboBox, YandexToLanguages... exclude) {
        updateValues(asList(valueList), addEmpty, comboBox, exclude);
    }

    public static void updateValues(@NotNull Iterable<String> valueList, final boolean addEmpty, @Nullable JComboBox<String> comboBox, YandexToLanguages... exclude) {
        values = DynamicListAdaptable.updateValues(EMPTY, valueList, addEmpty, YandexToLanguages::new);
        //noinspection unchecked
        ADAPTER.setDefaultValue(values[0]);

        if (comboBox != null) {
            ADAPTER.fillComboBox(comboBox, exclude);
        }
    }

    public static List<String> getDisplayNames() {
        return getDisplayNames(values);
    }

    @Override
    @NotNull
    public String getDisplayName() {
        return displayName;
    }

    @NotNull
    @Override
    public String name() {
        return displayName;
    }

    @Override
    public int getIntValue() {
        return intValue;
    }

    @NotNull
    @Override
    public ComboBoxAdapter<DynamicListAdaptable<YandexToLanguages>> getAdapter() {
        return ADAPTER;
    }

    @NotNull
    @Override
    public DynamicListAdaptable<YandexToLanguages>[] getValues() {
        //noinspection unchecked
        return values;
    }
}
