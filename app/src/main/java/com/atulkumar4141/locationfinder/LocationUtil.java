package com.atulkumar4141.locationfinder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import static android.content.Context.LOCATION_SERVICE;

public class LocationUtil {

    public static final int ENABLE_GPS_REQUEST_CODE = 6;

    private Activity activity;
    private LocationCallback locationListener;
    private GoogleApiClient googleApiClient;

    public static LocationUtil getInstance(Activity activity, LocationCallback locationListener) {
        return new LocationUtil(activity, locationListener);
    }

    private LocationUtil(Activity activity, LocationCallback locationListener) {
        this.activity = activity;
        this.locationListener = locationListener;
    }

   /* public void getLocation() {
        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        if (checkGpsEnabled(activity)) {
                            if (checkPlayServices(activity)) {
                                buildGoogleApiClient();
                            } else {
                               // getLocationViaLocationManager();
                            }
                        } else {
                            showEnableGPSDialog();
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        //showLocationPermissionPopup();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }*/
/*
    @SuppressLint("MissingPermission")
    private void getLocationViaLocationManager() {
        LocationManager locationManager1 = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
        if (locationManager1 != null) {
            Location locationGPS = locationManager1.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationNet = locationManager1.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (locationGPS != null) {
                locationListener.locationFound(locationGPS);
            } else if (locationNet != null) {
                locationListener.locationFound(locationNet);
            } else {
                locationListener.locationFailed();
            }
        }
    }*/


  /*  @SuppressLint("MissingPermission")
    public void requestSingleUpdate() {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isNetworkEnabled) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
       *//*    locationManager.requestSingleUpdate(criteria, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    locationListener.onNewLocationAvailable(new GPSCoordinates(location.getLatitude(), location.getLongitude()));
                }
            }, null);*//*


        } else {
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSEnabled) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                locationManager.requestSingleUpdate(criteria, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        callback.onNewLocationAvailable(new GPSCoordinates(location.getLatitude(), location.getLongitude()));
                    }

                    @Override public void onStatusChanged(String provider, int status, Bundle extras) { }
                    @Override public void onProviderEnabled(String provider) { }
                    @Override public void onProviderDisabled(String provider) { }
                }, null);
            }
        }
    }*/
/*
    @SuppressLint("MissingPermission")
    private void buildGoogleApiClient() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        googleApiClient = null;
        googleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Location mLastLocation = LocationServices.FusedLocationApi
                                .getLastLocation(googleApiClient);
                        if (mLastLocation != null) {
                            locationListener.locationFound(mLastLocation);
                        } else {
                            locationListener.locationFailed();
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.i("Tag", "Connection failed: ConnectionResult.getErrorCode() = "
                                + connectionResult.getErrorCode());
                    }
                })
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }*/
/*
    public void gpsActivityResult() {
        LocationManager locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
        if (locationManager != null && (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
            getLocation();
        }
    }*/

    private void showEnableGPSDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("GPS Not enabled");
        dialog.setMessage("Please enable GPS so that we can show posts nearest to you.");
        dialog.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivityForResult(myIntent, ENABLE_GPS_REQUEST_CODE);
                paramDialogInterface.dismiss();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                paramDialogInterface.dismiss();
               // locationListener.locationFailed();
            }
        });
        dialog.show();
    }

       /* private void showLocationPermissionPopup() {
            final Dialog dialog = new Dialog(activity);
            LayoutInflater inflater = activity.getLayoutInflater();
            View dialogLayout = inflater.inflate(R.layout.logout_dialog_layout, null);
            // dialog.getWindow().getAttributes().windowAnimations = R.style.custom_dialog_style;
            TextView titleText = (CustomTextView) dialogLayout.findViewById(R.id.tvPopUpTitleText);
            titleText.setText("Required Permission");
            CustomTextView tvDescription = (CustomTextView) dialogLayout.findViewById(R.id.tvPopUpTitleDescription);
            tvDescription.setText("Please allow us to access your location so we can provide you better services");
            CustomTextView cancel = (CustomTextView) dialogLayout.findViewById(R.id.tvSignOutNo);
            cancel.setText("Cancel");
            CustomTextView allow = (CustomTextView) dialogLayout.findViewById(R.id.tvSignOutYes);
            allow.setText("Allow");

            allow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getLocation();
                    dialog.dismiss();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    locationListener.locationFailed();
                }
            });

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(dialogLayout);
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }*/

 /*   public interface LocationListener {

        void locationSearchStarted();

        void locationFound(Location location);

        void locationFailed();
    }*/


    public static boolean checkGpsEnabled(Context context) {
        boolean gps_enabled, network_enabled;
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps_enabled || network_enabled;
    }

    public static boolean checkPlayServices(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }


    // consider returning Location instead of this dummy wrapper class
    public static class GPSCoordinates {
        public float longitude = -1;
        public float latitude = -1;

        public GPSCoordinates(float theLatitude, float theLongitude) {
            longitude = theLongitude;
            latitude = theLatitude;
        }

        public GPSCoordinates(double theLatitude, double theLongitude) {
            longitude = (float) theLongitude;
            latitude = (float) theLatitude;
        }
    }


    public static interface LocationCallback {
        public void onNewLocationAvailable(GPSCoordinates location);
    }


}
