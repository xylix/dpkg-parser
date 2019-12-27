package fi.xylix

import fi.xylix.Parser.toHtmlLink
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.freemarker.FreeMarker
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing

val HOSTNAME: String = System.getenv("HOSTNAME")
val packages = Parser.readPackages("status.real")
val packageList: List<Package> =  packages.values.toList().sortedBy { it.name }


fun main(args: Array<String>) {
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
                model =  mapOf("packageList" to packagesToLinks(packageList))))
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

data class Index(val items: List<String>)

