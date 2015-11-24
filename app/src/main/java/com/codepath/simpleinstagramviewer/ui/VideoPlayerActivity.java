package com.codepath.simpleinstagramviewer.ui;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.codepath.simpleinstagramviewer.R;
import com.codepath.simpleinstagramviewer.jsonhandler.DataLoader;

/**
 * Created by vvenkatraman on 11/24/15.
 */
public class VideoPlayerActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {
    private static final String TAG = VideoPlayerActivity.class.getName();
    VideoView videoView;
    int position = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.position = getIntent().getIntExtra(PictureDetailActivity.EXTRA_POSITION, -1);

        if (position < 0 || !DataLoader.getPicture(position).getType().equals("video")) {
            this.finish();
        }
        setContentView(R.layout.activity_player);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition fadeTransform = TransitionInflater.from(this).
                    inflateTransition(android.R.transition.fade);
            fadeTransform.setStartDelay(0);
            fadeTransform.setDuration(500);
            getWindow().setEnterTransition(fadeTransform);
            getWindow().setExitTransition(fadeTransform);
        }

        this.videoView = (VideoView) findViewById(R.id.videoView);
        this.videoView.setVideoURI(Uri.parse(DataLoader.getPicture(position).getVideoURL()));
        MediaController mediaController = new
                MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        videoView.stopPlayback();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.i(TAG, "Duration = " +
                videoView.getDuration());
    }
}
