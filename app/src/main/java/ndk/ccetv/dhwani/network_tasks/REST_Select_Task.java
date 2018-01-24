package ndk.ccetv.dhwani.network_tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import static ndk.utils.Network_Utils.perform_http_client_network_task;
import static ndk.utils.ProgressBar_Utils.showProgress;

/**
 * Created by Nabeel on 23-01-2018.
 */

//TODO : add items to spinner from network task
public class REST_Select_Task extends AsyncTask<Void, Void, String[]> {

    private String URL, TAG;
    private REST_Select_Task select_Members_task;
    private Context context;
    private View progressBar, form;
    //    private Spinner spinner;
//    ArrayList<String> spinner_items;
    private Pair[] name_value_pair;
    private AsyncResponse delegate = null;

    //    public REST_Select_Task(String URL, REST_Select_Task select_Members_task, Context context, View progressBar, View form, String TAG, Spinner spinner, Pair[] name_value_pair, ArrayList<String> spinner_items) {
    public REST_Select_Task(String URL, REST_Select_Task select_Members_task, Context context, View progressBar, View form, String TAG, Pair[] name_value_pair, AsyncResponse delegate) {

        this.URL = URL;
        this.select_Members_task = select_Members_task;
        this.context = context;
        this.progressBar = progressBar;
        this.form = form;
        this.TAG = TAG;
//        this.spinner = spinner;
        this.name_value_pair = name_value_pair;
//        this.spinner_items = spinner_items;
        this.delegate = delegate;
    }

    @Override
    protected String[] doInBackground(Void... params) {
        return perform_http_client_network_task(URL, name_value_pair);
    }

    // you may separate this or combined to caller class.
    public interface AsyncResponse {
        void processFinish(JSONArray output);
    }


    @Override
    protected void onPostExecute(final String[] network_action_response_array) {
        select_Members_task = null;

        showProgress(false, context, progressBar, form);

        Log.d(TAG, network_action_response_array[0]);
        Log.d(TAG, network_action_response_array[1]);

        if (network_action_response_array[0].equals("1")) {
            Toast.makeText(context, "Error : " + network_action_response_array[1], Toast.LENGTH_LONG).show();
            Log.d(TAG, network_action_response_array[1]);
        } else {

            try {
                JSONArray json_array = new JSONArray(network_action_response_array[1]);
                if (json_array.getJSONObject(0).getString("status").equals("1")) {
                    Toast.makeText(context, "Error...", Toast.LENGTH_LONG).show();
                } else if (json_array.getJSONObject(0).getString("status").equals("0")) {

//                    for (int i = 1; i < json_array.length(); i++) {
//
//                        spinner_items.add(json_array.getJSONObject(i).getString("name"));
//
//                    }
//                    Spinner_Utils.attach_items_to_simple_spinner(context, spinner, spinner_items);
                    delegate.processFinish(json_array);

                }
            } catch (JSONException e) {
                Toast.makeText(context, "Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG, e.getLocalizedMessage());
            }
        }
    }

    @Override
    protected void onCancelled() {
        select_Members_task = null;
        showProgress(false, context, progressBar, form);
    }
}