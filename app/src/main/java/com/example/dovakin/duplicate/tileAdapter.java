package com.example.dovakin.duplicate;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by DOVAKIN on 22.10.2016.
 */

public class tileAdapter extends BaseAdapter {
    private Context context;
    private List<tile> products;
    private View item;
    private Bitmap image;
    private ImageView im;

    public tileAdapter(Context context, List<tile> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    public void setUnknownItem(Bitmap i){image=i;}

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            item = new View(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
            item = inflater.inflate(R.layout.tile, parent, false);
            im = (ImageView)item.findViewById(R.id.imageView3);
            if(products.get(position).getClick()){
                im.setImageBitmap(products.get(position).getImage());
            }
            else {
                im.setImageBitmap(image);
            }

            if(products.get(position).getFound()) {
                im.setVisibility(View.INVISIBLE);
            }
            im.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            item = (View) convertView;
        }
        item.setId(position);

        return item;
    }
}
