package santosgagbegnon.com.busstopbattles;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


import static android.content.ContentValues.TAG;

public class HomeActivityModel extends AppCompatActivity{
    //Check that user has correct version of Google Play Services
    private static final String TAG = "HomeActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final int LocationPermission_RequestCode = 9853;
    private HomeActivity homeActivity;
    private Boolean LocationPermissionGranted = false;
    private FusedLocationProviderClient FusedLocationProviderClient;
    private Location currentLocation;


    public HomeActivityModel(HomeActivity homeActivity){
        this.homeActivity = homeActivity;
    }

    public void homeActivitySetup(){
        getLocationPermission();
        getDeviceLocation();
    }


    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: Getting Devices Current Location...");
        FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(homeActivity);
        try {
            if (LocationPermissionGranted) {
                FusedLocationProviderClient.getLastLocation().addOnSuccessListener(homeActivity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            currentLocation = location;
                            Log.d(TAG, "onComplete: USER LOCATION: " + currentLocation.toString());
                            Toast.makeText(homeActivity, currentLocation.toString(), Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Log.d(TAG, "onComplete: Current location is null");
                            Toast.makeText(homeActivity, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        catch (SecurityException e){
            Log.d(TAG, "getDeviceLocation: Security Exception " + e.getMessage());
        }
    }

    public Location giveCurrentLocation(){
        return currentLocation;
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

    private void getLocationPermission(){
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

    @Override // Keep as public because it is a Super class function
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
