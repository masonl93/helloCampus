package msj.hellocampus_android;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class AddMemoActivity extends FragmentActivity {

    Button submitBtn;
    String node_key;
    String node_id;
    private String node_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(this);
        final Firebase mRef = new Firebase(getResources().getString(R.string.firebase_url));

        final String user_id = mRef.getAuth().getUid();

        // Getting node ID from ViewNodeActivity
        Bundle b = getIntent().getExtras();
        node_key = b.getString("key");
        node_id = b.getString("id");
        node_name = b.getString("name");

        setContentView(R.layout.activity_add_memo);
        submitBtn = (Button) findViewById(R.id.submitMemoButton);

        TextView textView_id = (TextView) findViewById(R.id.textView_id);
        final TextView textView_name = (TextView) findViewById(R.id.textView_id);

        textView_id.setText(node_id);
        textView_name.setText(node_name);

        final EditText editText_title = (EditText) findViewById(R.id.editText_title);
        final EditText editText_memo = (EditText) findViewById(R.id.editText_memo);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pull strings from edit texts
                final String title = editText_title.getText().toString();
                final String memo = editText_memo.getText().toString();

                //push data to firebase
                Memo m = new Memo(memo, user_id, title);
                mRef.child("nodes").child(node_key).child("memos").push().setValue(m);
                Intent i = new Intent(AddMemoActivity.this, ViewNodeActivity.class);
                Bundle b = new Bundle();
                b.putString("key", node_id);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }

}
