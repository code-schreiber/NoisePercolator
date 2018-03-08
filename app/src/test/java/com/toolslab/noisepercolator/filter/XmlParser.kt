package com.toolslab.noisepercolator.filter

import org.w3c.dom.Element
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory

class XmlParser {

    internal fun parseSpamMessages(): List<XmlParser.SmsEntry> {
        val inputStream = XmlParser::class.java.classLoader.getResourceAsStream("spam_sms.xml")
        return parse(inputStream)
    }

    private fun parse(inputStream: InputStream): List<SmsEntry> {
        val entries = mutableListOf<SmsEntry>()
        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream)
        val smses = doc.getElementsByTagName("sms")
        (0 until smses.length)
                .map { smses.item(it) as Element }
                .mapTo(entries) {
                    SmsEntry(it.getAttribute("protocol").toInt(),
                            it.getAttribute("address"),
                            it.getAttribute("date").toLong(),
                            it.getAttribute("body"))
                }
        return entries
    }

    class SmsEntry constructor(val protocol: Int,
                               val address: String,
                               val date: Long,
                               val body: String)

}
