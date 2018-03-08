package com.toolslab.noisepercolator.filter

import org.w3c.dom.Element
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory

class XmlParser {

    private companion object {
        private const val SPAM_SMS_FILE = "spam_sms.xml"
        private const val SMS_TAG = "sms"
        private const val ADDRESS_ATTRIBUTE = "address"
        private const val BODY_ATTRIBUTE = "body"
    }

    internal fun parseSpamMessages(): List<XmlParser.SmsEntry> {
        val inputStream = XmlParser::class.java.classLoader.getResourceAsStream(SPAM_SMS_FILE)
        return parse(inputStream)
    }

    private fun parse(inputStream: InputStream): List<SmsEntry> {
        val entries = mutableListOf<SmsEntry>()
        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream)
        val smses = doc.getElementsByTagName(SMS_TAG)
        (0 until smses.length)
                .map { smses.item(it) as Element }
                .mapTo(entries) {
                    SmsEntry(it.getAttribute(ADDRESS_ATTRIBUTE), it.getAttribute(BODY_ATTRIBUTE))
                }
        return entries
    }

    internal class SmsEntry constructor(val address: String, val body: String)

}
