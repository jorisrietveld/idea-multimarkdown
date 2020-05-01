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
package com.vladsch.md.nav.flex;

import com.vladsch.md.nav.flex.language.FlexLanguageTestSuite;
import com.vladsch.md.nav.flex.parser.FlexmarkCachedFileElementsTest;
import com.vladsch.md.nav.flex.parser.FlexmarkParserTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TemplateTest.class,
        FlexLanguageTestSuite.class,
        FlexmarkCachedFileElementsTest.class,
        FlexmarkParserTest.class,
})
public class FlexmarkPluginTestSuite {
}
