package dev.scriptor.io.channels

interface SocketChannel : ByteChannel, ScatteringByteChannel, GatheringByteChannel, NetworkChannel {}
