package msj.hellocampus_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.content.Intent;
import android.widget.TextView;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;



public class MainActivity extends FragmentActivity {

    Location lastKnownLocation;
    String locationProvider;
    private Node node_temp = null;

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
                                    node_temp = node;
                                    break;
                                }
                            }
                            if(node_temp != null){
                                nodeFound(node_temp);
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
        alertDialog.setMessage("You discovered " + node.getName() + "!\n\nWould you like to explore this node?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(MainActivity.this, ViewNodeActivity.class);
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
                        node_temp = null;
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
                        Intent i = new Intent(MainActivity.this, CreateNodeActivity.class);
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
                        node_temp = null;
                    }
                });
        alertDialog.show();
    }
}