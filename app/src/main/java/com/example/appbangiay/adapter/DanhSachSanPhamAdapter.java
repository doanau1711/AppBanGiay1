package com.example.appbangiay.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbangiay.Interface.ItemClickListener;
import com.example.appbangiay.R;
import com.example.appbangiay.activity.ChiTietSanPhamActivity;
import com.example.appbangiay.model.SanPham;

import java.text.DecimalFormat;
import java.util.List;

public class DanhSachSanPhamAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<SanPham> array;
    private static  int VIEW_TYPE_DATA =0;
    private static  int VIEW_TYPE_LOADING = 1;


    public DanhSachSanPhamAdapter(Context context, List<SanPham> array) {
        this.context = context;
        this.array = array;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DATA) {
            // Nếu có data thì ta hiện lên
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_danh_sach_san_pham, parent, false);
            return new MyViewHolder(item);
        } else {
            // Ngược lại hiện loading
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            SanPham sanPham = array.get(position);
            myViewHolder.tensp.setText(sanPham.getTenSanPham().trim());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            myViewHolder.giasp.setText("Giá: " + decimalFormat.format(Double.parseDouble(sanPham.getGiaSanPham())) + "Đ");
//            myViewHolder.mota.setText(sanPham.getMoTa());
            Glide.with(context).load(sanPham.getHinhAnh()).into(myViewHolder.imgHinhAnh);
            myViewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int pos, boolean isLongClick) {
                    if(!isLongClick){
                        //click
                        Intent intent = new Intent(context, ChiTietSanPhamActivity.class);
                        intent.putExtra("chitietsp",sanPham);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }else{
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }
    @Override
    public int getItemCount() {
        return array.size();
    }

    @Override
    public int getItemViewType(int position) {
        return array.get(position) == null ? VIEW_TYPE_LOADING:VIEW_TYPE_DATA;
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;


        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tensp,giasp;
        ImageView imgHinhAnh;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tensp = itemView.findViewById(R.id.item_danh_sach_san_pham_ten);
            giasp = itemView.findViewById(R.id.item_danh_sach_san_pham_gia);
//            mota =  itemView.findViewById(R.id.item_danh_sach_sp_mota);
            imgHinhAnh = itemView .findViewById(R.id.item_danh_sach_san_pham_image);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }
    }
}
