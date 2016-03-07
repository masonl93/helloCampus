package msj.hellocampus_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddMemoActivity extends AppCompatActivity {

    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_node);
        submitBtn = (Button) findViewById(R.id.submitMemoButton);
        submitBtn.setOnClickListener(submitBtnHandler);
    }

    View.OnClickListener submitBtnHandler = new View.OnClickListener() {
        public void onClick(View v) {
            //push data to firebase
            finish();
        }
    };
}
