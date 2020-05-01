// Copyright (c) 2015-2020 Vladimir Schneider <vladimir.schneider@gmail.com> Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.vladsch.md.nav.actions.styling

import com.vladsch.md.nav.actions.handlers.util.CaretContextInfo
import com.vladsch.md.nav.actions.handlers.util.ListItemContext
import com.vladsch.md.nav.actions.styling.util.ElementListBag
import com.vladsch.md.nav.actions.styling.util.ElementType

class ListLooseAction : ListLooseTightAction() {
    override fun isSelected(editContext: CaretContextInfo, elementBag: ElementListBag<ElementType>): Boolean {
        val stats = getLooseTightStats(editContext, elementBag)
        return !stats.canLoosenList && !stats.allSingleItems
    }

    override fun isEnabled(editContext: CaretContextInfo, elementBag: ElementListBag<ElementType>): Boolean {
        val stats = getLooseTightStats(editContext, elementBag)
        return stats.canLoosenList
    }

    override fun performAction(editContext: CaretContextInfo, elementBag: ElementListBag<ElementType>) {
        val listData = listItemData(editContext, elementBag)
        for (list in listData.listOrder) {
            ListItemContext.makeLooseList(editContext, list, listData.listItemsMap[list])
        }
    }
}
