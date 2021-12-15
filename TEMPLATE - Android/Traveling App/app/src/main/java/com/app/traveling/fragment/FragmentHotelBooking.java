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
import com.app.traveling.ActivityBookingHotelDetails;
import com.app.traveling.R;
import com.app.traveling.adapter.FlightBookingListAdapter;
import com.app.traveling.adapter.HotelBookingListAdapter;
import com.app.traveling.data.GlobalVariable;
import com.app.traveling.model.FlightBooking;
import com.app.traveling.model.HotelBooking;

import java.util.List;

public class FragmentHotelBooking extends Fragment {

    private View root_view;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_hotel_booking, container, false);
        initComponent();
        return root_view;
    }

    private void initComponent() {
        recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        List<HotelBooking> hotelBookings = GlobalVariable.getInstance().getHotelBookings();
        HotelBookingListAdapter mAdapter = new HotelBookingListAdapter(getActivity(), hotelBookings);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new HotelBookingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, HotelBooking obj, int position) {
                ActivityBookingHotelDetails.navigate(getActivity(), obj);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
