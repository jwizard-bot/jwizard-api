package pl.jwizard.jwa.rest.route.status

import pl.jwizard.jwa.rest.route.status.dto.*

interface StatusService {
	fun getGlobalStatus(): GlobalStatusResDto

	fun getInstancesStatus(avatarSize: Int?): List<InstanceStatusResDto>

	fun getInstanceShardsStatus(instanceId: Int): List<ShardStatusResDto>

	fun getAudioNodesStatus(): AudioNodesStatusResDto

	fun checkIfShardForGuildNameOrIdIsUp(guildNameOrId: String, instanceId: Int?): ShardCheckResDto
}
