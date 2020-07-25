package com.phodal.gradal.plugins.gradle.data.service

import com.google.common.base.Charsets
import java.io.*
import java.util.*

object PropertiesFiles {
    @Throws(IOException::class)
    fun getProperties(filePath: File): Properties {
        require(!filePath.isDirectory) { String.format("The path '%1\$s' belongs to a directory!", filePath.path) }
        if (!filePath.exists()) {
            return Properties()
        }
        val properties = Properties()
        InputStreamReader(BufferedInputStream(FileInputStream(filePath)), Charsets.UTF_8).use { reader -> properties.load(reader) }
        return properties
    }
}
