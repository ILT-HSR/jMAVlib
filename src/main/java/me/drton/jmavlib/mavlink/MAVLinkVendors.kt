package me.drton.jmavlib.mavlink

/**
 * This class provides mappings between MAVLink vendors IDs and their name
 *
 * @since 1.0.0
 * @author IFS Institute for Software
 */
object MAVLinkVendors {

    private val fVendorMap = mapOf(
            0x26ac to "3D Robotics"
    ).toMutableMap()

    operator fun get(id: Int) = fVendorMap[id]?:"<unknown vendor '$id'>"

    operator fun set(id: Int, name: String) {
        fVendorMap[id] = name
    }

}