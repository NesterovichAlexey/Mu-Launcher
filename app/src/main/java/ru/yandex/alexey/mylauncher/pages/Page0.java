package ru.yandex.alexey.mylauncher.pages;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.yandex.alexey.mylauncher.R;

public class Page0 extends Fragment {

    public Page0() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page0, container, false);
    }
}
