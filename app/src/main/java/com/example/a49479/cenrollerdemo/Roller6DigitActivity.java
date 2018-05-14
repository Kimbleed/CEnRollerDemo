package com.example.a49479.cenrollerdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a49479.cenrollerdemo.customView.rollerView.RollerSixDigitView;

/**
 * Created by 49479 on 2018/5/14.
 */

public class Roller6DigitActivity extends Activity {

    private RollerSixDigitView rollerSixDigitView;

    private Button btn_locate;

    private Button btn_scroll;

    private EditText edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_6digit);

        rollerSixDigitView = (RollerSixDigitView)findViewById(R.id.custom_roller);

        btn_locate = (Button) findViewById(R.id.btn_locate);
        btn_locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tv = edit.getText().toString();
                rollerSixDigitView.stopRoll(tv);
            }
        });

        btn_scroll = (Button) findViewById(R.id.btn_scroll);
        btn_scroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollerSixDigitView.startRoll(new RollerSixDigitView.RollerSixDigitStopResponse() {
                    @Override
                    public void onResponse() {
                        Toast.makeText(Roller6DigitActivity.this, "全停了", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        edit = (EditText) findViewById(R.id.et_locate);
    }

}
