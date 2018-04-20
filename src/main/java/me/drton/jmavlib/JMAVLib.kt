package me.drton.jmavlib

import me.drton.jmavlib.mavlink.MAVLinkMessage
import me.drton.jmavlib.mavlink.MAVLinkSchema
import me.drton.jmavlib.mavlink.MAVLinkSchemaRegistry

private const val MESSAGE_HEARTBEAT = "HEARTBEAT"
private const val MESSAGE_COMMAND_LONG = "COMMAND_LONG"

enum class MAVLinkLongCommand(val value: Int) {
    COMPONENT_ARM_DISARM(400),
    REQUEST_AUTOPILOT_CAPABILITIES(520),
}

private val Boolean.int get() = if (this) 1 else 0

/**
 * Create a new MAVLink message
 *
 * @param name The MAVLink ID of the message (e.g. "HEARTBEAT")
 * @param system The sender system ID
 * @param component The sender component ID
 * @param schema The message schema
 *
 * @return A new, empty MAVLink message
 */
fun createMAVLinkMessage(name: String, system: Int, component: Int, schema: MAVLinkSchema) =
        MAVLinkMessage(schema, name, system, component)

/**
 * Create a new MAVLink message
 *
 * @param name The MAVLink ID of the message (e.g. "HEARTBEAT")
 * @param system The sender system ID
 * @param component The sender component ID
 * @param schema The name of the message schema
 *
 * @return A new, empty MAVLink message
 */
fun createMAVLinkMessage(name: String, system: Int, component: Int, schema: String = "common") =
        MAVLinkSchemaRegistry[schema]?.let { createMAVLinkMessage(name, system, component, it) }

/**
 * Create a new MAVLink 'Heartbeat' message
 *
 * @return A new MAVLink 'Heartbeat' message
 */
fun createHeartbeatMessage(system: Int, component: Int, schema: MAVLinkSchema): MAVLinkMessage {
    val heartbeat = createMAVLinkMessage(MESSAGE_HEARTBEAT, system, component, schema)

    heartbeat.set("type", 6)
    heartbeat.set("autopilot", 0)
    heartbeat.set("base_mode", 128)
    heartbeat.set("custom_mode", 0)
    heartbeat.set("system_status", 4)

    return heartbeat
}

/**
 * Create a new MAVLink 'Heartbeat' message
 *
 * @return A new MAVLink 'Heartbeat' message
 */
fun createHeartbeatMessage(system: Int, component: Int, schema: String = "common") =
        MAVLinkSchemaRegistry[schema]?.let { createHeartbeatMessage(system, component, it) }

/**
 * @internal
 *
 * Create a new MAVLink 'Long Command' message
 */
internal fun createLongCommandMessage(target: Int, command: MAVLinkLongCommand, source: Int, component: Int, schema: MAVLinkSchema): MAVLinkMessage {
    val msg = createMAVLinkMessage(MESSAGE_COMMAND_LONG, source, component, schema)
    msg.set("target_system", target)
    msg.set("target_component", 0)
    msg.set("command", command.value)
    msg.set("confirmation", 0)
    return msg
}

/**
 * @internal
 *
 * Create a new MAVLink 'Long Command' message for arming or disarming the vehicle
 */
internal fun newArmDisarmMessage(target: Int, source: Int, component: Int, schema: MAVLinkSchema) =
        createLongCommandMessage(target, MAVLinkLongCommand.COMPONENT_ARM_DISARM, source, component, schema)

/**
 * Create a new MAVLink 'Arm' message
 *
 * @return a new MAVLink 'Long Command' message containing an 'Arm' message
 */
fun createArmMessage(target: Int, source: Int, component: Int, schema: MAVLinkSchema): MAVLinkMessage {
    val msg = newArmDisarmMessage(target, source, component, schema)
    msg.set("param1", true.int)
    return msg
}

/**
 * Create a new MAVLink 'Arm' message
 *
 * @return a new MAVLink 'Long Command' message containing an 'Arm' message
 */
fun createArmMessage(target: Int, source: Int, component: Int, schema: String = "common") =
        MAVLinkSchemaRegistry[schema]?.let { createArmMessage(target, source, component, it) }

/**
 * Create a new MAVLink 'Disarm' message
 *
 * @return a new MAVLink 'Long Command' message containing an 'Disarm' message
 */
fun createDisarmMessage(target: Int, source: Int, component: Int, schema: MAVLinkSchema): MAVLinkMessage {
    val msg = newArmDisarmMessage(target, source, component, schema)
    msg.set("param1", false.int)
    return msg
}

/**
 * Create a new MAVLink 'Disarm' message
 *
 * @return a new MAVLink 'Long Command' message containing an 'Disarm' message
 */
fun createDisarmMessage(target: Int, source: Int, component: Int, schema: String = "common") =
        MAVLinkSchemaRegistry[schema]?.let { createDisarmMessage(target, source, component, it) }

/**
 * Create a new MAVLink 'Request Autopilot Capabilities' message
 *
 * @return a new MAVLink 'Long Command' message containing an 'Request Autopilot Capabilities' message
 */
fun createRequestAutopilotCapabilitiesMessage(target: Int, source: Int, component: Int, schema: MAVLinkSchema): MAVLinkMessage {
    val msg = createLongCommandMessage(target, MAVLinkLongCommand.REQUEST_AUTOPILOT_CAPABILITIES, source, component, schema)
    msg.set("param1", true.int)
    return msg
}
