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
saving is done on client side, use this program at your own risk.

A local desktop JavaFX application to display the backup codes that a user has for each social media platform they use. 

## Features
* The ability to create multiple accounts, which you can create and log in to. This allows for multiple users to share this application if they share the same computer.
    * Note that the account information and any data related to it (such as social media accounts, theme preference, and backup codes) are saved locally, so if you
    try to log in on a different computer, the data will not transfer. Unless you create the account again and
    add all the information again manually. The ability to import data across computers will be added in the
    distant future.
* The ability to add a social media account with the account username (of the social media account, not the user that logs in to the application) and social media type.
    * For each social media account a user adds, you can also delete it.
    * The ability to pin an account for faster access will be added in the distant future.
* The ability to search through your social media accounts if the list becomes too long.
* The ability to add backup codes / two-factor authentication codes for a specific account.
    * For each code a user adds, you can either copy it, delete it, or modify it.
* The ability to import a `.txt` file instead of having to type in each code one by one.
    * This functionality only works if your social media account is one of the following: Discord, Google, GitHub or Shopify. As they provided `.txt` files for their backup codes.
    * If you attempt to import the wrong type of `.txt` file for an account (For example, importing a GitHub file for a Google account) you'll end up with undefined results. Luckily the
    results can be deleted from your list of codes. Do take caution.

## Accessibility
This application offers three colour schemes in total, they can be changed in the
settings page and will be remembered the next time a user logs in or launches the application
if they didn't log out.

* A light mode colour scheme, which is the default.
* A high-contrast mode theme for those with sensitive eyes.
* A dark mode theme for those with sensitive eyes.

## Prerequisites
Java JDK version 21.0 or greater.
* The specific vendor this application uses is OpenJDK 
    * OpenJDK's source: <https://openjdk.org/>.
    * OpenJDK's prebuilt binaries can be found at Adoptium: <https://adoptium.net/>.

## Dependencies
* SLF4J version 2.0.9 or greater: <https://www.slf4j.org/>.
* Logback version 1.4.11 or greater: <https://logback.qos.ch/>.
* JavaFX version 21.0 or greater: <https://openjfx.io/>.
* ControlsFX version 11.1.2 or greater: <https://controlsfx.github.io/>.
* ValidatorFX version 0.4.2 or greater: <https://github.com/effad/ValidatorFX>.
* SQLite JDBC Driver version 3.43.2.2 or greater: <https://www.sqlitetutorial.net/sqlite-java/>.
* Apache Commons IO version 2.15.0 or greater: <https://commons.apache.org/proper/commons-io/>.

## Testing Dependencies
* Junit 5 version 5.10.0 or greater: <https://junit.org/junit5/>.

## Getting started
This application used Gradle as the build tool, which provided tasks to aid development. Here a few.

**Note:** When running this application as a native image
(such as an .exe for Windows) or a jar (outside the cloned repository) file, then the `database.db`
file will be created in the same directory in which the application is 
running in. So if you move the native image or jar file, make sure you also
move the `database.db` file with it. Otherwise, this application won't load 
any of the data and have to make a new empty `database.db` file. If you're running this
application inside the cloned repository and assuming you're inside the root directory
of this repository (which could be `Cypher-Enforcers`) then the `database.db` file
will appear in `./backup-code-generator/build/libs/database.db`.


To build a native image (such as an .exe for Windows) you can use this.

`./gradlew `

The file will be made here: `<directory goes here>`. 
You can them run the application as any other regular
application depending on your OS.

To launch the application after cloning this repository, you can run the application by doing the following:

`./gradlew run`

To run the tests, you can use any of these:

`./gradlew test` or `./gradlew check`

To just build the jar file (the jar won't contain the dependencies, you'll need to use module path for it to work).

`./gradlew build`

To build a jar uber or fat jar file (which is a jar of this main application as well as the dependencies, you won't need to use the module path in this case) you can use:

`./gradlew <something>`

To clean the build output (this will also remove the generated `database.db` file in the cloned repository), then use the following:

`./gradlew clean`

## Details
Entry point to application is `Launcher.java`

Dependencies are managed by Gradle.

## Acknowledgements
Special thanks to [Icons8](https://icons8.com/) for the following icons:
[app](https://icons8.com/) icon, [Google](https://icons8.com/icon/60984/google/) icon, [Discord](https://icons8.com/icon/30888/discord/) icon, [Shopify](https://icons8.com/icon/SZ0VDlOvY5zB/shopify/) icon, [GitHub](https://icons8.com/icon/62856/github/) icon, [Settings](https://icons8.com/icon/H6C79JoP90DH/settings/) icon, [Log Out](https://icons8.com/icon/26194/back-arrow/) icon, [Back Arrow](https://icons8.com/icon/O78uUJpfEyFx/log-out/) icon.

## Documentation
There are Javadocs for all
methods created in this file, 
but this section will show
how to use the application
from users perspective.

### Usage
Since this application
supports both light mode and
high contrast mode, for each
view in ths application, the
light view will be shown.

Note: This application is offline, so if a user makes an account on one computer, and tries to sign in with the same username and password on another computer, it won't transfer the data. Additionally, the username and password made in this tutorial won't be accessible to the users following this tutorial as two different computers don't have access to the same database containing the usernames, passwords, social media accounts, backup codes, and theme configurations.

When the application is launched for the first time, this is what should appear:

![1](https://user-images.githubusercontent.com/56102200/209496189-9256b55f-b70c-4702-a199-e4c0e8b57995.jpg)

For creating a new account, clicking the sign-up button will bring this pop-up:

![2](https://user-images.githubusercontent.com/56102200/209496190-f3334d99-86ae-4830-8adc-b33ec2528fa5.jpg)

Note that when creating a new account, all fields need to be filled out and the username and verified username must be the same. Same applies to the passwords. Once these requirements are filled out, the button will become clickable:

![4](https://user-images.githubusercontent.com/56102200/209496194-486d0966-41f9-4eb2-8617-c48fbf4e847c.jpg)

Clicking the sign-up button will bring the user to this page:

![8](https://user-images.githubusercontent.com/56102200/209496198-805770eb-2394-4cf8-ac0f-665dc79d6c36.jpg)

Now suppose the user wants to log into the application, from the home page view:

![1](https://user-images.githubusercontent.com/56102200/209496189-9256b55f-b70c-4702-a199-e4c0e8b57995.jpg)

Clicking the sign-in button will bring this pop-up:

![5](https://user-images.githubusercontent.com/56102200/209496195-56c2e6a9-7c27-4a8c-9fc3-46dd0b0ef03d.jpg)

If the user enters a username and password that does not match, the button will not be clickable:

![6](https://user-images.githubusercontent.com/56102200/209496196-04606635-64a3-4bd9-8099-ddaeab361495.jpg)

If the username and password do match, the button will be clickable:

![7](https://user-images.githubusercontent.com/56102200/209496197-4d854ede-488c-4287-9822-67b2de61df61.jpg)

Then clicking the sign-in button will lead to this page:

![8](https://user-images.githubusercontent.com/56102200/209496198-805770eb-2394-4cf8-ac0f-665dc79d6c36.jpg)

Note that this application does prevent duplicate accounts. Suppose the user starts the application at the home page, then clicks the sign-up button. If the user then enters the same username and password that's already been used to create an account, the user will not be able to create a duplicate account:

![36](https://user-images.githubusercontent.com/56102200/209513015-5f89622c-7ba2-45d0-a2f9-a032a9eb1f19.jpg)

To add a new account, click the Add Account button, which will bring this pop up:

![9](https://user-images.githubusercontent.com/56102200/209496200-f24a33ea-fd9c-4990-94a7-0c36135ea6f3.jpg)

The icons at the top of this new window are the social media platforms that are supported for importing `.txt` files. Clicking these icons will fill out the social media platform text field. For example, if the Discord icon is clicked, then it would look something like this:

![10](https://user-images.githubusercontent.com/56102200/209496201-f43369c6-4a76-4be0-8ab2-5aea440d9f48.jpg)

Note that the Platform and User/Email must both be filled for the button to be clickable. Now if the User/Email is filled, the button should be clickable:

![11](https://user-images.githubusercontent.com/56102200/209496202-7c47abf2-4b5d-4c2f-b636-0b7e620696a1.jpg)

Once the account is created, the account should appear on the list:

![12](https://user-images.githubusercontent.com/56102200/209512983-8550a77f-0385-4c1b-b407-37928e02e5b0.jpg)

Double-clicking the account icon (in this case, it would be the Discord icon, but could be different depending on which account the user would like to access) will bring the user to this view, which shows all the backup codes for this social media account:

![13](https://user-images.githubusercontent.com/56102200/209512984-d2cde6e8-e461-473e-8fd2-6a3ba1c81ac3.jpg)

The user can add backup codes in two different ways. One way is to type it manually in the text field like so:

![14](https://user-images.githubusercontent.com/56102200/209512986-6b943aa0-7541-4dde-a7ba-412a3f2bccc7.jpg)

Then from here, the user can either click enter if the cursor is active in the text field, or click the Add Code button. Now the code will be added:

![15](https://user-images.githubusercontent.com/56102200/209512988-42e41eec-3145-4797-8c38-48b8f82a399e.jpg)

Now the user can also click the Import Codes button, which will open the file explorer. From here, the user has to search for the `.txt` file containing the backup codes (in this case, for Discord) then double-click the corresponding `.txt` file, or click the Open button at the bottom right of the new file explorer window which pops up once the `.txt` file is selected:

![16](https://user-images.githubusercontent.com/56102200/209512989-97351c63-5b98-4a6a-a56e-95841d5c166f.jpg)

The user can also edit a specific code, by first double-clicking the code itself which contains the numbers, digits or symbols.

![17](https://user-images.githubusercontent.com/56102200/209512991-719272cb-90d3-4b61-8faa-1290ab7d56c9.jpg)

Then click the edit button:

![18](https://user-images.githubusercontent.com/56102200/209512992-a2d02e5b-78b7-401d-aea7-3056672f7675.jpg)

Now the user can make any changes to the code:

![19](https://user-images.githubusercontent.com/56102200/209512993-26ea520f-6db3-470d-8441-904a640073ba.jpg)

Once the changes are done, the user can click enter so the changes can be saved:

![20](https://user-images.githubusercontent.com/56102200/209512994-1d8bb44f-fe44-4185-ac85-bac4c85c9274.jpg)

The user can also copy a specific code, by first double-clicking the code itself which contains the numbers, digits or symbols.

![17](https://user-images.githubusercontent.com/56102200/209512991-719272cb-90d3-4b61-8faa-1290ab7d56c9.jpg)

Then clicking the copy button will copy the code to the user's clipboard.

The user can also delete a specific code, by first double-clicking the code itself which contains the numbers, digits or symbols.

![21](https://user-images.githubusercontent.com/56102200/209512995-927c4eaa-1e7f-4c6c-8bba-cdec9dcab43b.jpg)

Then clicking the delete button will remove the code:

![22](https://user-images.githubusercontent.com/56102200/209512998-6546a62d-a888-482b-b7ff-c7f5f8b56c2d.jpg)

If the user would like to delete all their backup codes, the Delete All Codes button will remove them all. Simply clicking the Delete All Codes button will remove all the codes from the list:

![23](https://user-images.githubusercontent.com/56102200/209512999-6fbeef30-b065-4752-89a4-6725ed2f0ab8.jpg)

Clicking the arrow button (bottom right of the screen) will take the user back to the previous menu:

![12](https://user-images.githubusercontent.com/56102200/209512983-8550a77f-0385-4c1b-b407-37928e02e5b0.jpg)

From here, the user can also add a social media account that does not support imports for `.txt` files, in this case a Reddit account:

![24](https://user-images.githubusercontent.com/56102200/209513000-cdffd32b-6cbb-4d63-b09d-b4b50d707275.jpg)

Then the account will appear on the list. (Note that a different icon is used if the social media account is not one of Shopify, Discord, GitHub or Google):

![25](https://user-images.githubusercontent.com/56102200/209513002-c7198334-e85d-41e3-9fdc-057c49750610.jpg)

Double-clicking the account will then show the codes for that specific account (in this case, the Reddit account):

![26](https://user-images.githubusercontent.com/56102200/209513004-af219260-7e21-45df-aa47-1f6af69a6510.jpg)

Now since Reddit does not give `.txt` files for their backup codes, the user can only add codes manually, which is why the Import Codes button cannot be clicked:

![27](https://user-images.githubusercontent.com/56102200/209513005-a17a3565-814c-4909-bd54-7feb263ad8a0.jpg)

Clicking the enter button when the cursor is active in the text field or the Add Code button will then add the code to the list:

![28](https://user-images.githubusercontent.com/56102200/209513007-a3ce79e2-c42f-4968-ae8d-8dbad0e084ed.jpg)

The user can then edit, copy or delete a specific code just like the example with the Discord account above. The user can also delete all codes, just as shown with the example with the Discord account above.

The user can also delete a specific social media account  by first selecting it, then clicking the Delete Accounts button:

![30](https://user-images.githubusercontent.com/56102200/209513009-bf96434c-d01b-49f6-b6f7-94f366fc6d73.jpg)

Now the account is removed from the list:

![31](https://user-images.githubusercontent.com/56102200/209513010-d34879cf-29ea-4a50-ab5b-5529f2a80b86.jpg)

Suppose the user has multiple accounts and would like to delete them all:

![32](https://user-images.githubusercontent.com/56102200/209513011-961bc81f-2778-4a80-a610-3e4d877fb954.jpg)

The user can select all accounts by first clicking the Select All Accounts button which should select all the accounts:

![33](https://user-images.githubusercontent.com/56102200/209513012-59e2002f-2173-4b39-a18e-1f257dec0378.jpg)

Then the Delete All Accounts button will remove all accounts from the list:

![34](https://user-images.githubusercontent.com/56102200/209513013-5f4bf2ca-b5e7-458d-b342-b6676854bc3f.jpg)

Clicking the gear icon will open the settings page. The theme of the application can be changed from here, as well as all Accounts and backup codes can be deleted. 

![35](https://user-images.githubusercontent.com/56102200/209513014-c1124355-8035-4e11-91e6-40dfae9245c7.jpg)

Now, suppose the user is in this view:

![8](https://user-images.githubusercontent.com/56102200/209496198-805770eb-2394-4cf8-ac0f-665dc79d6c36.jpg)

Then the user goes into the settings page:

![35](https://user-images.githubusercontent.com/56102200/209513014-c1124355-8035-4e11-91e6-40dfae9245c7.jpg)

Clicking the arrow button (bottom center) wll then take the user back to this page:

![8](https://user-images.githubusercontent.com/56102200/209496198-805770eb-2394-4cf8-ac0f-665dc79d6c36.jpg)

Now suppose the user is viewing the codes for a specific social media account, then goes into the settings page. Now if the user clicks the back arrow button again, the user will be brought to the list of codes that were being shown, rather than the list of social media accounts being shown.

The user can log out of the application in two ways. One way is to click the arrow button (bottom left) when the list of social media accounts is being shown:

![8](https://user-images.githubusercontent.com/56102200/209496198-805770eb-2394-4cf8-ac0f-665dc79d6c36.jpg)

The user can also log out of the application by clicking the button with the door icon (at the bottom center). 

Either way, the social media accounts, backup codes, as well as the UI (High Contrast, Dark, or Light) are saved so the next time the user signs in, everything is preloaded.

Once the user signs out, the user will be brought to the home-page where the user will have to sign-up/sign-in again:

![1](https://user-images.githubusercontent.com/56102200/209496189-9256b55f-b70c-4702-a199-e4c0e8b57995.jpg)

Now suppose the user closes the application without logging out (for example, clicking the red x at the top right) then the user will not need to sign-in once the application is re-launched. Instead, the user will be brought to the page with the list of social media accounts. The theme the user had set before clicking the red x at the top right will also be preloaded (for example, if the user set the theme to high-contrast, the theme will remain high-contrast once the user re-launches the application).

