package santosgagbegnon.com.busstopbattles;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;



public class UsernameSetupActivity extends AppCompatActivity {
    private UsernameSetupActivityModel usernameSetupActivityModel = new UsernameSetupActivityModel(this);
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_username_setup);
        Button signupButton = findViewById(R.id.signupButton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText usernameTextField = (EditText) findViewById(R.id.usernameTextField);
                String username = usernameTextField.getText().toString();

                if(username != null && username.length() != 0){
                    UsernameSetupActivityModel.addNewUser(username, new WasSuccessful() {
                        @Override
                        public void onSuccess() {
                            startActivity(new Intent(UsernameSetupActivity.this,HomeActivity.class));
                        }

                        @Override
                        public void onFailure(){
                            TextView usernameTakenTextView = findViewById(R.id.usernameTakenTextView);
                            usernameTakenTextView.setVisibility(View.VISIBLE);
                        }
                    });


                }




            }
        });
    }
}
