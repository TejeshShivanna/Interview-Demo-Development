package demo.interview.development;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class GraphApiMethods {
    String pageAccessToken = Constants.none;
    Map<String, PostInfo> postInfoMap = new HashMap<>();
    Map<String, GraphRequest> pageInSightsGraphRequestList = new HashMap<>();

    private static final String TAG = GraphApiMethods.class.getName();
    private static final String unpublishedGraphPath = Constants.forwardSlash + Constants.pageId + Constants.forwardSlash + Constants.promotablePosts;
    private static final GraphApiMethods graphApiMethods = new GraphApiMethods();

    public Map<String, PostInfo> getPostInfoMap() {
        return postInfoMap;
    }

    public static GraphApiMethods getInstance() {
        return graphApiMethods;
    }

    public void setPageAccessToken(String pageId) {
        String graphPath = Constants.forwardSlash + pageId;
        Bundle bundle = new Bundle();
        bundle.putString(Constants.fields, Constants.accessToken);
        GraphRequest graphRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), graphPath, bundle, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                JSONObject jsonObject = response.getJSONObject();
                try {
                    pageAccessToken = jsonObject.get(Constants.accessToken).toString();
                } catch (JSONException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }
        });
        graphRequest.executeAsync();
    }

    private PostInfo getPostInfo(JSONObject currentPost) {
        try {
            String postId = currentPost.getString(Constants.id);
            String message = currentPost.has(Constants.message)? currentPost.getString(Constants.message):null;
            String link = currentPost.has(Constants.link)? currentPost.getString(Constants.link):null;
            String name = currentPost.has(Constants.name)? currentPost.getString(Constants.name):null;
            String description = currentPost.has(Constants.description)? currentPost.getString(Constants.description):null;
            String createdTime = currentPost.has(Constants.createdTime)? currentPost.getString(Constants.createdTime):null;
            String fullPicture = currentPost.has(Constants.fullPicture)? currentPost.getString(Constants.fullPicture):null;
            return new PostInfo(postId, message, link, name, description, createdTime, fullPicture);
        } catch (JSONException jsonexception) {
            Log.d(TAG, jsonexception.getLocalizedMessage());
        }
        return null;
    }

    private void setPostInfoMap(JSONObject pagePostsJsonObject) {
        postInfoMap.clear();
        try {
            JSONArray jsonArrayPosts = (JSONArray) pagePostsJsonObject.get(Constants.data);
            for (int i = 0; i < jsonArrayPosts.length(); i++) {
                PostInfo postInfo = getPostInfo((JSONObject) jsonArrayPosts.get(i));
                postInfoMap.put(postInfo.getPostId(), postInfo);
            }
        } catch (JSONException jsonexception) {
            Log.d(TAG, jsonexception.getLocalizedMessage());
        }
    }

    public void setPagePosts(String graphPath) {
        GraphRequest graphRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), graphPath, null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                setPostInfoMap(response.getJSONObject());
            }
        });
        graphRequest.executeAsync();
    }

    public void setPageInSightsGraphRequestList(String pagePostId, String graphPath) {
        GraphRequest graphRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), graphPath, null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                System.out.println(response);
            }
        });
        pageInSightsGraphRequestList.put(pagePostId, graphRequest);
    }

    public List<GraphResponse> getPageInSights() {
        GraphRequest graphRequest = new GraphRequest();
        return graphRequest.executeBatchAndWait(pageInSightsGraphRequestList.values());
    }

    public void post(String graphPath, Bundle bundle) {
        GraphRequest graphRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), graphPath, bundle, HttpMethod.POST, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                if (response.getJSONObject() != null) {
                    Toast.makeText(getApplicationContext(), Constants.shareSuccess, Toast.LENGTH_LONG);
                }
            }
        });
        graphRequest.executeAsync();
    }

    public void getUnpublishedPagePosts() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.fields, Constants.id);
        bundle.putString(Constants.isPublished, Constants.isFalse);
        GraphRequest graphRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), unpublishedGraphPath, bundle, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                /* TODO:Use the fetched unpublished page posts to later post or something */
                Log.d(TAG, response.toString());
            }
        });
        graphRequest.executeAsync();
    }
}