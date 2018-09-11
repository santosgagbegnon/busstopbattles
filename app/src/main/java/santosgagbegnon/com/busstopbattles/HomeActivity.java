package santosgagbegnon.com.busstopbattles;

//import android.app.Dialog; unused
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.util.Log; unused
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
//import android.widget.Toast; unused

import com.google.android.gms.common.GoogleApiAvailability; //Needed to import for GoogleApiAvailability
import com.google.android.gms.common.ConnectionResult;

public class HomeActivity extends AppCompatActivity {
    private HomeActivityModel homeActivityModel = new HomeActivityModel(this);
    private TextView messageTextView;
    private Button playButton;
    public String userNearestBusStopID ="";
    public String userNearestBusStopName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        messageTextView = findViewById(R.id.activity_home_message);
        if(homeActivityModel.isServicesOK()){
            homeActivityModel.homeActivitySetup();
            init();
        }
    }


    private void init(){
        playButton = findViewById(R.id.playButton);
        playButton.setEnabled(false);
        playButton.setBackgroundColor(Color.GRAY);
        playButton.setAlpha(0.5f);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userNearestBusStopID != "" && userNearestBusStopID == null){
                    startActivity(new Intent(HomeActivity.this, SearchingActivity.class));
                }
            }
        });
    }
    public void updateLocation(){
        messageTextView.setText(userNearestBusStopName);
        playButton.setEnabled(true);
        playButton.setBackgroundColor(Color.GREEN);
        playButton.setAlpha(1);
    }
}
