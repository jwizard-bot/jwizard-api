/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.command

enum class CommandCategory(
	val regularName: String,
	val commandSupplier: (properties: CommandProperties) -> Pair<String, List<Command>>
) {
	OTHERS("others", { properties -> "others" to properties.others }),
	MUSIC("music", { properties -> "music" to properties.music }),
	PLAYLIST("playlist", { properties -> "music" to properties.playlist }),
	DJ("dj", { properties -> "dj" to properties.dj }),
	VOTE("vote", { properties -> "vote" to properties.vote }),
	;
}
