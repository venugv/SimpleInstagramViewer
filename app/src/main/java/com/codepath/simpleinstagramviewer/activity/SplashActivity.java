package com.codepath.simpleinstagramviewer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;

import com.codepath.simpleinstagramviewer.R;
import com.codepath.simpleinstagramviewer.data.DataLoader;

import java.lang.ref.WeakReference;

/**
 * Created by vvenkatraman on 11/23/15.
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition fadeTransform = TransitionInflater.from(this).
                    inflateTransition(android.R.transition.fade);
            fadeTransform.setStartDelay(0);
            fadeTransform.setDuration(500);
            getWindow().setEnterTransition(fadeTransform);
            getWindow().setExitTransition(fadeTransform);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        DataLoader.fetchPopularPhotos(new WeakReference<Context>(this), true);
    }

    public void onLoadFinished() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, PopularPictureViewerActivity.class);
                startActivity(mainIntent);
            }
        }, 1000);
    }

    @Override
    public void onPause() {
        super.onPause();
        supportFinishAfterTransition();
    }
}