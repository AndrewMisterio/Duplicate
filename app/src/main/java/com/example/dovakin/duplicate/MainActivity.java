package com.example.dovakin.duplicate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import static android.R.attr.data;

public class MainActivity extends AppCompatActivity {

    static{
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    }

    String select_size="",select_counterDuplicate="",select_gamestyle="";

    private SharedPreferences sPref;
    Spinner spinner1,spinner2,spinner3;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.imageView);
        image.setImageBitmap(getImage(0,0));

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
    private Bitmap getImage(int n,int m){
        int x=176,y=203;
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.dota);
        return Bitmap.createBitmap(b, x*n, y*m, x-2, y);
    }
    public void onClickStart(View v){
        save();
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
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
