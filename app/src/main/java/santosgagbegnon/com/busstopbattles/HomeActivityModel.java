package santosgagbegnon.com.busstopbattles;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class HomeActivityModel extends AppCompatActivity{
    //Check that user has correct version of Google Play Services
    private static final String TAG = "HomeActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final int LocationPermission_RequestCode = 9853;
    private static final String GOOGLE_PLACES_API_KEY = "AIzaSyD_4UI50bC-mi8x_wilemQ40IAq4yObIas";
    private HomeActivity homeActivity;
    private Boolean LocationPermissionGranted = false;
    private FusedLocationProviderClient FusedLocationProviderClient;
    private Location currentLocation;
    private String address;
    private double latitude_coord;
    private double longitude_coord;
    private Context homeActivityContext;

    public HomeActivityModel(HomeActivity homeActivity){
        this.homeActivity = homeActivity;
        this.homeActivityContext = homeActivity;
    }

    public void homeActivitySetup(){
        getLocationPermission();
        getDeviceLocation(new LocationDeterminedListener() {
            @Override
            public void onLocationDetermined() {
                new GetNearbyBusStopsTask().execute(currentLocation);
            }
        });
    }

    //Interface/method part that used to be inside getDeviceLocation method call
    /* new DeviceLocationSuccessfullyRecieved(){
            @Override
            public void getLatitudeandLongitude() {
                try{
                    latitude_coord = currentLocation.getLatitude();
                    longitude_coord = currentLocation.getLongitude();
                }
                catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
            }
        }

        */



    private void updateHomeActivityText(CharSequence homeMessage){
        TextView activity_home_message = ((Activity)homeActivityContext).findViewById(R.id.activity_home_message);
        activity_home_message.setText(homeMessage);
    }

    private void getAddress(double latitude, double longitude){
        Geocoder geocoder = new Geocoder(homeActivity, Locale.getDefault() );
        try{
            Log.d(TAG, "getAddress: Longitude " + longitude_coord);
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address obj = addresses.get(0);
            address = "";
            address += obj.getAddressLine(0);
            Log.d(TAG, "getAddress: ADDRESS!!!! " + address);
        }
        catch (IOException e){
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void getDeviceLocation(final LocationDeterminedListener anonFunc){
        Log.d(TAG, "getDeviceLocation: Getting Devices Current Location...");
        FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(homeActivity);
        try {
            if (LocationPermissionGranted) {
                FusedLocationProviderClient.getLastLocation().addOnSuccessListener(homeActivity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            currentLocation = location;
                            anonFunc.onLocationDetermined();
                            Log.d(TAG, "onComplete: USER LOCATION: " + currentLocation.toString());
                            Toast.makeText(homeActivity, currentLocation.toString(), Toast.LENGTH_SHORT).show();

                            Log.d(TAG, "onComplete: !!!!USER LOCATION: " + currentLocation.toString());
                            double longitude = currentLocation.getLongitude();
                            double latitude = currentLocation.getLatitude();
                            getAddress(latitude, longitude);
                            //Toast.makeText(homeActivity, currentLocation.toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(homeActivity, address, Toast.LENGTH_SHORT).show();
                            updateHomeActivityText(address);
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
    private static String generateNearbyBusStopsParams(String location, String radius,String rankBy, String type){
        if (location == null || radius == null || (rankBy != null && type == null)){
            return "";
        }

        String params = "location={location}&radius={radius}&rankBy={rankBy}&type={type}&key="+GOOGLE_PLACES_API_KEY;
        params = params.replace("{location}", location).replace("{radius}", radius).replace("{rankBy}", rankBy).replace("{type}", type);

        Log.d(TAG, "PARAMS" +params);

        return params;
    }

    private class GetNearbyBusStopsTask extends AsyncTask<Location,Void,JSONObject>{
        protected JSONObject doInBackground(Location...locations){
            JSONObject jsonResponse = null;
            final String latitudeAndLongitude = locations[0].getLatitude()+","+locations[0].getLongitude();
            String baseURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
            String params = generateNearbyBusStopsParams(latitudeAndLongitude,"800","distance","bus_station");
            if (params == null){
                return null;
            }
            String requestURL = baseURL + params;
            Log.d(TAG,"URL: " + requestURL);
            try {
                URL nearbyBusStopsRequestURL = new URL(requestURL);
                HttpURLConnection connection = (HttpURLConnection) nearbyBusStopsRequestURL.openConnection();
                connection.setRequestMethod("GET");

                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    BufferedReader bufferReader = new BufferedReader((new InputStreamReader(connection.getInputStream())));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while((inputLine = bufferReader.readLine()) != null){
                        response.append(inputLine);
                    }
                    bufferReader.close();
                    jsonResponse = new JSONObject(response.toString());
                    Log.d(TAG, response.toString());
                }
            }
            catch (MalformedURLException e) {
                Log.d(TAG, "Error creating base URL: "+e.toString());
            }
            catch (IOException e){
                Log.d(TAG, "Error creating connection: "+e.toString());

            }
            catch (JSONException e){
                Log.d(TAG, "Error creating JSON: "+e.toString());

            }
            return jsonResponse;
        }

        protected void onPostExecute(JSONObject results) {
            //Once complete, tell the UI to update
            try {
                String busStopName = results.getJSONArray("results").getJSONObject(0).getString("name");
                String busStopID = results.getJSONArray("results").getJSONObject(0).getString("id");
                HomeActivityModel.this.homeActivity.userNearestBusStopName = busStopName;
                HomeActivityModel.this.homeActivity.userNearestBusStopID = busStopID;
                HomeActivityModel.this.homeActivity.updateLocation();
            }
            catch  (JSONException e) {
                Log.d(TAG, "Error creating bus stop name: "+e.toString());

            }
        }
    }
}
