package com.example.appbangiay.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbangiay.R;
import com.example.appbangiay.retrofit.ApiBanGiay;
import com.example.appbangiay.retrofit.RetrofitClient;
import com.example.appbangiay.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKyActivity extends AppCompatActivity {
    private boolean passwordShowing = false;
    private boolean conPasswordShowing = false;
    private EditText fullname, email, mobile, password, conPassword;
    private ImageView conPasswordIcon,passwordIcon;

    private AppCompatButton signUpBtn;
    private TextView signInBtn;
    ApiBanGiay apiBanGiay;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        setControl();
        setEvent();
    }

    private void setEvent() {
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_username = fullname.getText().toString().trim();
                String str_email = email.getText().toString().trim();
                String str_pass = password.getText().toString().trim();
                String str_mobile = mobile.getText().toString().trim();
                String str_conpass = conPassword.getText().toString().trim();
                if(TextUtils.isEmpty(str_username)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập Full name",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(str_email)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(str_pass)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập mật khẩu",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(str_conpass)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập xác nhận mật khẩu", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(str_mobile)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập số Mobile", Toast.LENGTH_SHORT).show();
                }else {
                    if(str_conpass.equals(str_pass)){
                        compositeDisposable.add(apiBanGiay.dangKy(str_email,str_pass,str_username,str_mobile)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        userModel -> {
                                            if(userModel.isSuccess()){
                                                // tự chuyển emai, pass qua trang dang nhap
                                                Utils.user_current.setEmail(str_email);
                                                Utils.user_current.setPassword(str_pass);
                                                Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                                                startActivity(intent);
                                                finish();
                                                Toast.makeText(getApplicationContext(),"Đăng ký thành công",Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(getApplicationContext(),"Đăng kí thất baị",Toast.LENGTH_SHORT).show();
                                            }
                                        },
                                        throwable -> {
                                            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                ));
                    }else {
                        Toast.makeText(getApplicationContext(),"Password chưa khớp",Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DangKyActivity.this, DangNhapActivity.class));
            }
        });


        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordShowing) {
                    passwordShowing = false;
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.hienthi_mk);
                } else {
                    passwordShowing = true;
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.an_mk);
                }
                // move the cursor at last of the text
                password.setSelection(password.length());
            }
        });
        conPasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conPasswordShowing) {
                    conPasswordShowing = false;
                    conPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    conPasswordIcon.setImageResource(R.drawable.hienthi_mk);
                } else {
                    conPasswordShowing = true;
                    conPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    conPasswordIcon.setImageResource(R.drawable.an_mk);
                }
                // move the cursor at last of the text
                conPassword.setSelection(conPassword.length());
            }
        });

    }

    private void setControl() {
        apiBanGiay = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanGiay.class);
        fullname = findViewById(R.id.fullNameET);
        email = findViewById(R.id.emailET);
        mobile = findViewById(R.id.mobileET);

        password = findViewById(R.id.passwordET);
        conPassword = findViewById(R.id.conPasswordET);
        passwordIcon = findViewById(R.id.passwordIcon);
        conPasswordIcon = findViewById(R.id.conPasswordIcon);

        signUpBtn = findViewById(R.id.signUpBtn);
        signInBtn = findViewById(R.id.signInBtn);

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}