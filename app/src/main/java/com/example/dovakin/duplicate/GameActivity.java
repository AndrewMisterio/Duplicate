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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    static controller CTRL;
    TextView tapView;
    static List<Bitmap> icon = new ArrayList<>();
    String theme = "dota2";

    boolean exit=false;
    MyDialogFragment myDialogFragment;
    int threadcount=0;
    Animation anim = null,tapanim=null;
    GridLayout gl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        load();

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        switch (getIntent().getIntExtra("theme", 0)){
            case 0:theme="dota2";break;
            case 1:theme="pokemon";break;
        }
        cutImage();

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
        anim = AnimationUtils.loadAnimation(this,R.anim.scale);
    }

    private void cutImage(){
        int x,y,n=10,count=112;
        Bitmap b = null;
        if(theme.equals("dota2")){
            b = BitmapFactory.decodeResource(getResources(), R.drawable.dota);
            n=10;count=112;
        }
        if(theme.equals("pokemon")){
            b = BitmapFactory.decodeResource(getResources(), R.drawable.pokemon);
            n=9;count=108;
        }
        x=b.getWidth()/n;y=b.getHeight()/((count%n==0)?count/n:(count/n+1));



        CTRL = new controller(this,counter,countDuplicate, gmstl);
        icon.clear();
        icon.add(bitmapResize(Bitmap.createBitmap(b, 0, 0, x - 2, y),(float)(CTRL.getRow())));
        for(int i=0,k=0;i<n;i++)
            for(int m=0;m<count/n;m++) {
                Log.i("x"+x,"y"+(m+1));
                if(k<=count)
                    icon.add(bitmapResize(Bitmap.createBitmap(b, x * i, y * m, x, y),(float)(CTRL.getRow())));
                else break;
            }

    }

    @Override
    protected void onStart(){
        super.onStart();
        gridView.startAnimation(anim);
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
        String str = sPref.getString("sizeGrid", "");
        counter = Integer.parseInt((str.equals("")?"0":str));
        str = sPref.getString("counterDuplicate", "");
        countDuplicate = Integer.parseInt((str.equals("")?"0":str));
        str = sPref.getString("gameStyle", "");
        gmstl = Integer.parseInt((str.equals("")?"0":str));
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
    public void startAnim(int n){
        gridView.getChildAt(n).setAnimation(tapanim);
    }
    public void endAnim(int n){
        gridView.getChildAt(n).setAnimation(tapanim);
    }
}
