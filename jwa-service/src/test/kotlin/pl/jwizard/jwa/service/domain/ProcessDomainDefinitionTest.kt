package pl.jwizard.jwa.service.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import pl.jwizard.jwa.service.instance.domain.DevProcessDomainDefinition
import pl.jwizard.jwa.service.instance.domain.ProcessDefinition
import pl.jwizard.jwa.service.instance.domain.ProdProcessDomainDefinition
import pl.jwizard.jwl.vault.kvgroup.VaultKvGroupProperties
import java.util.*
import kotlin.random.Random

class ProcessDomainDefinitionTest {
	private val devProcessDomainDefinition = DevProcessDomainDefinition()
	private val prodProcessDomainDefinition = ProdProcessDomainDefinition()

	private val devPathDefinition = "http://localhost:%s"
	private val prodPathDefinition = "https://bot-instance-%s-f%se%s.jwizard.pl"

	@Test
	fun `should create dev cluster path definitions`() {
		val randomPort = Random.nextInt(49152, 65535)

		val properties = Properties()
		properties.setProperty("V_SERVER_PORT", randomPort.toString())
		properties.setProperty("V_SHARDS_PER_PROCESS", "2")

		val expectedPaths = listOf(ProcessDefinition(devPathDefinition.format(randomPort), 2))

		val paths = devProcessDomainDefinition.generatePaths(
			pathDefinition = devPathDefinition,
			properties = VaultKvGroupProperties(properties),
		)
		assertEquals(expectedPaths.toSet(), paths.toSet())
	}

	@Test
	fun `should create prod cluster path definitions`() {
		val prefix = "1"

		val properties = Properties()
		properties.setProperty("V_JDA_INSTANCE_PREFIX", prefix)
		properties.setProperty("V_SHARDS_PER_PROCESS", "12")
		properties.setProperty("V_SHARD_OVERALL_MAX", "29")

		val expectedPaths = listOf(
			ProcessDefinition(prodPathDefinition.format(prefix, 0, 11), 11),
			ProcessDefinition(prodPathDefinition.format(prefix, 12, 23), 11),
			ProcessDefinition(prodPathDefinition.format(prefix, 24, 29), 5),
		)
		val paths = prodProcessDomainDefinition.generatePaths(
			pathDefinition = prodPathDefinition,
			properties = VaultKvGroupProperties(properties),
		)
		assertEquals(expectedPaths.toSet(), paths.toSet())
	}
}
