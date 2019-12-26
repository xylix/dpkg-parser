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
                model =  mapOf(Pair("packageList", packageList.map { pac -> toHtmlLink(pac.name)}))))
        }
        get("/packages/{id}") {
            val id = call.parameters["id"] ?: ""
            call.respond(FreeMarkerContent(
                template = "package.ftl",
                model =  mapOf(Pair("package", packages[id]), Pair("packageList", packageList))))
        }
    }

}

data class Index(val items: List<String>)

