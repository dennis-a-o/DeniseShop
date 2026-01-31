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
		versionCode = 2
		versionName = "2.0"

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
	//Navigation2
	implementation(libs.androidx.navigation.compose)
	// Lifecycle components
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
	// OkHttp
	implementation (libs.okhttp)
	// Hilt
	implementation (libs.hilt.android)
	implementation(libs.androidx.compose.ui)
	ksp (libs.hilt.compiler)
	//annotationProcessor (libs.hilt.compiler)
	implementation (libs.androidx.hilt.navigation.compose)
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
	//navigation 3
	implementation(libs.androidx.navigation3.runtime)
	implementation(libs.androidx.navigation3.ui)
	implementation(libs.androidx.lifecycle.viewmodel.navigation3)

	ksp(libs.kotlin.metadata.jvm)

	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.androidx.ui.test.junit4)
	debugImplementation(libs.androidx.ui.tooling)
	debugImplementation(libs.androidx.ui.test.manifest)
}