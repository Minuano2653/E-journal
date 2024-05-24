buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
        classpath("androidx.navigation.safeargs:androidx.navigation.safeargs.gradle.plugin:2.7.7")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.3" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
}