package com.example.dovakin.duplicate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
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
                im.setImageBitmap(setMonochrome(products.get(position).getImage()));
            }
            im.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            item = (View) convertView;
        }
        item.setId(position);
        return item;
    }
    private Bitmap setMonochrome(Bitmap src){
        int width = src.getWidth();
        int height = src.getHeight();

        Bitmap bitmapResult = Bitmap
                .createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvasResult = new Canvas(bitmapResult);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(filter);
        canvasResult.drawBitmap(src, 0, 0, paint);
        return bitmapResult;
    }
}
