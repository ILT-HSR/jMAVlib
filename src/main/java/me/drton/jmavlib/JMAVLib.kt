package me.drton.jmavlib

import me.drton.jmavlib.mavlink.MAVLinkMessage
import me.drton.jmavlib.mavlink.MAVLinkSchemaRegistry

private const val MESSAGE_HEARTBEAT = "HEARTBEAT"
private const val MESSAGE_COMMAND_LONG = "COMMAND_LONG"

enum class MAVLinkLongCommand(val value: Int) {
    COMPONENT_ARM_DISARM(400)
}

private val Boolean.int get() = if(this) 1 else 0

/**
 * Create a new MAVLink message
 *
 * @param messageId The MAVLink ID of the message (e.g. "HEARTBEAT")
 * @param system The sender system ID
 * @param component The sender component ID
 *
 * @return A new, empty MAVLink message
 */
fun createMAVLinkMessage(schema: String = "common", name: String, system: Int, component: Int) =
        MAVLinkMessage(MAVLinkSchemaRegistry.instance[schema], name, system, component)

/**
 * Create a new MAVLink 'Heartbeat' message
 *
 * @return A new MAVLink 'Heartbeat' message
 */
fun createMAVLinkHeartbeat(schema: String = "common", system: Int, component: Int): MAVLinkMessage {
    val heartbeat = createMAVLinkMessage(schema, MESSAGE_HEARTBEAT, system, component)

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
fun createArmMessage(schema: String = "common", system: Int, component: Int): MAVLinkMessage {
    val msg = newArmDisarmMessage(schema, system, component)
    msg.set("param1", true.int)
    return msg
}

/**
 * Create a new MAVLink 'Disarm' message
 *
 * @return a new MAVLink 'Long Command' message containing an 'Disarm' message
 */
fun createDisarmMessage(schema: String = "common", system: Int, component: Int): MAVLinkMessage {
    val msg = newArmDisarmMessage(schema, system, component)
    msg.set("param1", false.int)
    return msg
}

private fun newArmDisarmMessage(schema: String = "common", system: Int, component: Int): MAVLinkMessage {
    val msg = createMAVLinkMessage(schema, MESSAGE_COMMAND_LONG, system, component)
    msg.set("target_system", 1)
    msg.set("target_component", 0)
    msg.set("command", MAVLinkLongCommand.COMPONENT_ARM_DISARM.value)
    msg.set("confirmation", 0)
    return msg
}
