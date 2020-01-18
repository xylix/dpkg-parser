package fi.xylix

import java.io.File
import java.io.FileNotFoundException

object Parser {
    private fun readFile(filename: String): File {
        return File("./$filename")
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
        if (nameAt == -1) throw IllegalStateException("Invalid Package data, no Package in $lines")
        val name = lines[nameAt].substringAfter(":").trim()

        val dependsAt = lines.indexOfStartsWith("Depends: ")
        var dependencies: List<String>? = null
        if (dependsAt != -1) {  dependencies = parseDepends(lines[dependsAt].substringAfter(":").trim()) }

        val descriptionStart = lines.indexOfStartsWith("Description: ")
        var description: String? = null
        if (descriptionStart != -1) { description = captureMultiline(lines, descriptionStart) }

        return Package(name, description ?: "", dependencies ?: listOf())

    }

    private fun parseDepends(depends: String): List<String> {
        // Take `Depends` row and return it split by every comma, stripped of version numbers
        // and split at OR operators.
        return depends.split(",", "|").map { it.substringBefore('(').trim() }
    }

    private fun captureMultiline(lines: List<String>, begins: Int): String {
        var value = lines.first()
        val possiblyIndentedLines = lines.subList(begins + 1, lines.size)
        for (line in possiblyIndentedLines) {
            /* All lines except the first in a multiline segment are intended.
             *  We use this to stop parsing when a segment ends */
            if (!line[0].isWhitespace()) break
            value += when {
                // Add newlines in place full-stop formatting rows
                line == " ." -> "<br></br>"
                // Match list item rows, they start with whitespace and contain a * to signify items
                line.contains(Regex("\\s*\\*")) -> "<li>${line.substringAfter("*")}</li>"
                else -> line
            }
        }
        return value
    }

    private fun List<String>.indexOfStartsWith(e: String): Int {
        return this.indexOfFirst { it.startsWith(e) }
    }

    fun toHtmlLink(name: String): String {
        return "<a href=\"$HOSTNAME/packages/$name\">$name</a>"
    }
}

data class Package (val name: String, val description: String, val dependencies: List<String>) {
    fun reverseDependencies(packages: List<Package>): List<Package> {
        return packages.filter { it.dependencies.contains(name) }
    }
}

