# MSF Communiqué Android Application #

Communiqué is a three component system that is built for the MSF DR-TB team based in Khayelitsha. It is used to collect and manage information 
on DR-TB patients.

The information that can be collected and stored on the application includes:
* Patient demographic and contact information
* Patient hospital admissions and adverse events
* Patient treatent phases and outcomes
* Patient medical regimens
* Counselling sessions and medical reports
* Pilot enrollments

## Setting up your development environment ##
* Download and install [Android Studio] (https://developer.android.com/studio/install.html)
* [Fork] (https://help.github.com/articles/fork-a-repo/) the Communique project 
* Clone your fork of the project locally. At the command line:
  * git clone https://github.com/your_username/msf_communique/ If you prefer not to use the command line, you can use Android Studio to create a new project from version control using https://github.com/your_username/msf_communique/
* Open the project in the folder of your clone from Android Studio. To run the project, click on the green arrow at the top of the screen. The emulator is very slow so it is best to use a physical device when possible.


## Note ##
The server URL can be changed in the communique-androidapp\app\src\main\java\com\example\msf\msf\API\AUth file ("SERVER_URL" variable). This URL determines where your data will be hosted.  
