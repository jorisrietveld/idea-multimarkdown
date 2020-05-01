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

package com.vladsch.md.nav.testUtil;

import com.vladsch.flexmark.test.util.spec.ResourceResolverManager;
import com.vladsch.flexmark.test.util.spec.ResourceUrlResolver;

public class MdResourceUrlResolvers {
    public static void registerResourceUrlResolvers() {
        ResourceResolverManager.registerUrlResolver(new OutTestClassUrlResolver());
    }

    private static class OutTestClassUrlResolver implements ResourceUrlResolver {
        static final String OUT_TEST = "/out/test/";

        OutTestClassUrlResolver() {}

        @Override
        public String apply(String externalForm) {
            if (ResourceUrlResolver.isFileProtocol(externalForm)) {
                String noFileProtocol = ResourceUrlResolver.removeProtocol(externalForm);

                int pos = noFileProtocol.indexOf(OUT_TEST);
                if (pos > 0) {
                    int pathPos = noFileProtocol.indexOf("/", pos + OUT_TEST.length());
                    if (pathPos > 0) {
                        return noFileProtocol.substring(0, pos) /* + "/" + noFileProtocol.substring(pos + OUT_TEST.length(), pathPos)*/ + "/test/src/" + noFileProtocol.substring(pathPos + 1);
                    }
                }
            }

            return null;
        }
    }
}
