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
 * Created by vvenkatraman on 11/23/15.
 */
public class PictureDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView previewPic;
    public ImageView playButton;
    public TextView imageCaption;
    public TextView likeCount;
    public TextView commentCount;
    public ImageView commentUserProfile;
    public TextView comment;
    public TextView commentElapsedTime;
    public int position;

    public PictureDetailViewHolder(View itemView) {
        super(itemView);

        this.previewPic = (ImageView) itemView.findViewById(R.id.previewPic);
        this.playButton = (ImageView) itemView.findViewById(R.id.playButton);
        this.imageCaption = (TextView) itemView.findViewById(R.id.imageCaption);
        this.likeCount = (TextView) itemView.findViewById(R.id.likeCount);
        this.commentCount = (TextView) itemView.findViewById(R.id.commentCount);
        this.commentUserProfile = (ImageView) itemView.findViewById(R.id.commentUserProfile);
        this.comment = (TextView) itemView.findViewById(R.id.comment);
        this.commentElapsedTime = (TextView) itemView.findViewById(R.id.commentElapsedTime);
    }

    @Override
    public void onClick(View v) {
        if (this.playButton.getVisibility() != View.GONE) {
            Intent playIntent = new Intent(this.itemView.getContext(), VideoPlayerActivity.class);
            playIntent.putExtra(PictureDetailActivity.EXTRA_POSITION, position);
            this.itemView.getContext().startActivity(playIntent);
//            String videourl = DataLoader.getPicture(this.position).getVideoURL();
//            Uri uri = Uri.parse(videourl);
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            intent.setDataAndType(uri, "video/mp4");
//            this.itemView.getContext().startActivity(intent);
        }
    }
}
