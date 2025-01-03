<h2 align="center">
    <img src="fastlane/metadata/android/en-US/images/icon.png" alt="icon" width="90"/>
    <br />
    <b>NotallyX | Minimalistic note taking app</b>
    <p>
        <center>
            <a href="https://f-droid.org/en/packages/com.philkes.notallyx"><img alt='IzzyOnDroid' height='80' src='https://fdroid.gitlab.io/artwork/badge/get-it-on.png' /></a>
            <a href="https://apt.izzysoft.de/fdroid/index/apk/com.philkes.notallyx"><img alt='F-Droid' height='80' src='https://gitlab.com/IzzyOnDroid/repo/-/raw/master/assets/IzzyOnDroid.png' /></a>
        </center>
    </p>
</h2>

<div style="display: flex; justify-content: space-between; width: 100%;">
  <img src="fastlane/metadata/android/en-US/images/phoneScreenshots/1.png" alt="Image 6" style="width: 32%;"/>
  <img src="fastlane/metadata/android/en-US/images/phoneScreenshots/2.png" alt="Image 2" style="width: 32%;"/>
  <img src="fastlane/metadata/android/en-US/images/phoneScreenshots/3.png" alt="Image 3" style="width: 32%;"/>
</div>

<div style="display: flex; justify-content: space-between; width: 100%;">
  <img src="fastlane/metadata/android/en-US/images/phoneScreenshots/4.png" alt="Image 4" style="width: 32%;"/>
  <img src="fastlane/metadata/android/en-US/images/phoneScreenshots/5.png" alt="Image 5" style="width: 32%;"/>
  <img src="fastlane/metadata/android/en-US/images/phoneScreenshots/7.png" alt="Image 7" style="width: 32%;"/>
</div>

### Features
[Notally](https://github.com/OmGodse/Notally), but eXtended

* Create **rich text** notes with support for bold, italics, mono space and strike-through
* Create **task lists** and order them with subtasks
* Complement your notes with any type of file such as **pictures**, PDFs, etc.
* **Sort notes** by title, last modified date, creation date
* **Color, pin and label** your notes for quick organisation
* Add **clickable links** to notes with support for phone numbers, email addresses and web urls
* **Undo/Redo actions**
* Use **Home Screen Widget** to access important notes fast
* **Lock your notes via Biometric/PIN**
* Configurable **auto-backups**
* Create quick audio notes
* Display the notes either in a **List or Grid**
* Quickly share notes by text
* Extensive preferences to adjust views to your liking
* Actions to quickly remove checked tasks
* Adaptive android app icon
* Support for Lollipop devices and up

---

### Translations
All translations are crowd sourced. To contribute, follow these [guidelines](https://m2.material.io/design/communication/writing.html) and open a pull request.

### Bug Reports / Feature-Requests
If you find any bugs or want to propose a new Feature/Enhancement, feel free to [create a new Issue](https://github.com/PhilKes/NotallyX/issues/new/choose)

When using the app and an unknown error occurs, causing the app to crash you will see a dialog (see showcase video in https://github.com/PhilKes/NotallyX/pull/171) from which you can immediately create a bug report on Github with the crash details pre-filled.

### Contributing

If you would like to contribute code yourself, just grab any open issue (that has no other developer assigned yet), leave a comment that you want to work on it and start developing by forking this repo.

The project is a default Android project written in Kotlin, I highly recommend using Android Studio for development. Also be sure to test your changes with an Android device/emulator that uses the same Android SDK Version as defined in the `build.gradle` `targetSdk`.

Before submitting your proposed changes as a Pull-Request, make sure all tests are still working (`./gradlew test`), and run `./gradlew ktfmtFormat` for common formatting (also executed automatically as pre-commit hook).

### Attribution
The original Notally project was developed by [OmGodse](https://github.com/OmGodse) under the [GPL 3.0 License](https://github.com/OmGodse/Notally/blob/master/LICENSE.md).

In accordance to GPL 3.0, this project is licensed under the same [GPL 3.0 License](https://github.com/PhilKes/NotallyX/blob/master/LICENSE.md).
