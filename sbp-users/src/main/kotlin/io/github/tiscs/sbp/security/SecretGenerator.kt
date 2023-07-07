package io.github.tiscs.sbp.security

import java.util.concurrent.ThreadLocalRandom

@Suppress("SpellCheckingInspection")
class SecretGenerator {
    companion object {
        const val DIGITS = "0123456789"
        const val UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        const val LOWER = "abcdefghijklmnopqrstuvwxyz"
        const val ALPHANUM = UPPER + LOWER + DIGITS
        const val BASE58 = "123456789abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ"

        fun randomCode(size: Int = 32, symbols: CharSequence = ALPHANUM): String {
            val buf = CharArray(size)
            for (i in buf.indices) {
                buf[i] = symbols[ThreadLocalRandom.current().nextInt(symbols.length)]
            }
            return String(buf)
        }
    }
}
