package ndk.ccetv.dhwani.to_utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import ndk.ccetv.dhwani.R;
import ndk.ccetv.dhwani.constants.Application_Specification;
import ndk.utils.Activity_Utils;
import ndk.utils.Toast_Utils;
import ndk.utils.network_task.REST_Select_Task;

public class Login extends AppCompatActivity {

    // UI references.
    private EditText username;
    private EditText passcode;
    private View mProgressView;
    private View mLoginFormView;

    private static REST_Select_Task REST_select_task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


        username = findViewById(R.id.username);
        passcode = findViewById(R.id.passcode);
        passcode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button sign_in_button = findViewById(R.id.sign_in_button);
        sign_in_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (REST_select_task != null) {
            REST_select_task.cancel(true);
            REST_select_task = null;
        }

        // Reset errors.
        username.setError(null);
        passcode.setError(null);

        // Store values at the time of the login attempt.
        String entered_username = username.getText().toString();
        String entered_passcode = passcode.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid passcode, if the user entered one.
        if (TextUtils.isEmpty(entered_passcode)) {
            passcode.setError("Please enter passcode");
            focusView = passcode;
            cancel = true;
        }

        // Check for a valid username, if the user entered one.
        if (TextUtils.isEmpty(entered_username)) {
            username.setError("Please enter username");
            focusView = username;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first form field with an error.
            focusView.requestFocus();
        } else {

            InputMethodManager inputManager =
                    (InputMethodManager) getApplicationContext().
                            getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(
                        this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }

            REST_Select_Task_Wrapper.execute(this,mProgressView,mLoginFormView,REST_select_task,getIntent().getStringExtra("URL"),Application_Specification.APPLICATION_NAME,new Pair[]{new Pair<>("username", entered_username), new Pair<>("password", entered_passcode)}, new REST_Select_Task.AsyncResponse() {

                public void processFinish(JSONArray json_array) {
                    try {
                        SharedPreference_Utils.commit_Shared_Preferences(getApplicationContext(),Application_Specification.APPLICATION_NAME,new Pair[]{new Pair<>("id", json_array.getJSONObject(1).getString("id")),new Pair<>("role", json_array.getJSONObject(1).getString("role"))});
                        Activity_Utils.start_activity_with_finish(getApplicationContext(),Class.forName(getIntent().getStringExtra("next_Activity")));
                    } catch (JSONException e) {
                        Toast_Utils.longToast(getApplicationContext(), "JSON Response Error");
                    } catch (ClassNotFoundException e) {
                        Toast_Utils.longToast(getApplicationContext(), "Next Activity Error");
                    }
                }
            });

        }
    }

}

