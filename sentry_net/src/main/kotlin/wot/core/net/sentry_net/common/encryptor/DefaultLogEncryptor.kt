package wot.core.net.sentry_net.common.encryptor

import android.util.Base64

/**
 * 默认实现, Base64 加密
 *
 * @author : yangsn
 * @date : 2025/5/28
 */

object DefaultLogEncryptor : LogEncryptor {
    
    override fun encrypt(input: String): String {
        return Base64.encodeToString(input.toByteArray(), Base64.NO_WRAP)
    }

    override fun decrypt(input: String): String {
        return String(Base64.decode(input, Base64.NO_WRAP))
    }
}