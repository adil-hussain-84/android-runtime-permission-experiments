# Demonstration of how to request the "post notifications" permission and how to deal with rejection

## Introduction

This Android application demonstrates (1) how to check whether the user has granted permission to post notifications
and (2) how to request permission when permission is required.

This application not only checks and requests the [POST_NOTIFICATIONS][1] runtime permission
as is required on Android 13+ devices but it goes further and checks whether the notification's channel group and channel are enabled.
If the notification's channel group and/or channel is disabled, the application directs the user to the exact place in the Settings app where they can be enabled.

## Show me the code!

Start off by looking at the code in the [MainActivity][2] class.
That's the place in the application which posts notifications and checks whether notifications can be posted.

## Demo

Here is an example of the app running on an Android 13 device:

![Demo of application][3]

[1]: https://developer.android.com/reference/android/Manifest.permission#POST_NOTIFICATIONS
[2]: src/main/java/com/tazkiyatech/app/MainActivity.kt
[3]: demo.gif
