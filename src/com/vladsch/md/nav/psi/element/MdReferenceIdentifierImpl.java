// Copyright (c) 2015-2020 Vladimir Schneider <vladimir.schneider@gmail.com> Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.vladsch.md.nav.psi.element;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.vladsch.md.nav.psi.reference.MdPsiReference;
import icons.MdIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

public class MdReferenceIdentifierImpl extends MdLinkTextImpl implements MdReferenceIdentifier {
    @Override
    public PsiElement getReferenceElement() {
        return getParent();
    }

    @Override
    public MdPsiReference createReference(@NotNull TextRange textRange, final boolean exactReference) {
        return null;
    }

    public MdReferenceIdentifierImpl(ASTNode node) {
        super(node);
    }

    @Override
    public PsiElement getNameIdentifier() {
        return this;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "[" + getName() + "]";
    }

    @Nullable
    @Override
    public Icon getIcon(int flags) {
        return MdIcons.Element.REFERENCE;
    }

    @Override
    public boolean isMemberInplaceRenameAvailable(@Nullable final PsiElement context) {
        return true;
    }

    @Override
    public String toString() {
        return "REFERENCE_TEXT '" + getName() + "' " + super.hashCode();
    }
}
