
# OpenCV Tutorial

## Instructions

1.	This code was written in Java and uses OpenCV, using Android Studio on a Windows 10 machine.  
2.	This application runs successfully on a Nexus 4 with API 26.  
3.	It was tested on Android studio version 2020.3.1, Android SDK 12.0 (API level 30).
4.	Java version 16.0.2 and OpenCV version 3.4.10.
5.	Make sure the Front and Back camera of the emulator are configured to Webcam0.
AVD Manager -> Select Device -> Edit -> Show Advanced Settings
6.	The application consists of 3 Major parts:  
* A basic functionality which opens the front camera and processes the frames. It listens to touch events on the screen and displays the touch coordinates as well as the average color around the touched point.   
* A filters section where the front camera opens, and the user can swipe between different filters applied to the frame.   
* A face detection feature where a bounding box is drawn around every detected face in the video.

7.	You can navigate between the different sections through the drawer menu to the left
8.	In order to use our application, make sure the java, android studio, and the correct emulator versions are installed.
9.	To run the application, download it first  using the following link
10.	Launch Android Studio and navigate to File -> Open -> Directory where project folder is
11.	It may warn you that the SDK path is configured differently and will be adjusted to the one found on your device. Click OK and leave Gradle to build.
12.	Once the build is finished successfully, select the right emulator, and click the green run button beside it.
13.	The camera might not open at first since the application should be granted the permission to access the camera. To do that navigate to Settings -> Applications -> Select the application -> Permissions -> Grant access to camera.
14.	The application might warn you to install OpenCV Manager for it to run. To do that, download OpenCV 3.4.1 from the official website and extract it to the C:/ folder. Open command prompt and cd to this folder: C:\Users\UserName\AppData\Local\Android\Sdk\platform-tools
Make sure the emulator is running and execute the following command :
C:\opencv-3.4.10-android-sdk\OpenCV-android-sdk\ apk\ OpenCV_3.4.10_Manager_3.49_x86.apk

