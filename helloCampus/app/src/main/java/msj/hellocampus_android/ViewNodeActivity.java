package msj.hellocampus_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class ViewNodeActivity extends AppCompatActivity {

    private Node node = null;
    Button addMemo;
    Button viewMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle b = getIntent().getExtras();
        final String new_id = b.getString("key");

        Firebase.setAndroidContext(this);
        final Firebase mPostsRef = new Firebase(getResources().getString(R.string.firebase_url)).child("nodes");

        setContentView(R.layout.activity_view_node);
        addMemo = (Button) findViewById(R.id.addMemoButton);
        viewMemo = (Button) findViewById(R.id.viewMemoButton);
        addMemo.setOnClickListener(addMemoHandler);
        viewMemo.setOnClickListener(viewMemoHandler);
        ListView memoList = new ListView(this);

        mPostsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot nodeSnapshot : snapshot.getChildren()) {
                    Node n = nodeSnapshot.getValue(Node.class);
                    if (n.getId().equals(new_id)) {
                        node = n;
                        break;
                    }
                }
                if (node == null)
                    Log.e("ERROR", "CANNOT FIND NODE");

                // Fill out page with node details
                TextView textView_id = (TextView) findViewById(R.id.textView_id);
                textView_id.setText(node.getId());

                TextView textView_name = (TextView) findViewById(R.id.textView_name);
                textView_name.setText(node.getName());

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    View.OnClickListener addMemoHandler = new View.OnClickListener() {
        public void onClick(View v) {
            startActivity(new Intent(v.getContext(), AddMemoActivity.class));
        }
    };
    View.OnClickListener viewMemoHandler = new View.OnClickListener() {
        public void onClick(View v) {
            startActivity(new Intent(v.getContext(), MemoListActivity.class));
        }
    };

/*    @Override
    public void onBackPressed(){
        startActivity(new Intent((this), MainActivity.class));
    }
*/
}
