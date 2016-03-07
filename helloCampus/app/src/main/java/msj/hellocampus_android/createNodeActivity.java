package msj.hellocampus_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CreateNodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_node);

        Bundle b = getIntent().getExtras();
        String new_id = b.getString("key");

        TextView textView_id = (TextView) findViewById(R.id.node_id);
        textView_id.setText(new_id);
    }


}