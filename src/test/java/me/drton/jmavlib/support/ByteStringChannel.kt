package me.drton.jmavlib.support

import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.channels.ByteChannel

class ByteStringChannel : ByteChannel {
    private val fBuffer = StringBuilder()

    override fun toString(): String {
        return fBuffer.toString().trim()
    }

    override fun isOpen(): Boolean = true

    override fun write(buffer: ByteBuffer): Int {
        for (index in 0 until buffer.remaining()) {
            fBuffer.append(String.format("0x%02x\n", buffer[index]))
        }

        return fBuffer.length
    }

    override fun close() = Unit

    override fun read(buffer: ByteBuffer?): Int = buffer?.let(ByteBuffer::asCharBuffer)?.let(CharBuffer::length)
            ?: 0
}