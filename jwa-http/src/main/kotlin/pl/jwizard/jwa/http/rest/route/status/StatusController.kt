package pl.jwizard.jwa.http.rest.route.status

import io.javalin.http.Context
import io.javalin.http.pathParamAsClass
import io.javalin.http.queryParamAsClass
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.server.ValidatorChainFacade
import pl.jwizard.jwl.server.route.HttpControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder

@Component
internal class StatusController(private val statusService: StatusService) : HttpControllerBase {
	override val basePath = "/v1/status"

	private fun getGlobalStatus(ctx: Context) {
		val resDto = statusService.getGlobalStatus()
		ctx.json(resDto)
	}

	// all instances combined status from all running shards
	private fun getInstancesStatus(ctx: Context) {
		val avatarSize = ctx.queryParam("avatarSize")
		val resDto = statusService.getInstancesStatus(avatarSize?.toIntOrNull())
		ctx.json(resDto)
	}

	// single instance shards status
	private fun getInstanceShardsStatus(ctx: Context) {
		val instanceId = ctx.pathParamAsClass<Int>("instanceId")
		val parsedInstanceId = ValidatorChainFacade(instanceId).get()
		val resDto = statusService.getInstanceShardsStatus(parsedInstanceId)
		ctx.json(resDto)
	}

	// all audio nodes status (also inactive)
	private fun getAudioNodesStatus(ctx: Context) {
		val resDto = statusService.getAudioNodesStatus()
		ctx.json(resDto)
	}

	// check, if shard for selected guild and (optional instance) is up and running
	// if shard is down or if bot is not on selected guild, return false
	// instanceId=null -> search in all bot instances
	private fun checkIfShardForGuildNameOrIdIsUp(ctx: Context) {
		val guildNameOrId = ctx.queryParamAsClass<String>("guild")
		val instanceId = ctx.queryParam("instanceId")
		val parsedGuildNameOrId = ValidatorChainFacade(guildNameOrId).disallowBlanks().get()
		val resDto = statusService.checkIfShardForGuildNameOrIdIsUp(
			parsedGuildNameOrId,
			instanceId?.toIntOrNull(),
		)
		ctx.json(resDto)
	}

	override val routes = RouteDefinitionBuilder()
		.get("/global", ::getGlobalStatus)
		.get("/instance/all", ::getInstancesStatus)
		.get("/instance/<instanceId>/shard/all", ::getInstanceShardsStatus)
		.get("/audio/all", ::getAudioNodesStatus)
		.get("/shard/up", ::checkIfShardForGuildNameOrIdIsUp)
		.compositeRoutes()
}
