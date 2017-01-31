package com.eventmeapp.eventmeapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.widget.Toast.*;


/**
 * Created by Erik Fok on 10/24/2016.
 */
public class TabMap extends Fragment implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks,
                                      GoogleApiClient.OnConnectionFailedListener, LocationListener,
        GoogleMap.OnInfoWindowClickListener {
    /*Macros*/
    public static final float ORIG_ZOOM = 15.44f;
    public static final float PINK_HUE = 340f;
    public static final float BLUE_HUE = 188f;
    public static final LatLng UMD = new LatLng(38.9860252, -76.94243789);
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static LatLng LatLngToUse;
    GoogleMap mMap;
    MapView mMapview;
    View mView;
    UiSettings mUiSettings;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    CameraPosition mOrigin;
    MarkerOptions mMarkerOptionsToday, mMarkerOptionsNotToday;
    //protected HashMap<Marker, InfoEvent> eventMarkerMap;
    ImageView MarkerHeader;


    public TabMap() {

    }

    static public LatLng getLatLng() {return LatLngToUse;}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //inflate layout for this project
        mView = inflater.inflate(R.layout.frag_map, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*Requests user for location services*/
        checkLocationPermission();

        /*Builds API, which allows us to find information about the user*/
        buildGoogleApiClient();


        mMapview = (MapView) mView.findViewById(R.id.map);
        if (mMapview != null) {
            mMapview.onCreate(null);
            mMapview.onResume();
            mMapview.getMapAsync(this);
        }


    }

    @Override
    public  void onMapReady(GoogleMap googleMap){

        MapsInitializer.initialize(getContext());
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mUiSettings = googleMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true); //Enables Zoom
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);
        mUiSettings.setMapToolbarEnabled(false);

        mMarkerOptionsToday = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(PINK_HUE));
        mMarkerOptionsNotToday = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BLUE_HUE));

        Marker mckeldinMarker = mMap.addMarker(mMarkerOptionsToday.position(UMD).title("Puppy Showcase")
                .snippet("Today - 10:30 AM"));

        Marker notTodayExample = mMap.addMarker(mMarkerOptionsNotToday.position(new LatLng(38.9860252, -76.9423)).title("UMD Meditation Club")
                .snippet("Jan 25 - 4:30 PM"));

        setUpEventSpots(); //Initializes Event Markers

        mOrigin = CameraPosition.builder().target(UMD).zoom(ORIG_ZOOM).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(mOrigin));




        /*Create Event Marker*/
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {
                Intent mainIntent = new Intent(getActivity(), CreateEvent.class);
                LatLngToUse = point;
                startActivity(mainIntent);
            }

        });
        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);

        // Set a listener for info window events.
        mMap.setOnInfoWindowClickListener(this);


    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    /*********** GoogleApiClient.ConnectionCallbacks *******************************/
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                                                == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation == null) {
                makeText(getContext(), "Oops! There was a problem retrieving your location.", LENGTH_LONG).show();
            } else {
                /*TODO Activate with Non-Emulator*/
                /*mOrigin = CameraPosition.builder().target(new LatLng(mLastLocation.getLatitude(),
                                  mLastLocation.getLongitude())).zoom(ORIG_ZOOM).tilt(45).build();
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mOrigin));
                mMap.setMyLocationEnabled(true); //Enables MyLocation button on TabMap*/
            }

        }



    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    /*********** GoogleApiClient.OnConnectionFailedListener ***********************/
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /*********** LocationListener *************************************************/
    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;

        //move map camera
        //Toast.makeText(getContext(), "Reached Move Camera", Toast.LENGTH_SHORT).show();
        mOrigin = CameraPosition.builder().target(new LatLng(mLastLocation.getLatitude(),
                mLastLocation.getLongitude())).zoom(ORIG_ZOOM).tilt(45).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mOrigin));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }


    /*********** Run-Time Permissions *************************************************/

    /*Checks to see if we have permission to see user location, if not prompts request*/
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                                                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    /*Returns result of dialog box requesting to access users location*/
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                                                                        == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);

                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    makeText(getContext(), "Disabling Location Services Disables the TabMap Feature!", LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            //You can add here other case statements according to your requirement.
        }

    }

    /*********************** Markers ******************************************/

   /* public Marker placeMarker() {

        Marker newMarker  = mMap.addMarker(new MarkerOptions()
                                    .position(eventInfo.getLatLng())
                                    .title(eventInfo.getEventTitle())
                                    .snippet(eventInfo.getLocation()));
        return newMarker;

    }
*/
    public void setUpEventSpots() {

    }




    @Override
    public boolean onMarkerClick(Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(getActivity() , "Your Message", Toast.LENGTH_LONG).show();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }
    @Override
    public void onInfoWindowClick(Marker marker) {
        Message mesg = new Message();
        Bundle b = new Bundle();
        b.putString("title", marker.getTitle());
        mesg.setData(b);
        handler.sendMessage(mesg);

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            Intent in = new Intent(getActivity(),EventPage.class);
            in.putExtra("title", b.getString("title"));
            TabMap.this.startActivity(in);
        }
    };

}

