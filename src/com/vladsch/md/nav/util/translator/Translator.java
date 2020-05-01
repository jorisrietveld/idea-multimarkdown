// Copyright (c) 2015-2020 Vladimir Schneider <vladimir.schneider@gmail.com> Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.vladsch.md.nav.util.translator;

import java.util.List;

public interface Translator {
    List<String> translate(String splitText, String fromLang, String toLang, boolean autoDetect);

    List<String> translate(List<String> splitText, String fromLang, String toLang, boolean autoDetect);

    int getMaxTextLength();
}
