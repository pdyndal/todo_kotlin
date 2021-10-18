import java.nio.ByteBuffer
import java.util.UUID

fun UUID.asBytes(): ByteArray {
    val bytes = ByteBuffer.wrap(ByteArray(16))
    bytes.putLong(mostSignificantBits)
    bytes.putLong(leastSignificantBits)

    return bytes.array()
}