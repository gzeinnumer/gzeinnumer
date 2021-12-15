package com.app.traveling.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.traveling.ActivityBookingFlightDetails;
import com.app.traveling.R;
import com.app.traveling.adapter.FlightBookingListAdapter;
import com.app.traveling.data.GlobalVariable;
import com.app.traveling.model.FlightBooking;

import java.util.List;

public class FragmentFlightBooking extends Fragment {

    private View root_view;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_flight_booking, container, false);
        initComponent();
        return root_view;
    }

    private void initComponent() {
        recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        List<FlightBooking> flightBookings = GlobalVariable.getInstance().getFlightBookings();
        FlightBookingListAdapter mAdapter = new FlightBookingListAdapter(getActivity(), flightBookings);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new FlightBookingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, FlightBooking obj, int position) {
                ActivityBookingFlightDetails.navigate(getActivity(), obj);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
