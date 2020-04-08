package com.fsck.k9.preferences

import com.fsck.k9.K9RobolectricTest
import com.fsck.k9.preferences.SettingsExporter.exportPreferences
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import org.jdom2.Document
import org.jdom2.input.SAXBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.robolectric.RuntimeEnvironment

class SettingsExporterTest : K9RobolectricTest() {
    @Test
    fun exportPreferences_producesXML() {
        val document = exportPreferences(false, emptySet())

        assertEquals("k9settings", document.rootElement.name)
    }

    @Test
    fun exportPreferences_setsVersionToLatest() {
        val document = exportPreferences(false, emptySet())

        assertEquals(Settings.VERSION.toString(), document.rootElement.getAttributeValue("version"))
    }

    @Test
    fun exportPreferences_setsFormatTo1() {
        val document = exportPreferences(false, emptySet())

        assertEquals("1", document.rootElement.getAttributeValue("format"))
    }

    @Test
    fun exportPreferences_exportsGlobalSettingsWhenRequested() {
        val document = exportPreferences(true, emptySet())

        assertNotNull(document.rootElement.getChild("global"))
    }

    @Test
    fun exportPreferences_ignoresGlobalSettingsWhenRequested() {
        val document = exportPreferences(false, emptySet())

        assertNull(document.rootElement.getChild("global"))
    }

    private fun exportPreferences(globalSettings: Boolean, accounts: Set<String>): Document {
        return ByteArrayOutputStream().use { outputStream ->
            exportPreferences(RuntimeEnvironment.application, outputStream, globalSettings, accounts)
            parseXml(outputStream.toByteArray())
        }
    }

    private fun parseXml(xml: ByteArray): Document {
        return SAXBuilder().build(ByteArrayInputStream(xml))
    }
}
