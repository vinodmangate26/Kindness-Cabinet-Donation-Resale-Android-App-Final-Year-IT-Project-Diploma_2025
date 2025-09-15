package com.gauravpatil.kindnesscabinet.AddDonateorSell;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gauravpatil.kindnesscabinet.R;

public class AddFragment extends Fragment {
    AppCompatButton btnAddFragmentDonate,btnAddFragmentSell;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        btnAddFragmentDonate = view.findViewById(R.id.btnAddFragmentDonate);
        btnAddFragmentSell = view.findViewById(R.id.btnAddFragmentSell);

        // Set a click listener on the button
        view.findViewById(R.id.btnAddFragmentDonate).setOnClickListener(v -> {
            // Create an intent to start the TargetActivity
            Intent intent = new Intent(getActivity(), DonateActivity.class);
            // Start the activity
            startActivity(intent);
        });

        // Set a click listener on the button
        view.findViewById(R.id.btnAddFragmentSell).setOnClickListener(v -> {
            // Create an intent to start the TargetActivity
            Intent intent = new Intent(getActivity(), SellerActivity.class);
            // Start the activity
            startActivity(intent);
        });
return view;
    }
}