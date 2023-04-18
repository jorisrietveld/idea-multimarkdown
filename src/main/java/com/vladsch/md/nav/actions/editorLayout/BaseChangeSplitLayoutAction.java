// Copyright (c) 2015-2023 Vladimir Schneider <vladimir.schneider@gmail.com> Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.vladsch.md.nav.actions.editorLayout;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Toggleable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbAware;
import com.vladsch.md.nav.actions.styling.util.MdActionUtil;
import com.vladsch.md.nav.editor.split.SplitFileEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class BaseChangeSplitLayoutAction extends AnAction implements DumbAware, Toggleable {
    @Nullable
    private final SplitFileEditor.SplitEditorLayout myLayoutToSet;

    protected BaseChangeSplitLayoutAction(@Nullable SplitFileEditor.SplitEditorLayout layoutToSet) {
        myLayoutToSet = layoutToSet;
    }

    @Override
    public void update(@NotNull AnActionEvent event) {
        final SplitFileEditor<?, ?> splitFileEditor = MdActionUtil.INSTANCE.findSplitEditor(event);

        if (splitFileEditor != null) {
            if (myLayoutToSet != null) event.getPresentation().putClientProperty(SELECTED_PROPERTY, splitFileEditor.getCurrentEditorLayout() == myLayoutToSet);
            event.getPresentation().setEnabled(true);
        } else {
            event.getPresentation().setEnabled(false);
        }
    }

    protected void doAction(@NotNull SplitFileEditor<?, ?> splitFileEditor) {
        splitFileEditor.triggerLayoutChange();
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        final SplitFileEditor<?, ?> splitFileEditor = MdActionUtil.INSTANCE.findSplitEditor(event);

        if (splitFileEditor != null) {
            if (myLayoutToSet == null) {
                doAction(splitFileEditor);
            } else {
                splitFileEditor.triggerLayoutChange(myLayoutToSet);
                event.getPresentation().putClientProperty(SELECTED_PROPERTY, true);
            }

            ApplicationManager.getApplication().invokeLater(splitFileEditor::takeFocus);
        }
    }
}
