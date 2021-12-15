package com.taxi.template;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import com.taxi.template.data.Tools;
import com.taxi.template.model.Booking;

import java.util.concurrent.TimeUnit;

public class ActivityBookingActiveDetails extends AppCompatActivity {

    private GoogleMap mMap;
    private Polyline polyline;
    private BottomSheetBehavior bottomSheetBehavior;

    public static final String EXTRA_OBJECT = "extra.data.BOOKING_OBJ";

    // give preparation animation activity transition
    public static void navigate(Activity activity, Booking obj) {
        Intent intent = new Intent(activity, ActivityBookingActiveDetails.class);
        intent.putExtra(EXTRA_OBJECT, obj);
        activity.startActivity(intent);
    }

    private Booking booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_active_details);

        // get extra object
        booking = (Booking) getIntent().getSerializableExtra(EXTRA_OBJECT);

        initMapFragment();
        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setCompleteSystemBarLight(this);
    }


    private void initComponent() {
        TextView status = (TextView) findViewById(R.id.status);
        TextView payment = (TextView) findViewById(R.id.payment);
        TextView ride_class = (TextView) findViewById(R.id.ride_class);
        TextView pickup = (TextView) findViewById(R.id.pickup);
        TextView destination = (TextView) findViewById(R.id.destination);
        TextView fare = (TextView) findViewById(R.id.fare);

        LinearLayout llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        final View top_shadow = (View) findViewById(R.id.top_shadow);

        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setHideable(false);

        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    top_shadow.setVisibility(View.GONE);
                } else {
                    top_shadow.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        payment.setText(booking.payment);
        ride_class.setText(booking.ride_class);
        pickup.setText(booking.pickup);
        destination.setText(booking.destination);
        fare.setText(booking.fare);
    }

    private void initMapFragment() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                configureMap(googleMap);
            }
        });
    }

    public void configureMap(GoogleMap googleMap) {
        mMap = Tools.configBasicGoogleMap(googleMap);
        CameraUpdate center = CameraUpdateFactory.newLatLng(new com.google.android.gms.maps.model.LatLng(48.83711709, 2.31855601));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(12.2f);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);

        LatLng origin = new LatLng(48.842948, 2.318795);
        LatLng destination = new LatLng(48.874050, 2.294372);

        displayMarker(origin, true);
        displayMarker(destination, false);

        drawPolyLine(origin, destination);

        Tools.displaySingleCarAroundMarker(this, mMap);
    }

    private void drawPolyLine(LatLng origin, LatLng destination) {
        GeoApiContext context = new GeoApiContext().setApiKey(getString(R.string.google_maps_key));
        context.setConnectTimeout(10, TimeUnit.SECONDS);
        DirectionsApiRequest d = DirectionsApi.newRequest(context);
        d.origin(origin).destination(destination).mode(TravelMode.DRIVING).alternatives(false);
        d.setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                final PolylineOptions polylineOptions = new PolylineOptions().width(8).color(getResources().getColor(R.color.colorAccent)).geodesic(true);
                for (DirectionsRoute d : result.routes) {
                    for (LatLng l : d.overviewPolyline.decodePath()) {
                        polylineOptions.add(new com.google.android.gms.maps.model.LatLng(l.lat, l.lng));
                    }
                }
                // draw polyline
                runOnUiThread(new Runnable() {
                    public void run() {
                        polyline = mMap.addPolyline(polylineOptions);
                    }
                });
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    private void displayMarker(LatLng location, boolean isOrigin) {
        // make current location marker
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new com.google.android.gms.maps.model.LatLng(location.lat, location.lng));

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View marker_view = inflater.inflate(R.layout.maps_marker, null);
        ImageView marker = (ImageView) marker_view.findViewById(R.id.marker);
        marker.setBackgroundResource(isOrigin ? R.drawable.marker_origin : R.drawable.marker_destination);
        if (isOrigin) {
            marker.setImageResource(R.drawable.ic_origin);
        }

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Tools.createBitmapFromView(this, marker_view)));
        mMap.addMarker(markerOptions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_booking_active_details, menu);
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.grey_very_hard));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else {
            Tools.showToastMiddle(getApplicationContext(), item.getTitle() + " clicked");
        }
        return super.onOptionsItemSelected(item);
    }
}
