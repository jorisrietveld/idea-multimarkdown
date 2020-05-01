// Copyright (c) 2015-2020 Vladimir Schneider <vladimir.schneider@gmail.com> Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.vladsch.md.nav.editor.resources

import com.vladsch.md.nav.MdBundle
import com.vladsch.md.nav.editor.text.TextHtmlPanelProvider
import com.vladsch.md.nav.editor.util.HtmlCssResource
import com.vladsch.md.nav.editor.util.HtmlCssResourceProvider

object TextHtmlCssProvider : HtmlCssResourceProvider() {
    val NAME = MdBundle.message("editor.text.html.css.provider.name")
    val ID = "com.vladsch.md.nav.editor.text.html.css"
    override val HAS_PARENT = false
    override val INFO = Info(ID, NAME)
    override val COMPATIBILITY = TextHtmlPanelProvider.COMPATIBILITY

    override val cssResource: HtmlCssResource = HtmlCssResource(INFO, "", "", "")
}
