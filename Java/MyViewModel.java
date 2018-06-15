package com.example.XXX.myroomwithrxjava;

import android.arch.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {
    private int count = 0;

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
