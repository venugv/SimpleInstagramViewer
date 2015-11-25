package com.codepath.simpleinstagramviewer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.codepath.simpleinstagramviewer.R;
import com.codepath.simpleinstagramviewer.data.PictureComment;
import com.codepath.simpleinstagramviewer.data.PopularPicture;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by vvenkatraman on 11/14/15.
 */
public class PopularPictureViewerAdapter extends RecyclerView.Adapter<PopularPictureViewHolder> {
    private Context context;
    private ArrayList<PopularPicture> data;
    private LayoutInflater inflater;

    public PopularPictureViewerAdapter(Context context, ArrayList<PopularPicture> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    public static String getElapsedTime(String time) {
        Calendar submittedTimeInstance = Calendar.getInstance();
        submittedTimeInstance.setTimeInMillis(Long.parseLong(time));
        Date submittedTime = submittedTimeInstance.getTime();
        long diffInMs = (new Date(System.currentTimeMillis()).getTime() / 1000) - submittedTime.getTime();

        long diffInDays = TimeUnit.SECONDS.toDays(diffInMs);
        if (diffInDays > 0) {
            return diffInDays + "d";
        }
        long diffInHour = TimeUnit.SECONDS.toHours(diffInMs);
        if (diffInHour > 0) {
            return diffInHour + "h";
        }
        long diffInMin = TimeUnit.SECONDS.toMinutes(diffInMs);
        if (diffInMin > 0) {
            return diffInMin + "m";
        }
        return diffInMs + "s";
    }

    @Override
    public PopularPictureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PopularPictureViewHolder viewHolder;
        View v;
        v = inflater.inflate(
                R.layout.popular_picture_card, parent, false);
        viewHolder = new PopularPictureViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PopularPictureViewHolder holder, int position) {
        PopularPicture item = data.get(position);
        Glide.with(context).load(item.getProfilePicURL())
                .asBitmap()
                .centerCrop()
                .placeholder(R.color.imagePlaceholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new BitmapImageViewTarget(holder.userProfile) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.userProfile.setImageDrawable(circularBitmapDrawable);
                    }
                });


        holder.personName.setText(item.getUsername());
        holder.elapsedTime.setText(getElapsedTime(item.getSubmittedTime()));

        Glide.with(context).load(data.get(position).getImageURL())
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.imagePlaceholder)
                .into(holder.previewPic);

        if (item.getType().equals("video")) {
            holder.playButton.setVisibility(View.VISIBLE);
            holder.playButton.setOnClickListener(holder);
        } else {
            holder.playButton.setVisibility(View.GONE);
            holder.playButton.setOnClickListener(null);
        }

        holder.imageCaption.setText(item.getCaption());
        holder.likeCount.setText(item.getLikes() + " likes");

        if (item.getCommentsCount() > 0) {
            PictureComment comment = item.getComments().get(0);
            Glide.with(context).load(comment.getProfilePicURL())
                    .asBitmap()
                    .placeholder(R.color.imagePlaceholder)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new BitmapImageViewTarget(holder.commentUserProfile) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            holder.commentUserProfile.setImageDrawable(circularBitmapDrawable);
                        }
                    });
            holder.commentElapsedTime.setText(getElapsedTime(comment.getSubmittedTime()));
            String text = "<font color=\"#235624\"><b>" + comment.getUserName() +
                    "</b></font>" + " " + comment.getComment();
            holder.comment.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class CircleTransform extends BitmapTransformation {
        public CircleTransform(Context context) {
            super(context);
        }

        private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }
}
