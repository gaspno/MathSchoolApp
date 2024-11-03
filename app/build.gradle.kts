plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
android {
    namespace = "com.example.mathschool"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mathschool"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildToolsVersion = "34.0.0"
}

dependencies {

    implementation("com.google.firebase:firebase-auth:23.1.0")
    implementation(platform("com.google.firebase:firebase-bom:33.5.0"))    // Also add the dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-appcheck-debug")
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("androidx.activity:activity:1.9.3")
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-appcheck-playintegrity")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}