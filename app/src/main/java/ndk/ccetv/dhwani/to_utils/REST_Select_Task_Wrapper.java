package ndk.ccetv.dhwani.to_utils;

import android.content.Context;
import android.support.v4.util.Pair;
import android.view.View;

import ndk.utils.ProgressBar_Utils;
import ndk.utils.Toast_Utils;
import ndk.utils.network_task.REST_Select_Task;

import static ndk.utils.Network_Utils.isOnline;

/**
 * Created by Nabeel on 29-01-2018.
 */

public class REST_Select_Task_Wrapper {
    public static void execute(Context context, View mProgressView, View mLoginFormView, REST_Select_Task REST_select_task, String task_URL, String application_Name, Pair[] name_value_pairs, REST_Select_Task.AsyncResponse async_Response) {
        // Show a progress spinner, and kick off a background task to perform the user login attempt.
        if (isOnline(context)) {
            ProgressBar_Utils.showProgress(true, context, mProgressView, mLoginFormView);
            REST_select_task = new REST_Select_Task(task_URL, REST_select_task, context, mProgressView, mLoginFormView, application_Name, name_value_pairs, async_Response);

            REST_select_task.execute((Void) null);
        } else {
            Toast_Utils.longToast(context, "Internet is unavailable");
        }
    }
}
