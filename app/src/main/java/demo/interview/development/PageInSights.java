package demo.interview.development;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageInSights extends AppCompatActivity {

    Button btnPageInSights;
    ListView lvPosts;
    String pageId;

    GraphApiMethods graphApiMethods = GraphApiMethods.getInstance();

    private final static String TAG = PageInSights.class.getName();

    private void initializeControls() {
        btnPageInSights = (Button) findViewById(R.id.btnPageInSights);
        lvPosts = (ListView) findViewById(R.id.lvPosts);
    }

    /* TODO: Change list view display data */
    private void loadListView(Map<String, Integer> pagePostViewMap) {
        final List<String> postLists = new ArrayList<>();
        for (String key: pagePostViewMap.keySet()) {
            postLists.add("PagePostId = " + key + "\n " + "View count = " + pagePostViewMap.get(key));
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, postLists);
        lvPosts.setAdapter(arrayAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_in_sights);

        initializeControls();

        pageId = Constants.pageId;
        graphApiMethods.setPageAccessToken(pageId);

        btnPageInSights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final JSONArray jsonArrayPosts = (JSONArray) graphApiMethods.pagePosts.get(Constants.data);
                    String edge = Constants.forwardSlash + Constants.insights + Constants.forwardSlash + Constants.metric;
                    for (int i =0; i < jsonArrayPosts.length(); i++) {
                        JSONObject nextPost = (JSONObject) jsonArrayPosts.get(i);
                        String pagePostId = nextPost.get(Constants.id).toString();
                        String graphPath = Constants.forwardSlash + nextPost.get(Constants.id) + edge;
                        graphApiMethods.setPageInSightsGraphRequestList(pagePostId, graphPath);
                    }
                    new PageInSightAsyncTask().execute(new String());
                } catch (JSONException e) {
                    Log.d(TAG, e.getLocalizedMessage());
                }

            }
        });
    }

    private class PageInSightAsyncTask extends AsyncTask<String, Void, Map<String, Integer>> {
        @Override
        protected Map<String,Integer> doInBackground(String... params) {
            List<GraphResponse> pageInSights = graphApiMethods.getPageInSights();
            Map<String, Integer> pagePostViewMap = new HashMap<>();
            try {
                JSONArray pageInSightsJsonArray;
                for (GraphResponse graphResponse: pageInSights) {
                    pageInSightsJsonArray = (JSONArray) graphResponse.getJSONObject().get(Constants.data);
                    JSONObject jsonObject = (JSONObject) pageInSightsJsonArray.get(0);
                    String id = jsonObject.get(Constants.id).toString();
                    String pagePostId = id.split(Constants.forwardSlash)[0];

                    JSONArray values = (JSONArray) jsonObject.get(Constants.values);
                    JSONObject jsonObjectValue = (JSONObject) values.get(0);
                    int value = Integer.parseInt(jsonObjectValue.get(Constants.value).toString());

                    pagePostViewMap.put(pagePostId, value);
                }
                return pagePostViewMap;
            } catch (Exception jsonException) {
                Log.d(TAG, jsonException.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Map<String, Integer> pagePostViewMap) {
            loadListView(pagePostViewMap);
        }
    }
}