// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
fun implementation(string: String) {}

dependencies {
    val navVersion = "2.8.0" // Usa la versión estable más reciente
    implementation("androidx.navigation:navigation-compose:$navVersion")
}