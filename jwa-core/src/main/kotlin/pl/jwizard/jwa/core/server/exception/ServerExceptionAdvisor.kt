package pl.jwizard.jwa.core.server.exception

import io.javalin.http.Context
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.server.ApiServerAttribute
import pl.jwizard.jwl.i18n.I18n
import pl.jwizard.jwl.server.exception.ExceptionAdvisor
import pl.jwizard.jwl.server.getAttribute
import pl.jwizard.jwl.util.logger

@Component
class ServerExceptionAdvisor(private val i18n: I18n) : ExceptionAdvisor<LocalizedHttpException> {
	companion object {
		private val log = logger<ServerExceptionAdvisor>()
	}

	override val clazz = LocalizedHttpException::class

	override fun executeStatement(ex: LocalizedHttpException, ctx: Context) {
		val language = ctx.getAttribute<String>(ApiServerAttribute.I18N_LOCALE)
		val error = mapOf("message" to i18n.t(ex.i18nLocaleSource, language, ex.args))
		log.error("Server controlled exception. Cause: {}.", ex.message)
		ctx.status(ex.httpStatus).json(error)
	}
}
