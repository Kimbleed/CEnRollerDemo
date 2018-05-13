package com.example.a49479.cenrollerdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by 49479 on 2018/5/10.
 */

public class RollerDemoActivity extends Activity {
    CEnSingleRollerView rvArr[] = new CEnSingleRollerView[6];

    private Button btn_locate;

    private Button btn_scroll;

    private EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roller);
        rvArr[0] = findViewById(R.id.rv_1);
        rvArr[1] = findViewById(R.id.rv_2);
        rvArr[2] = findViewById(R.id.rv_3);
        rvArr[3] = findViewById(R.id.rv_4);
        rvArr[4] = findViewById(R.id.rv_5);
        rvArr[5] = findViewById(R.id.rv_6);


        btn_locate = (Button) findViewById(R.id.btn_locate);
        btn_locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tv = edit.getText().toString();
                int tvInt = -1;
                try {
                    tvInt = Integer.parseInt(tv);
                } catch (Exception e) {

                }
                for (int i = rvArr.length - 1; i >= 0; i--) {
                    rvArr[i].stopRoll(tvInt % 10);
                    tvInt /= 10;
                }
            }
        });

        btn_scroll = (Button) findViewById(R.id.btn_scroll);
        btn_scroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (CEnSingleRollerView rv : rvArr) {
                    rv.startRoll();
                }
            }
        });

        edit = (EditText) findViewById(R.id.et_locate);

    }
}
