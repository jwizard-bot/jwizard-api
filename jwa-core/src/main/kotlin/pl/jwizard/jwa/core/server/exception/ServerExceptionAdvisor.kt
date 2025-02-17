package pl.jwizard.jwa.core.server.exception

import io.javalin.http.Context
import org.springframework.stereotype.Component
import pl.jwizard.jwl.i18n.I18n
import pl.jwizard.jwl.server.exception.ExceptionResponse
import pl.jwizard.jwl.server.exception.ExceptionsAdvisorBase
import pl.jwizard.jwl.util.logger

@Component
class ServerExceptionAdvisor(i18n: I18n) : ExceptionsAdvisorBase<LocalizedHttpException>(i18n) {
	companion object {
		private val log = logger<ServerExceptionAdvisor>()
	}

	override val clazz = LocalizedHttpException::class

	override fun advise(ex: LocalizedHttpException, ctx: Context): ExceptionResponse {
		log.error("Server controlled exception. Cause: {}.", ex.message)
		return ExceptionResponse(ex.httpStatus, ex.i18nLocaleSource, ex.args)
	}
}
