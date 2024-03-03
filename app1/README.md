# Example of the "request permission at runtime" API limitations

## Introduction

This Android application demonstrates how to request location permissions from users at runtime.
More importantly, it demonstrates a gaping hole in the Android "request permission at runtime" APIs.
That is, it is impossible to distinguish between the following two cases:

1. The user has never denied the permission request.
2. The user has denied the permission request twice.

## Why is it important to distinguish between these two cases?

It is important to distinguish between these two cases because the Android operating system will silently reject
your app's request for permission from the user after the user has denied the permission twice. It is necessary,
therefore, for your app to distinguish between the above two cases and, in the latter case, to show a message to
the user asking them to enable the permission via the Settings app.

## Why is it impossible to distinguish between these two cases?

These two cases are indistinguishable because in both cases the [shouldShowRequestPermissionRationale(permission:)][1]
method returns `false` and the [checkSelfPermission(permission:)][2] method returns [PackageManager.PERMISSION_DENIED][3].

## Show me the code!

All the logic of interest in this app is in the [MainActivity][4] class.

## Demo

Here is an example of the app running on an Android 12 device:

![Demo of application][5]

## Further reading

See [app2][6] for a workaround to this problem.

[1]: https://developer.android.com/reference/android/app/Activity#shouldShowRequestPermissionRationale(java.lang.String)
[2]: https://developer.android.com/reference/android/content/ContextWrapper#checkSelfPermission(java.lang.String)
[3]: https://developer.android.com/reference/android/content/pm/PackageManager#PERMISSION_DENIED
[4]: src/main/java/com/tazkiyatech/app/MainActivity.kt
[5]: demo.gif
[6]: ../app2
