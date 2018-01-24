package ndk.ccetv.dhwani.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;

import ndk.ccetv.dhwani.R;
import ndk.ccetv.dhwani.constants.API;
import ndk.ccetv.dhwani.constants.Application_Specification;
import ndk.utils.Spinner_Utils;
import ndk.utils.Toast_Utils;
import ndk.utils.Validation_Utils;
import ndk.utils.network_task.REST_Insert_Task;

import static ndk.utils.Network_Utils.isOnline;
import static ndk.utils.ProgressBar_Utils.showProgress;

public class Insert_Member extends AppCompatActivity {

    private android.widget.ProgressBar loginprogress;
    private android.widget.EditText editname;
    private android.widget.Spinner spinnerdepartment;
    private android.widget.Spinner spinnersemester;
    private android.widget.EditText editmobilenumber;
    private android.widget.Spinner spinnergender;
    private android.widget.EditText editage;
    private android.widget.ScrollView loginform;
    Context application_context;
    REST_Insert_Task REST_Insert_Task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_member);
        application_context = getApplicationContext();
        this.loginform = (ScrollView) findViewById(R.id.login_form);
        Button buttonsubmit = (Button) findViewById(R.id.button_submit);
        this.editage = (EditText) findViewById(R.id.edit_age);
        this.spinnergender = (Spinner) findViewById(R.id.spinner_gender);
        this.editmobilenumber = (EditText) findViewById(R.id.edit_mobile_number);
        this.spinnersemester = (Spinner) findViewById(R.id.spinner_semester);
        this.spinnerdepartment = (Spinner) findViewById(R.id.spinner_department);
        this.editname = (EditText) findViewById(R.id.edit_name);
        this.loginprogress = (ProgressBar) findViewById(R.id.login_progress);

//        Spinner_Utils.attach_items_to_simple_spinner(this, spinnerdepartment, new ArrayList<>(Arrays.asList("Mech", "Mechatronics", "Civil", "Chemical", "Computer Science", "EC", "EEE")));
//        Spinner_Utils.attach_items_to_simple_spinner(this, spinnersemester, new ArrayList<>(Arrays.asList("S1", "S2", "S3", "S4", "S5", "S6", "S7", "S8")));
        Spinner_Utils.get_json_from_network_and_populate(application_context,loginprogress,loginform,API.get_Android_API(API.select_Departments),Application_Specification.APPLICATION_NAME,1,spinnerdepartment,new ArrayList<String>(),"name");
        Spinner_Utils.get_json_from_network_and_populate(application_context,loginprogress,loginform,API.get_Android_API(API.select_Semesters),Application_Specification.APPLICATION_NAME,1,spinnersemester,new ArrayList<String>(),"name");
        Spinner_Utils.attach_items_to_simple_spinner(this, spinnergender, new ArrayList<>(Arrays.asList("Male", "Female")));

        buttonsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempt_Insert_Member();
            }
        });

    }

    private void attempt_Insert_Member() {
        Validation_Utils.reset_errors(new EditText[]{editname, editage, editmobilenumber});
        Pair<Boolean, EditText> empty_check_result = Validation_Utils.empty_check(new Pair[]{new Pair<>(editname, "Please Enter Name..."), new Pair<>(editmobilenumber, "Please Enter Mobile Number..."), new Pair<>(editage, "Please Enter Age...")});

        if (empty_check_result.first) {
            // There was an error; don't attempt login and focus the first form field with an error.
            if (empty_check_result.second != null) {
                empty_check_result.second.requestFocus();
            }
        } else {

            Pair<Boolean, EditText> zero_check_result = Validation_Utils.zero_check(new Pair[]{new Pair<>(editage, "Please Enter Valid Age...")});
            if (zero_check_result.first) {
                if (zero_check_result.second != null) {
                    zero_check_result.second.requestFocus();
                }
            } else {
                execute_Insert_Member_Task();
            }
        }
    }

    private void execute_Insert_Member_Task() {

        if (isOnline(application_context)) {
            showProgress(true, application_context, loginprogress, loginform);
            REST_Insert_Task = new REST_Insert_Task(API.get_Android_API(API.insert_Member), REST_Insert_Task, this, loginprogress, loginform, Application_Specification.APPLICATION_NAME, new Pair[]{new Pair<>("name", editname.getText().toString()), new Pair<>("department", spinnerdepartment.getSelectedItem().toString()), new Pair<>("semester", spinnersemester.getSelectedItem().toString()), new Pair<>("mobile_number", editmobilenumber.getText().toString()), new Pair<>("gender", spinnergender.getSelectedItem().toString()), new Pair<>("age", editage.getText().toString())}, editname,Insert_Member.class);
            REST_Insert_Task.execute((Void) null);
        } else {
            Toast_Utils.longToast(getApplicationContext(), "Internet is unavailable");
        }
    }
}
