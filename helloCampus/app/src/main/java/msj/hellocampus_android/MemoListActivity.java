package msj.hellocampus_android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.R.layout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

public class MemoListActivity extends ListActivity {

    private FirebaseListAdapter<Memo> mAdapter;
    ListView memoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_memo_list);

        //ListView memoList = (ListView) findViewById(R.id.list);

        // Getting node ID from ViewNodeActivity
        Bundle b = getIntent().getExtras();
        String node_key = b.getString("key");

        ListView memoList = getListView();

        Firebase.setAndroidContext(this);
        Firebase mMemosRef = new Firebase(getResources().getString(R.string.firebase_url)).child("nodes").child(node_key).child("memos");

        mAdapter = new FirebaseListAdapter<Memo>(this, Memo.class, R.layout.listviewtext, mMemosRef) {
            @Override
            protected void populateView(View view, Memo memo, int position) {
                //replace 'node.getX' with memo.getTitle OR memo.getMemo
                ((TextView) view.findViewById(android.R.id.text1)).setText(memo.getTitle());
                ((TextView) view.findViewById(android.R.id.text2)).setText(memo.getMessage());
            }
        };
        memoList.setAdapter(mAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Memo item = (Memo) getListView().getItemAtPosition(position);
        AlertDialog alertDialog = new AlertDialog.Builder(MemoListActivity.this).create();
        //replace 'item.getX' with item.getTitle OR item.getMemo
        alertDialog.setTitle(item.getTitle());
        alertDialog.setMessage(item.getMessage());
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}

