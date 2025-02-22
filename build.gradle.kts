// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
	alias(libs.plugins.android.application) apply false
	alias(libs.plugins.jetbrains.kotlin.android) apply false
	id("org.jetbrains.kotlin.plugin.serialization") version "1.6.0"
	id ("com.google.dagger.hilt.android") version "2.49" apply false
	id("com.google.devtools.ksp") version "1.9.24-1.0.20" apply false
	id("com.android.library") version "8.5.2" apply false
	//kotilit1.9.24//comp1.5.14
	//alias(libs.plugins.compose.compiler) apply false//kotlin 2.0

}