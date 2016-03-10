package msj.hellocampus_android;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
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

import java.util.ArrayList;
import java.util.List;

public class ViewNodeActivity extends FragmentActivity {

    private Node node = null;
    private String node_key = null;
    private String node_id;
    private String node_name;
    List<String> memos = new ArrayList<String>();
    String memo;
    Button addMemo;
    Button viewMemo;
    TextView textView_memo;
    Firebase mRef;
    private int memo_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);

        mRef = new Firebase(getResources().getString(R.string.firebase_url));

        // Getting node id that was passed from either MainAcivity or CreateNodeActivity
        Bundle b = getIntent().getExtras();
        node_id = b.getString("key");

        setContentView(R.layout.activity_view_node);
        addMemo = (Button) findViewById(R.id.addMemoButton);
        viewMemo = (Button) findViewById(R.id.viewMemoButton);
        addMemo.setOnClickListener(addMemoHandler);
        viewMemo.setOnClickListener(viewMemoHandler);
        ListView memoList = new ListView(this);

        mRef.child("nodes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // find our node and get its key
                for (DataSnapshot nodeSnapshot : snapshot.getChildren()) {
                    Node n = nodeSnapshot.getValue(Node.class);
                    if (n.getId().equals(node_id)) {
                        node = n;
                        node_key = nodeSnapshot.getKey();
                        break;
                    }
                }
                if (node == null)
                    Log.e("ERROR", "CANNOT FIND NODE");

                // Fill out page with node details
                TextView textView_id = (TextView) findViewById(R.id.textView_id);
                textView_id.setText(node.getId());

                node_name = node.getName();
                TextView textView_name = (TextView) findViewById(R.id.textView_name);
                textView_name.setText(node_name);

                // Inserting the most recent memo for the Node into the view
                textView_memo = (TextView) findViewById(R.id.textView_memo);
                //mRef.child("nodes").child(node_key).child("memos").limitToLast(1).addValueEventListener(new ValueEventListener() {
                mRef.child("nodes").child(node_key).child("memos").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        System.out.println("There are " + snapshot.getChildrenCount() + " memos");
                        memo_count = (int)snapshot.getChildrenCount();
                        for (DataSnapshot nodeSnapshot : snapshot.getChildren()) {
                            Memo m = nodeSnapshot.getValue(Memo.class);
                            memo = m.getMessage();
                        }


                        //System.out.println(memos);
                        //memo = memos.get(memos.size() - 1);
                        textView_memo.setText(memo);
                        viewMemo.setText(String.format("View Memos (%d)", memo_count));
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        System.out.println("The read failed: " + firebaseError.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    View.OnClickListener addMemoHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(v.getContext(), AddMemoActivity.class);
            Bundle b = new Bundle();
            b.putString("key", node_key);
            b.putString("id", node_id);
            b.putString("name", node_name);
            i.putExtras(b);
            startActivity(i);
            //startActivity(new Intent(v.getContext(), AddMemoActivity.class));
        }
    };
    View.OnClickListener viewMemoHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(v.getContext(), MemoListActivity.class);
            Bundle b = new Bundle();
            b.putString("key", node_key);
            i.putExtras(b);
            startActivity(i);
            //startActivity(new Intent(v.getContext(), MemoListActivity.class));
        }
    };

    @Override
    public void onBackPressed(){
        startActivity(new Intent((this), MainActivity.class));
    }

}
