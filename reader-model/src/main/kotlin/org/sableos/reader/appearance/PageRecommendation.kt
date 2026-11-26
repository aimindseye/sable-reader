package org.sableos.reader.appearance

/** The reading page the Reader recommends. Each is one of the page looks the Reader already has. */
enum class RecommendedPage(val label: String, val reason: String) {
    /** The plain Light or Dark page that follows the Reader's appearance. */
    AUTOMATIC("Light or dark page", "Soft dark grey in the dark, so the text does not glow or smear."),

    /** Warm paper. */
    SEPIA("Sepia", "Warm paper takes the glare out of a bright page."),

    /** White text on a pure black page. */
    WHITE_ON_BLACK("White on black", "Pure black switches the pixels off on an AMOLED screen: sharper text and less battery."),
}

/**
 * Which page to recommend for the Reader's appearance and screen. Unknown screens count as not AMOLED, because pure
 * black on a backlit LCD glows grey and makes white text bloom.
 */
object PageRecommendation {
    fun recommend(dark: Boolean, amoled: Boolean): RecommendedPage =
        when {
            !dark -> RecommendedPage.SEPIA
            amoled -> RecommendedPage.WHITE_ON_BLACK
            else -> RecommendedPage.AUTOMATIC
        }
}
