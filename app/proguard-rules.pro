# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/dp/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and change the file name in build.gradle.kts.

# Keep Jetpack Compose structures intact for R8 optimization
-keepclassmembers class * extends androidx.activity.ComponentActivity {
    public <init>();
}
