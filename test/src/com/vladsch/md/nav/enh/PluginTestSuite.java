/*
 * Copyright (c) 2015-2020 Vladimir Schneider <vladimir.schneider@gmail.com>, all rights reserved.
 *
 * This code is private property of the copyright holder and cannot be used without
 * having obtained a license or prior written permission of the copyright holder.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package com.vladsch.md.nav.enh;

import com.vladsch.md.nav.TestSuite;
import com.vladsch.md.nav.enh.action.EnhActionsTestSuite;
import com.vladsch.md.nav.enh.language.MdStripTrailingSpacesFilterFactoryTest;
import com.vladsch.md.nav.enh.language.injection.LanguageInjectionSpecTest;
import com.vladsch.md.nav.enh.language.inspection.IntentionSpecTest;
import com.vladsch.md.nav.enh.language.lineMarker.LineMarkerSpecTest;
import com.vladsch.md.nav.enh.parser.MdEnhFlexmarkParserTest;
import com.vladsch.md.nav.enh.parser.cache.MdEnhCachedFileElementsTest;
import com.vladsch.md.nav.flex.FlexmarkPluginTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestSuite.class,

        MdStripTrailingSpacesFilterFactoryTest.class,
        MdEnhCachedFileElementsTest.class,
        MdEnhFlexmarkParserTest.class,
        IntentionSpecTest.class,
        LineMarkerSpecTest.class,
        LanguageInjectionSpecTest.class,
        EnhActionsTestSuite.class,

        FlexmarkPluginTestSuite.class,
})
public class PluginTestSuite {
}
