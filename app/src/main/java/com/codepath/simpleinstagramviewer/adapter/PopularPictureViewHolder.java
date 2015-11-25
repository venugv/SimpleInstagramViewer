package com.codepath.simpleinstagramviewer.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.simpleinstagramviewer.R;
import com.codepath.simpleinstagramviewer.activity.PictureDetailActivity;
import com.codepath.simpleinstagramviewer.activity.VideoPlayerActivity;

/**
 * Created by vvenkatraman on 11/14/15.
 */
public class PopularPictureViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener {
    public ImageView userProfile;
    public TextView personName;
    public TextView elapsedTime;
    public ImageView previewPic;
    public ImageView playButton;
    public TextView imageCaption;
    public TextView likeCount;
    public ImageView commentUserProfile;
    public TextView comment;
    public TextView commentElapsedTime;

    public PopularPictureViewHolder(View itemView) {
        super(itemView);

        this.userProfile = (ImageView) itemView.findViewById(R.id.userProfile);
        this.personName = (TextView) itemView.findViewById(R.id.personName);
        this.elapsedTime = (TextView) itemView.findViewById(R.id.elapsedTime);
        this.previewPic = (ImageView) itemView.findViewById(R.id.previewPic);
        this.playButton = (ImageView) itemView.findViewById(R.id.playButton);
        this.imageCaption = (TextView) itemView.findViewById(R.id.imageCaption);
        this.likeCount = (TextView) itemView.findViewById(R.id.likeCount);
        this.commentUserProfile = (ImageView) itemView.findViewById(R.id.commentUserProfile);
        this.comment = (TextView) itemView.findViewById(R.id.comment);
        this.commentElapsedTime = (TextView) itemView.findViewById(R.id.commentElapsedTime);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();
        if (v.getId() == R.id.playButton) {
            Intent playIntent = new Intent(itemView.getContext(), VideoPlayerActivity.class);
            playIntent.putExtra(PictureDetailActivity.EXTRA_POSITION, position);
            itemView.getContext().startActivity(playIntent);
        } else {
            Intent pictureDetailIntent = new Intent(itemView.getContext(), PictureDetailActivity.class);
            pictureDetailIntent.putExtra(PictureDetailActivity.EXTRA_POSITION, position);
            itemView.getContext().startActivity(pictureDetailIntent);
        }
    }
}
