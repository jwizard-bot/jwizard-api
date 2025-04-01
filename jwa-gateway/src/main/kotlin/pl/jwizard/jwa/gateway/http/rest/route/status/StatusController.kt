package pl.jwizard.jwa.gateway.http.rest.route.status

import io.javalin.http.pathParamAsClass
import io.javalin.http.queryParamAsClass
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.server.ValidatorChainFacade
import pl.jwizard.jwl.server.route.HttpControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder
import pl.jwizard.jwl.server.route.handler.RouteHandler

@Component
internal class StatusController(private val statusService: StatusService) : HttpControllerBase {
	override val basePath = "/v1/status"

	private val getGlobalStatus = RouteHandler { ctx ->
		val resDto = statusService.getGlobalStatus()
		ctx.json(resDto)
	}

	// all instances combined status from all running shards
	private val getInstancesStatus = RouteHandler { ctx ->
		val avatarSize = ctx.queryParam("avatarSize")
		val resDto = statusService.getInstancesStatus(avatarSize?.toIntOrNull())
		ctx.json(resDto)
	}

	// single instance shards status
	private val getInstanceShardsStatus = RouteHandler { ctx ->
		val instanceId = ctx.pathParamAsClass<Int>("instanceId")
		val parsedInstanceId = ValidatorChainFacade(instanceId).get()
		val resDto = statusService.getInstanceShardsStatus(parsedInstanceId)
		ctx.json(resDto)
	}

	// all audio nodes status (also inactive)
	private val getAudioNodesStatus = RouteHandler { ctx ->
		val resDto = statusService.getAudioNodesStatus()
		ctx.json(resDto)
	}

	// check, if shard for selected guild and (optional instance) is up and running
	// if shard is down or if bot is not on selected guild, return false
	// instanceId=null -> search in all bot instances
	private val checkIfShardForGuildNameOrIdIsUp = RouteHandler { ctx ->
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
		.get("/global", getGlobalStatus)
		.get("/instance/all", getInstancesStatus)
		.get("/instance/<instanceId>/shard/all", getInstanceShardsStatus)
		.get("/audio/all", getAudioNodesStatus)
		.get("/shard/up", checkIfShardForGuildNameOrIdIsUp)
		.compositeRoutes()
}
