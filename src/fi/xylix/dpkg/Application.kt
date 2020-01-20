package fi.xylix.dpkg

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.routing.get
import io.ktor.routing.routing


val packageMap = Parser.readPackages("status.real")
val packageList: List<Package> =  packageMap.values.toList().sortedBy { it.name }

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
