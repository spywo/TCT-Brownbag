package com.autodesk.icp.community.mobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Welcome extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
    }

    public void welcome_login(View v) {
    	try{
        Intent intent = new Intent();
        intent.setClass(Welcome.this, Login.class);
        startActivity(intent);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
}
