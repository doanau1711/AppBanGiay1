package com.example.appbangiay.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.app.Notification;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;


import com.example.appbangiay.R;
import com.example.appbangiay.adapter.DanhSachSanPhamAdapter;
import com.example.appbangiay.model.SanPham;
import com.example.appbangiay.retrofit.ApiBanGiay;
import com.example.appbangiay.retrofit.RetrofitClient;
import com.example.appbangiay.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DanhSachSanPhamActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiBanGiay apiBanGiay;
    DanhSachSanPhamAdapter danhSachSanPhamAdapter;
    List<SanPham> sanPhamList;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page = 1;
    int amount = 5;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_san_pham);
        setControl();
        apiBanGiay = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanGiay.class);
        ActionBar();
        getDanhSachSp(page);
        addEventLoad();

    }

    private void addEventLoad() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!isLoading){
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition()== sanPhamList.size()-1){
                            isLoading =true;
                            loadmore();
                    }
                }
            }

        });
    }

    private void loadmore() {

        handler.post(new Runnable() {
            @Override
            public void run() {
                sanPhamList.add(null);
                danhSachSanPhamAdapter.notifyItemChanged(sanPhamList.size()-1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // remove null
                sanPhamList.remove(sanPhamList.size()-1);
                danhSachSanPhamAdapter.notifyItemRemoved(sanPhamList.size());
                page+=1;
                getDanhSachSp(page);
                danhSachSanPhamAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        },2000);
    }


    private void getDanhSachSp(int page) {
        compositeDisposable.add(apiBanGiay.getDanhSachSp(page,amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                      danhsachSpModel ->{
                        if(danhsachSpModel.isSuccess()){
                            if(danhSachSanPhamAdapter == null){
                                sanPhamList = danhsachSpModel.getResult();
                                danhSachSanPhamAdapter = new DanhSachSanPhamAdapter(getApplicationContext(),sanPhamList);
                                recyclerView.setAdapter(danhSachSanPhamAdapter);
                            }else{
                                int vitri = sanPhamList.size()-1;
                                int soluongadd= danhsachSpModel.getResult().size();
                                for(int i =0 ; i < soluongadd ; i++){
                                    sanPhamList.add(danhsachSpModel.getResult().get(i));
                                }
                                danhSachSanPhamAdapter.notifyItemRangeInserted(vitri,soluongadd);
                            }
                            }
                      },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối server", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    // Xử lý khi người dùng nhấn vào toolbar
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

    private void setControl() {
        toolbar = findViewById(R.id.toolbarDanhSachSp);
        recyclerView = findViewById(R.id.recycleviewDanhSachSp);
        // Cài đặt hiển thị cho các item trong recylerview
        linearLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        sanPhamList = new ArrayList<>();
    }
}