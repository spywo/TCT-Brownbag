package com.autodesk.icp.community.mobile.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.autodesk.icp.community.CommunicationBroker;
import com.autodesk.icp.community.common.model.User;
import com.autodesk.icp.community.mobile.handler.AuthenticationHandler;
import com.autodesk.icp.community.stomp.WebSocketStompClient;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor> {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mNTAccountView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mNTAccountView = (AutoCompleteTextView)findViewById(R.id.account);
        populateAutoComplete();
        mNTAccountView.setText(retrieveAccount());

        mPasswordView = (EditText)findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button)findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }

    /**
     * Attempts to sign in or register the account specified by the login form. If there are form errors (invalid email,
     * missing fields, etc.), the errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mNTAccountView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String account = mNTAccountView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(account)) {
            mNTAccountView.setError(getString(R.string.error_field_required));
            focusView = mNTAccountView;
            cancel = true;
        } else if (!isAccountValid(account)) {
            mNTAccountView.setError(getString(R.string.error_invalid_account));
            focusView = mNTAccountView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(account, password);
            mAuthTask.execute((Void)null);
        }
    }

    private boolean isAccountValid(String account) {
        return account.toLowerCase().startsWith("ads\\");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 0;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate()
                          .setDuration(shortAnimTime)
                          .alpha(show ? 0 : 1)
                          .setListener(new AnimatorListenerAdapter() {
                              @Override
                              public void onAnimationEnd(Animator animation) {
                                  mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                              }
                          });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate()
                         .setDuration(shortAnimTime)
                         .alpha(show ? 1 : 0)
                         .setListener(new AnimatorListenerAdapter() {
                             @Override
                             public void onAnimationEnd(Animator animation) {
                                 mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                             }
                         });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                                // Retrieve data rows for the device user's 'profile' contact.
                                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                                                     ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
                                ProfileQuery.PROJECTION,

                                // Select only email addresses.
                                ContactsContract.Contacts.Data.MIMETYPE + " = ?",
                                new String[] { ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE },

                                // Show primary email addresses first. Note that there won't be
                                // a primary email address if the user hasn't specified one.
                                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> accounts = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            accounts.add(cursor.getString(ProfileQuery.IDENTITY));
            cursor.moveToNext();
        }

        addAccountsToAutoComplete(accounts);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void forwardTo() {
        Intent intent = new Intent(this, WebActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void storeAccount(String account) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Editor editor = settings.edit();
        editor.putString("login.account", account);
        editor.apply();
        editor.commit();
    }

    private String retrieveAccount() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        return settings.getString("login.account", "ads\\");
    }

    private interface ProfileQuery {
        String[] PROJECTION = { ContactsContract.CommonDataKinds.Identity.IDENTITY,
                               ContactsContract.CommonDataKinds.Identity.IS_PRIMARY, };

        int IDENTITY = 0;
        int IS_PRIMARY = 1;
    }

    private void addAccountsToAutoComplete(List<String> emailAddressCollection) {
        // Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(LoginActivity.this,
                                                                android.R.layout.simple_dropdown_item_1line,
                                                                emailAddressCollection);

        mNTAccountView.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mAccount;
        private final String mPassword;

        UserLoginTask(String account, String password) {
            mAccount = account;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            WebSocketStompClient client = CommunicationBroker.getInstance();

            final CountDownLatch latch = new CountDownLatch(1);

            User user = new User();
            user.setLoginId(mAccount);
            user.setPassword(mPassword);
            AuthenticationHandler handler = new AuthenticationHandler(getString(R.string.ws_subscrible_login),
                                                                      getString(R.string.ws_destination_login),
                                                                      user,
                                                                      latch);
            client.connect(handler);

            try {
                latch.await(2, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
            }

            User authedUser = handler.getPayload();
            if (authedUser != null && authedUser.isAuthed()) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                storeAccount(mAccount);
                finish();
                forwardTo();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
