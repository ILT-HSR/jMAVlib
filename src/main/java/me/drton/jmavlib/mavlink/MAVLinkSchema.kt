package me.drton.jmavlib.mavlink

import android.content.Context
import android.util.Log
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.SAXException
import java.io.File
import java.io.IOException
import java.nio.ByteOrder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import kotlin.Exception
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.let
import kotlin.Comparator

@Suppress("UNCHECKED_CAST")
inline fun <T> NodeList.forEach(action: (T) -> Unit) {
    for (index in 0..length) {
        (item(index) as? T)?.let {
            action(it)
        }
    }
}

private val TAG = MAVLinkSchema::class.simpleName

private const val NODE_MAVLINK = "mavlink"
private const val NODE_INCLUDE = "include"
private const val NODE_MESSAGES = "messages"
private const val NODE_MESSAGE = "message"
private const val NODE_FIELD = "field"

private const val ATTRIBUTE_ID = "id"
private const val ATTRIBUTE_NAME = "name"
private const val ATTRIBUTE_TYPE = "type"

/**
 * User: ton Date: 03.06.14 Time: 12:31
 */
class MAVLinkSchema @Throws(ParserConfigurationException::class, IOException::class, SAXException::class)
constructor(private val fContext: Context, private val fFilename: String) {

    private val fByteOrder: ByteOrder = ByteOrder.LITTLE_ENDIAN
    private val fMessagesById = HashMap<Int, MAVLinkMessageDefinition>()
    private val fMessagesByName = HashMap<String, MAVLinkMessageDefinition>()

    init {
        readSchemaFile()
    }

    val byteOrder get() = fByteOrder

    override fun toString() = "$fFilename [${fMessagesById.size} message definitions]"

    operator fun set(id: Int, name: String, message: MAVLinkMessageDefinition) {
        fMessagesById[id] = message
        fMessagesByName[name] = message
    }

    operator fun get(id: Int) = fMessagesById[id]

    operator fun get(name: String) = fMessagesByName[name]

    private fun readSchemaFile(filename: String = fFilename) {
        val schemaFile = File(filename)
        val schemaStream = fContext.assets.open(schemaFile.path)

        try {
            val schemaDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(schemaStream)
            val rootNode = schemaDocument.documentElement
            rootNode.normalize()

            if (rootNode.nodeName != NODE_MAVLINK) {
                Log.e(TAG, "Expected <$NODE_MAVLINK>")
                return
            }

            rootNode.getElementsByTagName(NODE_INCLUDE).forEach<Node> {
                readSchemaFile(File(schemaFile.parentFile, it.textContent).path)
            }

            (rootNode.getElementsByTagName(NODE_MESSAGES).item(0) as Element).getElementsByTagName(NODE_MESSAGE).forEach<Element> { msg ->
                val id = Integer.parseInt(msg.getAttribute(ATTRIBUTE_ID))
                val name = msg.getAttribute(ATTRIBUTE_NAME)
                val fields = ArrayList<MAVLinkField>()

                msg.getElementsByTagName(NODE_FIELD).forEach<Element> { field ->
                    val typeString = field.getAttribute(ATTRIBUTE_TYPE).split("\\[".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = MAVLinkDataType.fromCType(typeString[0])
                    val arrayDimension = if(typeString.size > 1) {
                        Integer.parseInt(typeString[1].split("]".toRegex())[0])
                    } else {
                        -1
                    }
                    fields.add(MAVLinkField(type, arrayDimension, name))
                }

                fields.sortWith(Comparator { lhs, rhs ->
                    when {
                        rhs.type.size > lhs.type.size -> 1
                        rhs.type.size < lhs.type.size -> -1
                        else -> 0
                    }
                })

                if(id in 0..255) {
                    this[id, name] = MAVLinkMessageDefinition(id, name, fields.toTypedArray())
                }
            }
        } catch (e: Exception) {
            when (e) {
                is IOException,
                is SAXException,
                is ParserConfigurationException -> Log.e(TAG, "Error while trying to parse '$filename'", e)
                else -> throw e
            }
        }
    }

}
