package demo.interview.development;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class GraphApiMethods {
    String pageAccessToken = Constants.none;
    JSONObject pagePosts = null;
    private final static String TAG = GraphApiMethods.class.getName();
    Map<String, GraphRequest> pageInSightsGraphRequestList = new HashMap<>();

    private static final GraphApiMethods graphApiMethods = new GraphApiMethods();

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

    public void setPagePosts(String graphPath) {
        GraphRequest graphRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), graphPath, null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                pagePosts = response.getJSONObject();
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
}
