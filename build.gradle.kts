// Root build.gradle.kts for Sable Reader .
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.dagger.hilt.android) apply false
    alias(libs.plugins.devtools.ksp) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.jetbrains.dokka) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.ktlint) apply false
}

val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")
val appVersionName = libs.findVersion("app-versionName").get().requiredVersion

version = appVersionName

// Detekt runs on every module with the repository configuration (config/detekt.yml) plus the overlay
// config/detekt-reader.yml (PascalCase @Composable functions only).
//
// Scope: Sable-owned code. The imported Vaachak-derived sources (org.vaachak.*) and the canonical SableOS design
// sources (org.sableos.design) are third-party for this purpose and are excluded until they migrate into the
// org.sableos.reader namespace. There is no findings allowlist: everything in scope must be finding-free.
//
// ktlint reports on every module but does not fail the build yet (ignoreFailures), as in Sable Games; the imported
// sources are formatted gradually.
val readerRoot = rootProject.projectDir

// <detekt-config>
val detektConfigFile = readerRoot.resolve("config/detekt.yml")
val detektReaderOverlay = readerRoot.resolve("config/detekt-reader.yml")

subprojects {
    pluginManager.apply("io.gitlab.arturbosch.detekt")

    extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        buildUponDefaultConfig = true
        ignoreFailures = false
        parallel = true
        config.setFrom(detektReaderOverlay, detektConfigFile)
        source.setFrom(files("src"))
    }

    pluginManager.apply("org.jlleitschuh.gradle.ktlint")
    extensions.configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set("1.8.0")
        android.set(true)
        ignoreFailures.set(true)
        outputToConsole.set(true)
        reporters {
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.SARIF)
        }
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        exclude("**/org/vaachak/**", "**/org/sableos/design/**")
    }
}
// </detekt-config>

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
    group = "clean"
    description = "Clean build directory"
}
