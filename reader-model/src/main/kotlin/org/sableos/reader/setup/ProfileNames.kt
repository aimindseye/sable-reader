package org.sableos.reader.setup

import kotlin.random.Random

/** Random reader names such as "Quiet Owl", for people who do not want to type one. Used by the wizard and the profile screen. */
object ProfileNames {
    val adjectives = listOf("Quiet", "Curious", "Sleepy", "Brave", "Clever", "Cosmic", "Wandering", "Lunar", "Mystic", "Nimble")
    val nouns = listOf("Reader", "Owl", "Fox", "Panda", "Badger", "Bear", "Cat", "Dragon", "Raven", "Wolf")

    fun random(random: Random = Random.Default): String = "${adjectives.random(random)} ${nouns.random(random)}"
}
