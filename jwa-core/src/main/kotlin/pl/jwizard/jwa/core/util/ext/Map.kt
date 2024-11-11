/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.core.util.ext

/**
 * Extension function for `Map<String, Any>` to retrieve the value associated with the given key and convert it to a
 * string. If the key doesn't exist, it returns `null`.
 *
 * @param key The key whose associated value is to be returned as a string.
 * @return The string representation of the value associated with the given key, or `null` if the key does not exist.
 * @author Miłosz Gilga
 */
fun Map<String, Any>.take(key: String) = this[key].toString()

/**
 * Extension function for `MutableMap<String, Any>` to save a key-value pair to the map, where the value is a [String].
 *
 * This function simplifies saving a string value into the map by using a [String] key. It replaces any existing value
 * associated with the key with the new value.
 *
 * @param key The key with which the specified value is to be associated.
 * @param value The value to be associated with the specified key.
 * @author Miłosz Gilga
 */
fun MutableMap<String, Any>.save(key: String, value: String) {
	this[key] = value
}
