// Copyright (c) 2015-2020 Vladimir Schneider <vladimir.schneider@gmail.com> Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.vladsch.md.nav.psi.element;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MdAttributeIdValue extends MdAttributeValue, MdAnchorTarget, MdReferenceElement, MdReferenceElementIdentifier {
    @Nullable
    String getTypeText();

    MdAttributeIdValue setType(@NotNull String newName, int reason);
}
