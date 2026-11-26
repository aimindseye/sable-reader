package org.sableos.reader.setup

/**
 * The setup wizard's steps, in order. [PEOPLE] comes before the looks because each profile keeps its own settings:
 * the profile has to exist before the look is chosen for it.
 */
enum class SetupStep(val title: String) {
    WELCOME("Welcome"),
    OFFLINE("Private and offline"),
    PEOPLE("Who reads here?"),
    ACCESSIBILITY("How do you read?"),
    PHONE("Your phone"),
    LOOK("Look"),
    LIBRARY("Your books"),
    KEYS("Try the keys"),
    DONE("All set"),
    ;

    fun next(): SetupStep? = entries.getOrNull(ordinal + 1)

    fun previous(): SetupStep? = entries.getOrNull(ordinal - 1)

    /** "Step 3 of 9", read out and shown so nobody has to guess how far there is to go. */
    fun progress(): String = "Step ${ordinal + 1} of ${entries.size}"
}
