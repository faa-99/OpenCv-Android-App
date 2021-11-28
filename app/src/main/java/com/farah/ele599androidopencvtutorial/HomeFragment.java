package com.farah.ele599androidopencvtutorial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment{
    private ImageView user;
    private TextView status;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        user=(ImageView) getView().findViewById(R.id.imageView12);
        status=(TextView) getView().findViewById(R.id.textView37);
        user.setImageResource(R.drawable.ic_briefcase);
        status.setText("Current Status: Donator\nExercise safety and follow instructions to\nensure your health condition.\nCheck Prevention section.\n");
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

}

