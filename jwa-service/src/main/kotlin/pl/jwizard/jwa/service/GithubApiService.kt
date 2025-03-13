package pl.jwizard.jwa.service

import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.property.ServerProperty
import pl.jwizard.jwa.service.http.AuthTokenType
import pl.jwizard.jwa.service.http.SecureHttpService
import pl.jwizard.jwl.property.BaseEnvironment

@Component
internal class GithubApiService(
	private val secureHttpService: SecureHttpService,
	private val environment: BaseEnvironment,
) {
	private val githubApiUrl = environment.getProperty<String>(ServerProperty.GITHUB_API_URL)
	private val githubToken = environment.getProperty<String>(ServerProperty.GITHUB_API_TOKEN)

	fun getGithubProgrammingLanguageColors() = secureHttpService.prepareAndRunSecureHttpRequest(
		url = environment.getProperty<String>(ServerProperty.GITHUB_LANGUAGE_COLOR_API_URL),
	)

	fun performGithubGetRequest(urlSuffix: String) = secureHttpService.prepareAndRunSecureHttpRequest(
		url = "$githubApiUrl$urlSuffix",
		authToken = githubToken,
		authTokenType = AuthTokenType.TOKEN,
	)
}
