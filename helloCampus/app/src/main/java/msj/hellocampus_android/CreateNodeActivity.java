package msj.hellocampus_android;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;

public class CreateNodeActivity extends FragmentActivity {

    private GeoFire geoFire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_node);

        // Firebase ref's to Nodes, Memos, and Users
        Firebase.setAndroidContext(this);
        final Firebase mRef = new Firebase(getResources().getString(R.string.firebase_url));
        final String user_id = mRef.getAuth().getUid();
        final Firebase mNodesRef = mRef.child("nodes");
        final Firebase newNodeRef = mNodesRef.push();

        // Geofire database setup
        geoFire = new GeoFire(new Firebase(getResources().getString(R.string.firebase_url)).child("_geofire"));


        // Getting node ID from MainActivity
        Bundle b = getIntent().getExtras();
        final String new_id = b.getString("key");
        final double lat = b.getDouble("lat");
        final double lng = b.getDouble("lng");

        TextView textView_id = (TextView) findViewById(R.id.node_id);
        textView_id.setText(new_id);

        final EditText editText_name = (EditText) findViewById(R.id.editText_name);
        final Spinner spinner_type = (Spinner) findViewById(R.id.spinner_type);
        final EditText editText_memo = (EditText) findViewById(R.id.editText_memo);
        final EditText editText_title = (EditText) findViewById(R.id.editText_title);

        // Create node button
        Button new_btn = (Button) findViewById(R.id.create_node);
        new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the input from name, type, and memo
                final String name = editText_name.getText().toString();
                final String type = spinner_type.getSelectedItem().toString();
                final String memo = editText_memo.getText().toString();
                //final String title = editText_title.getText().toString();

                GeoLocation g = new GeoLocation(lat, lng);

                // Create new node object
                // Add it to database, and get its key
                Node n = new Node(new_id, type, name, g);
                newNodeRef.setValue(n);
                String node_Id = newNodeRef.getKey();

                // Add node location to Geofire database
                geoFire.setLocation(node_Id, g);

                // Create new memo object
                // Add it to database at firebase_url/nodes/node_id/memos
                Memo m = new Memo(memo, user_id, "First Memo");   //change last argument to title, not string
                mNodesRef.child(node_Id).child("memos").push().setValue(m);

                // View the node now, pass node id to activity
                Intent i = new Intent(CreateNodeActivity.this, ViewNodeActivity.class);
                Bundle b = new Bundle();
                b.putString("key", new_id);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent((this), MainActivity.class));
    }
}