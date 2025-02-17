package pl.jwizard.jwa.service

import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.i18n.I18nAppFragmentSource
import pl.jwizard.jwa.rest.route.home.HomeService
import pl.jwizard.jwa.rest.route.home.dto.FeaturesResDto
import pl.jwizard.jwa.rest.route.home.dto.StatisticsInfoResDto
import pl.jwizard.jwa.service.spi.KeyFeaturesSupplier
import pl.jwizard.jwa.service.spi.ProjectPackagesSupplier
import pl.jwizard.jwl.command.Command
import pl.jwizard.jwl.command.Module
import pl.jwizard.jwl.i18n.I18n
import pl.jwizard.jwl.property.AppBaseProperty
import pl.jwizard.jwl.property.BaseEnvironment
import pl.jwizard.jwl.radio.RadioStation

@Component
internal class HomeServiceImpl(
	private val i18n: I18n,
	private val projectPackagesSupplier: ProjectPackagesSupplier,
	private val keyFeaturesSupplier: KeyFeaturesSupplier,
	private val environment: BaseEnvironment,
) : HomeService {
	override fun getHomePageStatistics() = StatisticsInfoResDto(
		modules = Module.entries.size,
		commands = Command.entries.size,
		radioStations = RadioStation.entries.size,
		openSourceLibraries = projectPackagesSupplier.getProjectPackagesCount()
	)

	override fun getHomePageFeatures(language: String?): List<FeaturesResDto> {
		val keyFeatures = keyFeaturesSupplier.getKeyFeatures()
		val args = mapOf(
			"prefix" to environment.getProperty<String>(AppBaseProperty.GUILD_LEGACY_PREFIX)
		)
		return keyFeatures.map { (textId, isActive) ->
			FeaturesResDto(
				name = i18n.tRaw(I18nAppFragmentSource.KEY_FEATURE_NAME, arrayOf(textId), language),
				description = i18n.tRaw(
					I18nAppFragmentSource.KEY_FEATURE_DESCRIPTION, arrayOf(textId), args, language
				),
				isActive,
			)
		}.sortedByDescending(FeaturesResDto::isActive)
	}
}
