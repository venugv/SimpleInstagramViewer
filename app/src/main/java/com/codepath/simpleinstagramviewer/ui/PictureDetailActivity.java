package com.codepath.simpleinstagramviewer.ui;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.codepath.simpleinstagramviewer.R;
import com.codepath.simpleinstagramviewer.adapter.DividerItemDecoration;
import com.codepath.simpleinstagramviewer.adapter.PictureDetailAdapter;
import com.codepath.simpleinstagramviewer.adapter.PopularPictureViewerAdapter;
import com.codepath.simpleinstagramviewer.jsonhandler.DataLoader;
import com.codepath.simpleinstagramviewer.model.InstagramPicture;

public class PictureDetailActivity extends AppCompatActivity {
    public static final String EXTRA_POSITION = "ExtraPosition";
    private ImageView userProfile;
    private TextView personName;
    private TextView elapsedTime;
    private RecyclerView recyclerView;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_detail);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition fadeTransform = TransitionInflater.from(this).
                    inflateTransition(android.R.transition.fade);
            fadeTransform.setStartDelay(0);
            fadeTransform.setDuration(500);
            getWindow().setEnterTransition(fadeTransform);
            getWindow().setExitTransition(fadeTransform);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.position = getIntent().getIntExtra(EXTRA_POSITION, 0);

        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.userProfile = (ImageView) findViewById(R.id.userProfile);
        this.personName = (TextView) findViewById(R.id.personName);
        this.elapsedTime = (TextView) findViewById(R.id.elapsedTime);

        InstagramPicture instagramPicture = DataLoader.getPicture(position);
        init(instagramPicture);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this));
        DataLoader.fetchCommentsForPhoto(this, position);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init(InstagramPicture instagramPicture) {
        Glide.with(this).load(instagramPicture.getProfilePicURL())
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new BitmapImageViewTarget(this.userProfile) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        PictureDetailActivity.this.userProfile.setImageDrawable(circularBitmapDrawable);
                    }
                });

        this.personName.setText(instagramPicture.getUsername());
        this.elapsedTime.setText(PopularPictureViewerAdapter.getElapsedTime(instagramPicture.getSubmittedTime()));
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public int getPosition() {
        return position;
    }
}
