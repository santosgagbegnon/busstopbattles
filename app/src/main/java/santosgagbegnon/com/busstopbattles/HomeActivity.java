package santosgagbegnon.com.busstopbattles;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability; //Needed to import for GoogleApiAvailability
import com.google.android.gms.common.ConnectionResult;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        HomeActivityModel homeActivityModel = new HomeActivityModel(this);
        if(homeActivityModel.isServicesOK()){
            homeActivityModel.getLocationPermission();
            init();
        }
    }

    private void init(){
        Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SearchingActivity.class));
            }
        });
    }
}
