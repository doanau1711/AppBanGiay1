package com.example.appbangiay.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appbangiay.R;
import com.example.appbangiay.retrofit.ApiBanGiay;
import com.example.appbangiay.retrofit.RetrofitClient;
import com.example.appbangiay.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ResetPasswordActivity extends AppCompatActivity {
    Button btnCancel, btnReset;
    EditText edtEmail;
    ApiBanGiay apiBanGiay;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        setControl();
        setEvent();
    }

    private void setEvent() {
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_email = edtEmail.getText().toString();
                if (TextUtils.isEmpty(str_email) && !Patterns.EMAIL_ADDRESS.matcher(str_email).matches()) {
                    Toast.makeText(ResetPasswordActivity.this, "Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
                }else{
                    compositeDisposable.add(apiBanGiay.resetPassword(str_email)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        if(userModel.isSuccess()){
                                            Toast.makeText(getApplicationContext(),userModel.getMessage(),Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            Toast.makeText(getApplicationContext(),userModel.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
    }

    private void setControl() {
        apiBanGiay = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanGiay.class);
        btnCancel = findViewById(R.id.btnCancel);
        btnReset = findViewById(R.id.btnReset);
        edtEmail = findViewById(R.id.emailBox);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}