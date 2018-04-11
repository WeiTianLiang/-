package com.example.wtl.mynotes.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.wtl.mynotes.R;
import com.example.wtl.mynotes.Tool.HideScreenTop;

import yanzhikai.textpath.SyncTextPathView;
import yanzhikai.textpath.painter.FireworksPainter;

public class FirstStartActivity extends AppCompatActivity {

    private SyncTextPathView first_start;
    private TextView jump_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_start);
        HideScreenTop.HideScreenTop(getWindow());
        first_start = (SyncTextPathView) findViewById(R.id.first_start);
        first_start.setPathPainter(new FireworksPainter());
        first_start.startAnimation(0,1);
        jump_start = (TextView) findViewById(R.id.jump_start);
        jump_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstStartActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.first_start_visil,R.anim.first_start_gone);
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(FirstStartActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.first_start_visil,R.anim.first_start_gone);
            }
        },11000);
    }
}
