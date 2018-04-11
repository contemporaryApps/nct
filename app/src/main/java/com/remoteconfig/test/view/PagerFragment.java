package com.remoteconfig.test.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.remoteconfig.test.remoteconfig.databinding.FragmentMainBinding;

/**
 * A placeholder fragment containing a simple view.
 */
public class PagerFragment extends Fragment {
    private static final String DISPLAY_TEXT = "DISPLAY_TEXT";

    public PagerFragment() {
    }

    public static PagerFragment newInstance(String text) {
        PagerFragment fragment = new PagerFragment();
        Bundle args = new Bundle();
        args.putString(DISPLAY_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentMainBinding binding = FragmentMainBinding
                .inflate(inflater, container, false);
        binding.sectionLabel.setText(getArguments().getString(DISPLAY_TEXT));
        return binding.getRoot();
    }
}
