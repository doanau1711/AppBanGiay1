package com.example.appbangiay.activity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.appbangiay.R;
import com.example.appbangiay.adapter.DanhMucAdapter;
import com.example.appbangiay.adapter.SanPhamMoiAdapter;
import com.example.appbangiay.model.DanhMuc;
import com.example.appbangiay.model.SanPham;
import com.example.appbangiay.model.SanPhamMoiModel;
import com.example.appbangiay.retrofit.ApiBanGiay;
import com.example.appbangiay.retrofit.RetrofitClient;
import com.example.appbangiay.utils.Utils;
import com.google.android.material.navigation.NavigationView;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerviewManHinhChinh;
    NavigationView navigationView;
    ListView listViewManHinhChinh;
    DanhMucAdapter danhMucAdapter;
    List<DanhMuc> mangDanhMuc;
    List<SanPham> mangSpMoi;
    SanPhamMoiAdapter  spMoiAdapter;


    DrawerLayout drawerLayout;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanGiay apiBanGiay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setControl();
        ActionBar();
        apiBanGiay = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanGiay.class);

        if(isConnected(this)){
            ActionViewFlipper();
            getDanhMuc();
            getSanPhamMoi();
            getEventClick();
        }
        else{
            Toast.makeText(getApplicationContext(), " Khong co internet", Toast.LENGTH_SHORT).show();

        }
    }

    private void getEventClick() {
        listViewManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                    Intent trangChu = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(trangChu);
                    break;
                    case 1:
                        Intent danhsachsanpham = new Intent(getApplicationContext(), DanhSachSanPhamActivity.class);

                        startActivity(danhsachsanpham);
                        break;
                    case  2:
                        Intent donhang = new Intent(getApplicationContext(), XemDonHangActivity.class);
                        startActivity(donhang);
                        break;
                    case  3:
                        Intent vitri = new Intent(getApplicationContext(), MapActivity.class);
                        startActivity(vitri);
                        break;

                }
            }
        });

    }

    private void getSanPhamMoi() {
        compositeDisposable.add(apiBanGiay.getSanPhamMoiChinh()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {

                            if(sanPhamMoiModel.isSuccess()){
                                mangSpMoi = sanPhamMoiModel.getResult();
                                spMoiAdapter = new SanPhamMoiAdapter(getApplicationContext(),mangSpMoi);
                                recyclerviewManHinhChinh.setAdapter(spMoiAdapter);
                            }else{
                                Toast.makeText(getApplicationContext(),sanPhamMoiModel.getMessage(),Toast.LENGTH_SHORT).show();
                            }

                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),"Không kết nối được với server"+ throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void getDanhMuc() {
        compositeDisposable.add(apiBanGiay.getDanhMucChinh()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        danhMucModel -> {
                            if(danhMucModel.isSuccess()){
                                mangDanhMuc = danhMucModel.getResult();
                                danhMucAdapter = new DanhMucAdapter(mangDanhMuc,getApplicationContext());
                                listViewManHinhChinh.setAdapter(danhMucAdapter);
                            }
                        }
                ));

    }


    private void ActionViewFlipper() {
        List<String> mangQuangCao = new ArrayList<>();

        mangQuangCao.add("https://myshoes.vn/image/cache/catalog/2024/nike/nk4/giay-nike-zoomx-invincible-run-fk-3-nam-xanh-navy.01-800x800.jpg");
        mangQuangCao.add("https://myshoes.vn/image/cache/catalog/2024/nike/nk4/giay-nike-downshifter-13-nu-trang-01%20(2)-800x800.jpg");
        mangQuangCao.add("https://myshoes.vn/image/cache/catalog/2024/nike/nk4/giay-nike-zoom-metcon-turbo-2-xanh-01-800x800.jpg");
        mangQuangCao.add("https://simg.zalopay.com.vn/zlp-website/assets/emoji_df7627e1d5.png");
        mangQuangCao.add("https://simg.zalopay.com.vn/zlp-website/assets/web_720x360_424_TKTK_MUC_LAI_SUAT_5_7_5d8a3bf64f.jpg");
        for (int i= 0; i < mangQuangCao.size();i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangQuangCao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        viewFlipper.setAnimation(slide_in);
        viewFlipper.setAnimation(slide_out);



    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void setControl() {
        toolbar = findViewById(R.id.toobarmanhinhchinh);
        viewFlipper = findViewById(R.id.viewfilpper);
        recyclerviewManHinhChinh =findViewById(R.id.recycleviewManHinhChinh);

        // Hiển thị các item với số lượng 2
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerviewManHinhChinh.setLayoutManager(layoutManager);
        recyclerviewManHinhChinh.setHasFixedSize(true);

        listViewManHinhChinh = findViewById(R.id.listviewmanhinhchinh);
        navigationView = findViewById(R.id.navigationview);
        drawerLayout = findViewById(R.id.drawlayout);
        // khoi tao list
        mangDanhMuc = new ArrayList<>();

        mangSpMoi = new ArrayList<>();

        // khoi tao adapter


    }
    private boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); // nho them quyen vao khong bi loi
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(wifi != null && wifi.isConnected() ||(mobile != null && mobile.isConnected())){
            return true;
        }else{
            return false;
        }

    }




}