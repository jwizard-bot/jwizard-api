package pl.jwizard.jwa.service

import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.property.ServerProperty
import pl.jwizard.jwl.http.AuthTokenType
import pl.jwizard.jwl.http.SecureHttpClientService
import pl.jwizard.jwl.property.BaseEnvironment

@Component
internal class GithubApiService(
	private val secureHttpClientService: SecureHttpClientService,
	private val environment: BaseEnvironment,
) {
	private val githubApiUrl = environment.getProperty<String>(ServerProperty.GITHUB_API_URL)
	private val githubToken = environment.getProperty<String>(ServerProperty.GITHUB_API_TOKEN)

	fun getGithubProgrammingLanguageColors() = secureHttpClientService.prepareAndRunSecureHttpRequest(
		url = environment.getProperty<String>(ServerProperty.GITHUB_LANGUAGE_COLOR_API_URL),
	)

	fun performGithubGetRequest(
		urlSuffix: String,
	) = secureHttpClientService.prepareAndRunSecureHttpRequest(
		url = "$githubApiUrl$urlSuffix",
		authToken = githubToken,
		authTokenType = AuthTokenType.TOKEN,
	)
}
