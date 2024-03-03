# Workaround for the "request permission at runtime" API limitations

## Introduction

This Android application demonstrates a workaround for the "request permission at runtime" API limitations demonstrated in [app1][1].

Specifically, this application remembers in persistent storage when the user has seen and acknowledged the "request permission rationale" popup.
The app uses this piece of information as a heuristic to determine when it should direct the user to the Settings app
instead of requesting permission from the user once more.

## Show me the code!

All the logic of interest in this app is in the [MainActivity][2] class.

## Demo

Here is an example of the app running on an Android 12 device:

![Demo of application][3]

## A word of caution

Take care to clear the custom "user has acknowledged the request permission rationale" boolean in the scenario
where the user grants the permission to the app after seeing the "request permission rationale" popup.
If you don't do this, you'll end up in a scenario – when the app is killed and restarted – where you're directing
the user to the Settings app whilst you are in fact entitled to request permission from the user once more.

## Limitation of this workaround

Here's one limitation of this workaround to be aware of:
If the user selects the "Ask every time?" option for the permission in the Settings app
when they're directed to the Settings app by your app, your app will then be in a state where:

1. [checkSelfPermission(permission:)][4] returns [PERMISSION_DENIED][5];
2. [shouldShowRequestPermissionRationale(permission:)][6] returns `false`; and
3. your custom "user has has acknowledged the request permission rationale" value is `true`.

In this state, your app will direct the user to the Settings app the next time it needs the permission
even though it is entitled to request the permission from the user directly.

You need to weigh up whether you prefer this limitation or the limitation outlined in [app][1].

[1]: ../app1
[2]: src/main/java/com/tazkiyatech/app/MainActivity.kt
[3]: demo.gif
[4]: https://developer.android.com/reference/android/content/Context#checkSelfPermission(java.lang.String)
[5]: https://developer.android.com/reference/android/content/pm/PackageManager#PERMISSION_DENIED
[6]: https://developer.android.com/reference/android/app/Activity#shouldShowRequestPermissionRationale(java.lang.String)
