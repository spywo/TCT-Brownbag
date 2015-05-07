package com.autodesk.icp.community.mobile.activity;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import com.autodesk.icp.community.common.model.ServiceResponse;

public class Login extends BaseActivity {
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
        
        if ("test".equals(mUser.getText().toString()) && "123".equals(mPassword.getText().toString())) {
            final Handler myHandler = new Handler() {
                public void handleMessage(Message msg) {

                }
            };
            
            new Thread() {
                public void run() {      
                    authenticate(mUser.getText().toString(), mPassword.getText().toString());
                    Message msg = myHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("key", "value");
                    msg.setData(b);    
                    myHandler.sendMessage(msg);  
                }
            }.start();            
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
    
    private boolean authenticate(String username, String password) {
        String url = "http://10.148.202.55:8080/community/login";
        
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter ());
        
        MultiValueMap<String, String> postbody = new LinkedMultiValueMap<String, String>();
        postbody.add("username", username);
        postbody.add("password", password);
        
        ResponseEntity<ServiceResponse> result = restTemplate.postForEntity(url, postbody, ServiceResponse.class);
        return true;        
    }    
}
