package msj.hellocampus_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class viewNodeActivity extends AppCompatActivity {

    Button addMemo;
    Button viewMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_node);
        addMemo = (Button) findViewById(R.id.addMemoButton);
        viewMemo = (Button) findViewById(R.id.viewMemoButton);
        addMemo.setOnClickListener(addMemoHandler);
        viewMemo.setOnClickListener(viewMemoHandler);
        ListView memoList = new ListView(this);
    }

    View.OnClickListener addMemoHandler = new View.OnClickListener() {
        public void onClick(View v) {
            startActivity(new Intent(v.getContext(), addNodeActivity.class));
        }
    };
    View.OnClickListener viewMemoHandler = new View.OnClickListener() {
        public void onClick(View v) {
            startActivity(new Intent(v.getContext(), memoListActivity.class));
        }
    };

/*    @Override
    public void onBackPressed(){
        startActivity(new Intent((this), MainActivity.class));
    }
*/
}
