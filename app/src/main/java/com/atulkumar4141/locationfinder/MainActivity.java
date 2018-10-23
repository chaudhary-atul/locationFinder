package com.atulkumar4141.locationfinder;

import android.Manifest;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.Geofence;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

import io.nlopez.smartlocation.OnActivityUpdatedListener;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.geofencing.model.GeofenceModel;
import io.nlopez.smartlocation.location.config.LocationParams;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesWithFallbackProvider;
import io.nlopez.smartlocation.rx.ObservableFactory;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements OnLocationUpdatedListener, OnActivityUpdatedListener {
    // LocationProvider locationProvider;
    private double latitude, longitude;
    private TextView tvLocation;
    private Button btnGoToMap;
    private LocationGooglePlayServicesWithFallbackProvider provider;
    private Disposable disposable;
    private LocationCoordinate locationCoordinate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvLocation = findViewById(R.id.tvLocation);
        btnGoToMap = findViewById(R.id.btnGoToMap);
        getLocation();
        startLocation();
        btnGoToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("location", locationCoordinate);
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra("bundleExtra", bundle);
                startActivity(intent);
            }
        });
    }

    private void startLocation() {
        disposable = ObservableFactory.from(SmartLocation.with(this).location().oneFix())
                .subscribeOn(Schedulers.newThread())
                .subscribe(location -> {
                    locationCoordinate = new LocationCoordinate(location.getLatitude(), location.getLongitude());
                }, throwable -> {
                    Log.d("Exception", throwable.getLocalizedMessage());
                });

    }


    public void getLocation() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        startLocation();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }

    @Override
    public void onLocationUpdated(Location location) {
        showLocation(location);
    }

    private void showLocation(final Location location) {
        if (location != null) {
            Log.d("LocationText", location.toString());

            // We are going to get the address for the current position
            SmartLocation.with(this).geocoding().reverse(location, new OnReverseGeocodingListener() {
                @Override
                public void onAddressResolved(Location original, List<Address> results) {
                    if (results.size() > 0) {
                        Address result = results.get(0);
                        StringBuilder builder = new StringBuilder(location.toString());
                        builder.append("\n[Reverse Geocoding] ");
                        List<String> addressElements = new ArrayList<>();
                        for (int i = 0; i <= result.getMaxAddressLineIndex(); i++) {
                            addressElements.add(result.getAddressLine(i));
                        }
                        builder.append(TextUtils.join(", ", addressElements));
                        tvLocation.setText(builder.toString());
                        // Log.d("NewLocationString",builder.toString());
                    }
                }
            });
        } else {
            Log.d("Null location", "YES");
        }
    }

    @Override
    public void onActivityUpdated(DetectedActivity detectedActivity) {
        showActivity(detectedActivity);
    }

    private void showActivity(DetectedActivity detectedActivity) {
        if (detectedActivity != null) {
            Log.d("DetectedActivity", String.format("Activity %s with %d%% confidence",
                    getNameFromType(detectedActivity),
                    detectedActivity.getConfidence()));
        } else {
            Log.d("Null activity ", "YES");
        }
    }

    private String getNameFromType(DetectedActivity activityType) {
        switch (activityType.getType()) {
            case DetectedActivity.IN_VEHICLE:
                return "in_vehicle";
            case DetectedActivity.ON_BICYCLE:
                return "on_bicycle";
            case DetectedActivity.ON_FOOT:
                return "on_foot";
            case DetectedActivity.STILL:
                return "still";
            case DetectedActivity.TILTING:
                return "tilting";
            default:
                return "unknown";
        }
    }

    @Override
    protected void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }
}
