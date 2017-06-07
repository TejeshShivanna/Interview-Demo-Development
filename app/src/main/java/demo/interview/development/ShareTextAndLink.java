package demo.interview.development;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ShareTextAndLink extends AppCompatActivity {

    TextView tvMessage, tvLink;
    EditText etMessage, etLink;
    Button btnShareMessageAndLink, btnShareUnPublishedMessageAndLink;

    GraphApiMethods graphApiMethods = GraphApiMethods.getInstance();
    private static final String graphPath = Constants.forwardSlash + Constants.pageId + Constants.forwardSlash + Constants.feed;

    private void initializeControls() {
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        tvLink = (TextView) findViewById(R.id.tvLink);
        etMessage = (EditText) findViewById(R.id.etMessage);
        etLink = (EditText) findViewById(R.id.etLink);
        btnShareMessageAndLink = (Button) findViewById(R.id.btnShareMessageAndLink);
        btnShareUnPublishedMessageAndLink = (Button) findViewById(R.id.btnShareUnPublishedMessageAndLink);
    }

    private void clearFields() {
        etMessage.setText(Constants.emptyField);
        etLink.setText(Constants.emptyField);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_text_and_link);
        initializeControls();

        graphApiMethods.setPageAccessToken(Constants.pageId);

        btnShareMessageAndLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Parameters */
                Bundle bundle = new Bundle();
                bundle.putString(Constants.message, etMessage.getText().toString());
                bundle.putString(Constants.link, etLink.getText().toString());
                bundle.putString(Constants.accessToken, graphApiMethods.pageAccessToken);

                graphApiMethods.post(graphPath, bundle, true);
                clearFields();
            }
        });

        btnShareUnPublishedMessageAndLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Parameters */
                Bundle bundle = new Bundle();
                bundle.putString(Constants.message, etMessage.getText().toString());
                bundle.putString(Constants.link, etLink.getText().toString());
                bundle.putString(Constants.published, Constants.isFalse);
                bundle.putString(Constants.accessToken, graphApiMethods.pageAccessToken);

                graphApiMethods.post(graphPath, bundle, false);
                clearFields();
            }
        });
    }
}
