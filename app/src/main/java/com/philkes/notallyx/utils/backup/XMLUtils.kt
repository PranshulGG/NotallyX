package com.philkes.notallyx.utils.backup

import com.philkes.notallyx.data.model.BaseNote
import com.philkes.notallyx.data.model.Color
import com.philkes.notallyx.data.model.Folder
import com.philkes.notallyx.data.model.Label
import com.philkes.notallyx.data.model.ListItem
import com.philkes.notallyx.data.model.SpanRepresentation
import com.philkes.notallyx.data.model.Type
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

object XMLUtils {

    fun readBaseNoteFromFile(file: File, folder: Folder): BaseNote {
        val inputStream = FileInputStream(file)
        val parser = XmlPullParserFactory.newInstance().newPullParser()
        parser.setInput(inputStream, null)
        parser.next()
        return parseBaseNote(parser, parser.name, folder)
    }

    fun readBackupFromStream(inputStream: InputStream): Pair<List<BaseNote>, List<Label>> {
        val parser = XmlPullParserFactory.newInstance().newPullParser()
        parser.setInput(inputStream, null)

        val baseNotes = ArrayList<BaseNote>()
        val labels = ArrayList<Label>()

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.eventType == XmlPullParser.START_TAG) {
                when (parser.name) {
                    "notes" -> parseList(parser, parser.name, baseNotes, Folder.NOTES)
                    "deleted-notes" -> parseList(parser, parser.name, baseNotes, Folder.DELETED)
                    "archived-notes" -> parseList(parser, parser.name, baseNotes, Folder.ARCHIVED)
                    "label" -> labels.add(Label(parser.nextText()))
                }
            }
        }

        return Pair(baseNotes, labels)
    }

    private fun parseList(
        parser: XmlPullParser,
        rootTag: String,
        list: ArrayList<BaseNote>,
        folder: Folder,
    ) {
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.eventType == XmlPullParser.START_TAG) {
                val note = parseBaseNote(parser, parser.name, folder)
                list.add(note)
            } else if (parser.eventType == XmlPullParser.END_TAG) {
                if (parser.name == rootTag) {
                    break
                }
            }
        }
    }

    private fun parseBaseNote(parser: XmlPullParser, rootTag: String, folder: Folder): BaseNote {
        var color = Color.DEFAULT

        var body = String()
        var title = String()
        var timestamp = 0L
        var pinned = false
        val items = ArrayList<ListItem>()

        val labels = ArrayList<String>()
        val spans = ArrayList<SpanRepresentation>()

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.eventType == XmlPullParser.START_TAG) {
                when (parser.name) {
                    "color" -> color = Color.valueOf(parser.nextText())
                    "title" -> title = parser.nextText()
                    "body" -> body = parser.nextText()
                    "date-created" -> timestamp = parser.nextText().toLong()
                    "pinned" -> pinned = parser.nextText().toBoolean()
                    "label" -> labels.add(parser.nextText())
                    "item" -> items.add(parseItem(parser, parser.name))
                    "span" -> spans.add(parseSpan(parser))
                }
            } else if (parser.eventType == XmlPullParser.END_TAG) {
                if (parser.name == rootTag) {
                    break
                }
            }
        }

        // Can be either `note` or `list`
        val type =
            if (rootTag == "note") {
                Type.NOTE
            } else Type.LIST
        return BaseNote(
            0,
            type,
            folder,
            color,
            title,
            pinned,
            timestamp,
            timestamp,
            labels,
            body,
            spans,
            items,
            emptyList(),
            emptyList(),
            emptyList(),
        )
    }

    private fun parseItem(parser: XmlPullParser, rootTag: String): ListItem {
        var body = String()
        var checked = false
        var isChild = false
        var order: Int? = null

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.eventType == XmlPullParser.START_TAG) {
                when (parser.name) {
                    "text" -> body = parser.nextText()
                    "checked" -> checked = parser.nextText()?.toBoolean() ?: false
                    "isChild" -> isChild = parser.nextText()?.toBoolean() ?: false
                    "order" -> order = parser.nextText()?.toInt()
                }
            } else if (parser.eventType == XmlPullParser.END_TAG) {
                if (parser.name == rootTag) {
                    break
                }
            }
        }

        return ListItem(body, checked, isChild, order, mutableListOf())
    }

    private fun parseSpan(parser: XmlPullParser): SpanRepresentation {
        val start = parser.getAttributeValue(null, "start").toInt()
        val end = parser.getAttributeValue(null, "end").toInt()
        val bold = parser.getAttributeValue(null, "bold")?.toBoolean() ?: false
        val link = parser.getAttributeValue(null, "link")?.toBoolean() ?: false
        val linkData = parser.getAttributeValue(null, "linkData")?.toString()
        val italic = parser.getAttributeValue(null, "italic")?.toBoolean() ?: false
        val monospace = parser.getAttributeValue(null, "monospace")?.toBoolean() ?: false
        val strikethrough = parser.getAttributeValue(null, "strike")?.toBoolean() ?: false
        return SpanRepresentation(
            start,
            end,
            bold,
            link,
            linkData,
            italic,
            monospace,
            strikethrough,
        )
    }
}
