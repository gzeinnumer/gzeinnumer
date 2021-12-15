package com.taxi.template.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taxi.template.ActivityBookingHistoryDetails;
import com.taxi.template.R;
import com.taxi.template.adapter.BookingListAdapter;
import com.taxi.template.data.Constant;
import com.taxi.template.model.Booking;

import java.util.List;

public class FragmentBookingHistory extends Fragment {

    private View root_view;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_booking_history, container, false);
        initComponent();
        return root_view;
    }

    private void initComponent() {
        recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        List<Booking> bookingList = Constant.getBookingHistory(getActivity());
        BookingListAdapter mAdapter = new BookingListAdapter(getActivity(), bookingList);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new BookingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Booking obj, int position) {
                ActivityBookingHistoryDetails.navigate(getActivity(), obj);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
