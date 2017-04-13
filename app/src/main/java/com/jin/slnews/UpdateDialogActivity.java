package com.jin.slnews;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


public class UpdateDialogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_update_dialog);
        String updateinfo = this.getIntent().getExtras().getString("updateLog");
        TextView update_content = (TextView) findViewById(R.id.update_content);
        update_content.setText(updateinfo);
        Button update_id_ok = (Button) findViewById(R.id.update_id_ok);
        Button update_id_cancel = (Button) findViewById(R.id.update_id_cancel);
        CheckBox update_id_check = (CheckBox) findViewById(R.id.update_id_check);
        update_id_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        update_id_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }
}
