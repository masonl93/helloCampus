package msj.hellocampus_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

/**
 * Created by Mason on 2/22/16.
 */
public class SignUpActivity extends Activity {

    Button sign_up_btn;
    EditText emailET, usernameET, passwordET;
    Firebase mUsersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        emailET = (EditText) findViewById(R.id.etEmail);
        usernameET = (EditText) findViewById(R.id.etUserName);
        passwordET = (EditText) findViewById(R.id.etPass);

        sign_up_btn = (Button) findViewById(R.id.btnSignUp);
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signup();
            }
        });

    }

    private void signup() {
        final String email = emailET.getText().toString().trim();
        final String username = usernameET.getText().toString().trim();
        final String password = passwordET.getText().toString().trim();

        setup_firebase();

        mUsersRef.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                System.out.println("Successfully created user account with uid: " + result.get("uid"));
                log_user_in(email, password);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                System.out.println(firebaseError);
            }
        });

        startActivity(new Intent(this, MainActivity.class));
    }

    private void setup_firebase() {
        Firebase.setAndroidContext(this);
        mUsersRef = new Firebase(getResources().getString(R.string.firebase_url));
    }

    private void log_user_in(String email, String password) {
        mUsersRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
            }
            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                System.out.println(firebaseError);
            }
        });
    }
}
