package com.codepath.simpleinstagramviewer.data;

import android.content.Context;
import android.util.Log;

import com.codepath.simpleinstagramviewer.adapter.PictureDetailAdapter;
import com.codepath.simpleinstagramviewer.adapter.PopularPictureViewerAdapter;
import com.codepath.simpleinstagramviewer.activity.PictureDetailActivity;
import com.codepath.simpleinstagramviewer.activity.PopularPictureViewerActivity;
import com.codepath.simpleinstagramviewer.activity.SplashActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by vvenkatraman on 11/17/15.
 */
public class DataLoader {
    public static final String CLIENT_ID = "4e3ac30f1aec4a379c22284ed02500f7";
    public static final String POPULAR_URL = "https://api.instagram.com/v1/media/popular?client_id=";
    public static final String COMMENT_URL = "https://api.instagram.com/v1/media/%s/comments?client_id=%s";
    private static final String TAG = "DataLoader";
    private static boolean requestPending = false;
    private static long lastRefreshTime = -1;

    private static ArrayList<PopularPicture> pictureArrayList = new ArrayList<>();

    public static void fetchPopularPhotos(final WeakReference<Context> contextRef, boolean refresh) {
        if (!refresh || requestPending) {
            updateActivity(contextRef);
            return;
        }
        pictureArrayList.clear();
        String request_url = POPULAR_URL + CLIENT_ID;
        AsyncHttpClient client = new AsyncHttpClient();
        requestPending = true;
        lastRefreshTime = Calendar.getInstance().getTimeInMillis();
        client.get(request_url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                requestPending = false;
                JSONArray data = null;
                try {
                    data = response.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject photo = data.getJSONObject(i);
                        PopularPicture instagramPhoto = new PopularPicture();
                        if (!photo.isNull("id")) {
                            instagramPhoto.setId(photo.getString("id"));
                        }
                        instagramPhoto.setUsername(photo.getJSONObject("user").getString("username"));
                        if (!photo.isNull("caption")) {
                            instagramPhoto.setCaption(photo.getJSONObject("caption").getString("text"));
                        } else {
                            instagramPhoto.setCaption("");
                        }
                        if (!photo.isNull("comments")) {
                            int count = photo.getJSONObject("comments").getInt("count");
                            instagramPhoto.setCommentsCount(count);
                            JSONArray comments = photo.getJSONObject("comments").getJSONArray("data");
                            extractComments(comments, instagramPhoto, count);
                        }
                        instagramPhoto.setType(photo.getString("type"));
                        if (instagramPhoto.getType().equals("video")) {
                            instagramPhoto.setVideoURL(photo.getJSONObject("videos").
                                    getJSONObject("standard_resolution").getString("url"));
                        }
                        instagramPhoto.setSubmittedTime(photo.getString("created_time"));
                        instagramPhoto.setProfilePicURL(photo.getJSONObject("user").getString("profile_picture"));
                        instagramPhoto.setLikes(photo.getJSONObject("likes").getInt("count"));
                        instagramPhoto.setImageURL(photo.getJSONObject("images").
                                getJSONObject("standard_resolution").getString("url"));
                        instagramPhoto.setImageHeight(photo.getJSONObject("images").
                                getJSONObject("standard_resolution").getInt("height"));
                        pictureArrayList.add(instagramPhoto);
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                updateActivity(contextRef);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, "Response failed. Status Code : " + statusCode);
                Log.e(TAG, "Response failed. " + responseString);
                requestPending = false;
            }
        });
    }

    public static void fetchCommentsForPhoto(final PictureDetailActivity activity, int position) {
        final PopularPicture picture = getPicture(position);
        if (picture == null) {
            return;
        }
        picture.getComments().clear();
        String request_url = String.format(COMMENT_URL, picture.getId(), CLIENT_ID);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(request_url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONArray data = null;
                try {
                    data = response.getJSONArray("data");
                    extractComments(data, picture, picture.getCommentsCount());
                    PictureDetailAdapter adapter = new PictureDetailAdapter(activity,
                            picture);
                    activity.getRecyclerView().setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, "Response failed. Status Code : " + statusCode);
                Log.e(TAG, "Response failed. " + responseString);
                requestPending = false;
            }
        });
    }

    private static void extractComments(JSONArray comments, PopularPicture instagramPhoto, int count) throws JSONException {
        if (count > 0) {
            for (int j = comments.length() - 1; j > -1; j--) {
                JSONObject comment = comments.getJSONObject(j);
                instagramPhoto.getComments().add(
                        new PictureComment(comment.getString("text"),
                                comment.getJSONObject("from").getString("username"),
                                comment.getJSONObject("from").getString("profile_picture"),
                                comment.getString("created_time")));
            }
        }
    }

    private static void updateActivity(WeakReference<Context> contextRef) {
        Context context = contextRef.get();
        if (context instanceof PopularPictureViewerActivity && !requestPending) {
            PopularPictureViewerActivity activity = (PopularPictureViewerActivity) context;
            PopularPictureViewerAdapter adapter = new PopularPictureViewerAdapter(context,
                    pictureArrayList);
            activity.getRecyclerView().setAdapter(adapter);
            adapter.notifyDataSetChanged();
            activity.getSwipeContainer().setRefreshing(false);
        } else if (context instanceof SplashActivity) {
            SplashActivity splashActivity = (SplashActivity) context;
            splashActivity.onLoadFinished();
        }
    }

    public static void checkRefreshStatus(WeakReference<PopularPictureViewerActivity> activityRef) {
        long currentTime = Calendar.getInstance(Locale.getDefault()).getTimeInMillis();
        long elapsedTime = currentTime - lastRefreshTime;
        PopularPictureViewerActivity activity = activityRef.get();
        if (activity == null) {
            return;
        }
        if (!requestPending && elapsedTime > 20000/*20 seconds*/) {
            activity.getSwipeContainer().setRefreshing(true);
            fetchPopularPhotos(new WeakReference<Context>(activity), true);
        }
    }

    public static ArrayList<PopularPicture> getAllPopularPhotos() {
        return pictureArrayList;
    }

    public static PopularPicture getPicture(int key) {
        return pictureArrayList.get(key);
    }
}
