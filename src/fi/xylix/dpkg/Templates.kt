package fi.xylix.dpkg

import io.ktor.html.Template
import io.ktor.html.TemplatePlaceholder
import io.ktor.html.insert
import kotlinx.html.FlowContent
import kotlinx.html.HEAD
import kotlinx.html.HTML
import kotlinx.html.TABLE
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.h2
import kotlinx.html.head
import kotlinx.html.li
import kotlinx.html.link
import kotlinx.html.style
import kotlinx.html.table
import kotlinx.html.td
import kotlinx.html.tr
import kotlinx.html.ul
import kotlinx.html.unsafe


private const val styleLiteral =
    """
        body {
            margin:40px auto;max - width:650px;line - height:1.3;font - size:18px;
            color:#444;padding:0 10px
        } h1, h2, h3{ line - height:1.2 }
    """

// A table row containing passed data as a table data entry
private fun TABLE.trd(contents: () -> Unit) = tr { td { contents() } }

private fun HEAD.header() {
    link(
        rel = "stylesheet",
        href = "https://unpkg.com/purecss@1.0.1/build/pure-min.css"
        //crossorigin = "anonymous"
    )
    style(type = "text/css") {
        unsafe { raw(styleLiteral) }
    }
}


private fun toLink(name: String): String {
    return if (packageMap.containsKey(name)) "/packages/$name"
    else name
}


class MainTemplate(val packageList: List<Package>): Template<HTML> {
    private val mainBody = TemplatePlaceholder<PackageListContent>()

    override fun HTML.apply() {
        head { header() }
        body {
            // What kinds of magic gets done here
            insert(PackageListContent(packageList), mainBody)
        }
    }

    class PackageListContent(private val packageList: List<Package>): Template<FlowContent> {
        override fun FlowContent.apply() {
            table(classes = "pure-table pure-table-bordered") {
                for (pack in packageList) {
                    this@table.trd {
                        a(href = toLink(pack.name)) {
                            +pack.name
                        }
                    }
                }
            }
        }
    }
}

class PackageTemplate(
    private val pack: Package,
    private val dependencies: List<String>,
    private val reverseDependencies: List<Package>) : Template<HTML> {

    private val packageTemplate = TemplatePlaceholder<PackageContent>()
    override fun HTML.apply() {
        head { header() }
        body {
            insert(PackageContent(pack, dependencies, reverseDependencies), packageTemplate)
        }
    }

    class PackageContent(
        private val pack: Package,
        private val dependencies: List<String>,
        private val reverseDependencies: List<Package>)
    : Template<FlowContent> {
        override fun FlowContent.apply() {

            table(classes = "pure-table pure-table-bordered") {
                trd{ +"Name: ${pack.name}" }
                trd{ +"Description: ${pack.description}" }
                trd {
                    h2 { +"Dependencies: " }
                    ul {
                        for (dependency in dependencies) {
                            this@ul.li {
                                a(href = toLink(dependency)) {
                                    +dependency
                                }
                            }
                        }
                    }
                }

                trd {
                    h2 { +"Reverse Dependencies: " }
                    ul {
                        for (reverseDependency in reverseDependencies) {
                            this@ul.li {
                                a(href = toLink(reverseDependency.name)) {
                                    +reverseDependency.name
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
