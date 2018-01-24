package ndk.ccetv.dhwani.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ndk.ccetv.dhwani.R;
import ndk.ccetv.dhwani.constants.API;
import ndk.ccetv.dhwani.constants.Application_Specification;
import ndk.ccetv.dhwani.constants.Server_Endpiont;
import ndk.utils.network_tasks.update.Update_Check_Task;

import static ndk.utils.update.Check.attempt_Update_Check;

//TODO:Full screen splash

public class Splash extends AppCompatActivity {

    Context application_context;
    private Update_Check_Task Update_Task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        application_context = getApplicationContext();
        //TODO : add permissions on utils
        //TODO : add splash using meta data
        attempt_Update_Check(Application_Specification.APPLICATION_NAME,this, Server_Endpiont.SERVER_IP_ADDRESS + API.get_Android_API(API.select_Configuration), Application_Specification.APPLICATION_NAME,Update_Task, Server_Endpiont.UPDATE_URL,Registers.class);
    }
}
