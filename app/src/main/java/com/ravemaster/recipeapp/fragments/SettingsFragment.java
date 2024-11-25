package com.ravemaster.recipeapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.ravemaster.recipeapp.R;
import com.ravemaster.recipeapp.TestActivity;
import com.ravemaster.recipeapp.utilities.Constants;
import com.ravemaster.recipeapp.utilities.PreferenceManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    Button button;
    PreferenceManager preferenceManager;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    MaterialSwitch materialSwitch;
    TextView txtTheme;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initViews(view);
        preferenceManager = new PreferenceManager(getActivity());

        boolean isDarkTheme = preferenceManager.getIsDarkTheme(Constants.IS_DARK_THEME);
        materialSwitch.setChecked(isDarkTheme);
        if (isDarkTheme){
            txtTheme.setText("Dark theme selected");
        } else {
            txtTheme.setText("Light theme selected");
        }

        materialSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    preferenceManager.isDarkTheme(Constants.IS_DARK_THEME,true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    getActivity().recreate();
                    txtTheme.setText("Dark theme selected");
                    Toast.makeText(getActivity(), "Dark theme selected", Toast.LENGTH_SHORT).show();
                } else {
                    preferenceManager.isDarkTheme(Constants.IS_DARK_THEME,false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    getActivity().recreate();
                    txtTheme.setText("Light theme selected");
                    Toast.makeText(getActivity(), "Light theme selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void initViews(View view) {
        materialSwitch = view.findViewById(R.id.switchTheme);
        txtTheme = view.findViewById(R.id.txtTheme);
    }


}