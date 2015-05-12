package com.autodesk.icp.community.mobile.activity;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import android.app.AlertDialog;
import android.app.DialogFragment;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mUser = (EditText)findViewById(R.id.login_user_edit);
        mPassword = (EditText)findViewById(R.id.login_passwd_edit);

        if (isLoggedIn()) {
            User cachedUser = getSessionManager().getUserDetails();
            if (cachedUser != null) {
                String loginId = cachedUser.getLoginId();
                String password = cachedUser.getPassword();

                mUser.setText(loginId);
                mPassword.setText(password);
                
                if (StringUtils.isNotBlank(loginId) && StringUtils.isNotBlank(password)) {
                    login(null);
                }
            }
        }
    }

    public void login(View v) {
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
                        break;
                    case 1: // failure
                        Toast toast = Toast.makeText(Login.this,
                                                     getResources().getString(R.string.warning_up_invalid_incorrect),
                                                     Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        mPassword.requestFocus();

                        dismissWaitingDialog(loadingDialog);
                        break;
                    case 2:
                        Toast connectionWarning = Toast.makeText(Login.this,
                                                     getResources().getString(R.string.warning_connection_issue),
                                                     Toast.LENGTH_LONG);
                        connectionWarning.setGravity(Gravity.CENTER, 0, 0);
                        connectionWarning.show();
                        
                        mPassword.requestFocus();

                        dismissWaitingDialog(loadingDialog);
                    default:
                        break;
                }
            }
        };

        new Thread() {
            public void run() {
                Message msg = myHandler.obtainMessage();
                try {
                    boolean isLoggedIn = authenticate(mUser.getText().toString(), mPassword.getText().toString());
                    if (isLoggedIn) {
                        msg.what = 0;
                    } else {
                        msg.what = 1;
                    }
                } catch (RestClientException e) {
                    msg.what = 2;
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
                                               .setMessage(getResources().getString(R.string.warning_username_invalid_empty))
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
                                               .setMessage(getResources().getString(R.string.warning_password_invalid_empty))
                                               .create()
                                               .show();
            return false;
        }
        return true;
    }

    private boolean authenticate(String username, String password) {        
        String url = getConfigurationManager().getProperties("server.uri.http") + "/login";

        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(Arrays.asList(new FormHttpMessageConverter(), jsonConverter));

        MultiValueMap<String, String> postbody = new LinkedMultiValueMap<String, String>();
        postbody.add(Consts.USERNAME, username);
        postbody.add(Consts.PASSWORD, password);

        try {
            ResponseEntity<Object> result = restTemplate.postForEntity(url, postbody, Object.class);
            ServiceResponse<User> response = jsonConverter.getObjectMapper()
                                                          .convertValue(result.getBody(),
                                                                        new TypeReference<ServiceResponse<User>>() {
                                                                        });
            if (ServiceStatus.OK.equals(response.getStatus())) {
                SessionManager sm = getSessionManager();
                User user = response.getPayload();
                user.setPassword(password);
                sm.createLoginSession(user);
                sm.setJSESSIONID(getJSESSIONID(result));
                return true;
            } else {
                return false;
            }
        } catch (RestClientException e) {
            throw e;
        } catch (Exception e) {        
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

    private boolean isLoggedIn() {
        return getSessionManager().isLoggedIn();
    }
}
