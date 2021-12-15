package com.app.sample.mailbox.data;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.app.sample.mailbox.R;

public class Tools {
    public static void systemBarLolipop(Activity act){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(act.getResources().getColor(R.color.colorPrimaryDark));
        }
    }
}
