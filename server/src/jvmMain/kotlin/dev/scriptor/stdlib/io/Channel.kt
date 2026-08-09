package dev.scriptor.stdlib.io

actual fun open(path: Path): Channel {
    return object : Channel {

        override fun read(buffer: Buffer): Long {
            TODO("Not yet implemented")
        }

        override fun write(buffer: MutableBuffer): Long {
            TODO("Not yet implemented")
        }

        override fun transferTo(channel: Channel): Long {
            TODO("Not yet implemented")
        }
    }
}
