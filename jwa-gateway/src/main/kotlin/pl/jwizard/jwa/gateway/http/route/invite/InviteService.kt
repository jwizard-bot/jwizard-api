package pl.jwizard.jwa.gateway.http.route.invite

interface InviteService {
	fun createInviteRedirectUrl(instanceId: Int): String?
}
