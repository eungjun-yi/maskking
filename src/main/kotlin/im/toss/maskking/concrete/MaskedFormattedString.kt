package im.toss.maskking.concrete

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonValue
import im.toss.maskking.MaskedString


class MaskedFormattedString(
        private val format: String,
        vararg args: Any?
): MaskedString {

    private val maskedArgs = args

    private val masked by lazy {
        String.format(format, *maskedArgs)
    }

    private val unmasked by lazy {
        val unmaskedArgs = maskedArgs.map {
            when(it) {
                is MaskedString -> it.unmasked()
                else -> it
            }
        }.toTypedArray()
        String.format(format, *unmaskedArgs)
    }

    @JsonValue
    override fun toString() = masked
    override fun unmasked() = unmasked

    override fun equals(other: Any?): Boolean {
        return when(other) {
            is MaskedFormattedString -> unmasked.equals(other.unmasked())
            else -> super.equals(other)
        }
    }

    override fun hashCode(): Int {
        return unmasked.hashCode()
    }

    override val length: Int
        @JsonIgnore
        get() = masked.length

    override fun get(index: Int): Char {
        return masked[index]
    }

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
        return masked.subSequence(startIndex, endIndex)
    }
}
