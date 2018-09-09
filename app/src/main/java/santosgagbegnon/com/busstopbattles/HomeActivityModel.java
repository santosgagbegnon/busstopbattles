package santosgagbegnon.com.busstopbattles;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import static android.content.ContentValues.TAG;

public class HomeActivityModel extends AppCompatActivity{

    //Check that user has correct version of Google Play Services
    private static final String TAG = "HomeActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private HomeActivity homeActivity;
    private Boolean LocationPermissionGranted = false;
    private static final int LocationPermission_RequestCode = 9853;


    public HomeActivityModel(HomeActivity homeActivity){
        this.homeActivity = homeActivity;
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: Checking Google Services Version");
        int availability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(homeActivity); //Returns status code indicating whether error occurred. Can use with ConnectionResult.
        if(availability == ConnectionResult.SUCCESS){
            Log.d(TAG, "isServicesOK: Google PLay Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(availability)){
            //Issue with Play Services, but can fix it
            Log.d(TAG, "isServicesOK:Error occurred but is fixable");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(homeActivity, availability, ERROR_DIALOG_REQUEST);
            dialog.show();
        }

        else{
            Toast.makeText(homeActivity, "Cannot make Google Map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};

        if(ContextCompat.checkSelfPermission(homeActivity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            //Checking if fine location permission has been granted
            LocationPermissionGranted = true;
        }
        else{
            ActivityCompat.requestPermissions(homeActivity, permissions, LocationPermission_RequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        LocationPermissionGranted = false;

        switch(requestCode){
            case LocationPermission_RequestCode:{
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    LocationPermissionGranted = true;
                }
            }
        }
    }
}
