package com.codepath.simpleinstagramviewer.adapter;

import android.graphics.Bitmap;
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
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.codepath.simpleinstagramviewer.R;
import com.codepath.simpleinstagramviewer.data.PopularPicture;
import com.codepath.simpleinstagramviewer.data.PictureComment;
import com.codepath.simpleinstagramviewer.activity.PictureDetailActivity;

/**
 * Created by vvenkatraman on 11/23/15.
 */
public class PictureDetailAdapter extends RecyclerView.Adapter<PictureDetailViewHolder> {
    private PictureDetailActivity activity;
    private PopularPicture popularPicture;
    private LayoutInflater inflater;

    public PictureDetailAdapter(PictureDetailActivity activity, PopularPicture popularPicture) {
        this.activity = activity;
        this.popularPicture = popularPicture;
        this.inflater = LayoutInflater.from(activity);
    }

    @Override
    public PictureDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PictureDetailViewHolder viewHolder;
        View v;
        if (viewType == 0) {
            v = inflater.inflate(R.layout.list_item_popular_image, parent, false);
        } else {
            v = inflater.inflate(R.layout.list_item_comment_line, parent, false);
        }
        viewHolder = new PictureDetailViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PictureDetailViewHolder holder, int position) {
        if (position == 0) {
            Glide.with(activity).load(popularPicture.getImageURL())
                    .asBitmap()
                    .placeholder(R.color.imagePlaceholder)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.previewPic);

            holder.imageCaption.setText(popularPicture.getCaption());
            holder.likeCount.setText(popularPicture.getLikes() + " likes");
            holder.commentCount.setText(popularPicture.getCommentsCount() + " comments");
            if (popularPicture.getVideoURL() != null ) {
                holder.playButton.setVisibility(View.VISIBLE);
                holder.itemView.setOnClickListener(holder);
                holder.position = activity.getPosition();
            } else {
                holder.itemView.setOnClickListener(null);
            }
        } else {
            PictureComment comment = popularPicture.getComments().get(position - 1);
            Glide.with(activity).load(comment.getProfilePicURL())
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.color.imagePlaceholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new BitmapImageViewTarget(holder.commentUserProfile) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            holder.commentUserProfile.setImageDrawable(circularBitmapDrawable);
                        }
                    });
            holder.commentElapsedTime.setText(PopularPictureViewerAdapter.getElapsedTime(comment.getSubmittedTime()));
            String text = "<font color=\"#235624\"><b>" + comment.getUserName() +
                    "</b></font>" + " " + comment.getComment();
            holder.comment.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return 1 + popularPicture.getComments().size();
    }
}
