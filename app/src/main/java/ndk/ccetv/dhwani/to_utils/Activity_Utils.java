package ndk.ccetv.dhwani.to_utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Nabeel on 29-01-2018.
 */

public class Activity_Utils {
    public static void start_activity_with_string_extras_and_finsh(Context context, Class activity, Pair[] extras) {
        Intent intent = new Intent(context, activity);
        if (extras.length != 0) {
            Pair[] var4 = extras;
            int var5 = extras.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                Pair<String, String> extra = var4[var6];
                intent.putExtra(extra.first, extra.second);
            }
        }

        context.startActivity(intent);
        ((AppCompatActivity) context).finish();
    }
}
