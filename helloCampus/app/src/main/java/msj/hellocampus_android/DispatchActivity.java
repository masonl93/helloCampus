package msj.hellocampus_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.client.Firebase;

/**
 * Created by Mason on 2/22/16.
 */
public class DispatchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // connecting to our database
        Firebase.setAndroidContext(this);
        Firebase mUsersRef = new Firebase(getResources().getString(R.string.firebase_url));



        //check if user is authenticated
        if (mUsersRef.getAuth() != null)
            startActivity(new Intent(this, MainActivity.class));
        //ask user to login or sign up
        else
            startActivity(new Intent(this, WelcomeActivity.class));
    }
}
