package ndk.ccetv.dhwani.activities;

import android.support.v4.util.Pair;

import ndk.ccetv.dhwani.constants.API;
import ndk.ccetv.dhwani.constants.Application_Specification;
import ndk.ccetv.dhwani.to_utils.Activity_Utils;
import ndk.ccetv.dhwani.to_utils.Login;


/**
 * Created by Nabeel on 29-01-2018.
 */

public class Custom_Splash extends ndk.ccetv.dhwani.to_utils.Splash {

    @Override
    public void launch_Next_Screen() {
        Activity_Utils.start_activity_with_string_extras_and_finsh(this, Login.class, new Pair[]{new Pair<>("URL", API.get_Android_API(API.select_Committee)), new Pair<>("next_Activity", "ndk.ccetv.dhwani.activities.Registers"), new Pair<>("application_Name", Application_Specification.APPLICATION_NAME)});
    }
}

//    getIntent().getStringExtra("URL")
//        getIntent().getStringExtra("next_Activity")
//        getIntent().getStringExtra("application_Name")
