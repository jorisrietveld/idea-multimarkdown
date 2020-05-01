// Copyright (c) 2015-2020 Vladimir Schneider <vladimir.schneider@gmail.com> Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.vladsch.md.nav.psi.element

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement

open class MdJekyllIncludeStubImpl(
    parent: StubElement<*>,
    elementType: IStubElementType<MdJekyllIncludeStub, MdJekyllInclude>,
    private val myLinkRef: String
) : StubBase<MdJekyllInclude>(parent, elementType), MdJekyllIncludeStub {

    final override fun getLinkRefWithAnchorText(): String {
        return myLinkRef
    }
}
