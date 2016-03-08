package msj.hellocampus_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.geofire.GeoLocation;

public class CreateNodeActivity extends AppCompatActivity {

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


        // Getting node ID from MainActivity
        Bundle b = getIntent().getExtras();
        final String new_id = b.getString("key");

        TextView textView_id = (TextView) findViewById(R.id.node_id);
        textView_id.setText(new_id);

        final EditText editText_name = (EditText) findViewById(R.id.editText_name);
        final Spinner spinner_type = (Spinner) findViewById(R.id.spinner_type);
        final EditText editText_memo = (EditText) findViewById(R.id.editText_memo);

        // Create node button
        Button new_btn = (Button) findViewById(R.id.create_node);
        new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the input from name, type, and memo
                final String name = editText_name.getText().toString();
                final String type = spinner_type.getSelectedItem().toString();
                final String memo = editText_memo.getText().toString();

                // INSERT USERS LAT AND LONG below, not 0,0
                GeoLocation g = new GeoLocation(0, 0);

                // Create new node object
                // Add it to database, and get its key
                Node n = new Node(new_id, type, name, g);
                newNodeRef.setValue(n);
                String node_Id = newNodeRef.getKey();

                // Create new memo object
                // Add it to database at firebase_url/nodes/node_id/memos
                Memo m = new Memo(memo, user_id);
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
}