package me.drton.jmavlib.mavlink

import me.drton.jmavlib.createHeartbeatMessage
import me.drton.jmavlib.support.ByteStringChannel
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class MAVLinkMessageTest {

    private lateinit var schema: MAVLinkSchema

    @Before
    fun setUp() {
        MAVLinkSchemaRegistry[RuntimeEnvironment.application, "common"] = "schemas/common.xml"
        schema = MAVLinkSchemaRegistry["common"]!!
    }

    @Test
    fun heartbeat_messageIsSerializedCorrectly() {
        val heartbeat = createHeartbeatMessage(8, 250, schema)
        val channel = ByteStringChannel()

        MAVLinkStream(schema, channel).write(heartbeat)

        assertThat(channel.toString(), equalTo("0xfe\n0x09\n0x00\n0x08\n0xfa\n0x00\n0x00\n0x00\n0x00\n0x00\n0x06\n0x00\n0x80\n0x04\n0x00\n0xb9\n0x7c"))
    }

}
