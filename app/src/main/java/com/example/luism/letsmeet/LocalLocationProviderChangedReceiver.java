package com.example.luism.letsmeet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * Created by luism on 14/4/2018.
 */

public class LocalLocationProviderChangedReceiver extends BroadcastReceiver {

    private final WeakReference<MapsActivity> mWeakActivity;

    public LocalLocationProviderChangedReceiver(@NonNull MapsActivity activity) {
        mWeakActivity = new WeakReference<>(activity);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        MapsActivity activity = mWeakActivity.get();
        if (activity != null) {
            //activity.refresh();
        }
    }

}
