package com.roselism.spot.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.roselism.spot.R;
import com.roselism.spot.domain.User;
import com.roselism.spot.library.content.DataLoader;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.SaveListener;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * 登陆界面
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    @Bind(R.id.login_progress) ProgressBar mProgressView;
    @Bind(R.id.email) AutoCompleteTextView mEmailView;
    @Bind(R.id.password) EditText mPasswordView;
    @Bind(R.id.email_sign_in_button) Button mEmailSignInButton;
    @Bind(R.id.login_form) ScrollView mLoginFormView;

    //    private UserLoginTask mAuthTask = null; //Keep track of the login task to ensure we can cancel it if requested./Keep track of the login task to ensure we can cancel it if requested.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //        if (User.getCurrentUser(this, User.class) != null) //如果没登陆，跳转到LoginActivity进行登陆
        //            startActivity(new Intent(this, HomeActivity.class));

        populateAutoComplete();

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

        initClickListener();
    }

    private void initClickListener() {
        mEmailSignInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_sign_in_button:
                attemptLogin();
                break;
        }
    }

    /**
     * 自动补全
     */
    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        //        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    /**
     * 验证邮箱地址是否合法
     *
     * @param email 要被检查的邮箱
     * @return 如果合法，则返回true
     */
    private boolean isEmailValid(String email) {
        if (email == null || TextUtils.isEmpty(email)) {
            mEmailView.setError(getResources().getString(R.string.error_email_field_required));
            return false;
        }

        if (!email.contains("@") || !email.contains(".")) {
            mEmailView.setError(getResources().getString(R.string.error_wrong_format_email));
        }

        Pattern pattern = Pattern.compile("[!#$%^&*()_/*-+~,，<>/?:;,]+");
        Matcher matcher = pattern.matcher("email");
        if (matcher.find()) {
            mEmailView.setError(getResources().getString(R.string.error_wrong_format_email));
            return false;
        }

        return true;
    }


    /**
     * 验证密码是否有效
     *
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getResources().getString(R.string.error_empty_password));
            return false;
        }

        if (password.length() < 6) {
            mPasswordView.setError(getResources().getString(R.string.error_too_short_password));
            return false;
        }

        return true;
    }

    /**
     * 尝试登陆或注册
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!isPasswordValid(password)) { // Check for a valid password, if the user entered one.
            mPasswordView.setError(getString(R.string.error_too_short_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (!isEmailValid(email)) { // Check for a valid email address.
            mEmailView.setError(getString(R.string.error_email_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) { // 这里有错误; 不要尝试登陆并且让第一个有错误的域获得焦点 don't attempt login and focus the first form field with an error.
            focusView.requestFocus();
        } else { // Show a progress spinner, and kick off a background task to perform the user login attempt.
            showProgress(true);

            Runnable checkAccountThread = new Runnable() { // 检查账户是否存在
                boolean isExist;

                @Override
                public void run() {

                    final Message message = new Message();
                    final Bundle bundle = new Bundle();
                    // 查询该email是否存在，存在则登陆，不存在则注册
                    BmobQuery<User> query = new BmobQuery<>();
                    query.addWhereEqualTo("email", email);
                    query.count(LoginActivity.this, User.class, new CountListener() {
                        @Override
                        public void onSuccess(int i) {
                            isExist = i == 1;
                            bundle.putBoolean("isExist", isExist);
                            message.setData(bundle);
                            loginHandler.sendMessage(message); // 发送消息
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            bundle.putString("checkErro", "查询错误");
                        }
                    });
                }
            };
            checkAccountThread.run();
        }
    }

    private Handler loginHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String email = mEmailView.getText().toString();
            String password = mPasswordView.getText().toString();

            Bundle bundle = msg.getData();
            UserLoginThread loginThread;
            if (bundle.getBoolean("isExist"))  //账户存在，则进行登陆
                loginThread = new UserLoginThread(email, password, true);
            else  // 账户不存在，则进行注册
                loginThread = new UserLoginThread(email, password, false);
            loginThread.run();
        }
    };

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
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

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    private class UserLoginThread implements Runnable {
        private final String mEmail;
        private final String mPassword;
        private boolean isExist; // 用户是否存在
        private boolean isPasswordCorrect; // 密码是否正确

        public UserLoginThread(String mEmail, String mPassword, boolean isExist) {
            this.mEmail = mEmail;
            this.mPassword = mPassword;
            this.isExist = isExist;
        }

        @Override
        public void run() {
            showProgress(true); // 显示加载条

            User user = new User();
            user.setUsername(mEmail);
            user.setEmail(mEmail);
            user.setPassword(mPassword);
            if (isExist) {   // 账户存在，则进行登陆
                user.login(LoginActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        showProgress(false);
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        showProgress(false);
                        isPasswordCorrect = false;
                    }
                });
            } else {  // 账户不存在，则进行注册
                user.signUp(LoginActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        showProgress(false);
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        showProgress(false);
                    }
                });
            }
        }
    }
}