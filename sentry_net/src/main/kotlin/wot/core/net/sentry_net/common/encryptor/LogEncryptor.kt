package wot.core.net.sentry_net.common.encryptor

/**
 * 加密接口, 自定义加密需要实现它
 *
 * @author : yangsn
 * @date : 2025/5/28
 */
interface LogEncryptor {

    fun encrypt(input: String): String

    fun decrypt(input: String): String
}
