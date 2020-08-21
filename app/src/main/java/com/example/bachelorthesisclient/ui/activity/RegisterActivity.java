package com.example.bachelorthesisclient.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bachelorthesisclient.R;
import com.example.bachelorthesisclient.model.Register;
import com.example.bachelorthesisclient.model.RegisterResponse;
import com.example.bachelorthesisclient.service.AuthService;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class RegisterActivity extends AppCompatActivity {

    EditText etEmail;
    EditText etFirstName;
    EditText etLastName;
    EditText etPassword;
    EditText etRetypePassword;
    Button buttonRegister;
    TextView tvHaveAccount;
    TextView tvError;

    CompositeDisposable compositeDisposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        compositeDisposable = new CompositeDisposable();

        this.getUIComponents();
        this.setListeners();
    }

    @Override
    protected void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        super.onDestroy();
    }

    private void getUIComponents() {
        etEmail = findViewById(R.id.et_register_email);
        etFirstName = findViewById(R.id.et_register_first_name);
        etLastName = findViewById(R.id.et_register_last_name);
        etPassword = findViewById(R.id.et_register_password);
        etRetypePassword = findViewById(R.id.et_register_retype_password);
        buttonRegister = findViewById(R.id.button_register);
        tvHaveAccount = findViewById(R.id.tv_have_an_account);
        tvError = findViewById(R.id.tv_register_error);
    }

    private void setListeners() {
        this.tvHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        this.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpAction();
            }
        });
    }

    private void signUpAction() {
        String email = etEmail.getText().toString().trim();
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String retypePassword = etRetypePassword.getText().toString().trim();

        Register register = new Register();
        try {
            register.setEmail(email);
            register.setFirstName(firstName);
            register.setLastName(lastName);
            register.setPassword(password, retypePassword);
        } catch (Exception ex) {
            this.setErrorText(ex.getMessage());
            return;
        }

        AuthService.getInstance().registerUser(register)
                .subscribe(new SingleObserver<RegisterResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(RegisterResponse registerResponse) {
                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        setErrorText("Something went wrong");
                    }
                });

    }

    private void setErrorText(String text) {
        if (this.tvError != null) {
            tvError.setText(text);
            tvError.setVisibility(TextView.VISIBLE);
        }
    }
}
