package com.example.massage;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.SyncStateContract;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class StopWatchAct extends AppCompatActivity {

    Button btnstart, btnstop, btnresume;
    Animation roundingalone;
    ImageView icanchor, bgcircle;
    Chronometer timeHere;
    float rotationOffset = 32;
    int minutes;
    long elapsedMillis;
    long milliseconds;

    private float getRotationAngle(float minutes, long elapsedMillis) {
        System.out.println("mins: " + minutes + " elapsedmills: " + elapsedMillis);
        float seconds = minutes * 60;
        float milliseconds = seconds * 1000;
        float rotationPercentage = elapsedMillis / milliseconds;
        return 360 * rotationPercentage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent a = getIntent();
        minutes = a.getIntExtra("minutes", 0);
        int seconds = minutes * 60;
        milliseconds = seconds * 1000;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);
        btnstop = findViewById(R.id.btnStop);
        btnstart = findViewById(R.id.btnStart);
        btnresume = findViewById(R.id.btnResume);
        icanchor = findViewById(R.id.icanchor);
        bgcircle = findViewById(R.id.bgcircle);
        timeHere = findViewById(R.id.timerHere);
        btnstop.setAlpha(0);
        btnresume.setAlpha(0);
        roundingalone = AnimationUtils.loadAnimation(this, R.anim.roundingalone);
        roundingalone.setDuration(milliseconds);
        roundingalone.setInterpolator(new LinearInterpolator());
        Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/MLight.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MMedium.ttf");
        Typeface MRegular = Typeface.createFromAsset(getAssets(), "fonts/MRegular.ttf");
        btnstart.setTypeface(MMedium);
        btnstop.setTypeface(MMedium);

        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icanchor.setRotation(rotationOffset);
                icanchor.startAnimation(roundingalone);

                btnstop.bringToFront();
                btnstop.animate().alpha(1).translationY(-80).setDuration(300).start();

                btnresume.animate().alpha(0).setDuration(300).start();
                btnstart.animate().alpha(0).setDuration(300).start();
                btnresume.setEnabled(false);
                btnstart.setEnabled(false);
                btnstop.setEnabled(true);
                timeHere.setBase(SystemClock.elapsedRealtime());
                timeHere.start();
            }
        });

        btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnresume.bringToFront();
                btnstart.animate().alpha(1).translationY(-80).setDuration(300).start();
                btnstart.setEnabled(true);
                btnresume.setEnabled(true);
                btnstop.setEnabled(false);


                btnresume.animate().alpha(1).translationY(-80).setDuration(300).start();
                btnstop.animate().alpha(0).setDuration(300).start();
                icanchor.clearAnimation();
                elapsedMillis = SystemClock.elapsedRealtime() - timeHere.getBase();
                float rotationAngle = getRotationAngle(minutes, elapsedMillis);
                icanchor.setRotation(rotationAngle + rotationOffset);
                btnstart.setText("Reset");
                Drawable bgbtnred = getResources().getDrawable(R.drawable.bgbtnred);
                btnstart.setBackgroundDrawable(bgbtnred);
                timeHere.stop();
            }
        });

        btnresume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icanchor.startAnimation(roundingalone);
                System.out.println("KEEP DIS SHIT GOING HOME BOI!");
                btnresume.setEnabled(false);
                btnstart.setEnabled(false);
                btnstop.setEnabled(true);
                btnstop.bringToFront();
                btnstop.animate().alpha(1).translationY(-80).setDuration(300).start();
                btnresume.animate().alpha(0).setDuration(300).start();
                btnstart.animate().alpha(0).setDuration(300).start();
                timeHere.setBase(SystemClock.elapsedRealtime() - elapsedMillis);
                timeHere.start();
            }
        });

        timeHere.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {

                long elapsedMillis = SystemClock.elapsedRealtime() - timeHere.getBase();

                if(elapsedMillis >= milliseconds)
                {
                    timeHere.stop();
                    timeHere.setTextColor(Color.parseColor("#FF769A"));
                    icanchor.clearAnimation();
                    elapsedMillis = SystemClock.elapsedRealtime() - timeHere.getBase();
                    float rotationAngle = getRotationAngle(minutes, elapsedMillis);
                    icanchor.setRotation(rotationAngle + rotationOffset);
                    bgcircle.setColorFilter( 0xffff0000, PorterDuff.Mode.MULTIPLY );
                    Intent a = new Intent(StopWatchAct.this, End.class);
                    a.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    a.putExtra("time", timeHere.getText().toString());
                    startActivity(a);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    StopWatchAct.this.finish();
                }
            }
        });
    }
}
