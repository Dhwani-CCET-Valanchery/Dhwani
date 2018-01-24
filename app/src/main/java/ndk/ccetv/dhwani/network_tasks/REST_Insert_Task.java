package ndk.ccetv.dhwani.network_tasks;

import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ndk.utils.Network_Utils;

import static ndk.utils.Network_Utils.perform_http_client_network_task;
import static ndk.utils.ProgressBar_Utils.showProgress;

/**
 * Created by Nabeel on 23-01-2018.
 */
//TODO : insert network task
public class REST_Insert_Task extends AsyncTask<Void, Void, String[]> {
    private String URL, TAG;
    private REST_Insert_Task REST_Insert_task;
    private AppCompatActivity current_activity;
    private View progressBar, form, focus_on_error;
    private Pair[] name_value_pair;
    Class next_activity;

    public REST_Insert_Task(String URL, REST_Insert_Task REST_Insert_task, AppCompatActivity current_activity, View progressBar, View form, String TAG, Pair[] name_value_pair, View focus_on_error, Class next_activity) {

        this.URL = URL;
        this.REST_Insert_task = REST_Insert_task;
        this.current_activity = current_activity;
        this.progressBar = progressBar;
        this.form = form;
        this.TAG = TAG;
        this.name_value_pair = name_value_pair;
        this.focus_on_error = focus_on_error;
        this.next_activity = next_activity;

    }

    @Override
    protected String[] doInBackground(Void... params) {
        return perform_http_client_network_task(URL, name_value_pair);
    }


    @Override
    protected void onPostExecute(final String[] network_action_response_array) {
        REST_Insert_task = null;

        showProgress(false, current_activity, progressBar, form);
//        TODO : remove redundent parameter context
        Network_Utils.handle_json_insertion_response_and_switch_with_finish(network_action_response_array, current_activity, next_activity, current_activity, focus_on_error, TAG);
    }

    @Override
    protected void onCancelled() {
        REST_Insert_task = null;
        showProgress(false, current_activity, progressBar, form);
    }
}