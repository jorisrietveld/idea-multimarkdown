/*
 * Copyright (c) 2015-2023 Vladimir Schneider <vladimir.schneider@gmail.com>, all rights reserved.
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

import com.intellij.openapi.project.Project
import com.vladsch.md.nav.testUtil.TestCaseUtils
import com.vladsch.md.nav.vcs.GitHubVcsRoot
import com.vladsch.md.nav.vcs.MdLinkResolver
import com.vladsch.plugin.util.ifEmpty
import com.vladsch.plugin.util.suffixWith
import java.util.*

val data = ArrayList<Array<Any?>>()
val cleanData = true

object MarkdownTestData : MdLinkResolver.ProjectResolver {

    val mainGitHubRepo = GitHubVcsRoot.create("https://github.com/vsch/MarkdownTest", "/Users/vlad/src/MarkdownTest")
    val wikiGitHubRepo = GitHubVcsRoot.create("https://github.com/vsch/MarkdownTest", "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki")

    override fun isUnderVcs(fileRef: FileRef): Boolean {
        return fileRef.filePath !in nonVcsFiles
    }

    override fun isUnderVcsSynced(fileRef: FileRef): Boolean {
        return fileRef.filePath !in nonVcsFiles
    }

    override fun getVcsRoot(fileRef: FileRef): GitHubVcsRoot? {
        return getGitHubRepo(fileRef.filePath)
    }

    override fun getVcsRootForUrl(url: String): GitHubVcsRoot? {
        val urlPath = PathInfo(url).path.suffixWith('/')
        return if (urlPath.startsWith(wikiGitHubRepo.baseUrl.suffixWith('/'))) mainGitHubRepo
        else if (urlPath.startsWith(mainGitHubRepo.baseUrl.suffixWith('/').suffixWith("wiki/"))) wikiGitHubRepo
        else null
    }

    override fun getGitHubRepo(path: String?): GitHubVcsRoot? {
        val filePath = path ?: return null
        return if (filePath.suffixWith('/').startsWith(wikiGitHubRepo.basePath.suffixWith('/'))) wikiGitHubRepo
        else if (filePath.suffixWith('/').startsWith(mainGitHubRepo.basePath.suffixWith('/'))) mainGitHubRepo
        else null
    }

    override val projectBasePath: String
        get() = mainGitHubRepo.basePath

    override val project: Project?
        get() = null

    override fun vcsRepoBasePath(fileRef: FileRef): String? {
        return getVcsRoot(fileRef)?.mainRepoBaseDir
    }

    override fun vcsRootBase(fileRef: FileRef): String? {
        return getVcsRoot(fileRef)?.basePath
    }

    override fun projectFileList(fileTypes: List<String>?): List<FileRef>? {
        val matched = ArrayList<FileRef>()
        if (fileTypes != null) {
            val extSet = HashSet<String>()

            for (ext in fileTypes) {
                val cleanExt = ext.removePrefix(".")
                when (cleanExt) {
                    in PathInfo.MARKDOWN_EXTENSIONS -> extSet.addAll(PathInfo.MARKDOWN_EXTENSIONS)
                    in PathInfo.IMAGE_EXTENSIONS -> extSet.addAll(PathInfo.IMAGE_EXTENSIONS)
                    else -> extSet.add(cleanExt)
                }
            }

            for (file in fileList) {
                if (file.ext in extSet) {
                    matched.add(file)
                }
            }
        }
        return matched
    }

    val fileList: List<FileRef> by lazy {
        val fileList = ArrayList<FileRef>()
        for (path in filePaths) {
            fileList.add(FileRef(path))
        }
        fileList
    }

    val nonVcsFiles = arrayOf(
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/non-vcs-image.png",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Non-Vcs-Page.md",
        "/Users/vlad/src/MarkdownTest/SubDirectory/sub-dir-non-vcs-image.png",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/sub-dir-non-vcs-image.png",
        "/Users/vlad/src/MarkdownTest/SubDirectory/NonVcsNestedFile.md",
        "/Users/vlad/src/MarkdownTest/non-vcs-image.png"
    )

    val filePaths = arrayOf(
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/File-In-Subdirectory.md",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/In-Name.md",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/Multiple-Match.md",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/sub-dir-non-vcs-image.png",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/sub-dir-vcs-image.png",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/anchor-in-name#5.md",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/anchor-in-name.md#5",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Multiple-Match.markdown",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Multiple-Match.md",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Multiple-Match.mkd",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/non-vcs-image.png",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Non-Vcs-Page.md",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Not-Wiki-Ext.mkd",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Not-Wiki-Ext-2.markdown",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/single-link-test.md",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Space In Name.md",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Space In Name#6.md",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Test.kt",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/Test2.kt",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/Sub-Test.kt",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Test 4.2.md",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Test-Name.kt",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Test-Name.kt.md",
        "/Users/vlad/src/MarkdownTest/Test.kt",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/vcs-image.png",
        "/Users/vlad/src/MarkdownTest/SubDirectory/NestedFile.md",
        "/Users/vlad/src/MarkdownTest/SubDirectory/Sub/NestedFile2.md",
        "/Users/vlad/src/MarkdownTest/SubDirectory/NestedFile#5.md",
        "/Users/vlad/src/MarkdownTest/SubDirectory/NestedFile.md#5",
        "/Users/vlad/src/MarkdownTest/SubDirectory/NonVcsNestedFile.md",
        "/Users/vlad/src/MarkdownTest/SubDirectory/Test.kt",
        "/Users/vlad/src/MarkdownTest/SubDirectory/sub-dir-non-vcs-image.png",
        "/Users/vlad/src/MarkdownTest/SubDirectory/sub-dir-vcs-image.png",
        "/Users/vlad/src/MarkdownTest/SubDirectory/Multiple-Match.md",
        "/Users/vlad/src/MarkdownTest/Multiple-Match.markdown",
        "/Users/vlad/src/MarkdownTest/Multiple-Match.md",
        "/Users/vlad/src/MarkdownTest/Multiple-Match.mkd",
        "/Users/vlad/src/MarkdownTest/untitled/README.md",
        "/Users/vlad/src/MarkdownTest/untitled/untitled.iml",
        "/Users/vlad/src/MarkdownTest/anchor-in-name#5.md",
        "/Users/vlad/src/MarkdownTest/MarkdownTest.iml",
        "/Users/vlad/src/MarkdownTest/non-vcs-image.png",
        "/Users/vlad/src/MarkdownTest/NonWikiFile.md",
        "/Users/vlad/src/MarkdownTest/Readme.md",
        "/Users/vlad/src/MarkdownTest/Rendering-Sanity-Test.md",
        "/Users/vlad/src/MarkdownTest/single-link-test.md",
        "/Users/vlad/src/MarkdownTest/vcs-image.png",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/autoNumeric/autoNumeric-2.0/autoNumeric-2.0-BETA change log.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/ChangeLog.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-datepicker/README.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-datepicker/tests/README.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-sass/CONTRIBUTING.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/cropper/README.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/CONTRIBUTING.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/cropper/LICENSE.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/cropper/CHANGELOG.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Extras.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/README.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/autoNumeric/readme.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-datepicker/CHANGELOG.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Installing.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Version 4 Contributors guide.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-modal/README.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/autoNumeric/change log.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Functions.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/ContributorsGuide.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Options.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-sass/CHANGELOG.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Events.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/autoNumeric/autoNumeric-2.0/readme.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/index.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-datepicker/CONTRIBUTING.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap/README.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/FAQ.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Version 4 Changelog.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/cropper/CONTRIBUTING.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-datepicker/docs/README.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-daterangepicker/README.md",
        "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-sass/README.md"
    )
}

fun List<PathInfo>.asFilePaths(): List<String> {
    return this.map { it.filePath }
}

fun List<String>.asURI(): List<String> {
    return this.map { "file://" + it.replace(" ", "%20").replace("#", "%23") }
}

fun List<String>.mdOnly(): List<String> {
    return this.filter { it.endsWith(".md") }
}

fun List<String>.with(list: List<String>): List<String> {
    val result = ArrayList<String>()
    result.addAll(this)
    result.addAll(list)
    return result
}

fun List<String>.asRemoteImageURI(branchOrTag: String? = null): List<String> {
    return this.asRemoteUriType(branchOrTag, "raw")
}

fun List<String>.asRemoteURI(branchOrTag: String? = null): List<String> {
    return this.asRemoteUriType(branchOrTag, "blob")
}

fun List<String>.asRemoteUriType(branchOrTag: String? = null, gitHubLink: String?): List<String> {
    val _branchOrTag = branchOrTag ?: "master"
    val projectResolver: MdLinkResolver.ProjectResolver = MarkdownTestData
    val result = this.map {
        val fileRef = FileRef(it)

        val vcsRoot = projectResolver.getVcsRoot(fileRef)
        if (projectResolver.isUnderVcs(fileRef) && vcsRoot != null) {
            val baseUrl = vcsRoot.baseUrl
            val basePath = vcsRoot.basePath
            val fileName = it.substring(basePath.length)
            val fileInfo = PathInfo(fileName)
            val fileNameNoExt = fileInfo.fileNameNoExt

            if (fileRef.isUnderWikiDir) {
                if (fileName == "Home.md") {
                    baseUrl.suffixWith('/') + "wiki"
                } else if (fileInfo.isMarkdownExt) {
                    baseUrl.suffixWith('/') + "wiki/" + fileNameNoExt.replace(" ", "%20").replace("#", "%23")
                } else {
                    baseUrl.suffixWith('/') + "wiki/" + fileName.replace(" ", "%20").replace("#", "%23")
                }
            } else {
                baseUrl.suffixWith('/') + gitHubLink.ifEmpty("blob") + "/$_branchOrTag/" + fileName.replace(" ", "%20").replace("#", "%23")
            }
        } else {
            "file://$it"
        }
    }
    return result.filterIndexed { index, s -> !result.subList(0, index).contains(s) }
}

val EMPTY_LIST = arrayListOf<String>()

val gitHubLinks = arrayListOf(
    "https://github.com/vsch/MarkdownTest/fork",
    "https://github.com/vsch/MarkdownTest/graphs",
    "https://github.com/vsch/MarkdownTest/issues",
    "https://github.com/vsch/MarkdownTest/labels",
    "https://github.com/vsch/MarkdownTest/milestones",
    "https://github.com/vsch/MarkdownTest/pulls",
    "https://github.com/vsch/MarkdownTest/pulse"
)

val wikiImageRemoteFiles = arrayListOf<String>(
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/sub-dir-vcs-image.png",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/vcs-image.png"
)

val wikiImageFiles = arrayListOf<String>(
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/sub-dir-non-vcs-image.png",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/sub-dir-vcs-image.png",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/non-vcs-image.png",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/vcs-image.png"
)

val imageRemoteFiles = arrayListOf<String>(
    "/Users/vlad/src/MarkdownTest/SubDirectory/sub-dir-vcs-image.png",
    "/Users/vlad/src/MarkdownTest/vcs-image.png",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/sub-dir-vcs-image.png",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/vcs-image.png"
)

val imageRemoteFilesFromWiki = arrayListOf<String>(
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/sub-dir-vcs-image.png",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/vcs-image.png",
    "/Users/vlad/src/MarkdownTest/SubDirectory/sub-dir-vcs-image.png",
    "/Users/vlad/src/MarkdownTest/vcs-image.png"
)

val imageFiles = arrayListOf<String>(
    "/Users/vlad/src/MarkdownTest/SubDirectory/sub-dir-non-vcs-image.png",
    "/Users/vlad/src/MarkdownTest/SubDirectory/sub-dir-vcs-image.png",
    "/Users/vlad/src/MarkdownTest/non-vcs-image.png",
    "/Users/vlad/src/MarkdownTest/vcs-image.png",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/sub-dir-non-vcs-image.png",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/sub-dir-vcs-image.png",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/non-vcs-image.png",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/vcs-image.png"
)

val imageFilesFromWiki = arrayListOf<String>(
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/sub-dir-non-vcs-image.png",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/sub-dir-vcs-image.png",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/non-vcs-image.png",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/vcs-image.png",
    "/Users/vlad/src/MarkdownTest/SubDirectory/sub-dir-non-vcs-image.png",
    "/Users/vlad/src/MarkdownTest/SubDirectory/sub-dir-vcs-image.png",
    "/Users/vlad/src/MarkdownTest/non-vcs-image.png",
    "/Users/vlad/src/MarkdownTest/vcs-image.png"
)

val wikiKotlinFiles = arrayListOf<String>(
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/Sub-Test.kt",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/Test2.kt",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Test-Name.kt",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Test.kt"
)

val kotlinFiles = arrayListOf<String>(
    "/Users/vlad/src/MarkdownTest/SubDirectory/Test.kt",
    "/Users/vlad/src/MarkdownTest/Test.kt",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/Sub-Test.kt",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/Test2.kt",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Test-Name.kt",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Test.kt"
)

val kotlinFilesFromWiki = arrayListOf<String>(
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/Sub-Test.kt",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/Test2.kt",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Test-Name.kt",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Test.kt",
    "/Users/vlad/src/MarkdownTest/SubDirectory/Test.kt",
    "/Users/vlad/src/MarkdownTest/Test.kt"
)

val wikiMarkdownRemoteFiles = arrayListOf<String>(
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Multiple-Match.markdown",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Multiple-Match.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Multiple-Match.mkd",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Not-Wiki-Ext-2.markdown",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Not-Wiki-Ext.mkd",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Space In Name#6.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Space In Name.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/File-In-Subdirectory.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/In-Name.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/Multiple-Match.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Test 4.2.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Test-Name.kt.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/anchor-in-name#5.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/single-link-test.md"
)

val wikiMarkdownFiles = arrayListOf<String>(
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Multiple-Match.markdown",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Multiple-Match.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Multiple-Match.mkd",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Non-Vcs-Page.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Not-Wiki-Ext-2.markdown",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Not-Wiki-Ext.mkd",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Space In Name#6.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Space In Name.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/File-In-Subdirectory.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/In-Name.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/Multiple-Match.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Test 4.2.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Test-Name.kt.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/anchor-in-name#5.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/single-link-test.md"
)

val markdownRemoteFiles = arrayListOf<String>(
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/autoNumeric/autoNumeric-2.0/autoNumeric-2.0-BETA change log.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/autoNumeric/autoNumeric-2.0/readme.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/autoNumeric/change log.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/autoNumeric/readme.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-datepicker/CHANGELOG.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-datepicker/CONTRIBUTING.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-datepicker/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-datepicker/docs/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-datepicker/tests/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-daterangepicker/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-modal/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-sass/CHANGELOG.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-sass/CONTRIBUTING.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-sass/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/cropper/CHANGELOG.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/cropper/CONTRIBUTING.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/cropper/LICENSE.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/cropper/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/CONTRIBUTING.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/ChangeLog.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/ContributorsGuide.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Events.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Extras.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/FAQ.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Functions.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Installing.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Options.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Version 4 Changelog.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Version 4 Contributors guide.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/index.md",
    "/Users/vlad/src/MarkdownTest/Multiple-Match.markdown",
    "/Users/vlad/src/MarkdownTest/Multiple-Match.md",
    "/Users/vlad/src/MarkdownTest/Multiple-Match.mkd",
    "/Users/vlad/src/MarkdownTest/NonWikiFile.md",
    "/Users/vlad/src/MarkdownTest/Readme.md",
    "/Users/vlad/src/MarkdownTest/Rendering-Sanity-Test.md",
    "/Users/vlad/src/MarkdownTest/SubDirectory/Multiple-Match.md",
    "/Users/vlad/src/MarkdownTest/SubDirectory/NestedFile#5.md",
    "/Users/vlad/src/MarkdownTest/SubDirectory/NestedFile.md",
    "/Users/vlad/src/MarkdownTest/SubDirectory/Sub/NestedFile2.md",
    "/Users/vlad/src/MarkdownTest/anchor-in-name#5.md",
    "/Users/vlad/src/MarkdownTest/single-link-test.md",
    "/Users/vlad/src/MarkdownTest/untitled/README.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Multiple-Match.markdown",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Multiple-Match.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Multiple-Match.mkd",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Not-Wiki-Ext-2.markdown",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Not-Wiki-Ext.mkd",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Space In Name#6.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Space In Name.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/File-In-Subdirectory.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/In-Name.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/Multiple-Match.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Test 4.2.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Test-Name.kt.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/anchor-in-name#5.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/single-link-test.md"
)

val markdownRemoteFilesFromWiki = arrayListOf<String>(
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/autoNumeric/autoNumeric-2.0/autoNumeric-2.0-BETA change log.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/autoNumeric/autoNumeric-2.0/readme.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/autoNumeric/change log.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/autoNumeric/readme.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-datepicker/CHANGELOG.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-datepicker/CONTRIBUTING.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-datepicker/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-datepicker/docs/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-datepicker/tests/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-daterangepicker/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-modal/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-sass/CHANGELOG.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-sass/CONTRIBUTING.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-sass/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/cropper/CHANGELOG.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/cropper/CONTRIBUTING.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/cropper/LICENSE.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/cropper/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/CONTRIBUTING.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/ChangeLog.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/ContributorsGuide.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Events.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Extras.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/FAQ.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Functions.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Installing.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Options.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Version 4 Changelog.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Version 4 Contributors guide.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/index.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Multiple-Match.markdown",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Multiple-Match.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Multiple-Match.mkd",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Not-Wiki-Ext-2.markdown",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Not-Wiki-Ext.mkd",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Space In Name#6.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Space In Name.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/File-In-Subdirectory.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/In-Name.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/Multiple-Match.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Test 4.2.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Test-Name.kt.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/anchor-in-name#5.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/single-link-test.md",
    "/Users/vlad/src/MarkdownTest/Multiple-Match.markdown",
    "/Users/vlad/src/MarkdownTest/Multiple-Match.md",
    "/Users/vlad/src/MarkdownTest/Multiple-Match.mkd",
    "/Users/vlad/src/MarkdownTest/NonWikiFile.md",
    "/Users/vlad/src/MarkdownTest/Readme.md",
    "/Users/vlad/src/MarkdownTest/Rendering-Sanity-Test.md",
    "/Users/vlad/src/MarkdownTest/SubDirectory/Multiple-Match.md",
    "/Users/vlad/src/MarkdownTest/SubDirectory/NestedFile#5.md",
    "/Users/vlad/src/MarkdownTest/SubDirectory/NestedFile.md",
    "/Users/vlad/src/MarkdownTest/SubDirectory/Sub/NestedFile2.md",
    "/Users/vlad/src/MarkdownTest/anchor-in-name#5.md",
    "/Users/vlad/src/MarkdownTest/single-link-test.md",
    "/Users/vlad/src/MarkdownTest/untitled/README.md"
)

val markdownFiles = arrayListOf<String>(
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/autoNumeric/autoNumeric-2.0/autoNumeric-2.0-BETA change log.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/autoNumeric/autoNumeric-2.0/readme.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/autoNumeric/change log.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/autoNumeric/readme.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-datepicker/CHANGELOG.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-datepicker/CONTRIBUTING.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-datepicker/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-datepicker/docs/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-datepicker/tests/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-daterangepicker/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-modal/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-sass/CHANGELOG.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-sass/CONTRIBUTING.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-sass/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/cropper/CHANGELOG.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/cropper/CONTRIBUTING.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/cropper/LICENSE.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/cropper/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/CONTRIBUTING.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/ChangeLog.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/ContributorsGuide.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Events.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Extras.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/FAQ.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Functions.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Installing.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Options.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Version 4 Changelog.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Version 4 Contributors guide.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/index.md",
    "/Users/vlad/src/MarkdownTest/Multiple-Match.markdown",
    "/Users/vlad/src/MarkdownTest/Multiple-Match.md",
    "/Users/vlad/src/MarkdownTest/Multiple-Match.mkd",
    "/Users/vlad/src/MarkdownTest/NonWikiFile.md",
    "/Users/vlad/src/MarkdownTest/Readme.md",
    "/Users/vlad/src/MarkdownTest/Rendering-Sanity-Test.md",
    "/Users/vlad/src/MarkdownTest/SubDirectory/Multiple-Match.md",
    "/Users/vlad/src/MarkdownTest/SubDirectory/NestedFile#5.md",
    "/Users/vlad/src/MarkdownTest/SubDirectory/NestedFile.md",
    "/Users/vlad/src/MarkdownTest/SubDirectory/NonVcsNestedFile.md",
    "/Users/vlad/src/MarkdownTest/SubDirectory/Sub/NestedFile2.md",
    "/Users/vlad/src/MarkdownTest/anchor-in-name#5.md",
    "/Users/vlad/src/MarkdownTest/single-link-test.md",
    "/Users/vlad/src/MarkdownTest/untitled/README.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Multiple-Match.markdown",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Multiple-Match.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Multiple-Match.mkd",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Non-Vcs-Page.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Not-Wiki-Ext-2.markdown",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Not-Wiki-Ext.mkd",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Space In Name#6.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Space In Name.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/File-In-Subdirectory.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/In-Name.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/Multiple-Match.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Test 4.2.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Test-Name.kt.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/anchor-in-name#5.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/single-link-test.md"
)

val markdownFilesFromWiki = arrayListOf<String>(
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/autoNumeric/autoNumeric-2.0/autoNumeric-2.0-BETA change log.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/autoNumeric/autoNumeric-2.0/readme.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/autoNumeric/change log.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/autoNumeric/readme.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-datepicker/CHANGELOG.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-datepicker/CONTRIBUTING.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-datepicker/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-datepicker/docs/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-datepicker/tests/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-daterangepicker/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-modal/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-sass/CHANGELOG.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-sass/CONTRIBUTING.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap-sass/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/bootstrap/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/cropper/CHANGELOG.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/cropper/CONTRIBUTING.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/cropper/LICENSE.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/cropper/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/CONTRIBUTING.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/README.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/ChangeLog.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/ContributorsGuide.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Events.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Extras.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/FAQ.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Functions.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Installing.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Options.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Version 4 Changelog.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/Version 4 Contributors guide.md",
    "/Users/vlad/src/MarkdownTest/GitHubIssues/Issue-46/webbeheer_package/bower_components/eonasdan-bootstrap-datetimepicker/docs/index.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Home.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Multiple-Match.markdown",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Multiple-Match.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Multiple-Match.mkd",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Non-Vcs-Page.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Not-Wiki-Ext-2.markdown",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Not-Wiki-Ext.mkd",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Space In Name#6.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Space In Name.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/File-In-Subdirectory.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/In-Name.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/SubDirectory/Multiple-Match.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Test 4.2.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/Test-Name.kt.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/anchor-in-name#5.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/normal-file.md",
    "/Users/vlad/src/MarkdownTest/MarkdownTest.wiki/single-link-test.md",
    "/Users/vlad/src/MarkdownTest/Multiple-Match.markdown",
    "/Users/vlad/src/MarkdownTest/Multiple-Match.md",
    "/Users/vlad/src/MarkdownTest/Multiple-Match.mkd",
    "/Users/vlad/src/MarkdownTest/NonWikiFile.md",
    "/Users/vlad/src/MarkdownTest/Readme.md",
    "/Users/vlad/src/MarkdownTest/Rendering-Sanity-Test.md",
    "/Users/vlad/src/MarkdownTest/SubDirectory/Multiple-Match.md",
    "/Users/vlad/src/MarkdownTest/SubDirectory/NestedFile#5.md",
    "/Users/vlad/src/MarkdownTest/SubDirectory/NestedFile.md",
    "/Users/vlad/src/MarkdownTest/SubDirectory/NonVcsNestedFile.md",
    "/Users/vlad/src/MarkdownTest/SubDirectory/Sub/NestedFile2.md",
    "/Users/vlad/src/MarkdownTest/anchor-in-name#5.md",
    "/Users/vlad/src/MarkdownTest/single-link-test.md",
    "/Users/vlad/src/MarkdownTest/untitled/README.md"
)

fun printResultData() {
    if (cleanData) {
        val header = arrayOf(
            "fullPath",
            "linkType",
            "linkRef",
            "linkAnchor",
            "options",
            "optionsText",
            "multiResolve"
        )

        printData(data, header)
    }
}

fun matchOptions(matchOptions: Int): String {
    return Want.testData(matchOptions)
}

val availableLists = mapOf<List<String>, String>(
    Pair(EMPTY_LIST, "EMPTY_LIST"),
    Pair(gitHubLinks, "gitHubLinks"),
    Pair(markdownFiles, "markdownFiles"),
    Pair(wikiImageRemoteFiles, "wikiImageRemoteFiles"),
    Pair(wikiImageFiles, "wikiImageFiles"),
    Pair(imageRemoteFiles, "imageRemoteFiles"),
    Pair(imageFiles, "imageFiles"),
    Pair(wikiKotlinFiles, "wikiKotlinFiles"),
    Pair(kotlinFiles, "kotlinFiles"),
    Pair(wikiMarkdownRemoteFiles, "wikiMarkdownRemoteFiles"),
    Pair(wikiMarkdownFiles, "wikiMarkdownFiles"),
    Pair(markdownRemoteFiles, "markdownRemoteFiles"),

    Pair(markdownFilesFromWiki, "markdownFilesFromWiki"),
    Pair(imageRemoteFilesFromWiki, "imageRemoteFilesFromWiki"),
    Pair(imageFilesFromWiki, "imageFilesFromWiki"),
    Pair(kotlinFilesFromWiki, "kotlinFilesFromWiki"),
    Pair(markdownRemoteFilesFromWiki, "markdownRemoteFilesFromWiki")
)

val availablePermutations = mapOf<(List<String>) -> List<String>, String>(
    Pair({ it -> it.with(gitHubLinks) }, ".with(gitHubLinks)"),
    Pair({ it -> it.mdOnly() }, ".mdOnly()"),
    Pair({ it -> it.asURI() }, ".asURI()"),
    Pair({ it -> it.asRemoteURI() }, ".asRemoteURI()"),
    Pair({ it -> it.asRemoteImageURI() }, ".asRemoteImageURI()"),
    Pair({ it -> it.asURI().with(gitHubLinks) }, ".asURI().with(gitHubLinks)"),
    Pair({ it -> it.asRemoteURI().with(gitHubLinks) }, ".asRemoteURI().with(gitHubLinks)"),
    Pair({ it -> it.asRemoteImageURI().with(gitHubLinks) }, ".asRemoteImageURI().with(gitHubLinks)"),
    Pair({ it -> it.mdOnly().asURI() }, ".mdOnly().asURI()"),
    Pair({ it -> it.mdOnly().asRemoteURI() }, ".mdOnly().asRemoteURI()"),
    Pair({ it -> it.mdOnly().asRemoteImageURI() }, ".mdOnly().asRemoteImageURI()"),
    Pair({ it -> it.mdOnly().asURI().with(gitHubLinks) }, ".mdOnly().asURI().with(gitHubLinks)"),
    Pair({ it -> it.mdOnly().asRemoteURI().with(gitHubLinks) }, ".mdOnly().asRemoteURI().with(gitHubLinks)"),
    Pair({ it -> it.mdOnly().asRemoteImageURI().with(gitHubLinks) }, ".mdOnly().asRemoteImageURI().with(gitHubLinks)")
)

fun exactMatch(list: List<String>, other: List<String>): Boolean {
    if (list.size == other.size && list.size > 0) {
        for (i in list.indices) {
            if (list[i] != other[i]) return false
        }
        return true
    }
    return false
}

fun selectExactList(list: List<String>, availableLists: Map<List<String>, String>, availablePermutations: Map<(List<String>) -> List<String>, String>): String? {
    for (other in availableLists.keys) {
        if (exactMatch(list, other)) return availableLists[other]
    }

    for (perm in availablePermutations.keys) {
        for (other in availableLists.keys) {
            if (exactMatch(list, perm(other))) return availableLists[other] + availablePermutations[perm]
        }
    }
    return dataColText(null, null, list)
}

fun addCompletionData(linkRef: LinkRef, matchOptions: Int, expectedResult: Any?) {
    //        "fullPath",
    //        "linkType",
    //        "linkRef",
    //        "linkAnchor",
    //        "options",
    //        "optionsText",
    //        "multiResolve"
    @Suppress("UNCHECKED_CAST")
    val result = arrayOf<Any?>(
        linkRef.containingFile.filePath,
        when (linkRef) {
            is WikiLinkRef -> { containingFile: FileRef, fullPath: String, anchor: String?, targetRef: FileRef? -> WikiLinkRef(containingFile, fullPath, anchor, targetRef, false) }
            is ImageLinkRef -> { containingFile: FileRef, fullPath: String, anchor: String?, targetRef: FileRef? -> ImageLinkRef(containingFile, fullPath, anchor, targetRef, false) }
            else -> { containingFile: FileRef, fullPath: String, anchor: String?, targetRef: FileRef? -> LinkRef(containingFile, fullPath, anchor, targetRef, false) }
        },
        linkRef.filePath,
        linkRef.anchor,
        matchOptions, matchOptions(matchOptions),
        if (expectedResult as? List<String> != null) selectExactList(expectedResult, availableLists, availablePermutations) else expectedResult
    )
    data.add(result)
}

fun selectExactList(expectedResult: Any?): String {
    @Suppress("UNCHECKED_CAST")
    return if (expectedResult as? List<String> != null) selectExactList(expectedResult, availableLists, availablePermutations)
        ?: "null" else "null"
}

fun validateResults(message: String, expected: List<String>, actual: List<String>, linkRef: LinkRef, matchOptions: Int) {
    if (cleanData) addCompletionData(linkRef, matchOptions, expected)
    TestCaseUtils.compareOrderedLists(message, expected, actual)
}


