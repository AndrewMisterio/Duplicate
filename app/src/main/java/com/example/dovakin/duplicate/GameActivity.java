package com.example.dovakin.duplicate;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class GameActivity extends Activity {

    private SharedPreferences sPref;

    ProgressBar PB;
    GridView gridView;
    Integer counter=0,column=1,countDuplicate=0,gmstl=0;
    tileAdapter tA;
    Handler handler,handlerBar;
    controller CTRL;
    TextView tapView;
    static List<Bitmap> icon = new ArrayList<>();

    boolean exit=false;
    MyDialogFragment myDialogFragment;
    int threadcount=0;

    GridLayout gl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        load();

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        int x=176,y=203;
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.dota);

        CTRL = new controller(this,counter,countDuplicate, gmstl);
        icon.clear();
        icon.add(bitmapResize(Bitmap.createBitmap(b, 0, 0, x - 2, y),(float)(CTRL.getRow())));
        for(int n=0;n<10;n++)
            for(int m=0;m<10;m++) {
                icon.add(bitmapResize(Bitmap.createBitmap(b, x * n, y * (m+1), x - 2, y),(float)(CTRL.getRow())));
            }
        Log.i("y",""+CTRL.getRow());
        CTRL.createList();
        tA=new tileAdapter(this, CTRL.getL());
        tA.setUnknownItem(getImage(0));
        tapView=(TextView) findViewById(R.id.viewTap);
        gridView = (GridView) findViewById(R.id.gw);
        gridView.setNumColumns(CTRL.getColumn());
        gridView.setAdapter(tA);
        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                CTRL.checkList(position);
                }
        });
        handler = new Handler() {
            public void handleMessage(Message msg) {
                adapted();
            }
        };
        handlerBar = new Handler() {
            public void handleMessage(Message msg) {
                if(msg.what==0)
                    PB.setProgress(PB.getProgress()+10);
                if(msg.what==1){
                    dialog(getResources().getString(R.string.youlose)+""+ CTRL.getCountTap());}
            }
        };
        PB = (ProgressBar) findViewById(R.id.progressBar);
        switch (gmstl){
            case 0: PB.setVisibility(ProgressBar.INVISIBLE);
                break;
            case 1:
                PB.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar));
                PB.setMax(CTRL.optimalTime());
                startTime(CTRL.optimalTime());
                break;
            case 2: PB.setVisibility(ProgressBar.INVISIBLE);
                break;
        }

        myDialogFragment = new MyDialogFragment();
        myDialogFragment.setActivity(this);
    }
    public Bitmap bitmapResize(Bitmap imageBitmap, float scale) {

        Bitmap bitmap = imageBitmap;
        float lengthbmp = bitmap.getHeight();
        float widthbmp = bitmap.getWidth();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        float hight = (float)(displaymetrics.heightPixels*0.95f) / scale;
        Log.i("scale",""+CTRL.getSize());
        float width = widthbmp/(lengthbmp/hight);

        int convertHighet = (int) hight, convertWidth = (int) width;

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        return bitmap;
    }
    public Bitmap getImage(int n){
        return icon.get(n);
    }
    public void setColumn(Integer c){column=c;}

    public void setTextTapView(String s){ tapView.setText(s+(gmstl==2?("/"+CTRL.optimalTaps()):""));}

    public void replGrid(final Integer i){
        new Thread(new Runnable() {
            public void run() {
                try {

                    Thread.sleep(i);
                    CTRL.closeAll();
                    handler.sendEmptyMessage(0);
                }
                catch (InterruptedException e) {
                }
            }
        }).start();

    }

    public void startTime(final Integer t){
        threadcount++;
        new Thread(new Runnable() {
            public void run() {
                try {
                    for(int i=0;i<t&&!exit;i+=10){
                        handlerBar.sendEmptyMessage(0);
                        Thread.sleep(10);
                        if(threadcount>1){ threadcount--; break;}
                        if(i>=t-10)
                            handlerBar.sendEmptyMessage(1);
                    }threadcount--;
                }
                catch (Exception e) {
                }
            }
        }).start();
    }

    public void onClickR(View v){
        mixItem();
    }
    public void mixItem(){
        CTRL.createList();
        adapted();
        PB.setProgress(0);
        if(gmstl==1) startTime(20000);
    }
    private void load(){
        sPref = getSharedPreferences("OPTION", MODE_PRIVATE);
        counter = Integer.parseInt(sPref.getString("sizeGrid", ""));
        countDuplicate = Integer.parseInt(sPref.getString("counterDuplicate", ""));
        gmstl = Integer.parseInt(sPref.getString("gameStyle", ""));
    }
    public void adapted(){
        gridView.setAdapter(tA);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        exit=true;
    }
    public void dialog(String res){
        myDialogFragment.setResult(res);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        myDialogFragment.show(transaction, "dialog");
    }
}
