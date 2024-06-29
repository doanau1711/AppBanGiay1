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

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhapActivity extends AppCompatActivity {
    private EditText emailET, passwordET;
    private ImageView passwordIcon;
    private AppCompatButton signInBtn;

    private TextView forgotPasswordBtn, signUpBtn;
    ApiBanGiay apiBanGiay;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    private boolean passwordShowing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        setControl();
        setEvent();
    }

    private void setEvent() {
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_email = emailET.getText().toString().trim();
                String str_pass = passwordET.getText().toString().trim();
                if(TextUtils.isEmpty(str_email)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập email",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(str_pass)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập mật khẩu",Toast.LENGTH_SHORT).show();
                }else {

                    // save information login, mỗi khi out ứng dụng nó tự lưu
                    Paper.book().write("email",str_email);
                    Paper.book().write("password",str_pass);
                    compositeDisposable.add(apiBanGiay.dangnhap(str_email,str_pass)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        if(userModel.isSuccess()){
                                            Utils.user_current = userModel.getResult().get(0);
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else {
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

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DangNhapActivity.this, DangKyActivity.class));
            }
        });

        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DangNhapActivity.this, ResetPasswordActivity.class));
            }
        });

        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check if password showing or not
                if (passwordShowing) {
                    passwordShowing = false;
                    passwordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.hienthi_mk);
                } else {
                    passwordShowing = true;
                    passwordET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.an_mk);
                }
                // move the cursor at last of the text
                passwordET.setSelection(passwordET.length());
            }
        });
    }

    private void setControl() {
        Paper.init(this);
        apiBanGiay = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanGiay.class);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        passwordIcon = findViewById(R.id.passwordIcon);
        signInBtn = findViewById(R.id.signInBtn);
        signUpBtn = findViewById(R.id.signUpBtn);
        forgotPasswordBtn = findViewById(R.id.forgotPasswordBtn);

        // read data login sau khi thoát ứng dụng
        if(Paper.book().read("email") != null && Paper.book().read("password") != null){
            emailET.setText(Paper.book().read("email"));
            passwordET.setText(Paper.book().read("password"));
        }
    }



    // set email pass, tu dang nhap bang utils bien cuc bo
    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.user_current.getEmail() != null && Utils.user_current.getPassword() != null){
            emailET.setText(Utils.user_current.getEmail());
            passwordET.setText(Utils.user_current.getPassword());
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}