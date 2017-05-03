package com.example.root.okfit.logic;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.root.okfit.R;
import com.example.root.okfit.view.bubbleview.BubbleLayout;
import com.example.root.okfit.view.bubbleview.BubbleView;
import com.github.lzyzsd.randomcolor.RandomColor;

public class BubbleActivity extends AppCompatActivity {

    private static String[] labels = {"66666666",  "55555555", "44444444","33333333", "22222222", "11111111"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble);
        BubbleLayout layout = (BubbleLayout) findViewById(R.id.bubble_layout);
        for (String label : labels) {
            BubbleView bubbleView = new BubbleView(this);
            bubbleView.setText("三个字 三个字",label);
            bubbleView.setGravity(Gravity.CENTER);
            bubbleView.setCircleColor(new RandomColor().randomColor());
            layout.addView(bubbleView);
            bubbleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(BubbleActivity.this,label.substring(0,1).toUpperCase(),Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
