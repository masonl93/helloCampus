package msj.hellocampus_android;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.TextView;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;



public class MainActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Node node_temp = null;
    private GoogleApiClient mGoogleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    public static final String TAG = Activity.class.getSimpleName();
    private double lat, lng;
    private Location user_location;
    private Button mapsBtn;
    private boolean near_node = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        final Firebase mNodesRef = new Firebase(getResources().getString(R.string.firebase_url)).child("nodes");

        setContentView(R.layout.activity_main);

        //initialize googleApiClient
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        final EditText editText = (EditText) findViewById(R.id.search);
        mapsBtn = (Button) findViewById(R.id.mapButton);
        mapsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                Bundle b = new Bundle();
                b.putDouble("lat", lat);
                System.out.println("lat: " + lat);
                System.out.println("lng: " + lng);
                b.putDouble("lng", lng);
                i.putExtras(b);
                startActivity(i);
                // Starts an intent for the sign up activity
                //startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    final String input = editText.getText().toString();
                    mNodesRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            System.out.println("There are " + snapshot.getChildrenCount() + " nodes");
                            for (DataSnapshot nodeSnapshot : snapshot.getChildren()) {
                                Node node = nodeSnapshot.getValue(Node.class);
                                System.out.println(node.getId());
                                if (node.getId().equals(input)) {
                                    // Determine how far the user is from the node
                                    Location node_location = new Location("");
                                    node_location.setLatitude(node.getLatitude());
                                    node_location.setLongitude(node.getLongitude());
                                    float dist_in_meters = node_location.distanceTo(user_location);
                                    // if more than 50 meters, then either not close enough
                                    // or we are close enough but there are two nodes with
                                    // same ID codes
                                    if (dist_in_meters > 50) {
                                        near_node = false;
                                    }
                                    else {
                                        node_temp = node;
                                        near_node = true;
                                        break;
                                    }
                                }
                            }
                            if (node_temp != null) {
                                nodeFound(node_temp);
                            }
                            // not close enough to node, notify user to move closer
                            else if (!near_node){
                                nodify_not_near_node(input);
                            }
                            else {
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

    // back button takes you to home screen again
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
    private void nodeFound(final Node node) {
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Discovery!");
        alertDialog.setMessage("You discovered " + node.getName() + "!\n\nWould you like to explore this node?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(MainActivity.this, ViewNodeActivity.class);
                        Bundle b = new Bundle();
                        b.putString("key", node.getId());
                        i.putExtras(b);
                        dialog.dismiss();
                        startActivity(i);
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
                        b.putDouble("lat", lat);
                        b.putDouble("lng", lng);
                        i.putExtras(b);
                        dialog.dismiss();
                        startActivity(i);
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

    private void nodify_not_near_node(final String id) {
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Not close enough to the node!");
        alertDialog.setMessage("Be at least 30 meters from the object and try again");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        node_temp = null;
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onConnected(Bundle bundle) {
        System.out.println("onConnected bitch");
        Log.i(TAG, "Location services connected.");
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Permissions are chill.");
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.out.println("Did not connect bitch");
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    private void handleNewLocation(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        user_location = location;
        Log.d(TAG, location.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }
}