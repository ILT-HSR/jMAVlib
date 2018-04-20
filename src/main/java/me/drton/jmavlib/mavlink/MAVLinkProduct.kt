package me.drton.jmavlib.mavlink

/**
 * This class provides mappings between MAVLink product IDs and their product name
 *
 * @since 1.0.0
 * @author IFS Institute for Software
 */
object MAVLinkProducts {

    private val fProductMap = mapOf(
            0x0011 to "PX4 FMU v2.x"
    ).toMutableMap()

    operator fun get(id: Int) = fProductMap[id]?:"<unknown product>"

    operator fun set(id: Int, name: String) {
        fProductMap[id] = name
    }

}