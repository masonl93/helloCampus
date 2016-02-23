package msj.hellocampus_android;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.ui.auth.core.AuthProviderType;
import com.firebase.ui.auth.core.FirebaseLoginBaseActivity;
import com.firebase.ui.auth.core.FirebaseLoginError;

/**
 * Created by Mason on 2/22/16.
 */
public class LoginActivity extends FirebaseLoginBaseActivity {

    Firebase mUsersRef;


    @Override
    protected void onStart() {
        super.onStart();
        // All providers are optional! Remove any you don't want.
        //setEnabledAuthProvider(AuthProviderType.FACEBOOK);
        //setEnabledAuthProvider(AuthProviderType.TWITTER);
        //setEnabledAuthProvider(AuthProviderType.GOOGLE);
        setEnabledAuthProvider(AuthProviderType.PASSWORD);
    }

    @Override
    public Firebase getFirebaseRef() {
        // Return your Firebase ref
        setup_firebase();
        return mUsersRef;
    }

    @Override
    public void onFirebaseLoginProviderError(FirebaseLoginError firebaseError) {
        // TODO: Handle an error from the authentication provider
    }

    @Override
    public void onFirebaseLoginUserError(FirebaseLoginError firebaseError) {
        // TODO: Handle an error from the user
    }

    @Override
    public void onFirebaseLoggedIn(AuthData authData) {
        // TODO: Handle successful login
    }


    @Override
    public void onFirebaseLoggedOut() {
        // TODO: Handle logout
    }

    private void setup_firebase() {
        Firebase.setAndroidContext(this);
        mUsersRef = new Firebase(getResources().getString(R.string.firebase_url));
    }
}
