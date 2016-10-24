package com.android.joinmi.app.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ccheung on 2016-10-23.
 */
public class AppUtils {

    public static void showShortToastMessage(String text, Context appContext) {
        CharSequence toastText = "Hello toast!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(appContext, text, duration);
        toast.show();

    }
}
