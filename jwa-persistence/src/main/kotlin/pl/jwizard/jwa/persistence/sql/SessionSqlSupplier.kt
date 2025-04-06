package pl.jwizard.jwa.persistence.sql

import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.server.spi.SessionUser
import pl.jwizard.jwa.service.spi.SessionSupplier
import pl.jwizard.jwa.service.spi.dto.SessionDataRow
import pl.jwizard.jwa.service.spi.dto.SessionExpirationState
import pl.jwizard.jwl.persistence.sql.JdbiQuery
import pl.jwizard.jwl.persistence.sql.SqlColumn
import pl.jwizard.jwl.util.base64decode
import java.sql.JDBCType
import java.time.LocalDateTime
import java.time.ZoneOffset

@Component
class SessionSqlSupplier(private val jdbiQuery: JdbiQuery) : SessionSupplier {
	override fun getUserSession(sessionId: String): SessionUser? {
		val sql = """
			SELECT user_snowflake, access_token, refresh_token, token_expired_at_utc,
			session_expired_at_utc, csrf_token FROM user_sessions
			WHERE session_id = ? AND session_expired_at_utc > ?
		"""
		val now = LocalDateTime.now(ZoneOffset.UTC)
		return jdbiQuery.queryForNullableObject(sql, SessionUser::class, base64decode(sessionId), now)
	}

	override fun getMySessions(userSnowflake: Long): List<SessionDataRow> {
		val sql = """
			SELECT session_id, last_login_utc, device_system, device_mobile, geolocation_info
			FROM user_sessions WHERE session_expired_at_utc > ? AND user_snowflake = ?
		"""
		val now = LocalDateTime.now(ZoneOffset.UTC)
		return jdbiQuery.queryForList(sql, SessionDataRow::class, now, userSnowflake)
	}

	override fun getSessionEndTime(sessionId: String): LocalDateTime? {
		val sql = "SELECT session_expired_at_utc FROM user_sessions WHERE session_id = ?"
		return jdbiQuery.queryForNullableObject(sql, LocalDateTime::class, base64decode(sessionId))
	}

	override fun saveUserSession(
		sessionId: String,
		userSnowflake: Long,
		accessToken: String,
		refreshToken: String,
		tokenExpiredAtUtc: LocalDateTime,
		sessionExpiredAtUtc: LocalDateTime,
		lastLoginUtc: LocalDateTime,
		deviceSystem: String?,
		deviceMobile: Boolean?,
		geolocationInfo: String?,
		csrfToken: String,
	): Int {
		val columns = createCommonUserSessionColumns(
			sessionExpiredAtUtc, lastLoginUtc, csrfToken, deviceSystem, deviceMobile, geolocationInfo,
			accessToken, refreshToken, tokenExpiredAtUtc,
		)
		columns += "session_id" to SqlColumn(base64decode(sessionId), JDBCType.BINARY)
		columns += "user_snowflake" to SqlColumn(userSnowflake, JDBCType.BIGINT)
		return jdbiQuery.insertMultiples(tableName = "user_sessions", columns.toMap())
	}

	override fun updateUserSession(
		sessionId: String,
		sessionExpiredAtUtc: LocalDateTime,
		lastLoginUtc: LocalDateTime,
		deviceSystem: String?,
		deviceMobile: Boolean?,
		geolocationInfo: String?,
		csrfToken: String,
		accessToken: String?,
		refreshToken: String?,
		tokenExpiredAtUtc: LocalDateTime?,
	): Int {
		val columns = createCommonUserSessionColumns(
			sessionExpiredAtUtc, lastLoginUtc, csrfToken, deviceSystem, deviceMobile, geolocationInfo,
			accessToken, refreshToken, tokenExpiredAtUtc,
		)
		return jdbiQuery.updateSingle(
			tableName = "user_sessions",
			columns = columns.toMap(),
			findColumn = "session_id" to SqlColumn(base64decode(sessionId), JDBCType.BINARY),
		)
	}

	override fun updateTokenSession(
		sessionId: String,
		accessToken: String,
		refreshToken: String,
		tokenExpiredAtUtc: LocalDateTime,
	) = jdbiQuery.updateSingle(
		tableName = "user_sessions",
		columns = mapOf(
			"access_token" to SqlColumn(accessToken, JDBCType.VARCHAR),
			"refresh_token" to SqlColumn(refreshToken, JDBCType.VARCHAR),
			"token_expired_at_utc" to SqlColumn(tokenExpiredAtUtc, JDBCType.TIMESTAMP),
		),
		findColumn = "session_id" to SqlColumn(base64decode(sessionId), JDBCType.BINARY),
	)

	override fun updateCsrfToken(
		sessionId: String,
		encryptedCsrfToken: String,
	) = jdbiQuery.updateSingle(
		tableName = "user_sessions",
		columns = mapOf(
			"csrf_token" to SqlColumn(encryptedCsrfToken, JDBCType.VARCHAR),
		),
		findColumn = "session_id" to SqlColumn(base64decode(sessionId), JDBCType.BINARY),
	)

	override fun updateSessionTime(
		sessionId: String,
		sessionExpiredAtUtc: LocalDateTime,
	) = jdbiQuery.updateSingle(
		tableName = "user_sessions",
		columns = mapOf(
			"session_expired_at_utc" to SqlColumn(sessionExpiredAtUtc, JDBCType.TIMESTAMP),
		),
		findColumn = "session_id" to SqlColumn(base64decode(sessionId), JDBCType.BINARY),
	)

	override fun getSessionExpirationState(sessionId: String): SessionExpirationState? {
		val sql = """
			SELECT session_expired_at_utc, token_expired_at_utc, access_token, refresh_token
			FROM user_sessions WHERE session_id = ?
		"""
		return jdbiQuery.queryForNullableObject(
			sql,
			type = SessionExpirationState::class,
			base64decode(sessionId),
		)
	}

	override fun getNonExpiredAccessTokens(sessionId: String, userSnowflake: Long): List<String> {
		val sql = """
			SELECT access_token FROM user_sessions
			WHERE user_snowflake = ? AND token_expired_at_utc > ? AND session_id != ?
		"""
		return jdbiQuery.queryForList(
			sql,
			type = String::class,
			userSnowflake,
			LocalDateTime.now(ZoneOffset.UTC),
			base64decode(sessionId),
		)
	}

	override fun deleteUserSession(userSnowflake: Long, sessionId: String): Int {
		val sql = "DELETE FROM user_sessions WHERE session_id = ? AND user_snowflake = ?"
		return jdbiQuery.update(sql, base64decode(sessionId), userSnowflake)
	}

	override fun deleteAllUserSessions(
		sessionId: String,
		userSnowflake: Long,
	): Int {
		val sql = "DELETE FROM user_sessions WHERE user_snowflake = ? AND session_id != ?"
		return jdbiQuery.update(sql, userSnowflake, base64decode(sessionId))
	}

	private fun createCommonUserSessionColumns(
		sessionExpiredUtc: LocalDateTime,
		lastLoginUtc: LocalDateTime,
		csrfToken: String,
		deviceSystem: String?,
		deviceMobile: Boolean?,
		geolocationInfo: String?,
		accessToken: String? = null,
		refreshToken: String? = null,
		tokenExpiredAtUtc: LocalDateTime? = null,
	): MutableMap<String, SqlColumn> {
		val columns = mutableMapOf<String, SqlColumn>()

		columns += "session_expired_at_utc" to SqlColumn(sessionExpiredUtc, JDBCType.TIMESTAMP)
		columns += "last_login_utc" to SqlColumn(lastLoginUtc, JDBCType.TIMESTAMP)
		columns += "csrf_token" to SqlColumn(csrfToken, JDBCType.VARCHAR)

		deviceSystem?.let { columns += "device_system" to SqlColumn(it, JDBCType.VARCHAR) }
		deviceMobile?.let { columns += "device_mobile" to SqlColumn(it, JDBCType.BOOLEAN) }
		geolocationInfo?.let { columns += "geolocation_info" to SqlColumn(it, JDBCType.VARCHAR) }
		accessToken?.let { columns += "access_token" to SqlColumn(it, JDBCType.VARCHAR) }
		refreshToken?.let { columns += "refresh_token" to SqlColumn(it, JDBCType.VARCHAR) }
		tokenExpiredAtUtc?.let {
			columns += "token_expired_at_utc" to SqlColumn(it, JDBCType.TIMESTAMP)
		}
		return columns
	}
}
