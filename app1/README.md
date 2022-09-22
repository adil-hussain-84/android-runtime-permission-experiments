# Request location permissions example

This Android application demonstrates how to request location permissions from users at runtime.
However, more importantly, it demonstrates a gaping hole in the Android "request permission at
runtime" APIs. That is, it is impossible to distinguish the following two cases:

1. The user has never denied the permission request.
2. The user has denied the permission request twice and therefore cannot be asked for the permission
   again.

The reason why the above two cases are indistinguishable is because in both cases
the [shouldShowRequestPermissionRationale(permission:)](https://developer.android.com/reference/android/app/Activity#shouldShowRequestPermissionRationale(java.lang.String))
method returns `false` and
the [checkSelfPermission(permission:)](https://developer.android.com/reference/android/content/ContextWrapper#checkSelfPermission(java.lang.String))
method
returns [PackageManager.PERMISSION_DENIED](https://developer.android.com/reference/android/content/pm/PackageManager#PERMISSION_DENIED)
.

At this stage, you're probably thinking: Why is it important to distinguish between these two cases?
It is important because the Android OS will reject the app's attempts at requesting the permission
from the user after the user has denied the permission twice. It is necessary, therefore, for the
app to determine when the user has denied the permission twice and, in this case, to show a message
to the user asking them to enable the permission via the device's Settings app.

Here's an example of the app running on an Android 13 device:

![Demo of application](demo.gif)
