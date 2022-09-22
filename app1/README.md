# Request location permissions example

This Android application demonstrates how to request location permissions from users at runtime.
However, more importantly, it demonstrates a gaping hole in the Android "request permission at
runtime" APIs. That is, it is impossible to distinguish between the following two cases:

1. The user has never denied the permission request.
2. The user has denied the permission request twice.

The reason why the above two cases are indistinguishable is because in both cases
the [shouldShowRequestPermissionRationale(permission:)](https://developer.android.com/reference/android/app/Activity#shouldShowRequestPermissionRationale(java.lang.String))
method returns `false` and
the [checkSelfPermission(permission:)](https://developer.android.com/reference/android/content/ContextWrapper#checkSelfPermission(java.lang.String))
method
returns [PackageManager.PERMISSION_DENIED](https://developer.android.com/reference/android/content/pm/PackageManager#PERMISSION_DENIED)
.

At this stage, you're probably thinking: Why is it important to distinguish between these two cases?
It is important because the Android operating system will reject the app's attempts at requesting
permission from the user after the user has denied the permission twice. It is necessary, therefore,
for the app to distinguish between the above two cases and, in the latter case, to show a message to
the user asking them to enable the permission via the device's Settings app.

Here's an example of the app running on an Android 13 device:

![Demo of application](demo.gif)
