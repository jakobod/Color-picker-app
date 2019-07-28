package com.example.tabbing.ui.main.colorPicker;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.tabbing.R;
import com.example.tabbing.WifiTask;
import com.larswerkman.holocolorpicker.SVBar;

import java.util.Objects;

import static java.util.Locale.*;

public class ColorPickerFragment extends Fragment
    implements com.larswerkman.holocolorpicker.ColorPicker.OnColorChangedListener {

  private static final String ARG_SECTION_NUMBER = "section_number";

  public static ColorPickerFragment newInstance(int index) {
    ColorPickerFragment fragment = new ColorPickerFragment();
    Bundle bundle = new Bundle();
    bundle.putInt(ARG_SECTION_NUMBER, index);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    SVBar svBar = Objects.requireNonNull(getView()).findViewById(R.id.svbar);
    com.larswerkman.holocolorpicker.ColorPicker picker =
                                            getView().findViewById(R.id.picker);

    picker.addSVBar(svBar);
    picker.setShowOldCenterColor(false);
    picker.setOnColorChangedListener(this);
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_main, container, false);
  }

  @Override
  public void onColorChanged(int color) {
    String colorMessage = "";

    colorMessage += (String.format(GERMAN, "%03d", Color.red(color)));
    colorMessage += (String.format(GERMAN, "%03d", Color.green(color)));
    colorMessage += (String.format(GERMAN, "%03d", Color.blue(color)));

    System.out.println("color changed: " + colorMessage);
    WifiTask.send(colorMessage);
  }
}