# TourTrek

[![Build Status](https://github.com/JohnBednarczyk/TourTrek/workflows/android-master/badge.svg)](https://github.com/JohnBednarczyk/TourTrek/actions) [![Releases](https://img.shields.io/github/v/release/JohnBednarczyk/TourTrek)](https://github.com/JohnBednarczyk/TourTrek/releases/latest) [![Slack](https://img.shields.io/badge/slack-join-e01563.svg)](https://f20-cs506.slack.com/archives/G01A7TE27TR)


TourTrek is an android application developed for those who love to explore new places. In this application, a user can create their own personal account where they can keep track of excursions they decide to embark on. Users can create a Tour, which includes a location, date, and name. Tours consist of attractions a user would like to attend, which can range anywhere from restaurants to amusement parks. Each attraction can store its name, location, cost, and date to keep track of what time/date you want to attend each attraction with reminders included. TourTrek also features a public tour market where you can explore tours that other users have completed and embark on these excursions yourself.

[<img src=".github/assets/google-play-badge.png"
      alt="Get it on Google Play"
      height="80">](https://play.google.com/store)
[<img src="https://yt3dl.net/images/apk-download-badge.png"
      alt="Direct apk download"
      height="80">](https://github.com/JohnBednarczyk/TourTrek/releases)


## Configuring Development Environment

### Requirements

- [Android Studio 4.1](https://developer.android.com/studio/index.html)
- [Git](https://git-scm.com/downloads)

### Setup

1. Make sure all the required software mentioned above are installed

2. Open Project in Android Studio (Windows):
    * Download latest release from here: https://github.com/JohnBednarczyk/TourTrek/releases
    * Unzip file to target destination
    * Open Android Studio and click "Open an Existing Project"
    * Select Directory of un-zipped file from above
    * Wait for project to load and build (Once you see all loading bars disappear from the bottom)
      
3. Setup Android Emulator:
    * Select Tools --> AVD Manager:
    * Select + Create Virtual Device... (Bottom Left)
    * Select Pixel 2 --> Next
    * Download <Pie, 28, x86, Android 9.0 (Google Play)>
    * Select <Pie, 28, x86, Android 9.0 (Google Play)> --> Next
    * Keep all defaults and select Finish
    * Exit AVD Manager
    
4. Build and Sync Gradle:
    * Select Build --> Make Project
    * Wait for project status to display “gradle build finished” (located in the bottom left corner of Android Studio)
    * Select File --> Sync Project with Gradle Files
    * Wait for project status to display “Gradle Sync Finished”

5. Run Application:
    * Select Run --> Run...
    * Select app from popup screen
    * App should begin running in the emulator
     
## Testing
* Run All Instrumentation Unit Tests:
    * From the Project Directory tab open app/java/
    * Right click on com.tourtrek(androidTest) to run all Instrumentation Unit Tests
    * Click Run 'Tests in 'com.tourtr...'
    * Click Run
    
* Run An Instrumentation Unit Test Class:
    * From the Project Directory tab open app/java/com.tourtrek(androidTest)
    * Right click on a specific test class
    * Click Run '<Name of class>'
    
* Run A Single Instrumentation Unit Test:
    * From the Project Directory tab open app/java/com.tourtrek(androidTest)
    * Open the test class file of the test you want to run
    * Click the play symbol next to the individual test you want to run

* Run A Code Coverage Test:
    * Open the Gradle sidebar navigator view
    * Navigate to TourTrek --> Tasks --> verification
    * Double click "createDebugCoverageReport"
    * Wait for project status to display "Gradle build finished"
    * In the project directory, navigate to app --> build --> reports --> coverage --> debug
    * Open index.html with a web browser to view full code coverage report

## FAQ

<details>
  <summary>I can't see the AVD Manager in Tools --> AVD Manager</summary>
  <p>
   This is because when you unzipped the release zip file, you added an additional folder on top of the app folder. When you import the project, it should show the android icon next to the folder like so:

<img src=".github/assets/android_app_folder_icon.PNG"
      alt="Android App Folder Icon"
      height="30">

By selecting a folder with this icon, you will correctly import the project

Also keep in mind that once you import a project, regardless of whether or not it was successful, android studio will convert it into an android project and give that folder the icon above, giving you a false positive
      </p>
</details>

<details>
  <summary>Gradle Build never completes</summary>
  <p>Restart Android Studio</p>
</details>

<details>
  <summary>I can't see createDebugCoverageReport in my Gradle sidebar navigator view</summary>
  <p>You need to perform a gradle sync by selecting File --> Sync Project with Gradle Files</p>
</details>

<details>
  <summary>The app in the emulator keeps crashing unexpectidly with no errors in the log</summary>
  <p>This is most likely the cause of not having up to date libraries downloaded on your machine. You'll want to: </p>
      <p>
      <ol>
            <li> Select File --> Sync Project with Gradle Files </li>
            <li> Wait for project status to display “Gradle Sync Finished” </li>
      </ol>
      </p>
</details>

<details>
  <summary>Running a code coverage test fails</summary>
  <p>This could be because you're emulator is stalling or not able to keep up with the tests that are being conducted. If the exception that is being thrown says something along the lines of "could not find view", then this is most likely the issue.</p>
</details>

<details>
  <summary>Adding an image to a tour or attraction gets distorted and cut off</summary>
  <p>This is actually a bug with the 3rd party library that we use (Glide). Some images work and others do not. Please try another image</p>
</details>


<details>
  <summary>Attempting to post a tour to Facebook tells me I need to be added as a developer</summary>
  <p>While the app is in a development state, users will need to be added as developers to our facebook group. Please reach out to us in order to be added to this group</p>
</details>

## Screenshots

| Login | Register | Tour Market |
|:-:|:-:|:-:|
| <img src=".github/assets/screenshots/Login-Screen.png" alt="Login" height="500"> | <img src=".github/assets/screenshots/Registration-Screen.png" alt="Registration" height="500"> | <img src=".github/assets/screenshots/Tour-Market-Screen.png" alt="Tour Market" height="500"> |

| Personal Tours | Profile | Tour Top |
|:-:|:-:|:-:|
| <img src=".github/assets/screenshots/Personal-Tours-Screen.png" alt="Personal Tours" height="500"> | <img src=".github/assets/screenshots/Profile-Screen.png" alt="Profile" height="500"> | <img src=".github/assets/screenshots/Tour-Screen-1.png" alt="Tour Top" height="500"> |

| Tour Bottom | Attraction Top | Attraction Bottom |
|:-:|:-:|:-:|
| <img src=".github/assets/screenshots/Tour-Screen-2.png" alt="Tour Bottom" height="500"> | <img src=".github/assets/screenshots/Attraction-Screen-1.png" alt="Attraction Top" height="500"> | <img src=".github/assets/screenshots/Attraction-Screen-2.png" alt="Attraction Bottom" height="500"> |

| Friends | Settings |
|:-:|:-:|
| <img src=".github/assets/screenshots/Friends-Screen.png" alt="Friends" height="500"> | <img src=".github/assets/screenshots/Settings-Screen.png" alt="Settings" height="500"> |
