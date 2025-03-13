package pl.jwizard.jwa.service.crypto

import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.property.ServerProperty
import pl.jwizard.jwa.core.util.base64decode
import pl.jwizard.jwa.core.util.base64encode
import pl.jwizard.jwl.property.BaseEnvironment
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

@Component
internal class EncryptService(environment: BaseEnvironment) {
	companion object {
		private const val ALGORITHM = "AES"
	}

	private val aesSecretKey = environment.getProperty<String>(ServerProperty.SERVER_AES_SECRET_KEY)
	private val secretKey = SecretKeySpec(aesSecretKey.toByteArray(), ALGORITHM)

	fun encrypt(plainText: String): String {
		val cipher = Cipher.getInstance(ALGORITHM)
		cipher.init(Cipher.ENCRYPT_MODE, secretKey)
		val encryptedValue = cipher.doFinal(plainText.toByteArray())
		return base64encode(encryptedValue)
	}

	fun decrypt(encryptedText: String): String {
		val cipher = Cipher.getInstance(ALGORITHM)
		cipher.init(Cipher.DECRYPT_MODE, secretKey)
		val decodedValue = base64decode(encryptedText)
		return String(cipher.doFinal(decodedValue))
	}
}
