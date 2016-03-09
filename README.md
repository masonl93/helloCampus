# helloCampus
The geo-exploration platform that ties the virtual world to the physical

Firebase url: in res/values/keys = https://crackling-torch-8127.firebaseio.com

Currently the app brings you to the dispatch activity which then sends you either to the main activity (just a picture of a wave currently, if you are already logged in) or the welcome activity (where you either login activity or you sign up activity). The login is done by FirebaseUI at https://github.com/firebase/FirebaseUI-Android. The sign up is a custom layout. You can view the users by going to https://crackling-torch-8127.firebaseio.com/?page=Auth and scrolling down to the bottom where Users are listed. Once you sign up or log in, you must delete the entire app and redownload it from android studio since there is no log out feature yet.


Setup:
Move debug.keystore to ~/.android/
