package me.drton.jmavlib

import me.drton.jmavlib.mavlink.MAVLinkField
import me.drton.jmavlib.mavlink.MAVLinkMessage
import me.drton.jmavlib.mavlink.MAVLinkSchema












/**
 * A MAVLinkSchema instance representing the Mavlink 1.0 'Common' schema
 *
 * MAVLink supports a variety of schemas. This instance represents the 'Common' schema of MAVLink
 * 1.0. The 'Common' schema specifies the common subset of MAVLink messages, supported by most
 * MAVLink compatible devices.
 *
 * @since 1.0.0
 */
lateinit var MAVLINK_SCHEMA_COMMON: MAVLinkSchema

/**
 * Create a new MAVLink message
 *
 * @param messageId The MAVLink ID of the message (e.g. "HEARTBEAT")
 * @param system The sender system ID
 * @param component The sender component ID
 *
 * @return A new, empty MAVLink message
 */
fun newMAVLinkMessage(messageId: String, system: Int, component: Int): MAVLinkMessage =
        MAVLinkMessage(MAVLINK_SCHEMA_COMMON, messageId, system, component)

/**
 * Create a new MAVLink 'Heartbeat' message
 *
 * @return A new MAVLink 'Heartbeat' message
 */
fun newMAVLinkHeartbeat(): MAVLinkMessage {
    val heartbeat = newMAVLinkMessage("HEARTBEAT", 8, 250)

    heartbeat.set("type", 6)
    heartbeat.set("autopilot", 0)
    heartbeat.set("base_mode", 128)
    heartbeat.set("custom_mode", 0)
    heartbeat.set("system_status", 4)

    return heartbeat
}

/**
 * Create a new MAVLink 'Arm' message
 *
 * @return a new MAVLink 'Long Command' message containing an 'Arm' message
 */
fun newArmMessage(): MAVLinkMessage {
    val msg = newArmDisarmMessage()
    msg.set("param1", 1)
    return msg
}

/**
 * Create a new MAVLink 'Disarm' message
 *
 * @return a new MAVLink 'Long Command' message containing an 'Disarm' message
 */
fun newDisarmMessage(): MAVLinkMessage {
    val msg = newArmDisarmMessage()
    msg.set("param1", 0)
    return msg
}

private fun newArmDisarmMessage(): MAVLinkMessage {
    val msg = newMAVLinkMessage("COMMAND_LONG", 8, 250)
    msg.set("target_system", 1)
    msg.set("target_component", 0)
    msg.set("command", 400)
    msg.set("confirmation", 0)
    msg.set("param2", 0)
    msg.set("param3", 0)
    msg.set("param4", 0)
    msg.set("param5", 0)
    msg.set("param6", 0)
    msg.set("param7", 0)
    return msg
}
