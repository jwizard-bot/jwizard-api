package pl.jwizard.jwa.core.server.exception

import io.javalin.http.Context
import pl.jwizard.jwl.i18n.I18nBean
import pl.jwizard.jwl.ioc.stereotype.SingletonComponent
import pl.jwizard.jwl.server.exception.ExceptionResponse
import pl.jwizard.jwl.server.exception.ExceptionsAdvisorBase
import pl.jwizard.jwl.util.logger

@SingletonComponent
class ServerExceptionAdvisor(
	i18nBean: I18nBean,
) : ExceptionsAdvisorBase<LocalizedHttpException>(i18nBean) {
	companion object {
		private val log = logger<ServerExceptionAdvisor>()
	}

	override val clazz = LocalizedHttpException::class

	override fun advise(ex: LocalizedHttpException, ctx: Context): ExceptionResponse {
		log.error("Server controlled exception. Cause: {}.", ex.message)
		return ExceptionResponse(ex.httpStatus, ex.i18nLocaleSource, ex.args)
	}
}
