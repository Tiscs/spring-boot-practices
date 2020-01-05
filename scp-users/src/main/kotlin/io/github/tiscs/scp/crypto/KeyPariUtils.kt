package io.github.tiscs.scp.crypto

import org.bouncycastle.asn1.ASN1Sequence
import org.bouncycastle.asn1.pkcs.RSAPrivateKey
import org.bouncycastle.asn1.pkcs.RSAPublicKey
import org.springframework.security.jwt.codec.Codecs.b64Decode
import org.springframework.security.jwt.codec.Codecs.utf8Encode
import java.security.KeyFactory
import java.security.KeyPair
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.security.spec.RSAPrivateCrtKeySpec
import java.security.spec.RSAPublicKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.regex.Pattern

private val PEM_DATA = Pattern.compile("-----BEGIN (.*)-----(.*)-----END (.*)-----", Pattern.DOTALL)

/**
 * @see org.springframework.security.jwt.crypto.sign.RsaKeyHelper.parseKeyPair
 */
fun parseKeyPair(pem: String): KeyPair {
    val matcher = PEM_DATA.matcher(pem.trim())

    if (!matcher.matches()) {
        throw IllegalArgumentException("String is not PEM encoded data")
    }

    val keyType = matcher.group(1)
    val content = b64Decode(utf8Encode(matcher.group(2)))

    try {
        val factory = KeyFactory.getInstance("RSA")
        return when (keyType) {
            "RSA PRIVATE KEY" -> {
                val seq = ASN1Sequence.getInstance(content)
                if (seq.size() != 9) {
                    throw IllegalArgumentException("Invalid RSA Private Key ASN1 sequence.")
                }
                val key = RSAPrivateKey.getInstance(seq)
                val publicKey = factory.generatePublic(RSAPublicKeySpec(key.modulus, key.publicExponent))
                val privateKey = factory.generatePrivate(RSAPrivateCrtKeySpec(key.modulus, key.publicExponent,
                        key.privateExponent, key.prime1, key.prime2, key.exponent1, key.exponent2, key.coefficient))
                KeyPair(publicKey, privateKey)
            }
            "PUBLIC KEY" -> {
                val spec = X509EncodedKeySpec(content)
                KeyPair(factory.generatePublic(spec), null)
            }
            "RSA PUBLIC KEY" -> {
                val seq = ASN1Sequence.getInstance(content)
                val key = RSAPublicKey.getInstance(seq)
                val publicKey = factory.generatePublic(RSAPublicKeySpec(key.modulus, key.publicExponent))
                KeyPair(publicKey, null)
            }
            else -> {
                throw java.lang.IllegalArgumentException("$keyType is not a supported format")
            }
        }
    } catch (e: InvalidKeySpecException) {
        throw RuntimeException(e)
    } catch (e: NoSuchAlgorithmException) {
        throw IllegalStateException(e)
    }
}
