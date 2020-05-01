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
package com.vladsch.md.nav.util

import com.vladsch.md.nav.MdPlugin
import com.vladsch.plugin.test.util.ParamRowGenerator
import com.vladsch.plugin.test.util.ParamRowGenerator.ColumnProvider
import com.vladsch.plugin.test.util.ParamRowGenerator.Decorator
import com.vladsch.md.nav.vcs.GitHubLinkResolver
import com.vladsch.md.nav.vcs.MdLinkResolver
import com.vladsch.plugin.util.prefixWith
import com.vladsch.md.nav.testUtil.TestCaseUtils.compareOrderedLists
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class TestLinkResolver_Completion constructor(
    val location: String
    , val containingFile: String
    , val linkRefType: (containingFile: FileRef, linkRef: String, anchor: String?, targetRef: FileRef?, isNormalized: Boolean) -> LinkRef
    , val linkAddress: String
    , val linkAnchor: String?
    , val matchOptions: Int
    , val matchOptionsText: String
    , val multiResolve: List<String>
) {

    val projectResolver: MdLinkResolver.ProjectResolver = MarkdownTestData
    val containingFileRef = FileRef(containingFile)
    val resolver = GitHubLinkResolver(MarkdownTestData, containingFileRef)
    val linkRef = LinkRef.parseLinkRef(containingFileRef, linkAddress + linkAnchor.prefixWith('#'), null, linkRefType)

    @Test
    fun test_Completion() {
        val list = resolver.multiResolve(linkRef, matchOptions)
        val matchText = resolver.getLastMatcher()?.linkLooseMatch

        compareOrderedLists(location + "\n$matchText does not match\n", multiResolve, list.asFilePaths())
    }

    companion object {
        init {
            MdPlugin.RUNNING_TESTS = true
        }

        class RowGenerator(private val lineProvider: LineProvider? = null) : ParamRowGenerator() {
            fun row(
                containingFile: String
                , linkRefType: (containingFile: FileRef, linkRef: String, anchor: String?, targetRef: FileRef?, isNormalized: Boolean) -> LinkRef
                , linkAddress: String
                , linkAnchor: String?
                , matchOptions: Int
                , matchOptionsText: String
                , multiResolve: List<String>
            ): RowGenerator {
                super.row(1, arrayOf(
                    /*  1 */ containingFile,
                    /*  2 */ linkRefType,
                    /*  3 */ linkAddress,
                    /*  4 */ linkAnchor,
                    /*  5 */ matchOptions,
                    /*  6 */ matchOptionsText,
                    /*  7 */ multiResolve
                ),

                    Decorator { _, prefix, suffix -> "$prefix\"$containingFile\"\n$suffix" },
                    lineProvider,
                    ColumnProvider { 39 })
                return this
            }
        }

        @Parameterized.Parameters(name = "{index}: filePath = {1}, linkRef = {3}, matchOptions = {6}")
        @JvmStatic
        fun data(): Collection<Array<Any?>> {
            val data = RowGenerator()
                /* @formatter:off */
                /*            "fullPath"                                                     , "linkType"    , "linkRef", "linkAnchor", "options"                                                , "optionsText"                                              , "multiResolve") */
                /*   0 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::LinkRef     , ".kt"    , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", kotlinFiles.asRemoteURI())
                /*   1 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::LinkRef     , ".md"    , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", markdownRemoteFiles.mdOnly().asRemoteURI())
                /*   2 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::LinkRef     , ".png"   , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , imageFiles.asURI())
                /*   3 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::LinkRef     , ".png"   , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", imageRemoteFiles.asRemoteURI())
                /*   4 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::LinkRef     , ".png"   , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , imageFiles)
                /*   5 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::LinkRef     , ".png"   , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , imageFiles.asURI())
                /*   6 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::LinkRef     , ""       , null        , Want(Local.URI, Remote.URI, Links.URL, Match.COMPLETION) , "Want(Local.URI, Remote.URI, Links.URL, Match.COMPLETION)" , markdownFiles.asURI().with(gitHubLinks))
                /*   7 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::LinkRef     , ""       , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , markdownRemoteFiles)
                /*   8 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::LinkRef     , ".kt"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , kotlinFiles.asURI())
                /*   9 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::LinkRef     , ".md"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , markdownFiles.mdOnly().asURI())
                /*  10 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::LinkRef     , ".kt"    , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , kotlinFiles)
                /*  11 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::LinkRef     , ".md"    , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , markdownFiles.mdOnly())
                /*  12 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::LinkRef     , ".png"   , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , imageRemoteFiles)
                /*  13 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::LinkRef     , ""       , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", markdownRemoteFiles.asRemoteURI().with(gitHubLinks))
                /*  14 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::LinkRef     , ".kt"    , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , kotlinFiles)
                /*  15 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::LinkRef     , ".md"    , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , markdownRemoteFiles.mdOnly())
                /*  16 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::LinkRef     , ""       , null        , Want(Local.REF, Remote.REF, Links.URL, Match.COMPLETION) , "Want(Local.REF, Remote.REF, Links.URL, Match.COMPLETION)" , markdownFiles.with(gitHubLinks))
                /*  17 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::LinkRef     , ".kt"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , kotlinFiles.asURI())
                /*  18 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::LinkRef     , ".md"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , markdownFiles.mdOnly().asURI())
                /*  19 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::LinkRef     , ""       , null        , Want(Local.URI, Remote.URI, Links.URL, Match.COMPLETION) , "Want(Local.URI, Remote.URI, Links.URL, Match.COMPLETION)" , markdownFiles.asURI().with(gitHubLinks))
                /*  20 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::WikiLinkRef , ".kt"    , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", kotlinFilesFromWiki.asRemoteURI())
                /*  21 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::WikiLinkRef , ".md"    , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", markdownRemoteFilesFromWiki.asRemoteURI())
                /*  22 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::WikiLinkRef , ".png"   , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , imageFilesFromWiki.asURI())
                /*  23 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::WikiLinkRef , ".png"   , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", imageRemoteFilesFromWiki.asRemoteURI())
                /*  24 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::WikiLinkRef , ".png"   , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , imageFilesFromWiki)
                /*  25 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::WikiLinkRef , ".png"   , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , imageFilesFromWiki.asURI())
                /*  26 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::WikiLinkRef , ""       , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , markdownFilesFromWiki.asURI())
                /*  27 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::WikiLinkRef , ""       , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , markdownRemoteFilesFromWiki)
                /*  28 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::WikiLinkRef , ".kt"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , kotlinFilesFromWiki.asURI())
                /*  29 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::WikiLinkRef , ".md"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , markdownFilesFromWiki.asURI())
                /*  30 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::WikiLinkRef , ".kt"    , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , kotlinFilesFromWiki)
                /*  31 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::WikiLinkRef , ".md"    , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , markdownFilesFromWiki)
                /*  32 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::WikiLinkRef , ".png"   , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , imageRemoteFilesFromWiki)
                /*  33 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::WikiLinkRef , ""       , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", markdownRemoteFilesFromWiki.asRemoteURI())
                /*  34 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::WikiLinkRef , ".kt"    , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , kotlinFilesFromWiki)
                /*  35 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::WikiLinkRef , ".md"    , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , markdownRemoteFilesFromWiki)
                /*  36 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::WikiLinkRef , ""       , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , markdownFilesFromWiki)
                /*  37 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::WikiLinkRef , ".kt"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , kotlinFilesFromWiki.asURI())
                /*  38 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::WikiLinkRef , ".md"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , markdownFilesFromWiki.asURI())
                /*  39 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::WikiLinkRef , ""       , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , markdownFilesFromWiki.asURI())
                /*  40 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::ImageLinkRef, ".kt"    , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", kotlinFilesFromWiki.asRemoteImageURI())
                /*  41 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::ImageLinkRef, ".md"    , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", markdownRemoteFilesFromWiki.mdOnly().asRemoteImageURI())
                /*  42 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::ImageLinkRef, ".png"   , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , imageFilesFromWiki.asURI())
                /*  43 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::ImageLinkRef, ".png"   , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", imageRemoteFilesFromWiki.asRemoteImageURI())
                /*  44 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::ImageLinkRef, ".png"   , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , imageFilesFromWiki)
                /*  45 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::ImageLinkRef, ".png"   , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , imageFilesFromWiki.asURI())
                /*  46 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::ImageLinkRef, ""       , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , imageFilesFromWiki.asURI())
                /*  47 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::ImageLinkRef, ""       , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , imageRemoteFilesFromWiki)
                /*  48 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::ImageLinkRef, ".kt"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , kotlinFilesFromWiki.asURI())
                /*  49 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::ImageLinkRef, ".md"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , markdownFilesFromWiki.mdOnly().asURI())
                /*  50 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::ImageLinkRef, ".kt"    , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , kotlinFilesFromWiki)
                /*  51 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::ImageLinkRef, ".md"    , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , markdownFilesFromWiki.mdOnly())
                /*  52 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::ImageLinkRef, ".png"   , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , imageRemoteFilesFromWiki)
                /*  53 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::ImageLinkRef, ""       , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", imageRemoteFilesFromWiki.asRemoteImageURI())
                /*  54 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::ImageLinkRef, ".kt"    , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , kotlinFilesFromWiki)
                /*  55 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::ImageLinkRef, ".md"    , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , markdownRemoteFilesFromWiki.mdOnly())
                /*  56 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::ImageLinkRef, ""       , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , imageFilesFromWiki)
                /*  57 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::ImageLinkRef, ".kt"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , kotlinFilesFromWiki.asURI())
                /*  58 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::ImageLinkRef, ".md"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , markdownFilesFromWiki.mdOnly().asURI())
                /*  59 */.row("/Users/vlad/src/MarkdownTest/Readme.md"                       , ::ImageLinkRef, ""       , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , imageFilesFromWiki.asURI())
                /*  60 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::LinkRef     , ".kt"    , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", kotlinFiles.asRemoteURI())
                /*  61 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::LinkRef     , ".md"    , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", markdownRemoteFiles.mdOnly().asRemoteURI())
                /*  62 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::LinkRef     , ".png"   , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , imageFiles.asURI())
                /*  63 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::LinkRef     , ".png"   , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", imageRemoteFiles.asRemoteURI())
                /*  64 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::LinkRef     , ".png"   , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , imageFiles)
                /*  65 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::LinkRef     , ".png"   , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , imageFiles.asURI())
                /*  66 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::LinkRef     , ""       , null        , Want(Local.URI, Remote.URI, Links.URL, Match.COMPLETION) , "Want(Local.URI, Remote.URI, Links.URL, Match.COMPLETION)" , markdownFiles.asURI().with(gitHubLinks))
                /*  67 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::LinkRef     , ""       , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , markdownRemoteFiles)
                /*  68 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::LinkRef     , ".kt"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , kotlinFiles.asURI())
                /*  69 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::LinkRef     , ".md"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , markdownFiles.mdOnly().asURI())
                /*  70 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::LinkRef     , ".kt"    , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , kotlinFiles)
                /*  71 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::LinkRef     , ".md"    , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , markdownFiles.mdOnly())
                /*  72 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::LinkRef     , ".png"   , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , imageRemoteFiles)
                /*  73 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::LinkRef     , ""       , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", markdownRemoteFiles.asRemoteURI().with(gitHubLinks))
                /*  74 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::LinkRef     , ".kt"    , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , kotlinFiles)
                /*  75 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::LinkRef     , ".md"    , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , markdownRemoteFiles.mdOnly())
                /*  76 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::LinkRef     , ""       , null        , Want(Local.REF, Remote.REF, Links.URL, Match.COMPLETION) , "Want(Local.REF, Remote.REF, Links.URL, Match.COMPLETION)" , markdownFiles.with(gitHubLinks))
                /*  77 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::LinkRef     , ".kt"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , kotlinFiles.asURI())
                /*  78 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::LinkRef     , ".md"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , markdownFiles.mdOnly().asURI())
                /*  79 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::LinkRef     , ""       , null        , Want(Local.URI, Remote.URI, Links.URL, Match.COMPLETION) , "Want(Local.URI, Remote.URI, Links.URL, Match.COMPLETION)" , markdownFiles.asURI().with(gitHubLinks))
                /*  80 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::WikiLinkRef , ".kt"    , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", wikiKotlinFiles.asRemoteURI())
                /*  81 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::WikiLinkRef , ".md"    , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", wikiMarkdownRemoteFiles.asRemoteURI())
                /*  82 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::WikiLinkRef , ".png"   , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , wikiImageFiles.asURI())
                /*  83 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::WikiLinkRef , ".png"   , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", wikiImageRemoteFiles.asRemoteURI())
                /*  84 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::WikiLinkRef , ".png"   , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , wikiImageFiles)
                /*  85 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::WikiLinkRef , ".png"   , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , wikiImageFiles.asURI())
                /*  86 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::WikiLinkRef , ""       , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , wikiMarkdownFiles.asURI())
                /*  87 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::WikiLinkRef , ""       , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , wikiMarkdownRemoteFiles)
                /*  88 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::WikiLinkRef , ".kt"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , wikiKotlinFiles.asURI())
                /*  89 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::WikiLinkRef , ".md"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , wikiMarkdownFiles.asURI())
                /*  90 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::WikiLinkRef , ".kt"    , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , wikiKotlinFiles)
                /*  91 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::WikiLinkRef , ".md"    , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , wikiMarkdownFiles)
                /*  92 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::WikiLinkRef , ".png"   , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , wikiImageRemoteFiles)
                /*  93 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::WikiLinkRef , ""       , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", wikiMarkdownRemoteFiles.asRemoteURI())
                /*  94 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::WikiLinkRef , ".kt"    , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , wikiKotlinFiles)
                /*  95 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::WikiLinkRef , ".md"    , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , wikiMarkdownRemoteFiles)
                /*  96 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::WikiLinkRef , ""       , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , wikiMarkdownFiles)
                /*  97 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::WikiLinkRef , ".kt"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , wikiKotlinFiles.asURI())
                /*  98 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::WikiLinkRef , ".md"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , wikiMarkdownFiles.asURI())
                /*  99 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::WikiLinkRef , ""       , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , wikiMarkdownFiles.asURI())
                /* 100 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::ImageLinkRef, ".kt"    , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", kotlinFilesFromWiki.asRemoteImageURI())
                /* 101 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::ImageLinkRef, ".md"    , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", markdownRemoteFilesFromWiki.mdOnly().asRemoteImageURI())
                /* 102 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::ImageLinkRef, ".png"   , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , imageFilesFromWiki.asURI())
                /* 103 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::ImageLinkRef, ".png"   , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", imageRemoteFilesFromWiki.asRemoteImageURI())
                /* 104 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::ImageLinkRef, ".png"   , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , imageFilesFromWiki)
                /* 105 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::ImageLinkRef, ".png"   , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , imageFilesFromWiki.asURI())
                /* 106 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::ImageLinkRef, ""       , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , imageFilesFromWiki.asURI())
                /* 107 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::ImageLinkRef, ""       , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , imageRemoteFilesFromWiki)
                /* 108 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::ImageLinkRef, ".kt"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , kotlinFilesFromWiki.asURI())
                /* 109 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::ImageLinkRef, ".md"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , markdownFilesFromWiki.mdOnly().asURI())
                /* 110 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::ImageLinkRef, ".kt"    , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , kotlinFilesFromWiki)
                /* 111 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::ImageLinkRef, ".md"    , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , markdownFilesFromWiki.mdOnly())
                /* 112 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::ImageLinkRef, ".png"   , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , imageRemoteFilesFromWiki)
                /* 113 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::ImageLinkRef, ""       , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", imageRemoteFilesFromWiki.asRemoteImageURI())
                /* 114 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::ImageLinkRef, ".kt"    , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , kotlinFilesFromWiki)
                /* 115 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::ImageLinkRef, ".md"    , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , markdownRemoteFilesFromWiki.mdOnly())
                /* 116 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::ImageLinkRef, ""       , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , imageFilesFromWiki)
                /* 117 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::ImageLinkRef, ".kt"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , kotlinFilesFromWiki.asURI())
                /* 118 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::ImageLinkRef, ".md"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , markdownFilesFromWiki.mdOnly().asURI())
                /* 119 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md"       , ::ImageLinkRef, ""       , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , imageFilesFromWiki.asURI())
                /* 120 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::LinkRef     , ".kt"    , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", kotlinFiles.asRemoteURI())
                /* 121 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::LinkRef     , ".md"    , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", markdownRemoteFiles.mdOnly().asRemoteURI())
                /* 122 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::LinkRef     , ".png"   , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , imageFiles.asURI())
                /* 123 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::LinkRef     , ".png"   , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", imageRemoteFiles.asRemoteURI())
                /* 124 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::LinkRef     , ".png"   , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , imageFiles)
                /* 125 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::LinkRef     , ".png"   , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , imageFiles.asURI())
                /* 126 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::LinkRef     , ""       , null        , Want(Local.URI, Remote.URI, Links.URL, Match.COMPLETION) , "Want(Local.URI, Remote.URI, Links.URL, Match.COMPLETION)" , markdownFiles.asURI().with(gitHubLinks))
                /* 127 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::LinkRef     , ""       , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , markdownRemoteFiles)
                /* 128 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::LinkRef     , ".kt"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , kotlinFiles.asURI())
                /* 129 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::LinkRef     , ".md"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , markdownFiles.mdOnly().asURI())
                /* 130 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::LinkRef     , ".kt"    , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , kotlinFiles)
                /* 131 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::LinkRef     , ".md"    , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , markdownFiles.mdOnly())
                /* 132 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::LinkRef     , ".png"   , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , imageRemoteFiles)
                /* 133 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::LinkRef     , ""       , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", markdownRemoteFiles.asRemoteURI().with(gitHubLinks))
                /* 134 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::LinkRef     , ".kt"    , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , kotlinFiles)
                /* 135 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::LinkRef     , ".md"    , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , markdownRemoteFiles.mdOnly())
                /* 136 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::LinkRef     , ""       , null        , Want(Local.REF, Remote.REF, Links.URL, Match.COMPLETION) , "Want(Local.REF, Remote.REF, Links.URL, Match.COMPLETION)" , markdownFiles.with(gitHubLinks))
                /* 137 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::LinkRef     , ".kt"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , kotlinFiles.asURI())
                /* 138 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::LinkRef     , ".md"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , markdownFiles.mdOnly().asURI())
                /* 139 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::LinkRef     , ""       , null        , Want(Local.URI, Remote.URI, Links.URL, Match.COMPLETION) , "Want(Local.URI, Remote.URI, Links.URL, Match.COMPLETION)" , markdownFiles.asURI().with(gitHubLinks))
                /* 140 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::WikiLinkRef , ".kt"    , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", wikiKotlinFiles.asRemoteURI())
                /* 141 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::WikiLinkRef , ".md"    , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", wikiMarkdownRemoteFiles.asRemoteURI())
                /* 142 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::WikiLinkRef , ".png"   , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , wikiImageFiles.asURI())
                /* 143 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::WikiLinkRef , ".png"   , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", wikiImageRemoteFiles.asRemoteURI())
                /* 144 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::WikiLinkRef , ".png"   , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , wikiImageFiles)
                /* 145 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::WikiLinkRef , ".png"   , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , wikiImageFiles.asURI())
                /* 146 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::WikiLinkRef , ""       , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , wikiMarkdownFiles.asURI())
                /* 147 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::WikiLinkRef , ""       , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , wikiMarkdownRemoteFiles)
                /* 148 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::WikiLinkRef , ".kt"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , wikiKotlinFiles.asURI())
                /* 149 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::WikiLinkRef , ".md"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , wikiMarkdownFiles.asURI())
                /* 150 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::WikiLinkRef , ".kt"    , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , wikiKotlinFiles)
                /* 151 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::WikiLinkRef , ".md"    , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , wikiMarkdownFiles)
                /* 152 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::WikiLinkRef , ".png"   , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , wikiImageRemoteFiles)
                /* 153 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::WikiLinkRef , ""       , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", wikiMarkdownRemoteFiles.asRemoteURI())
                /* 154 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::WikiLinkRef , ".kt"    , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , wikiKotlinFiles)
                /* 155 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::WikiLinkRef , ".md"    , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , wikiMarkdownRemoteFiles)
                /* 156 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::WikiLinkRef , ""       , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , wikiMarkdownFiles)
                /* 157 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::WikiLinkRef , ".kt"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , wikiKotlinFiles.asURI())
                /* 158 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::WikiLinkRef , ".md"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , wikiMarkdownFiles.asURI())
                /* 159 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::WikiLinkRef , ""       , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , wikiMarkdownFiles.asURI())
                /* 160 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::ImageLinkRef, ".kt"    , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", kotlinFilesFromWiki.asRemoteImageURI())
                /* 161 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::ImageLinkRef, ".md"    , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", markdownRemoteFilesFromWiki.mdOnly().asRemoteImageURI())
                /* 162 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::ImageLinkRef, ".png"   , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , imageFilesFromWiki.asURI())
                /* 163 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::ImageLinkRef, ".png"   , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", imageRemoteFilesFromWiki.asRemoteImageURI())
                /* 164 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::ImageLinkRef, ".png"   , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , imageFilesFromWiki)
                /* 165 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::ImageLinkRef, ".png"   , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , imageFilesFromWiki.asURI())
                /* 166 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::ImageLinkRef, ""       , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , imageFilesFromWiki.asURI())
                /* 167 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::ImageLinkRef, ""       , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , imageRemoteFilesFromWiki)
                /* 168 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::ImageLinkRef, ".kt"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , kotlinFilesFromWiki.asURI())
                /* 169 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::ImageLinkRef, ".md"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , markdownFilesFromWiki.mdOnly().asURI())
                /* 170 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::ImageLinkRef, ".kt"    , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , kotlinFilesFromWiki)
                /* 171 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::ImageLinkRef, ".md"    , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , markdownFilesFromWiki.mdOnly())
                /* 172 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::ImageLinkRef, ".png"   , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , imageRemoteFilesFromWiki)
                /* 173 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::ImageLinkRef, ""       , null        , Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION), "Want(Local.NONE, Remote.URL, Links.URL, Match.COMPLETION)", imageRemoteFilesFromWiki.asRemoteImageURI())
                /* 174 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::ImageLinkRef, ".kt"    , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , kotlinFilesFromWiki)
                /* 175 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::ImageLinkRef, ".md"    , null        , Want(Local.NONE, Remote.REF, Match.COMPLETION)           , "Want(Local.NONE, Remote.REF, Match.COMPLETION)"           , markdownRemoteFilesFromWiki.mdOnly())
                /* 176 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::ImageLinkRef, ""       , null        , Want(Local.REF, Remote.REF, Match.COMPLETION)            , "Want(Local.REF, Remote.REF, Match.COMPLETION)"            , imageFilesFromWiki)
                /* 177 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::ImageLinkRef, ".kt"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , kotlinFilesFromWiki.asURI())
                /* 178 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::ImageLinkRef, ".md"    , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , markdownFilesFromWiki.mdOnly().asURI())
                /* 179 */.row("/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md", ::ImageLinkRef, ""       , null        , Want(Local.URI, Remote.URI, Match.COMPLETION)            , "Want(Local.URI, Remote.URI, Match.COMPLETION)"            , imageFilesFromWiki.asURI())
                /* @formatter:on */
                .rows
            return data
        }
    }
}
