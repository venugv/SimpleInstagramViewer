package com.codepath.simpleinstagramviewer.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.simpleinstagramviewer.R;
import com.codepath.simpleinstagramviewer.adapter.DividerItemDecoration;
import com.codepath.simpleinstagramviewer.adapter.PopularPictureViewerAdapter;
import com.codepath.simpleinstagramviewer.jsonhandler.DataLoader;

import java.util.logging.Handler;

public class PopularPictureViewerActivity extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_picture_viewer);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition fadeTransform = TransitionInflater.from(this).
                    inflateTransition(android.R.transition.fade);
            fadeTransform.setStartDelay(0);
            fadeTransform.setDuration(500);
            getWindow().setEnterTransition(fadeTransform);
            getWindow().setExitTransition(fadeTransform);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeContainer.setRefreshing(true);
                DataLoader.fetchPopularPhotos(PopularPictureViewerActivity.this, true);
            }
        });
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this));
        DataLoader.fetchPopularPhotos(this, false);
    }

    @Override
    public void onRefresh() {
        DataLoader.fetchPopularPhotos(this, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        DataLoader.checkRefreshStatus(this);
    }

    public SwipeRefreshLayout getSwipeContainer() {
        return swipeContainer;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}
