package ndk.ccetv.dhwani.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ndk.ccetv.dhwani.R;
import ndk.ccetv.dhwani.constants.Application_Specification;
import ndk.ccetv.dhwani.constants.Server_Endpiont;
import ndk.utils.update.network_tasks.Update_Check_Task;

import static ndk.utils.update.Check.attempt_Update_Check;

//TODO:Full screen splash

public class Splash extends AppCompatActivity {

    Context application_context;
    private Update_Check_Task Update_Task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        application_context = getApplicationContext();
        //TODO : refactor URL to get_version_URL
        //TODO : add server API constants
        attempt_Update_Check("Dhwani",this, Server_Endpiont.SERVER_IP_ADDRESS + "/android/get_version.php", Application_Specification.TAG,Update_Task, Server_Endpiont.UPDATE_URL,Portal.class);
    }
}
