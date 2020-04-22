# Simple activities for Pepper Android SDK

## How to run

1. Create an Empty Application with Android Studio

        Language        Java
        Minimum SDK     API 23: Android 6.0


2. Create a New Robot Application for the new Empty Application

        File -> New -> Robot Application 
        
        Minimum SDK:    API 6
        Apply to:       Module: 'app'

        File -> Project Structure

        Modules

            Source compatibility    1.8
            Target compatibility    1.8


3. Replace the content of `MainActivity.java` with the content of any of the files in this folder after the `package` statement. You must leave the `package` statement of the current `MainActivity.java` file and replace all the rest.


4. Set Pepper tablet

    Select `activity_main.xml` tab, select the device `Pepper 1.9` under 'Generic Phones and Tablets', select `Landscape` mode


5. Build the project

        Build -> Make project

    or `CTRL-F9` or click on the build icon (green hammer).


6. Run the emulator

        Tools -> Pepper SDK -> Emulator

    or click on the Emulator button in the toolbar.


7. Run the project


        Run -> Run 'app'

    or `Shift+F10` or click on the run icon (green triangle).


8. Quit the project

        Run -> Stop 'app'

    or `CTRL+F2` or click on the stop icon (red square).

