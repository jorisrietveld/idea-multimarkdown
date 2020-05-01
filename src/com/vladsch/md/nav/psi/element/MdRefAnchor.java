// Copyright (c) 2015-2020 Vladimir Schneider <vladimir.schneider@gmail.com> Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.vladsch.md.nav.psi.element;

import org.jetbrains.annotations.Nullable;

public interface MdRefAnchor extends MdReferenceElement, MdAnchorTarget {

    @Nullable
    String getAnchorText();

    @Nullable
    MdRefAnchorId getReferenceIdentifier();
}
