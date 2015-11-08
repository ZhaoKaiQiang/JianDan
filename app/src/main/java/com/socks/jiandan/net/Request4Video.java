package com.socks.jiandan.net;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.socks.jiandan.model.Video;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class Request4Video extends Request<ArrayList<Video>> {


    private Response.Listener<ArrayList<Video>> listener;

    public Request4Video(String url, Response.Listener<ArrayList<Video>> listener,
                         Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = listener;
    }

    @Override
    protected Response<ArrayList<Video>> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            JSONObject jsonObject = new JSONObject(jsonStr);

            if ("ok".equals(jsonObject.optString("status"))) {

                JSONArray commentsArray = jsonObject.optJSONArray("comments");
                ArrayList<Video> videos = new ArrayList<>();

                for (int i = 0; i < commentsArray.length(); i++) {

                    JSONObject commentObject = commentsArray.getJSONObject(i);
                    JSONObject videoObject = commentObject.optJSONArray("videos").optJSONObject(0);

                    if (videoObject != null) {
                        Video video = new Video();
                        video.setTitle(videoObject.optString("title"));
                        String videoSource = videoObject.optString("video_source");
                        video.setComment_ID(commentObject.optString("comment_ID"));
                        video.setVote_positive(commentObject.optString("vote_positive"));
                        video.setVote_negative(commentObject.optString("vote_negative"));
                        video.setVideo_source(videoSource);

                        if (videoSource.equals("youku")) {
                            video.setUrl(videoObject.optString("link"));
                            video.setDesc(videoObject.optString("description"));
                            video.setImgUrl(videoObject.optString("thumbnail"));
                            video.setImgUrl4Big(videoObject.optString("thumbnail_v2"));
                        } else if (videoSource.equals("56")) {
                            video.setUrl(videoObject.optString("url"));
                            video.setDesc(videoObject.optString("desc"));
                            video.setImgUrl4Big(videoObject.optString("img"));
                            video.setImgUrl(videoObject.optString("mimg"));
                        } else if (videoSource.equals("tudou")) {
                            video.setUrl(videoObject.optString("playUrl"));
                            video.setImgUrl(videoObject.optString("picUrl"));
                            video.setImgUrl4Big(videoObject.optString("picUrl"));
                            video.setDesc(videoObject.optString("description"));
                        }

                        videos.add(video);
                    }
                }

                return Response.success(videos, HttpHeaderParser.parseCacheHeaders(response));
            } else {
                return Response.success(new ArrayList<Video>(), HttpHeaderParser.parseCacheHeaders(response));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(ArrayList<Video> response) {
        listener.onResponse(response);
    }

}
