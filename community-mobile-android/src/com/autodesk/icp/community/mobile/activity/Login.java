package com.autodesk.icp.community.mobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Login extends Activity {
    private EditText mUser;
    private EditText mPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mUser = (EditText)findViewById(R.id.login_user_edit);
        mPassword = (EditText)findViewById(R.id.login_passwd_edit);

    }

    public void login(View v) {
        if (!verifyUsername() || !verifyPassword()) {
            return;
        }
        
        if ("test".equals(mUser.getText().toString()) && "123".equals(mPassword.getText().toString()))

        {
            Intent intent = new Intent();
            intent.setClass(Login.this, LoadingActivity.class);
            startActivity(intent);
        }
    }

    public void login_back(View v) {
        this.finish();
    }

    private boolean verifyUsername() {       
        if (mUser.getText().toString().isEmpty()) {
            new AlertDialog.Builder(Login.this).setIcon(getResources().getDrawable(R.drawable.login_error_icon))
                                               .setTitle(getResources().getString(R.string.label_warning))
                                               .setMessage(getResources().getString(R.string.label_username_invalid_empty))
                                               .create()
                                               .show();
            return false;
        }
        return true;
    }

    private boolean verifyPassword() {
        if (mPassword.getText().toString().isEmpty()) {
            new AlertDialog.Builder(Login.this).setIcon(getResources().getDrawable(R.drawable.login_error_icon))
                                               .setTitle(getResources().getString(R.string.label_warning))
                                               .setMessage(getResources().getString(R.string.label_password_invalid_empty))
                                               .create()
                                               .show();
            return false;
        }
        return true;
    }
}
