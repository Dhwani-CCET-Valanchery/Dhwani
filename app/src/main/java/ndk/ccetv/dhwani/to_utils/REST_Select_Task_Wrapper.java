package ndk.ccetv.dhwani.to_utils;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ndk.utils.Network_Utils;
import ndk.utils.ProgressBar_Utils;
import ndk.utils.Toast_Utils;

import static ndk.utils.Network_Utils.isOnline;

/**
 * Created by Nabeel on 29-01-2018.
 */

public class REST_Select_Task_Wrapper {
    public static void execute(Context context, View mProgressView, View mLoginFormView, REST_Select_Task REST_select_task, String task_URL, String application_Name, Pair[] name_value_pairs, REST_Select_Task.AsyncResponse async_Response) {
        // Show a progress spinner, and kick off a background task to perform the user login attempt.
        if (isOnline(context)) {
            ProgressBar_Utils.showProgress(true, context, mProgressView, mLoginFormView);
            REST_select_task = new REST_Select_Task(task_URL, context, mProgressView, mLoginFormView, application_Name, name_value_pairs, async_Response);

            REST_select_task.execute((Void) null);
        } else {
            Toast_Utils.longToast(context, "Internet is unavailable");
        }
    }

    public static void execute_splash(final Context activity_context, REST_Select_Task REST_select_task, final String task_URL, final String application_Name, final Pair[] name_value_pairs, final REST_Select_Task.AsyncResponse async_Response) {

        if (REST_select_task != null) {
            REST_select_task.cancel(true);
            REST_select_task = null;
        }

        if (isOnline(activity_context)) {
            REST_select_task = new REST_Select_Task(task_URL, activity_context, application_Name, name_value_pairs, async_Response);
            REST_select_task.execute((Void) null);
        } else {
            final REST_Select_Task final_REST_select_task = REST_select_task;
            View.OnClickListener retry_Failed_Network_Task = new View.OnClickListener() {
                public void onClick(View view) {
                    execute_splash(activity_context, final_REST_select_task, task_URL, application_Name, name_value_pairs, async_Response);
                }
            };
            Network_Utils.display_Long_no_FAB_no_network_bottom_SnackBar(((AppCompatActivity) activity_context).getWindow().getDecorView(), retry_Failed_Network_Task);
        }
    }
}
