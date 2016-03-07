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

    private FirebaseListAdapter<Node> mAdapter;
    ListView memoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_memo_list);

        //ListView memoList = (ListView) findViewById(R.id.list);

        ListView memoList = getListView();

        Firebase.setAndroidContext(this);
        Firebase ref = new Firebase("https://crackling-torch-8127.firebaseio.com/nodes");

        mAdapter = new FirebaseListAdapter<Node>(this, Node.class, layout.two_line_list_item, ref) {
            @Override
            protected void populateView(View view, Node node, int position) {
                //replace 'node.getX' with memo.getTitle OR memo.getMemo
                ((TextView) view.findViewById(android.R.id.text1)).setText(node.getName());
                ((TextView) view.findViewById(android.R.id.text2)).setText(node.getType());
            }
        };
        memoList.setAdapter(mAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Node item = (Node) getListView().getItemAtPosition(position);
        AlertDialog alertDialog = new AlertDialog.Builder(MemoListActivity.this).create();
        //replace 'item.getX' with item.getTitle OR item.getMemo
        alertDialog.setTitle(item.getName());
        alertDialog.setMessage(item.getType());
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}

