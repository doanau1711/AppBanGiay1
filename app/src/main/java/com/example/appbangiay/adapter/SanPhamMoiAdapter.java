package com.example.appbangiay.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class SanPhamMoiAdapter extends RecyclerView.Adapter<SanPhamMoiAdapter.MyViewHolder> {
    Context context;
    List<SanPham> array;

    public SanPhamMoiAdapter(Context context, List<SanPham> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_san_pham_moi,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SanPham sanPham = array.get(position);

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtGia.setText("Giá: " + decimalFormat.format(Double.parseDouble(sanPham.getGiaSanPham()))+ "Đ");
        holder.txtTen.setText(sanPham.getTenSanPham());
        Glide.with(context).load(sanPham.getHinhAnh()).into(holder.imgHinhAnh);
        holder.setItemClickListener(new ItemClickListener() {
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
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView txtGia, txtTen;
            ImageView imgHinhAnh;
            private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtGia = itemView.findViewById(R.id.item_sp_moi_gia);
            txtTen = itemView.findViewById(R.id.item_sp_moi_ten);
            imgHinhAnh = itemView.findViewById(R.id.item_sp_moi_image);
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
