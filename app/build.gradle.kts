plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.fkandroidstudio"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.fkandroidstudio"
        minSdk = 29
        targetSdk = 33
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
}

dependencies {
    val roomVer = "2.5.2"
    val appcompatVer = "1.6.1"

    implementation("androidx.room:room-runtime:$roomVer")
    annotationProcessor("androidx.room:room-compiler:$roomVer")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:$appcompatVer")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.2.0")
    implementation ("androidx.fragment:fragment:1.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("com.github.mangstadt:vinnie:2.0.2")
    //implementation("com.github.alamkanak:android-week-view:1.2.6")
    implementation ("net.sf.biweekly:biweekly:0.6.5")
}