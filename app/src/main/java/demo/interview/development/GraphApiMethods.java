package demo.interview.development;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

public class GraphApiMethods {
    String pageAccessToken = "";
    GraphRequest graphRequest;

    private final static String TAG = GraphApiMethods.class.getName();

    public void setPageAccessToken(String pageId) {
        String graphPath = "/" + pageId;
        Bundle bundle = new Bundle();
        bundle.putString(Constants.fields, Constants.accessToken);
        graphRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), graphPath, bundle, HttpMethod.GET, new GraphRequest.Callback() {
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
}
