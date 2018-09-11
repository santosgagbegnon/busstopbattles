package santosgagbegnon.com.busstopbattles;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;



public class UsernameSetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_username_setup);
        Button signupButton = findViewById(R.id.signupButton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText usernameTextField = findViewById(R.id.usernameTextField);
                String username = usernameTextField.getText().toString();
                final TextView usernameTakenTextView = findViewById(R.id.usernameTakenTextView);
                UsernameSetupActivityModel.addNewUser(username, new UsernameCreationListener() {
                    @Override
                    public void onSuccess() {
                        startActivity(new Intent(UsernameSetupActivity.this,HomeActivity.class));
                    }

                    @Override
                    public void onUsernameTakenFailure(){
                        usernameTakenTextView.setText("That username is taken!");
                        usernameTakenTextView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onUsernameTooShortFailure() {
                        usernameTakenTextView.setText("Username must be at least 3 characters");
                        usernameTakenTextView.setVisibility(View.VISIBLE);

                    }
                    @Override
                    public void onIllegalCharacterFailure() {
                        usernameTakenTextView.setText("Illegal character! Only letters, numbers, _ and . are allowed");
                        usernameTakenTextView.setVisibility(View.VISIBLE);
                    }
                });







            }
        });
    }
}
