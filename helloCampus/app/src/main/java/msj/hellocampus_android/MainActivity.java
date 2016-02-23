package msj.hellocampus_android;

import android.content.SharedPreferences;
import android.location.LocationListener;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import android.content.Intent;
import android.net.Uri;
import android.content.SharedPreferences.Editor;
import android.util.Log;


import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.core.Repo;
import com.firebase.ui.auth.core.AuthProviderType;
import com.firebase.ui.auth.core.FirebaseLoginBaseActivity;
import com.firebase.ui.auth.core.FirebaseLoginError;


public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(this);

        setContentView(R.layout.activity_main);

        /*
        imageView = (ImageView) this.findViewById(R.id.profile_image);
        Button selectImg = (Button) this.findViewById(R.id.select_image_button);
        */

        // Below allows image to be clickable
        /*
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        */
        /*
        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        */
    }
}

