package fi.xylix

import fi.xylix.Parser.toHtmlLink
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.*
import io.ktor.freemarker.FreeMarker
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing

const val HOSTNAME = "0.0.0.0:8080"
val packages = Parser.readPackages("status.real")
val packageList: List<Package> =  packages.values.toList().sortedBy { it.name }


fun main(args: Array<String>): Unit {
    io.ktor.server.netty.EngineMain.main(args)
}


@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    // Init templating engine
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    routing {
        get("/") {
            call.respond(FreeMarkerContent(
                template= "index.ftl",
                model =  mapOf("packageList" to toHtmlLinks(packageList))))
        }
        get("/packages/{id}") {
            val id = call.parameters["id"] ?: ""
            val pack = packages[id]!!
            call.respond(FreeMarkerContent(
                template = "package.ftl",
                model =  mapOf(
                    "package" to pack,
                    "dependencyLinks" to stringsToLinks(pack.dependencies),
                    "reverseDependencies" to packagesToLinks(pack.reverseDependencies(packageList)))
            ))
        }
    }

}

fun stringsToLinks(packageList: List<String>): List<String> {
    return packageList.map { toHtmlLink(it)}
}

fun packagesToLinks(packageList: List<Package>): List<String> {
    return packageList.map { toHtmlLink(it.name)}
}

data class Index(val items: List<String>)

