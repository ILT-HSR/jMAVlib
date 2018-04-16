package me.drton.jmavlib.mavlink

import me.drton.jmavlib.MAVLINK_SCHEMA_COMMON
import me.drton.jmavlib.newMAVLinkHeartbeat
import me.drton.jmavlib.support.ByteStringChannel
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.BeforeClass
import org.junit.Test

class MAVLinkMessageTest {

    companion object {
        @BeforeClass
        @JvmStatic
        fun setupClass() {
            MAVLINK_SCHEMA_COMMON = MAVLinkSchema(javaClass.classLoader.getResourceAsStream("assets/common.xml"))
        }
    }

    @Test
    fun heartbeat_messageIsSerializedCorrectly() {
        val heartbeat = newMAVLinkHeartbeat()
        val channel = ByteStringChannel()

        MAVLinkStream(MAVLINK_SCHEMA_COMMON, channel).write(heartbeat)

        assertThat(channel.toString(), equalTo("0xfe\n0x09\n0x00\n0x08\n0xfa\n0x00\n0x00\n0x00\n0x00\n0x00\n0x06\n0x00\n0x80\n0x04\n0x00\n0xb9\n0x7c"))
    }

}
