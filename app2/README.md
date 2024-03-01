# Workaround for the "request permission at runtime" API limitations

## Introduction

This Android application demonstrates a workaround for the "request permission at runtime" API limitations demonstrated in [app1][1].

Specifically, this application remembers in persistent storage when the user has seen and
acknowledged the "request permission rationale" popup. The app uses this piece of information as a
heuristic to determine when it should direct the user to the Settings screen instead of requesting
permission from the user once more.

## Show me the code!

All the logic of interest in this app is in the [MainActivity][2] class.

## Demo

Here is an example of the app running on an Android 12 device:

![Demo of application][3]

## A word of caution

Take care to clear the "user has acknowledged the request permission rationale" boolean in the scenario
where the user grants the permission to the app after seeing the "request permission rationale" popup.
If you don't do this, you'll end up in a scenario – when the app is killed and restarted – where you're directing
the user to the Settings screen whilst you are in fact entitled to request permission from the user once more.

[1]: ../app1
[2]: src/main/java/com/tazkiyatech/app/MainActivity.kt
[3]: demo.gif
