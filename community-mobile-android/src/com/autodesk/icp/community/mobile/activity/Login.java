package com.autodesk.icp.community.mobile.activity;

import java.util.Arrays;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.autodesk.icp.community.common.model.ServiceResponse;
import com.autodesk.icp.community.common.model.ServiceStatus;
import com.autodesk.icp.community.common.model.User;
import com.autodesk.icp.community.common.util.Consts;
import com.autodesk.icp.community.mobile.util.SessionManager;
import com.fasterxml.jackson.core.type.TypeReference;

public class Login extends BaseActivity {
    private EditText mUser;
    private EditText mPassword;
    //private ProgressDialog dialog; 

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mUser = (EditText)findViewById(R.id.login_user_edit);
        mPassword = (EditText)findViewById(R.id.login_passwd_edit);
    }

    public void login(View v) {
    	//new de progress dialog
        //dialog = ProgressDialog.show(this, "", "Logining..."); 
        
        if (!verifyUsername() || !verifyPassword()) {
            return;
        }

        final DialogFragment loadingDialog = showLoadingDialog();

        final Handler myHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0: // success
                        Intent intent = new Intent(Login.this, MainCommunity.class);
                        startActivity(intent);

                        Login.this.finish();
                        
                        //dialog.dismiss(); 
                        break;
                    case 1: // failure
                        Toast toast = Toast.makeText(Login.this,
                                                     getResources().getString(R.string.label_up_invalid_incorrect),
                                                     Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        mPassword.requestFocus();
                        
                        dismissWaitingDialog(loadingDialog);
                        break;

                    default:
                        break;
                }
            }
        };

        new Thread() {
            public void run() {

                boolean isLoggedIn = authenticate(mUser.getText().toString(), mPassword.getText().toString());
                Message msg = myHandler.obtainMessage();
                if (isLoggedIn) {
                    msg.what = 0;
                } else {
                    msg.what = 1;
                }
                myHandler.sendMessage(msg);
            }
        }.start();

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
        String url = "http://10.148.206.109:8080/community/login";

        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(Arrays.asList(new FormHttpMessageConverter(), jsonConverter));

        MultiValueMap<String, String> postbody = new LinkedMultiValueMap<String, String>();
        postbody.add(Consts.USERNAME, username);
        postbody.add(Consts.PASSWORD, password);

        ResponseEntity<Object> result = restTemplate.postForEntity(url, postbody, Object.class);
        ServiceResponse<User> response = jsonConverter.getObjectMapper()
                                                      .convertValue(result.getBody(),
                                                                    new TypeReference<ServiceResponse<User>>() {
                                                                    });

        if (ServiceStatus.OK.equals(response.getStatus())) {
            SessionManager sm = new SessionManager(this);
            sm.createLoginSession(response.getPayload());
            sm.setJSESSIONID(getJSESSIONID(result));
            return true;
        } else {
            return false;
        }
    }

    private String getJSESSIONID(ResponseEntity<?> entity) {
        for (String cookie : entity.getHeaders().get("Set-Cookie")) {
            String[] items = cookie.split(";");
            for (String item : items) {
                String[] pair = item.split("=");
                if ("jsessionid".equalsIgnoreCase(pair[0].trim())) {
                    return pair[1].trim();
                }
            }
        }
        return null;
    }
}
