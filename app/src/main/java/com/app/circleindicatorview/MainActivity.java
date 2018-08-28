package com.app.circleindicatorview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int index1 = 0;
    private int index2 = 0;
    CircleIndicatorView mCircleIndicatorView1;
    CircleIndicatorView mCircleIndicatorView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCircleIndicatorView1 = findViewById(R.id.circle_indicator_view1);
        mCircleIndicatorView1.setIndicatorCount(10);
        mCircleIndicatorView1.setIndicatorClickListener(new CircleIndicatorView.IndicatorClickListener() {
            @Override
            public void OnClick(int position) {
                index1 = position;
            }
        });

        mCircleIndicatorView2 = findViewById(R.id.circle_indicator_view2);
        mCircleIndicatorView2.setIndicatorCount(5);
        mCircleIndicatorView2.setIndicatorClickListener(new CircleIndicatorView.IndicatorClickListener() {
            @Override
            public void OnClick(int position) {
                index2 = position;
            }
        });

        findViewById(R.id.last1).setOnClickListener(this);
        findViewById(R.id.next1).setOnClickListener(this);
        findViewById(R.id.last2).setOnClickListener(this);
        findViewById(R.id.next2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.last1:
                if (index1 >= 1) {
                    index1--;
                    mCircleIndicatorView1.setSelectIndicator(index1);
                }
                break;
            case R.id.next1:
                if (index1 <= 8) {
                    index1++;
                    mCircleIndicatorView1.setSelectIndicator(index1);
                }
                break;
            case R.id.last2:
                if (index2 >= 1) {
                    index2--;
                    mCircleIndicatorView2.setSelectIndicator(index2);
                }
                break;
            case R.id.next2:
                if (index2 <= 3) {
                    index2++;
                    mCircleIndicatorView2.setSelectIndicator(index2);
                }
                break;
        }
    }
}
