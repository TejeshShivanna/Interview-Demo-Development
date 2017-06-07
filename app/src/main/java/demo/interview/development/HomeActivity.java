package demo.interview.development;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    TextView tvWelcome, tvField;
    ProfilePictureView profilePictureView;
    Spinner spinnerShareType;
    Button btnInsights;
    GraphApiMethods graphApiMethods = GraphApiMethods.getInstance();
    String shareType = Constants.none;

    static final String pagePostsGraphPath = Constants.forwardSlash + Constants.pageId + Constants.forwardSlash + Constants.posts + Constants.postFields;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        shareType = parent.getItemAtPosition(position).toString();
        Intent nextIntent;
        switch (shareType) {
            case Constants.messageAndLink:
                nextIntent = new Intent(this, ShareTextAndLink.class);
                startActivity(nextIntent);
                break;
            case Constants.photo:
                nextIntent = new Intent(this, SharePhoto.class);
                startActivity(nextIntent);
                break;
            case Constants.video:
                /* TODO*/
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        shareType = Constants.none;
    }

    void setNameAndProfilePhoto() {
        Profile profile= Profile.getCurrentProfile();
        if (profile != null) {
            tvWelcome.setText(tvWelcome.getText() + " " + profile.getName());
            profilePictureView.setProfileId(profile.getId());

            tvWelcome.setVisibility(View.VISIBLE);
            profilePictureView.setVisibility(View.VISIBLE);
        }
        else {
            Toast.makeText(HomeActivity.this, Constants.profileNotLoaded, Toast.LENGTH_LONG).show();
            tvField.setVisibility(View.INVISIBLE);
            profilePictureView.setVisibility(View.INVISIBLE);
        }
    }

    private void initializeControls() {
        tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        tvField = (TextView) findViewById(R.id.tvField);
        profilePictureView = (ProfilePictureView) findViewById(R.id.profilePhoto);
        btnInsights = (Button) findViewById(R.id.btnInsights);

        spinnerShareType = (Spinner) findViewById(R.id.spinnerShareType);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.share_type, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerShareType.setAdapter(arrayAdapter);
        spinnerShareType.setOnItemSelectedListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initializeControls();

        if (AccessToken.getCurrentAccessToken() != null) {
            setNameAndProfilePhoto();
        }

        btnInsights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphApiMethods.setPagePosts(pagePostsGraphPath);
                Intent pageInsightsIntent = new Intent(HomeActivity.this, PageInSights.class);
                startActivity(pageInsightsIntent);
            }
        });
    }
}
