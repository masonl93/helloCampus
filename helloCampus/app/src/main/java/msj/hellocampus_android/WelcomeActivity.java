package msj.hellocampus_android;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.ui.auth.core.AuthProviderType;
import com.firebase.ui.auth.core.FirebaseLoginBaseActivity;
import com.firebase.ui.auth.core.FirebaseLoginError;

/**
 * Created by Mason on 2/22/16.
 */
public class WelcomeActivity extends FirebaseLoginBaseActivity {

    private Button login_btn;
    private Button sign_up_btn;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Permissions are chill.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        // Sign in button click handler
        login_btn = (Button) findViewById(R.id.btnSignIn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Starts an intent of the log in activity
                //startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                showFirebaseLoginPrompt();
            }
        });

        // Sign up button click handler
        sign_up_btn = (Button) findViewById(R.id.btnSignUp);
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Starts an intent for the sign up activity
                startActivity(new Intent(WelcomeActivity.this, SignUpActivity.class));
            }
        });
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
        // Handle successful login
        startActivity(new Intent(this, MainActivity.class));
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
