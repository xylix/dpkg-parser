package fi.xylix

import java.io.File
import java.io.FileNotFoundException

object Parser {

    fun readFile(filename: String): File {
        val path = Parser.javaClass.classLoader.getResource(filename) ?:
            throw FileNotFoundException("File $filename not found")
        return File(path.file)
    }

    fun toHtmlLink(name: String): String {
        return "<a href=\"http://$hostName/packages/$name\">$name</a>"
    }

    fun readPackages(fileName: String): Map<String, Package> {
        val contents =  readFile(fileName).readLines()
        for (i in 0..contents.size) {
            
        }
    }
}
data class Package (val name: String, val description: String,
                    val dependencies: List<String>, val reverseDependencies: List<String>)

