package com.example.appbangiay.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.appbangiay.R;
import com.example.appbangiay.model.SanPham;

import java.text.DecimalFormat;
import java.util.Objects;

public class ChiTietSanPhamActivity extends AppCompatActivity {
    TextView tensp,giasp,mota;
    Button btnthem;
    ImageView imgHinhAnh;
    Spinner spinner;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);
        setControl();
        ActionBar();
        initData();
    }

    private void initData() {
        SanPham sanPham = (SanPham) getIntent().getSerializableExtra("chitietsp");
        tensp.setText(sanPham.getTenSanPham());
        mota.setText(sanPham.getMoTa());
        Glide.with(getApplicationContext()).load(sanPham.getHinhAnh()).into(imgHinhAnh);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        giasp.setText("Giá: " + decimalFormat.format(Double.parseDouble(sanPham.getGiaSanPham())) + "Đ");
        Integer[] so = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,so);
        spinner.setAdapter(adapterspin);

    }

    private void setControl() {
        tensp = findViewById(R.id.chi_tiet_sp_ten);
        giasp = findViewById(R.id.chi_tiet_sp_gia);
        mota = findViewById(R.id.txtmotachitiet);
        btnthem = findViewById(R.id.btnthemvaogiohang);
        spinner = findViewById(R.id.spinner);
        imgHinhAnh = findViewById(R.id.imgChitiet);
        toolbar = findViewById(R.id.toolbarChitietSp);
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}