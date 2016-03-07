package msj.hellocampus_android;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;



public class MainActivity extends FragmentActivity {

    Location lastKnownLocation;
    String locationProvider;
    private Node temp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationProvider = LocationManager.NETWORK_PROVIDER;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    2);
        }

        lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

        if (lastKnownLocation == null) {
            locationProvider = LocationManager.GPS_PROVIDER;
            lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        }
        double lat = lastKnownLocation.getLatitude();
        double lng = lastKnownLocation.getLongitude();
        System.out.println("LATITUDE " + lat);
        System.out.println("LONGITUDE " + lng);
        */


        Firebase.setAndroidContext(this);
        final Firebase mPostsRef = new Firebase(getResources().getString(R.string.firebase_url)).child("nodes");

        setContentView(R.layout.activity_main);

        final EditText editText = (EditText) findViewById(R.id.search);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    final String input = editText.getText().toString();

                    mPostsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            System.out.println("There are " + snapshot.getChildrenCount() + " nodes");
                            for (DataSnapshot nodeSnapshot : snapshot.getChildren()) {
                                Node node = nodeSnapshot.getValue(Node.class);
                                System.out.println(node.getId());
                                if (node.getId().equals(input)) {
                                    // check if my location is close to node's location
                                    // if so, return node's posts
                                    // if not, ask user to turn on location services and/or wifi/data so we can get location,
                                    // and return to search page
                                    //nodeFound(node);
                                    temp = node;
                                    break;
                                }
                            }
                            if(temp != null){
                                nodeFound(temp);
                            }
                            else{
                                nodeNotFound(input);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            System.out.println("The read failed: " + firebaseError.getMessage());
                        }
                    });
                    handled = true;
                }
                return handled;
            }
        });


        /*
        Button searchButton = (Button) findViewById(R.id.search_btn);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String input = editText.getText().toString();

                mPostsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        System.out.println("There are " + snapshot.getChildrenCount() + " nodes");
                        for (DataSnapshot nodeSnapshot: snapshot.getChildren()) {
                            Node node = nodeSnapshot.getValue(Node.class);
                            System.out.println(node.getId());
                            if (node.getId().equals(input)) {
                                // check if my location is close to node's location
                                // if so, return node's posts
                                // if not, ask user to turn on location services and/or wifi/data so we can get location,
                                // and return to search page
                            }
                        }
                        // Node not found
                        Intent i = new Intent(MainActivity.this, CreateNodeActivity.class);
                        Bundle b = new Bundle();
                        b.putString("key", input);
                        i.putExtras(b);
                        startActivity(i);
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        System.out.println("The read failed: " + firebaseError.getMessage());
                    }
                });*/
                /*
                GeoLocation g = new GeoLocation(-1.1111, -2.22222);
                Post p = new Post("123", "lightpost", "first_item", g);
                mPostsRef.push().setValue(p);*/
        //}
        // });


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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startActivity(new Intent(this, MainActivity.class));

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void nodeFound(final Node node) {
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Discoverey!");
        alertDialog.setMessage("You discovered" + node.getName() + "!\n\nWould you like to explore this node?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(MainActivity.this, viewNodeActivity.class);
                        Bundle b = new Bundle();
                        b.putString("key", node.getId());
                        i.putExtras(b);
                        startActivity(i);
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        temp = null;
                    }
                });
        alertDialog.show();
    }

    private void nodeNotFound(final String id) {
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Node not yet discovered!");
        alertDialog.setMessage("This node has not yet been discovered.\n\nWould you like to become this node's founder?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(MainActivity.this, createNodeActivity.class);
                        Bundle b = new Bundle();
                        b.putString("key", id);
                        i.putExtras(b);
                        startActivity(i);
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        temp = null;
                    }
                });
        alertDialog.show();
    }
}