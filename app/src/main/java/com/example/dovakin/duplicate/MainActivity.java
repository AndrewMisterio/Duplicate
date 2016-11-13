package com.example.dovakin.duplicate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.bitmap;
import static android.R.attr.data;

public class MainActivity extends AppCompatActivity{

    static{
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    }

    ImageSwitcher imageswitcher;
    int imageindex;
    List<ImageView> listbitmap = new ArrayList<>();


    String select_size="",select_counterDuplicate="",select_gamestyle="";

    private SharedPreferences sPref;
    Spinner spinner1,spinner2,spinner3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imageswitcher = (ImageSwitcher) findViewById(R.id.iswitcher);

        Animation slideInLeftAnimation = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_in_left);
        Animation slideOutRight = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_out_right);
        imageswitcher.setInAnimation(slideInLeftAnimation);
        imageswitcher.setOutAnimation(slideOutRight);


        imageswitcher.setFactory(new ViewSwitcher.ViewFactory() {

            @Override
            public View makeView() {

                ImageView imageView = new ImageView(MainActivity.this);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                LayoutParams params = new ImageSwitcher.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                imageView.setLayoutParams(params);
                return imageView;
            }
        });
        getImage();
        imageindex = 0;
        imageswitcher.setImageDrawable(listbitmap.get(imageindex).getDrawable());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.size));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner complexity
        spinner1 = (Spinner) findViewById(R.id.spinner);
        spinner1.setAdapter(adapter);
        spinner1.setSelection(0);
        select_size="0";
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                select_size=""+position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        //spinner number duplucate
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.duplicate));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setAdapter(adapter);
        spinner2.setSelection(0);
        select_counterDuplicate="0";
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                select_counterDuplicate=""+position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        //spinner game style
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.gamestyle));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner3.setAdapter(adapter);
        spinner3.setSelection(0);
        select_gamestyle="0";
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                select_gamestyle=""+position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        load();
    }

    public void onClickSwitcher(View v) {
        if (imageindex == listbitmap.size() - 1) {
            imageindex = 0;
            imageswitcher.setImageDrawable(listbitmap.get(imageindex).getDrawable());
        } else {
            imageswitcher.setImageDrawable(listbitmap.get(++imageindex).getDrawable());
        }
    }

    private void getImage(){
        Bitmap b = null;
        int x,y,n,count;

        listbitmap.clear();

        n=10;count=112;
        b = BitmapFactory.decodeResource(getResources(), R.drawable.dota);
        x=b.getWidth()/n;
        y=b.getHeight()/((count%n==0)?count/n:(count/n+1));
        listbitmap.add(new ImageView(this));
        listbitmap.get(listbitmap.size()-1).setImageBitmap(Bitmap.createBitmap(b, 0, 0, x, y));



        b = BitmapFactory.decodeResource(getResources(), R.drawable.pokemon);
        n=9;count=108;
        x=b.getWidth()/n;
        y=b.getHeight()/((count%n==0)?count/n:(count/n+1));
        listbitmap.add(new ImageView(this));
        listbitmap.get(listbitmap.size()-1).setImageBitmap(Bitmap.createBitmap(b, 0, 0, x, y));
    }
    public void onClickStart(View v){
        save();
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra("theme", imageindex);
        startActivity(intent);
    }
    private void save(){
        sPref = getSharedPreferences("OPTION", MODE_PRIVATE);
        Editor ed = sPref.edit();
        ed.putString("sizeGrid",select_size);
        ed.putString("counterDuplicate",select_counterDuplicate);
        ed.putString("gameStyle",select_gamestyle);
        ed.commit();
    }
    public void load(){
        sPref = getSharedPreferences("OPTION", MODE_PRIVATE);
        if(sPref.contains("OPTION")){
        spinner1.setSelection(Integer.parseInt(sPref.getString("sizeGrid", "")));
        spinner2.setSelection(Integer.parseInt(sPref.getString("counterDuplicate", "")));}

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        save();
    }
}
