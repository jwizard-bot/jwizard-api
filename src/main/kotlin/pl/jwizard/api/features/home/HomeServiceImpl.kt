/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.features.home

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import pl.jwizard.api.features.home.dto.KeyFeatureResDto
import pl.jwizard.api.features.home.dto.StatsInfoResDto
import pl.jwizard.api.i18n.I18nService
import pl.jwizard.api.scaffold.AbstractLoggingBean
import pl.jwizard.api.util.JdbcUtils.parse

@Service
class HomeServiceImpl(
	private val jdbcTemplate: JdbcTemplate,
	private val i18nService: I18nService,
) : HomeService, AbstractLoggingBean(HomeServiceImpl::class) {

	override fun getHomePageStats(): StatsInfoResDto = StatsInfoResDto(
		modules = getElementsCountByColumn("command_modules"),
		commands = getElementsCountByColumn("bot_commands"),
		radioStations = getElementsCountByColumn("radio_stations"),
		audioSources = getElementsCountByColumn("audio_sources"),
	)

	override fun getHomePageKeyFeaturs(): List<KeyFeatureResDto> {
		val currentLang = i18nService.getCurrentLanguage()
		val sql = parse(
			"SELECT title_{{lng}}, desc_{{lng}}, is_active FROM key_features",
			mapOf("lng" to currentLang),
		)
		val keyFeatures = jdbcTemplate
			.queryForList(sql)
			.map {
				KeyFeatureResDto(
					name = it["title_$currentLang"] as String,
					description = it["desc_$currentLang"] as String,
					isActive = it["is_active"] as Boolean
				)
			}
		log.debug("Successfully fetched: {} key features", keyFeatures.size)
		return keyFeatures
	}

	private fun getElementsCountByColumn(columnName: String): Int {
		val sql = parse(
			"SELECT COUNT(*) FROM {{columnName}}",
			mapOf("columnName" to columnName),
		)
		return jdbcTemplate.queryForObject(sql, Int::class.java) ?: 0
	}
}
