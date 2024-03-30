import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")

}

android {
    namespace = "com.loverian.weatherapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.loverian.weatherapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String","BASE_URL", "${getBaseUrl()}")
        buildConfigField("String","APP_ID", "${getAppId()}")
        buildConfigField("String","MAP_KEY",getMapKey() )
        buildConfigField("String","PLACES_API_KEY",getPlacesKey() )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        dataBinding = true
    }
}
secrets {
    // Optionally specify a different file name containing your secrets.
    // The plugin defaults to "local.properties"
    propertiesFileName = "secrets.properties"

    // A properties file containing default secret values. This file can be
    // checked in version control.
    defaultPropertiesFileName = "local.properties"

}
dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("com.github.ihsanbal:LoggingInterceptor:3.0.0") {
        exclude(group = "org.json", module = "json")
    }

    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.google.android.libraries.places:places:3.4.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    implementation("com.adevinta.android:leku:11.1.4")

    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))
    implementation("com.google.android.libraries.places:places:3.4.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}


fun getBaseUrl(): String {
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())

    return properties.getProperty("base_url")
        ?: throw GradleException("Add 'base_url' field at local.properties file")
}

fun getAppId(): String {
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())

    return properties.getProperty("app_id")
        ?: throw GradleException("Add 'app_id' field at local.properties file")
}

fun getMapKey(): String {
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())

    return properties.getProperty("map_api")
        ?: throw GradleException("Add 'map_key' field at local.properties file")
}

fun getPlacesKey(): String {
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())

    return properties.getProperty("PLACES_API_KEY")
        ?: throw GradleException("Add 'places_key' field at local.properties file")
}