
-keepattributes SourceFile,LineNumberTable

-keep class com.facebook.** {
   *;
}
-keep public class com.google.android.gms.ads.**{
   public *;
}
-keep public class com.google.ads.**{
   public *;
}

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-keep class com.google.appengine.** { *; }
-keepattributes *Annotation*
-keepattributes Signature
-dontwarn
-ignorewarnings

-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**
-dontwarn org.xmlpull.v1.**
-dontnote org.xmlpull.v1.**
-keep class org.xmlpull.** { *; }
-keepclassmembers class org.xmlpull.** { *; }

    # Add this global rule
    -keepattributes Signature
    -keepclassmembers class com.test.project.samplelocation.** {
      *;
    }
