package demo.interview.development;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PostDescription extends AppCompatActivity {

    TextView tvPostInfoHeader, tvPostDescription;
    Button btnPublishPost;

    String pagePostId;

    GraphApiMethods graphApiMethods = GraphApiMethods.getInstance();

    private void initializeControls() {
        tvPostInfoHeader = (TextView) findViewById(R.id.tvPostInfoHeader);
        tvPostDescription = (TextView) findViewById(R.id.tvPostDescription);
        btnPublishPost = (Button) findViewById(R.id.btnPublishPost);
    }

    private void setTextViewDescription(PostInfo postInfo) {
        StringBuilder postDescription = new StringBuilder();
        if (postInfo.getMessage() != null) postDescription.append(Constants.postInfoMessage + " : " + postInfo.getMessage() + "\n");
        if (postInfo.getLink() != null) postDescription.append(Constants.postInfoLink + " : " + postInfo.getLink() + "\n");
        if (postInfo.getDescription() != null) postDescription.append(Constants.postInfoDescription + " : " + postInfo.getDescription() + "\n");
        if (postInfo.getCreatedTime() != null) postDescription.append(Constants.postInfoCreatedTime + " : " + postInfo.getCreatedTime() + "\n");
        if (postInfo.getName() != null) postDescription.append(Constants.postInfoName + " : " + postInfo.getName() + "\n");

        String isPublished = postInfo.getIsPublished() == true? "Yes":"No";
        postDescription.append(Constants.postInfoIsPublished + " : " + isPublished);

        tvPostDescription.setText(postDescription.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_description);

        initializeControls();

        pagePostId = getIntent().getStringExtra(Constants.id);

        PostInfo postInfo = graphApiMethods.getPostInfoMap().get(pagePostId);
        if (postInfo != null) {
            setTextViewDescription(postInfo);

            if (!postInfo.getIsPublished()) {
                btnPublishPost.setVisibility(View.VISIBLE);
            }
        }

        btnPublishPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphApiMethods.postUnPublishedPagePosts(pagePostId);
                btnPublishPost.setVisibility(View.INVISIBLE);
            }
        });
    }
}