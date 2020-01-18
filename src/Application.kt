package fi.xylix

import fi.xylix.Parser.toHtmlLink
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.html.PlaceholderList
import io.ktor.html.Template
import io.ktor.html.TemplatePlaceholder
import io.ktor.html.each
import io.ktor.html.insert
import io.ktor.html.respondHtmlTemplate
import io.ktor.routing.get
import io.ktor.routing.routing
import kotlinx.html.FlowContent
import kotlinx.html.HEAD
import kotlinx.html.HTML
import kotlinx.html.UL
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

val HOSTNAME: String = System.getenv("HOSTNAME")
val packages = Parser.readPackages("status.real")
val packageList: List<Package> =  packages.values.toList().sortedBy { it.name }
const val styleLiteral =
    """
        body {
            margin:40px auto;max - width:650px;line - height:1.3;font - size:18px;
            color:#444;padding:0 10px
        } h1, h2, h3{ line - height:1.2 }
    """

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    routing {
        get("/") {
            call.respondHtmlTemplate(MainTemplate(
                
            )) {}
        }
        get("/packages/{id}") {
            val id = call.parameters["id"] ?: ""
            val pack = packages[id]!!

            call.respondHtmlTemplate(PackageTemplate(
                pack = pack,
                dependencyLinks = stringsToLinks(pack.dependencies),
                reverseDependencies = packagesToLinks(pack.reverseDependencies(packageList)))) {

            }
        }
    }

}

class MainTemplate(packageList: List<String>): Template<HTML> {
    private val mainBody = TemplatePlaceholder<PackageListContent>()

    override fun HTML.apply() {
        head { header() }
        body {
            // What does this do??
            insert(PackageListContent(), mainBody)
        }
    }

    class PackageListContent: Template<FlowContent>{
        override fun FlowContent.apply() {

            table(classes = "pure-table pure-table-bordered") {
                for (pack in packageList) {
                    this@table.tr {
                        td {
                            +pack.
                        }
                    }
                    }
                }
            }
        }
    }
}

class PackageTemplate(
    val pack: Package,
    val dependencyLinks: List<String>,
    val reverseDependencies: List<String>) : Template<HTML> {

    private val packageTemplate = TemplatePlaceholder<PackageContent>()
    override fun HTML.apply() {
        head { header() }
        body {
            insert(PackageContent(pack, dependencyLinks, reverseDependencies), packageTemplate)
        }
    }

    val freemarkerTemplate = """
        <tr><td>Name: </td><td>${pack.name}</td></tr>
                    <tr><td>Description: </td><td>${pack.description}</td></tr>
                    
            """

    class PackageContent(
        private val pack: Package,
        private val dependencyLinks: List<String>,
        private val reverseDependencies: List<String>): Template<FlowContent>
    {
        override fun FlowContent.apply() {
            table(classes = "pure-table pure-table-bordered") {
                tr {
                    td { "Name:" + pack.name}
                    td { "Description: " + pack.description}
                }

                h2 { +"Dependencies: " }
                for (dependency in dependencyLinks) {
                    this@table.tr {
                        td {
                            +dependency
                        }
                    }
                }

                h2 { +"Reverse Dependencies: " }
                for (reverseDependency in reverseDependencies) {
                    this@table.tr {
                        td {
                            +reverseDependency
                        }
                    }
                }

            }
        }
    }
}

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

fun stringsToLinks(strings: List<String>): List<String> {
    // Create links where the referenced package exists on the system
    return strings.map{
        if (packages.containsKey(it)) toHtmlLink(it)
        else it
    }
}

fun packagesToLinks(packages: List<Package>): List<String> {
    return packages.map { toHtmlLink(it.name)}
}
