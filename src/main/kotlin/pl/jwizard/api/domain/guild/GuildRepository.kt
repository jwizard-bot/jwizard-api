/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.domain.guild

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface GuildRepository : MongoRepository<GuildDocument, ObjectId> {
	fun findByGuildId(guildId: String): Optional<GuildDocument>
	fun existsByGuildId(guildId: String): Boolean
	fun deleteByGuildId(guildId: String)
}
