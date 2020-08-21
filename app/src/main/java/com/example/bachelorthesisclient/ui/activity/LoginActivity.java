package com.example.bachelorthesisclient.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bachelorthesisclient.R;
import com.example.bachelorthesisclient.model.BasicLogin;
import com.example.bachelorthesisclient.model.LoggedUserPersistence;
import com.example.bachelorthesisclient.service.AuthService;
import com.example.bachelorthesisclient.util.LoggedInUserPersistenceUtil;
import com.example.bachelorthesisclient.wrapper.GsonWrapper;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class LoginActivity extends AppCompatActivity {
    public static final String LOGGED_USER_KEY = "LOGGED_USER_KEY";

    EditText editTextEmail;
    EditText editTextPassword;
    Button buttonSignIn;
    TextView textViewNoAccount;
    TextView textViewErrorMessage;

    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        editTextEmail = findViewById(R.id.et_login_email);
        editTextPassword = findViewById(R.id.et_login_password);
        buttonSignIn = findViewById(R.id.button_sign_in);
        textViewNoAccount = findViewById(R.id.tv_no_account);
        textViewErrorMessage = findViewById(R.id.tv_login_error);

        compositeDisposable = new CompositeDisposable();

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                AuthService.getInstance().login(new BasicLogin(email, password))
                        .subscribe(new SingleObserver<LoggedUserPersistence>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                compositeDisposable.add(d);
                            }

                            @Override
                            public void onSuccess(LoggedUserPersistence loggedUserPersistence) {
                                String loggedUserJson = GsonWrapper.toJson(loggedUserPersistence);

                                try {
                                    LoggedInUserPersistenceUtil.setLoggedInUserData(loggedUserJson);
//                                    SharedPreferenceWrapper.getInstance().put(LOGGED_USER_KEY, loggedUserJson);
                                    textViewErrorMessage.setVisibility(TextView.INVISIBLE);

                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    finish();
                                } catch (Exception ex) {
                                    textViewErrorMessage.setVisibility(TextView.VISIBLE);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                textViewErrorMessage.setVisibility(TextView.VISIBLE);
                            }
                        });

            }
        });

        textViewNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        super.onDestroy();
    }
}
