package pl.jwizard.jwa.gateway.http.rest.route.session

import pl.jwizard.jwa.core.server.filter.LoggedUser
import pl.jwizard.jwa.gateway.http.rest.route.session.dto.CsrfTokenResDto
import pl.jwizard.jwa.gateway.http.rest.route.session.dto.GeolocationProviderInfoResDto
import pl.jwizard.jwa.gateway.http.rest.route.session.dto.RevalidateStateResDto
import pl.jwizard.jwa.gateway.http.rest.route.session.dto.SessionsDataResDto

interface SessionService {
	fun mySessions(loggedUser: LoggedUser): SessionsDataResDto

	fun geolocationProviderInfo(): GeolocationProviderInfoResDto

	// return true, if session exists, otherwise false
	fun deleteMySessionBasedSessionId(toDeleteSessionId: String, loggedUser: LoggedUser): Boolean

	fun deleteAllMySessionsWithoutCurrentSession(loggedUser: LoggedUser)

	fun updateAndGetCsrfToken(sessionId: String): CsrfTokenResDto

	fun revalidate(sessionId: String?): RevalidateStateResDto

	fun logout(loggedUser: LoggedUser): Boolean
}
