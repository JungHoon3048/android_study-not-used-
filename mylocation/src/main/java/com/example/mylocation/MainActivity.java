package com.example.mylocation;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends Activity {

    //----------------------------------------------------------------------
    // Views

    private EditText editTextLatitude = null;
    private EditText editTextLongitude = null;
    private Spinner spinnerLocationProvider = null;

    //----------------------------------------------------------------------
    // Fields regarding location

    private String locationProvider = null;
    private Location lastKnownLocation = null;
    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

            // Get the last location, and update UI.
            lastKnownLocation = location;
            editTextLatitude.setText("" + lastKnownLocation.getLatitude());
            editTextLongitude.setText("" + lastKnownLocation.getLongitude());

            // Stop the update to prevent changing the location.
            lm.removeUpdates(this);
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

    };
    private View.OnClickListener buttonRefreshClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            locationProvider = ((String)spinnerLocationProvider.getSelectedItem()).toLowerCase();

            // Update location to get.
            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            lm.removeUpdates(locationListener);    // Stop the update if it is in progress.
            lm.requestLocationUpdates(locationProvider, 0, 0, locationListener);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Access views.
        editTextLatitude = (EditText)findViewById(R.id.editTextLatitude);
        editTextLongitude = (EditText)findViewById(R.id.editTextLongitude);
        spinnerLocationProvider = (Spinner)findViewById(R.id.spinnerLocationProvider);
        Button buttonGetLocation = (Button)findViewById(R.id.buttonGetLocation);

        // Set listeners to view(s).
        buttonGetLocation.setOnClickListener(buttonRefreshClickListener);

        // Set views.
        String[] locationProviders = {"GPS", "Network"};
        ArrayAdapter<CharSequence> adapterOfSpinnerLocationProvider = new ArrayAdapter<CharSequence>(
                this,
                android.R.layout.simple_spinner_item,
                locationProviders
        );
        adapterOfSpinnerLocationProvider.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocationProvider.setAdapter(adapterOfSpinnerLocationProvider);
    }

}
