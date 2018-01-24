package ndk.ccetv.dhwani.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import ndk.ccetv.dhwani.R;
import ndk.ccetv.dhwani.constants.API;
import ndk.ccetv.dhwani.constants.Application_Specification;
import ndk.ccetv.dhwani.constants.Server_Endpiont;
import ndk.ccetv.dhwani.network_tasks.REST_Insert_Task;
import ndk.ccetv.dhwani.network_tasks.REST_Select_Task;
import ndk.utils.Date_Picker_Utils;
import ndk.utils.Date_Utils;
import ndk.utils.Spinner_Utils;
import ndk.utils.Toast_Utils;
import ndk.utils.Validation_Utils;

import static ndk.utils.Network_Utils.isOnline;
import static ndk.utils.ProgressBar_Utils.showProgress;

public class Insert_Transaction extends AppCompatActivity {

    private ProgressBar loginprogress;
    private Button buttondate;
    private Spinner spinnermember;
    private Spinner spinnerpurpose;
    private EditText editcash;
    private ScrollView loginform;
    private Calendar calendar = Calendar.getInstance();
    Context application_context, activity_context = this;
    REST_Select_Task select_Members_task = null;
    REST_Insert_Task REST_Insert_Task = null;
    ArrayList<String> spinner_items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_transaction);
        application_context = getApplicationContext();
        this.loginform = (ScrollView) findViewById(R.id.login_form);
        Button buttonsubmit = (Button) findViewById(R.id.button_submit);
        this.editcash = (EditText) findViewById(R.id.edit_cash);
        this.spinnerpurpose = (Spinner) findViewById(R.id.spinner_purpose);
        this.spinnermember = (Spinner) findViewById(R.id.spinner_member);
        this.buttondate = (Button) findViewById(R.id.button_date);
        this.loginprogress = (ProgressBar) findViewById(R.id.login_progress);
        buttondate.setText(Date_Utils.normal_Date_Format_words.format(calendar.getTime()));
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                buttondate.setText(Date_Utils.normal_Date_Format_words.format(calendar.getTime()));
            }
        };
        buttondate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date_Picker_Utils.show_date_picker_upto_today(activity_context, date, calendar);
            }
        });
        buttonsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempt_insert_Transaction();
            }
        });

        Spinner_Utils.attach_items_to_simple_spinner(this, spinnerpurpose, new ArrayList<>(Arrays.asList("Registration Fee")));

        if (select_Members_task != null) {
            select_Members_task.cancel(true);
            select_Members_task = null;
        }

        if (isOnline(application_context)) {
            showProgress(true, application_context, loginprogress, loginform);
            select_Members_task = new REST_Select_Task(Server_Endpiont.SERVER_IP_ADDRESS + API.get_Android_API(API.select_Members), select_Members_task, this, loginprogress, loginform, Application_Specification.APPLICATION_NAME, new Pair[]{}, new REST_Select_Task.AsyncResponse() {

                @Override
                public void processFinish(JSONArray json_array) {
                    for (int i = 1; i < json_array.length(); i++) {

                        try {
                            spinner_items.add(json_array.getJSONObject(i).getString("name"));
                        } catch (JSONException e) {
                            Toast.makeText(application_context, "Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            Log.d(Application_Specification.APPLICATION_NAME, e.getLocalizedMessage());
                        }

                    }
                    Spinner_Utils.attach_items_to_simple_spinner(application_context, spinnermember, spinner_items);
                }

            });
            select_Members_task.execute((Void) null);
        } else {
            Toast_Utils.longToast(getApplicationContext(), "Internet is unavailable");
        }
    }

    private void attempt_insert_Transaction() {
        Validation_Utils.reset_errors(new EditText[]{editcash});
        Pair<Boolean, EditText> empty_check_result = Validation_Utils.empty_check(new Pair[]{new Pair<>(editcash, "Please Enter Valid Amount...")});

        if (empty_check_result.first) {
            // There was an error; don't attempt login and focus the first form field with an error.
            if (empty_check_result.second != null) {
                empty_check_result.second.requestFocus();
            }
        } else {

            Pair<Boolean, EditText> zero_check_result = Validation_Utils.zero_check(new Pair[]{new Pair<>(editcash, "Please Enter Valid Amount...")});
            if (zero_check_result.first) {
                if (zero_check_result.second != null) {
                    zero_check_result.second.requestFocus();
                }
            } else {
                execute_insert_Transaction_Task();
            }
        }
    }

    private void execute_insert_Transaction_Task() {
        if (isOnline(application_context)) {
            showProgress(true, application_context, loginprogress, loginform);
            try {
                REST_Insert_Task = new REST_Insert_Task(Server_Endpiont.SERVER_IP_ADDRESS + API.get_Android_API(API.insert_Transaction), REST_Insert_Task, this, loginprogress, loginform, Application_Specification.APPLICATION_NAME, new Pair[]{new Pair<>("event_date_time", Date_Utils.date_to_mysql_date_string(Date_Utils.normal_Date_Format_words.parse(buttondate.getText().toString()))), new Pair<>("member_id", spinnermember.getSelectedItem().toString()), new Pair<>("fund_id", spinnerpurpose.getSelectedItem().toString()), new Pair<>("amount", editcash.getText().toString())}, editcash, Insert_Transaction.class);
            } catch (ParseException e) {
                Toast_Utils.longToast(getApplicationContext(), "Date conversion error");
                Log.d(Application_Specification.APPLICATION_NAME, e.getLocalizedMessage());
            }
            REST_Insert_Task.execute((Void) null);
        } else {
            Toast_Utils.longToast(getApplicationContext(), "Internet is unavailable");
        }
    }
}
