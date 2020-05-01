// Copyright (c) 2015-2020 Vladimir Schneider <vladimir.schneider@gmail.com> Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.vladsch.md.nav.settings;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.Disposer;
import com.vladsch.md.nav.MdBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;

public class MdEditorConfigurable implements SearchableConfigurable {
    @Nullable private MdEditorSettingsForm myForm = null;
    private final MdApplicationSettings myApplicationSettings;

    public MdEditorConfigurable() {
        myApplicationSettings = MdApplicationSettings.getInstance();
    }

    @NotNull
    @Override
    public String getId() {
        return "MarkdownNavigator.Settings.Document";
    }

    @Nullable
    @Override
    public Runnable enableSearch(String option) {
        return null;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return MdBundle.message("settings.markdown.editor.name");
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "com.vladsch.markdown.navigator.settings.editor";
    }

    @NotNull
    @Override
    public JComponent createComponent() {
        return getForm().getComponent();
    }

    @NotNull
    public MdEditorSettingsForm getForm() {
        if (myForm == null) {
            myForm = new MdEditorSettingsForm(myApplicationSettings, RenderingProfileSynchronizer.getInstance(ProjectManager.getInstance().getDefaultProject()));
            MdApplicationSettings.getInstance().addDocumentSettingsUpdater(myForm);
        }
        return myForm;
    }

    @Override
    public boolean isModified() {
        return getForm().isModified(myApplicationSettings);
    }

    @Override
    public void apply() throws ConfigurationException {
        myApplicationSettings.groupNotifications(() -> {
            getForm().apply(myApplicationSettings);
            //myApplicationSettings.notifyOnSettingsChanged();
        });
    }

    @Override
    public void reset() {
        getForm().reset(myApplicationSettings);
    }

    @Override
    public void disposeUIResources() {
        if (myForm != null) {
            MdApplicationSettings.getInstance().removeDocumentSettingsUpdater(myForm);
            Disposer.dispose(myForm);
            myForm = null;
        }
    }
}
