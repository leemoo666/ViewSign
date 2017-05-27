package com.ycx.canvas;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewSignBak viewSignBak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewSignBak = (ViewSignBak) findViewById(R.id.vs_main);

        findViewById(R.id.btColor).setOnClickListener(this);
        findViewById(R.id.btCrop).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btColor:
                break;
            case R.id.btCrop:
                viewSignBak.saveBitmap("aaa.png");
                break;
        }
    }
}
