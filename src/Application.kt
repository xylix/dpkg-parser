package fi.xylix

import freemarker.cache.ClassTemplateLoader
import io.ktor.application.*
import io.ktor.freemarker.FreeMarker
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.response.respond
import io.ktor.routing.get

import io.ktor.routing.routing

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    val packages: Map<String, Package> = Parser.readPackages()

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    routing {
        get("/") {
            call.respond(FreeMarkerContent("index.ftl",
                mapOf("index.ftl" to Index(listOf("one", "two", "three"))), ""))
        }
        get("/packages/{id}") {
            val id = call.parameters["id"] ?: ""
            call.respond(FreeMarkerContent("package.ftl", packages[id]))
        }
    }

}

data class Index(val items: List<String>)

