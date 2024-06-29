package com.example.appbangiay.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appbangiay.R;
import com.example.appbangiay.model.DanhMuc;
import com.example.appbangiay.utils.Utils;

import java.util.List;


public class DanhMucAdapter extends BaseAdapter {

    List<DanhMuc> array;
    Context context;

    public DanhMucAdapter(List<DanhMuc> array, Context context) {
        this.array = array;
        this.context = context;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public static class ViewHolder {
        TextView textTenDanhMuc;
        ImageView ivHinhAnh;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_danh_muc, null);
            viewHolder.textTenDanhMuc = view.findViewById(R.id.item_ten_danh_muc);
            viewHolder.ivHinhAnh = view.findViewById(R.id.item_danh_muc_image);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();

        }
        viewHolder.textTenDanhMuc.setText(array.get(i).getTenDanhMuc());
        Glide.with(context).load(array.get(i).getHinhAnh()).into(viewHolder.ivHinhAnh);

            return view;
        }

}
