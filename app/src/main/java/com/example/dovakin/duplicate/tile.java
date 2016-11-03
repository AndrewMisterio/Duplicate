package com.example.dovakin.duplicate;

import android.graphics.Bitmap;
import android.media.Image;
import android.widget.ImageView;

/**
 * Created by DOVAKIN on 22.10.2016.
 */

public class tile {
    //Image I;
    String text;
    boolean click = false;//нажата
    boolean found = false;//угадана
    Bitmap image;

    public void setImage(Bitmap i){image=i;}
    public Bitmap getImage(){return image;}


    public boolean getFound(){return found;}
    public void setFound(boolean f){found=f;}

    public boolean getClick(){return click;}
    public void setClick(boolean b){click=b;}
    public void setText(String t){
        text=t;
    }
    public String getText(){return text;}

    public tile(String t){text=t;}

}
