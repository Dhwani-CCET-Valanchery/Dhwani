package ndk.ccetv.dhwani.to_utils;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import ndk.utils.Network_Utils;

import static ndk.utils.Network_Utils.perform_http_client_network_task;
import static ndk.utils.ProgressBar_Utils.showProgress;

/**
 * Created by Nabeel on 23-01-2018.
 */

public class REST_Select_Task extends AsyncTask<Void, Void, String[]> {

    private String URL, TAG;
    private Context activity_context;
    private View progressBar, form;
    private Pair[] name_value_pair;
    private AsyncResponse delegate = null;
    private int splash_flag = 0;

    REST_Select_Task(String URL, Context activity_context, View progressBar, View form, String TAG, Pair[] name_value_pair, AsyncResponse delegate) {

        this.URL = URL;
        this.activity_context = activity_context;
        this.progressBar = progressBar;
        this.form = form;
        this.TAG = TAG;
        this.name_value_pair = name_value_pair;
        this.delegate = delegate;
    }

    REST_Select_Task(String URL, Context activity_context, String TAG, Pair[] name_value_pair, AsyncResponse delegate) {

        this.URL = URL;
        this.activity_context = activity_context;
        this.TAG = TAG;
        this.name_value_pair = name_value_pair;
        this.delegate = delegate;
        splash_flag = 1;
    }

    @Override
    protected String[] doInBackground(Void... params) {
        return perform_http_client_network_task(URL, name_value_pair);
    }

    @Override
    protected void onPostExecute(final String[] network_action_response_array) {

        if (splash_flag == 0) {
            showProgress(false, activity_context, progressBar, form);
        }

        Log.d(TAG, network_action_response_array[0]);
        Log.d(TAG, network_action_response_array[1]);

        if (network_action_response_array[0].equals("1")) {
            Network_Utils.display_Friendly_Exception_Message(activity_context, network_action_response_array[1]);
            Log.d(TAG, network_action_response_array[1]);
            if (splash_flag == 1) {
                ((AppCompatActivity) activity_context).finish();
            }
        } else {

            try {
                JSONArray json_array = new JSONArray(network_action_response_array[1]);
                if (json_array.getJSONObject(0).getString("status").equals("1")) {
                    Toast.makeText(activity_context, "Error...", Toast.LENGTH_LONG).show();
                } else if (json_array.getJSONObject(0).getString("status").equals("0")) {
                    delegate.processFinish(json_array);
                }
            } catch (JSONException e) {
                Toast.makeText(activity_context, "Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG, e.getLocalizedMessage());
            }
        }
    }

    @Override
    protected void onCancelled() {
        if (splash_flag == 0) {
            showProgress(false, activity_context, progressBar, form);
        }
    }

    // you may separate this or combined to caller class.
    public interface AsyncResponse {
        void processFinish(JSONArray output);
    }
}