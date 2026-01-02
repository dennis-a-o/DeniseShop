plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.compose)
	alias(libs.plugins.dagger.hilt.android)
	alias(libs.plugins.kotlin.serialization)
	alias(libs.plugins.ksp)
}

android {
	namespace = "com.example.deniseshop"
	compileSdk = 36

	defaultConfig {
		applicationId = "com.example.deniseshop"
		minSdk = 24
		targetSdk = 36
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
			signingConfig = signingConfigs.getByName("debug")
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}

	kotlin{
		jvmToolchain(17)
	}

	buildFeatures {
		compose = true
	}

	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
	hilt {
		enableAggregatingTask = true
	}
}

dependencies {
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.ui.graphics)
	implementation(libs.androidx.ui.tooling.preview)
	implementation(libs.androidx.material3)
	//kotlin serialization
	implementation(libs.kotlinx.serialization.json)
	implementation(libs.retrofit.kotlinx.serialization.converter)
	//preff datastore
	implementation(libs.androidx.datastore.preferences.core)
	//Protobuff datastore
	implementation(libs.androidx.datastore.core)
	//splash
	implementation(libs.androidx.core.splashscreen)
	//Navigation277
	implementation(libs.androidx.navigation.compose)
	// Lifecycle components283
	// viewModel
	implementation(libs.androidx.lifecycle.viewmodel.ktx)
	// viewModel utilities for Compose
	implementation (libs.androidx.lifecycle.viewmodel.compose)
	// LiveData
	implementation (libs.androidx.lifecycle.livedata.ktx)
	implementation (libs.androidx.runtime.livedata)
	// Lifecycles only (without viewModel or LiveData)
	implementation (libs.androidx.lifecycle.runtime.ktx)
	// Retrofit
	implementation (libs.retrofit)
	implementation(libs.converter.scalars)
	// Retrofit with Moshi Converter
	implementation (libs.converter.moshi)
	// OkHttp
	implementation (libs.okhttp)
	// Moshi
	implementation (libs.moshi.kotlin)
	//testImplementation(libs.junit.jupiter)
	//ksp(libs.moshi.kotlin.codegen)
	//gson
	implementation (libs.gson)
	implementation (libs.converter.gson)
	// Hilt
	implementation (libs.hilt.android)
	ksp (libs.hilt.compiler)
	//annotationProcessor (libs.hilt.compiler)
	implementation (libs.androidx.hilt.navigation.compose)
	// Room260
	implementation(libs.androidx.room.runtime)
	implementation(libs.androidx.room.ktx)
	//annotationProcessor(libs.androidx.room.compiler)
	//kapt("androidx.room:room-compiler:$room_version")
	ksp(libs.androidx.room.room.compiler)
	//Coil
	implementation(libs.coil.compose)
	//paging 330
	implementation (libs.androidx.paging.runtime.ktx)
	implementation (libs.androidx.paging.compose)
	//coroutines
	implementation(libs.kotlinx.coroutines.android)
	implementation(libs.kotlinx.coroutines.core)
	implementation(libs.androidx.lifecycle.runtime.compose)
	//lottie
	implementation (libs.android.lottie.compose)
	//paypal
	implementation (libs.card.payments)
	implementation (libs.paypal.web.payments)
	//webkit
	implementation(libs.androidx.browser)
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.androidx.ui.test.junit4)
	debugImplementation(libs.androidx.ui.tooling)
	debugImplementation(libs.androidx.ui.test.manifest)
}