// Copyright (c) 2015-2023 Vladimir Schneider <vladimir.schneider@gmail.com> Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.vladsch.md.nav.psi.element

import com.intellij.psi.stubs.StubElement
import com.vladsch.md.nav.psi.util.MdTypes

class MdWikiLinkStubImpl(parent: StubElement<*>, linkRefWithAnchor: String) :
    MdLinkElementStubImpl<MdWikiLink, MdWikiLinkStub>(parent, MdTypes.WIKI_LINK, linkRefWithAnchor),
    MdWikiLinkStub
