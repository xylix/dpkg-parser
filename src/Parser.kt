package fi.xylix

import java.io.File
import java.io.FileNotFoundException

object Parser {
    private fun readFile(filename: String): File {
        val path = Parser.javaClass.classLoader.getResource(filename) ?:
            throw FileNotFoundException("File $filename not found")
        return File(path.file)
    }

    fun toHtmlLink(name: String): String {
        return "<a href=\"http://$HOSTNAME/packages/$name\">$name</a>"
    }

    fun readPackages(fileName: String): Map<String, Package> {
        val packages = mutableMapOf<String, Package>()

        val contents =  readFile(fileName).readLines()
        var previousEnd = 0
        for ((index, value) in contents.withIndex()) {
            /* Empty line marks paragraph separation, all packages are separated by empty lines.
            *  We find the package ends and create packages from the data that was between.*/
            if (value.trim().isEmpty()) {
                val packageLines = contents.subList(previousEnd, index)
                val parsed = parsePackage(packageLines)
                packages[parsed.name] = parsed
                previousEnd = index
            }
        }
        return packages
    }

    private fun parsePackage(lines: List<String>): Package {
        val nameAt = lines.indexOfStartsWith("Package: ")
        if (nameAt == -1) throw IllegalStateException("Illegal parsePackage call, no Package in $lines")
        val name = lines[nameAt].substringAfter(":").trim()

        val dependsAt = lines.indexOfStartsWith("Depends: ")
        var dependencies: List<String>? = null
        if (dependsAt != -1) {  dependencies = parseDeps(lines[dependsAt].substringAfter(":").trim()) }

        val descriptionStart = lines.indexOfStartsWith("Description: ")
        var description: String? = null
        if (descriptionStart != -1) { description = captureMultiline(lines, descriptionStart) }

        return Package(name, description ?: "", dependencies ?: listOf())

    }

    private fun captureMultiline(lines: List<String>, begins: Int): String {
        var value = lines.first()
        val continues = lines.subList(begins + 1, lines.size)
        for (line in continues) {
            // All lines except the first in a multiline segment
            if (Regex("^\\s*") !in line) break
            value += " $line"
        }
        return value
    }

    private fun parseDeps(depends: String): List<String> {
        // Take `Depends` row and return it split by every comma, stripped of version numbers.
        return depends.split(",").map { it.substringBefore('(').trim() }
    }

    private fun List<String>.indexOfStartsWith(e: String): Int {
        return this.indexOfFirst { it.startsWith(e) }
    }
}

data class Package (val name: String, val description: String, val dependencies: List<String>) {
    fun reverseDependencies(packages: List<Package>): List<Package> {
        return packages.filter { it.dependencies.contains(name) }
    }
}

