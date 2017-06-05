package demo.interview.development;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {

    TextView tvMessage;
    ProfilePictureView profilePhoto;
    EditText etMessage;
    Button btnShareMessage;
    static final String TAG = HomeActivity.class.getName();
    static final  String myself = "/me/feed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvMessage = (TextView) findViewById(R.id.tvMessage);
        profilePhoto = (ProfilePictureView) findViewById(R.id.profilePhoto);
        etMessage = (EditText) findViewById(R.id.etMessage);
        btnShareMessage = (Button) findViewById(R.id.btnShareMessage);

        if (AccessToken.getCurrentAccessToken() != null) {
            Profile profile= Profile.getCurrentProfile();
            if (profile != null) {
                tvMessage.setText(profile.getName());
                profilePhoto.setProfileId(profile.getId());
            }
            else {
                Toast.makeText(HomeActivity.this, "Profile not loaded", Toast.LENGTH_LONG).show();
            }
        }

        btnShareMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken != null) {
                    String message = etMessage.getText().toString();
                    Bundle bundle = new Bundle();
                    bundle.putString("message", message);

                    GraphRequest graphRequest = new GraphRequest(accessToken, myself, bundle, HttpMethod.POST, new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse response) {
                            try {
                                JSONObject jsonObject = response.getJSONObject();
                                if (jsonObject != null) {
                                    Toast.makeText(getApplicationContext(), jsonObject.get("id").toString() , Toast.LENGTH_LONG);
                                }
                            }
                            catch (JSONException ex) {
                                Log.e(TAG, ex.getLocalizedMessage());
                            }
                        }
                    });

                    graphRequest.executeAsync();
                }
            }
        });
    }
}
