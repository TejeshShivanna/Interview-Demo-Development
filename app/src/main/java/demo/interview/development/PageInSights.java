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
import org.json.JSONObject;

import java.util.ArrayList;
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
    private void loadListView(Map<String, PostInfo> pagePostViewMap) {
        final List<String> postLists = new ArrayList<>();
        for (String key: pagePostViewMap.keySet()) {
            postLists.add(Constants.postMessage + " = " + pagePostViewMap.get(key).getMessage() + "\n " + Constants.postReach + " = " + pagePostViewMap.get(key).getReach());
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
                String edge = Constants.forwardSlash + Constants.insights + Constants.forwardSlash + Constants.metric;
                for (String pagePostId : graphApiMethods.getPostInfoMap().keySet()) {
                    String graphPath = Constants.forwardSlash + pagePostId + edge;
                    graphApiMethods.setPageInSightsGraphRequestList(pagePostId, graphPath);
                }
                new PageInSightAsyncTask().execute(new String());
            }
        });
    }

    private class PageInSightAsyncTask extends AsyncTask<String, Void, Map<String, PostInfo>> {
        @Override
        protected Map<String,PostInfo> doInBackground(String... params) {
            List<GraphResponse> pageInSights = graphApiMethods.getPageInSights();
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

                    graphApiMethods.getPostInfoMap().get(pagePostId).setReach(value);
                }
                return graphApiMethods.getPostInfoMap();
            } catch (Exception jsonException) {
                Log.d(TAG, jsonException.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Map<String, PostInfo> pagePostViewMap) {
            loadListView(pagePostViewMap);
        }
    }
}