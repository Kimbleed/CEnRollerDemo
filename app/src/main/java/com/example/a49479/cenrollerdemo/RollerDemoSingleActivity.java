package com.example.a49479.cenrollerdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by 49479 on 2018/5/14.
 */

public class RollerDemoSingleActivity extends Activity {

    CEnSingleRollerView rv;

    private Button btn_locate;

    private Button btn_scroll;

    private EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roller3);
        rv = findViewById(R.id.rv_1);
        btn_locate = (Button) findViewById(R.id.btn_locate);
        btn_locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tv = edit.getText().toString();
                rv.stopOnTarget(tv);
            }
        });

        btn_scroll = (Button) findViewById(R.id.btn_scroll);
        btn_scroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rv.startRoll();
            }
        });

        edit = (EditText) findViewById(R.id.et_locate);
    }
}
