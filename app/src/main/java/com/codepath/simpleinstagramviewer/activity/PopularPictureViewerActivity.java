package com.codepath.simpleinstagramviewer.activity;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;

import com.codepath.simpleinstagramviewer.R;
import com.codepath.simpleinstagramviewer.data.DataLoader;

import java.lang.ref.WeakReference;

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
                DataLoader.fetchPopularPhotos(new WeakReference<Context>(PopularPictureViewerActivity.this), true);
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
        DataLoader.fetchPopularPhotos(new WeakReference<Context>(this), false);
    }

    @Override
    public void onRefresh() {
        DataLoader.fetchPopularPhotos(new WeakReference<Context>(this), true);
    }

    @Override
    public void onResume() {
        super.onResume();
        DataLoader.checkRefreshStatus(new WeakReference<PopularPictureViewerActivity>(this));
    }

    public SwipeRefreshLayout getSwipeContainer() {
        return swipeContainer;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private final int[] ATTRS = new int[]{android.R.attr.listDivider};

        private Drawable mDivider;

        /**
         * Default divider will be used
         */
        public DividerItemDecoration(Context context) {
            final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
            mDivider = styledAttributes.getDrawable(0);
            styledAttributes.recycle();
        }

        /**
         * Custom divider will be used
         */
        public DividerItemDecoration(Context context, int resId) {
            mDivider = ContextCompat.getDrawable(context, resId);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}
