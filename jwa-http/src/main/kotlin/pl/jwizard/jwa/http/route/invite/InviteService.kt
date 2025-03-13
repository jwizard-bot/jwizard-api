package pl.jwizard.jwa.http.route.invite

interface InviteService {
	fun createInviteRedirectUrl(instanceId: Int): String?
}
