package com.example.a49479.cenrollerdemo;

import android.app.Activity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by 49479 on 2018/5/10.
 */

public class RollerDemoActivity extends Activity {
    CEnSingleRollerView CEnSingleRollerView;

    private Button btn_locate;

    private Button btn_scroll;

    private EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roller);
        CEnSingleRollerView = findViewById(R.id.rv);

        btn_locate = (Button) findViewById(R.id.btn_locate);
        btn_locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CEnSingleRollerView.stopRoll(3);
            }
        });

        btn_scroll = (Button) findViewById(R.id.btn_scroll);
        btn_scroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CEnSingleRollerView.startRoll();
            }
        });

        edit = (EditText) findViewById(R.id.et_locate);

    }
}
