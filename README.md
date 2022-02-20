# Platform Science Interview Project

This was an interesting assignment. Most of my experience with Android development has either been in native Java or Flutter, so I challenged myself to use Kotlin. I look forward to constructive feedback on my use of Kotlin and the overall structure of the application.

If this was a normal application, the JSON data containing the drivers and assignments would be coming asynchronously from the back end. When the data is received, the application would calculate the assignments for each driver and update the UI accordingly, all without blocking the UI thread. However, the data is hardcoded and a relatively small, so we could perform the calculations without using a asynchronous method.

Where possible, the application saves previously calculated data (like street names and SS scores for shipments) to decrease the calculation time.

## Build Instructions

The easiest way to build the application is to open the root directory in Android Studio. Select "Build"->"Make Project" from the top menu to build the application.

For command line building:

Linux/Mac
```bash
./gradlew build
```
Windows:
```bash
gradlew.bat build
```
