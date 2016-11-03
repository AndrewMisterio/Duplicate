package com.example.dovakin.duplicate;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by DOVAKIN on 22.10.2016.
 */

public class tileAdapter extends BaseAdapter {
    private Context context;
    private List<tile> products;
    private View item;

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

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            item = new View(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            item = inflater.inflate(R.layout.tile, parent, false);
            ((TextView) item.findViewById(R.id.textView)).setText((products.get(position).getClick())?products.get(position).getText():"?");
            if(products.get(position).getFound()) {
                ((TextView) item.findViewById(R.id.textView)).setText(products.get(position).getText());
                ((TextView) item.findViewById(R.id.textView)).setTextColor(Color.GRAY);
            }
        } else {
            item = (View) convertView;
        }
        item.setId(position);

        return item;
    }
}
