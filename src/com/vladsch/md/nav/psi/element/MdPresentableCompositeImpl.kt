// Copyright (c) 2015-2020 Vladimir Schneider <vladimir.schneider@gmail.com> Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.vladsch.md.nav.psi.element

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation

open class MdPresentableCompositeImpl(node: ASTNode, val itemPresentation: ItemPresentation) : ASTWrapperPsiElement(node), MdComposite, MdStructureViewPresentableElement {
    override fun getStructureViewPresentation(): ItemPresentation {
        return itemPresentation
    }
}
