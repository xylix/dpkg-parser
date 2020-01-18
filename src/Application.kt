package fi.xylix

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.html.Template
import io.ktor.html.TemplatePlaceholder
import io.ktor.html.insert
import io.ktor.html.respondHtmlTemplate
import io.ktor.routing.get
import io.ktor.routing.routing
import kotlinx.html.FlowContent
import kotlinx.html.HEAD
import kotlinx.html.HTML
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.h2
import kotlinx.html.head
import kotlinx.html.link
import kotlinx.html.style
import kotlinx.html.table
import kotlinx.html.td
import kotlinx.html.tr
import kotlinx.html.unsafe

val HOSTNAME: String = System.getenv("HOSTNAME")
val packageMap = Parser.readPackages("status.real")
val packageList: List<Package> =  packageMap.values.toList().sortedBy { it.name }
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
                packageList = packageList
            )) {}
        }
        get("/packages/{id}") {
            val id = call.parameters["id"] ?: ""
            val pack = packageMap[id] ?: error("Package not found")

            call.respondHtmlTemplate(PackageTemplate(
                pack = pack,
                dependencies = pack.dependencies,
                reverseDependencies = pack.reverseDependencies(packageList))) {

            }
        }
    }

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

    class PackageListContent(val packageList: List<Package>): Template<FlowContent>{
        override fun FlowContent.apply() {
            table(classes = "pure-table pure-table-bordered") {
                for (pack in packageList) {
                    this@table.tr {
                        td {
                            a (href=toLink(pack.name)) {
                                +pack.name
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
    val dependencies: List<String>,
    val reverseDependencies: List<Package>) : Template<HTML> {

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
                tr {
                    td { "Name:" + pack.name}
                    td { "Description: " + pack.description}
                }

                h2 { +"Dependencies: " }
                for (dependency in dependencies) {
                    this@table.tr {
                        td {
                            a (href=toLink(dependency)){
                                +dependency
                            }
                        }
                    }
                }

                h2 { +"Reverse Dependencies: " }
                for (reverseDependency in reverseDependencies) {
                    this@table.tr {
                        td {
                            a (href=toLink(reverseDependency.name)){
                                +reverseDependency.name
                            }
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


private fun toLink(name: String): String {
    return if (packageMap.containsKey(name)) "/packages/$name"
    else name
}
