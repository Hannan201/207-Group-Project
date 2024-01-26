# Backup Code Generator
> **Disclaimer:** The *University Of Toronto*, *University Of Toronto Mississauga*,
*University Of Toronto Scarborough*, any person or thing related to the
*University Of Toronto*, or any contributor for this project is not responsible
for the loss of your backup codes or account data. Since the data is saved 
locally on the machine this program runs on and not on a server, it's possible
for the data to be lost if the computer is wiped clean, or if any other user 
on your machine modifies the source code and rebuilds the project for a
dangerous attack (kind of like using inspect element on a web browser to trick someone
else into giving away import details). Since all authentication and data
saving are done on client side, use this program at your own risk.

A local desktop JavaFX application to display the backup codes / two-factor 
authentication codes that a user has for each social media platform they use. 

## Acknowledgements
Special thanks to [Icons8](https://icons8.com/) for the following icons:
[app](https://icons8.com/) icon, [Google](https://icons8.com/icon/60984/google/) icon, [Discord](https://icons8.com/icon/30888/discord/) icon, [Shopify](https://icons8.com/icon/SZ0VDlOvY5zB/shopify/) icon,
[GitHub](https://icons8.com/icon/62856/github/) icon, [Settings](https://icons8.com/icon/H6C79JoP90DH/settings/) icon, [Log Out](https://icons8.com/icon/26194/back-arrow/) icon, [Back Arrow](https://icons8.com/icon/O78uUJpfEyFx/log-out/) icon.

Special credits to @Hansunkekes and @Kedi24 for designing the UIs of
this application.

## Features
* The ability to create multiple accounts, which you can create and log in to. This 
  allows for multiple users to share this application if they share the same computer.
    * Note that the account information and any data related to it (such as social media
    accounts, theme preference, and backup codes) are saved locally.
    If you try to log in on a different computer, the data will not transfer.
    Unless you create the account again on the new computer, add all the 
    information again manually. The ability to import data across computers will be added
    in the distant future.
* The ability to add a social media account with the account username and social media type.
    * For each social media account a user adds, you can also delete it.
    * The ability to pin an account for faster access will be added in the distant future.
* The ability to search through your social media accounts if the list becomes too long.
* The ability to add backup codes / two-factor authentication codes for a specific account.
    * For each code a user adds, you can either copy it, delete it, or modify it.
* The ability to import a `.txt` file instead of having to type in each code one by one.
    * This functionality only works if the type of your social media account is one of the following: 
    **Discord**, **Google**, **GitHub** or **Shopify**. As they provided `.txt` files for their backup codes.
    If you attempt to import the wrong type of `.txt` file for an account (For example, 
    importing a GitHub file for a Google account) you'll end up with undefined results.
    Luckily, the results can be deleted from your list of codes. Do take caution.

## Accessibility
This application offers three colour schemes in total, they can be changed in the
settings page and will be remembered the next time a user logs in or launches the application
if they didn't log out.

* A **light mode** theme, which is the default.
* A **high-contrast mode** theme for those with sensitive eyes.
* A **dark mode** theme for those who prefer a darker colour scheme.

## Prerequisites
Java OpenJDK version 21.0.2.
* The specific vendor this application uses for OpenJDK is Adoptium.
    * Adoptium OpenJDK's 21.0.2 source: <https://github.com/adoptium/jdk21u>.
    * Adoptium OpenJDK's prebuilt binaries can be found at: <https://adoptium.net/temurin/releases/>.
    * Gradle will automatically download the required OpenJDK with the correct vendor 
    automatically if you don't already have it. If you don't have it, Gradle will download it
    at: `<home directory of the current user>/.gradle/jdks/`. There should be a folder
    which contains `eclipse_adoptium-21`, the ending of the folder name might vary depending on your OS 
    and computer architecture. 

## Details
Entry point to application is `cypher.enforcers.Launcher`.

Main module of this application is `backup.code.generator`.

Gradle manages dependencies.
Thanks to the Gradle wrapper, you don't need to install
it to run this application.

## About Gradle

Below, you'll find examples of Gradle commands. There are two things to note:
* The commands will assume you're in the root directory of this cloned repository
  (which should be `Cypher-Enforcers`).
* If you're on a Unix-based Operating System or using a Unix-like terminal (such
  as Git Bash on Windows), you'll need to start the Gradle commands with `./gradlew`,
  and if you're on Windows, you'll need to start the Gradle command with `.\gradlew`.

## Dependencies
* SLF4J version 2.0.9 or greater: <https://www.slf4j.org/>.
* Logback version 1.4.11 or greater: <https://logback.qos.ch/>.
* JavaFX version 21.0 or greater: <https://openjfx.io/>.
* ControlsFX version 11.2.0 or greater: <https://controlsfx.github.io/>.
* ValidatorFX version 0.4.2 or greater: <https://github.com/effad/ValidatorFX>.
* SQLite JDBC Driver version 3.44.0.0 or greater: <https://www.sqlitetutorial.net/sqlite-java/>.
* Apache Commons IO version 2.15.0 or greater: <https://commons.apache.org/proper/commons-io/>.

## Testing Dependencies
* Junit-5 version 5.10.1 or greater: <https://junit.org/junit5/>.

## Installation
Check out the [release page](https://github.com/Hannan201/Cypher-Enforcers/releases/tag/v2.0.2)
for installers depending on your Operating System
as well as other assets / binaries.

## Getting started
This application uses Gradle as the build tool, which provided tasks to aid 
development.
Here are a few of them you can execute from the terminal.

### For IntelliJ users

For those using IntelliJ IDEA, be sure to execute:

```
./gradlew idea
```

To ease development with IntelliJ IDEA.

### Launching the application

To launch the application after cloning this repository, you can run the 
application by doing the following:

```
./gradlew run
```

There will also be a `database.db` file which will appear at 
`./backup-code-generator/database.db`.
This file is used to save and load data.
So take caution when deleting it.

### Building From source

#### Native Image

To build a native image (such as an .exe for Windows), you can use this:

```
./gradlew jpackageImage
```

**Note:** To launch the native image, you don't need to have the JDK installed,
it comes with a bundled JDK that takes less space than an uber jar or fat jar.

The image file will be made here: `./backup-code-generator/build/jpackage/Backup Code Generator/`. 
You can then run the application as any other regular application depending on your OS.

**Note:** In the `./backup-code-generator/build/jpackage/Backup Code Generator` folder
there should be two directories and one file present:

```
app
runtime
Backup Code Generator.exe
```

Once the `.exe` file is launched, there will also be a `database.db` file created in the same folder.
If you decide to move the `.exe` file to another directory, you'll also need to move
the `app` and `runtime` directories along with the `database.db` file, they all need to be
inside the same folder as the `.exe` file.
Otherwise, the correct data for the user might
not load since a new `database.db` file will be created or the application won't start since
it depends on the `app` and `runtime` folders which won't be created.

#### Jar
To just build the jar file, use this:

```
./gradlew jar
```

The jar file will appear at: `./backup-code-generator/build/libs/backup-code-generator-2.0.2.jar`.

To build an uber jar or fat jar file (which includes the main application as well as the
dependencies), you can use:

```
./gradlew uberJar
```

The jar file will appear at: `./backup-code-generator/build/uberJars/backup-code-genarator-2.0.2-uber.jar`.

**Note:** Unlike the native image, to run this `jar` file, you'll need to have the correct version
of the JDK installed since it's not bundled in the `jar` file.
This `jar` file also takes up more space compared to the native image.

Once the `.jar` file is launched, there will also be a `database.db` file created
in the same folder.
If you decide to move the `.jar` file to another directory,
you'll also need to move the `database.db` file, it needs to be inside the same
folder as the `.jar` file.
Otherwise, the correct data for the user might not 
load since a new `database.db` file will be created.

### Running tests

To run the tests, you can use any of these:

`./gradlew test` or `./gradlew check`

**Note:** Some tests copy a file, modify it and read it.
So they need to be deleted afterwords for the next time the tests are run to return to
their original state.
With Gradle, there's a task to do this (you can run the task
by using `./gradlew cleanUpTestFiles`).
Gradle will call this task automatically when running tests by
`./gradlew test` or `./graldew check` so you won't have to.
If you're using Gradle to run the tests, everything should work.
Otherwise, you'll need to delete the generated files from the tests manually 
to ensure they work.

### Clean up

To clean the build output then use the following:

```
./gradlew clean
```

## Documentation
There are JavaDocs for all
methods created in this project.
You can download the JavaDoc jar from
the [release page](https://github.com/Hannan201/Cypher-Enforcers/releases/tag/v2.0.2).

## Usage
Check out the [wiki](https://github.com/Hannan201/Cypher-Enforcers/wiki) on how to use our application.