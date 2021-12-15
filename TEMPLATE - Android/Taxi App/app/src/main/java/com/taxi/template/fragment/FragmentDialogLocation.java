package com.taxi.template.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataBuffer;
import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.common.data.Freezable;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.maps.errors.ApiException;
import com.taxi.template.R;
import com.taxi.template.adapter.LocationListAdapter;
import com.taxi.template.data.Tools;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class FragmentDialogLocation extends DialogFragment {

    public CallbackResult callbackResult;
    private String hint = "";

    private GoogleApiClient mGoogleApiClient;
    private LocationListAdapter mAdapter;
    private ItemFilter mFilter = new ItemFilter();
    private ProgressBar progress_bar;
    PlacesClient placesClient;

    public void setOnCallbackResult(final CallbackResult callbackResult) {
        this.callbackResult = callbackResult;
    }

    private int request_code = 0;
    private View root_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_dialog_location, container, false);
        initComponent();

        Tools.setSystemBarColorFragment(getActivity(), this, R.color.grey_soft);
        Tools.setSystemBarLightFragment(this);
        Tools.checkInternetConnection(getActivity());

        return root_view;
    }

    private void initComponent() {
        final ImageView img_clear = (ImageView) root_view.findViewById(R.id.img_clear);
        final EditText et_search = (EditText) root_view.findViewById(R.id.et_search);
        RecyclerView recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerView);
        progress_bar = (ProgressBar) root_view.findViewById(R.id.progress_bar);

        //set data and list adapter
        mAdapter = new LocationListAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        et_search.setHint(hint);

        progress_bar.setVisibility(View.GONE);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = et_search.getText().toString().trim();
                if (!str.equals("")) {
                    img_clear.setVisibility(View.VISIBLE);
                    tes(str);
                    progress_bar.setVisibility(View.VISIBLE);
                } else {
                    img_clear.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mAdapter.setOnItemClickListener(new LocationListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, FindAutocompletePredictionsRequest obj, int position) {
                sendDataResult(obj.getCountry().toString());
                dismissDialog();
            }
        });

        img_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText("");
            }
        });

        ((ImageView) root_view.findViewById(R.id.img_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });

        ((View) root_view.findViewById(R.id.lyt_select_from_map)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Select location from map", Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize Places.
        Places.initialize(getContext(), getString(R.string.google_maps_key));

// Create a new Places client instance.
        placesClient = Places.createClient(getContext());

//        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//                .enableAutoManage(getActivity(), 0, onConnectionFailedListener)
//                .addApi(Places.GEO_DATA_API)
//                .build();

        Tools.hideKeyboardFragment(this);
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<AutocompletePrediction> filterData = new ArrayList<>();
            if (constraint != null) filterData = requestPredictionToGoogle(constraint.toString());
            results.values = filterData;
            if (filterData != null) {
                results.count = filterData.size();
            } else {
                results.count = 0;
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<FindAutocompletePredictionsRequest> items = (ArrayList<FindAutocompletePredictionsRequest>) results.values;
            if (items == null) items = new ArrayList<>();
            mAdapter.setItems(items);
            progress_bar.setVisibility(View.GONE);
        }

    }

    private void tes(String a) {
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
// Create a RectangularBounds object.
//        RectangularBounds bounds = RectangularBounds.newInstance(
//                new LatLng(-33.880490, 151.184363),
//                new LatLng(-33.858754, 151.229596));
// Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
// Call either setLocationBias() OR setLocationRestriction().
//                .setLocationBias(bounds)
                //.setLocationRestriction(bounds)
                .setCountry("id")
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(a)
                .build();

//            PendingResult<AutocompletePredictionBuffer> results;
//            results = Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, keyword.toString(), null, null);
//            AutocompletePredictionBuffer autocompletePredictions = results.await(60, TimeUnit.SECONDS);
//            final Status status = autocompletePredictions.getStatus();
//            if (!status.isSuccess()) {
//                autocompletePredictions.release();
//                Toast.makeText(getActivity(), "Error : " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
//                return null;
//            }

        placesClient.findAutocompletePredictions(request).addOnSuccessListener(new OnSuccessListener<FindAutocompletePredictionsResponse>() {
            @Override
            public void onSuccess(FindAutocompletePredictionsResponse findAutocompletePredictionsResponse) {
                for (AutocompletePrediction prediction : findAutocompletePredictionsResponse.getAutocompletePredictions()) {
                    Log.i("Tes", prediction.getSecondaryText(null).toString());
                    Log.i("Tes", prediction.getFullText(null).toString());
                    Log.i("Tes", prediction.getPrimaryText(null).toString());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    ApiException apiException = (ApiException) e;
                    Log.e("Tes", "Place not found: " + apiException.getMessage());
                }
            }
        });
    }

    private ArrayList<AutocompletePrediction> requestPredictionToGoogle(String keyword) {
        if (mGoogleApiClient.isConnected()) {

            return DataBufferUtils.freezeAndClose((DataBuffer<Freezable<AutocompletePrediction>>) placesClient);
        }
        return null;
    }

    private OnConnectionFailedListener onConnectionFailedListener = new OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT).show();
        }
    };

    private void sendDataResult(String loc) {
        if (callbackResult != null) {
            callbackResult.sendResult(request_code, loc);
        }
    }

    private void dismissDialog() {
        Tools.hideKeyboardFragment(this);
        dismiss();
    }

    @Override
    public int getTheme() {
        return R.style.AppTheme_FullScreenDialog;
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setRequestCode(int request_code) {
        this.request_code = request_code;
    }

    public interface CallbackResult {
        void sendResult(int requestCode, String loc);
    }

}