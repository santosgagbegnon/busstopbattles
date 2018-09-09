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

    //Check that user has correct version of Google Play Services
    private static final String TAG = "HomeActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if(isServicesOK()){
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

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: Checking Google Services Version");
        int availability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(HomeActivity.this); //Returns status code indicating whether error occurred. Can use with ConnectionResult.
        if(availability == ConnectionResult.SUCCESS){
            Log.d(TAG, "isServicesOK: Google PLay Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(availability)){
            //Issue with Play Services, but can fix it
            Log.d(TAG, "isServicesOK:Error occurred but is fixable");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(HomeActivity.this, availability, ERROR_DIALOG_REQUEST);
            dialog.show();
            }

        else{
            Toast.makeText(this, "Cannot make Google Map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
