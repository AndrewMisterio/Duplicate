package com.example.dovakin.duplicate;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by DOVAKIN on 22.10.2016.
 */

public class controller {

    private GameActivity GA;
    private Integer size[][] = {{8,12,20},{9,15,24},{12,16,48}};     //packs size countDuplicate {{2}{3}{4}}
    private Integer cS=1;//count for size
    private List<tile> L= new ArrayList<>();
    private Integer countDuplicate=2;

    private Integer countTap=0;
    private Integer gameStyle=0; //gamestyle

    boolean enebleThread = false;

    public void setEnebleThread(boolean p){
        enebleThread=p;
    }

    public Integer optimalTime(){
        return 8000*(cS+countDuplicate);
    }
    public Integer optimalTaps(){return (int)(size[countDuplicate-2][cS]*2*0.875);}

    public int getCountTap(){return countTap;}
    controller (GameActivity g, Integer s,int cD, Integer gs){
        gameStyle = gs;
        GA=g;
        countDuplicate=cD+2;
        setSize(s);
    }
    private void setSize(Integer s){cS=s;createList();}

    public Integer getColumn(){return (int)Math.sqrt(size[countDuplicate-2][cS]);}

    public List getL(){return L;}

    void createList(){
        L.clear();
        for (int i = 0; i < size[countDuplicate-2][cS]; i++)
            L.add(new tile("" + (int)(i / countDuplicate)));
        List<Bitmap> B = new ArrayList<>();
        String str = "";
        Integer j;
        final Random random = new Random();
        B.clear();
        for(int i=0;i<(size[countDuplicate-2][cS]/countDuplicate);i++){
            do{
                j=Math.abs(random.nextInt()%100);
            }while (str.contains(""+j));
            str+=j.toString()+"|";
            Log.i("str",str);
            B.add(GA.getImage(j));
        }
        for (int i = 0; i < size[countDuplicate-2][cS]; i++)
            L.get(i).setImage(B.get(Integer.parseInt(L.get(i).getText())));
        //mixList();
    }

    public void checkList(Integer n){
        int c=0;
        if(!L.get(n).getFound()&&!L.get(n).getClick()){
            for(int i=0;i<size[countDuplicate-2][cS];i++){
                c+=L.get(i).getClick()?1:0;
            }
            if(c<countDuplicate&&!enebleThread) {
                L.get(n).setClick(true);
                GA.adapted();
                countTap++;

                GA.setTextTapView(countTap.toString());
                if (c > 0) {
                    boolean p = true;
                    for (int i = 0; i < size[countDuplicate - 2][cS]; i++) {
                        if (L.get(i).getClick() && i != n) {
                            p = p && (L.get(n).getText().equals(L.get(i).getText()));
                        }
                        if (!p) break;
                    }

                    if (p&&c==countDuplicate-1) {
                        for (int i = 0; i < size[countDuplicate - 2][cS]; i++) {
                            if (L.get(i).getClick()) {
                                L.get(i).setFound(true);
                                L.get(i).setClick(false);
                                win();
                            }
                        }
                    }
                    if(countTap>=optimalTaps()&&GA.gmstl==2){
                        GA.dialog(GA.getResources().getString(R.string.youlose)+"  "+ getCountTap()+"/"+optimalTaps());
                    }
                    if(!p){ GA.replGrid(500); setEnebleThread(true);}
                }
            }
        }
    }
    public void closeAll(){
        for(int i=0;i<size[countDuplicate-2][cS];i++){
            if(!L.get(i).getFound())L.get(i).setClick(false);
        }
        setEnebleThread(false);
    }
    private void mixList() {
        countTap=0;
        final Random random = new Random();
        List<tile> LR = new ArrayList<>(L);
        L.clear();
        int r=0;
        for(int i=0;i<size[countDuplicate-2][cS];i++){
            r = Math.abs(random.nextInt()%LR.size());
            L.add(LR.get(r));
            LR.remove(r);
        }

    }
    public void win(){
        boolean p=true;
        for(int i=0;i<size[countDuplicate-2][cS];i++){
            p=p&&L.get(i).getFound();
        }
        if(p) GA.dialog(GA.getResources().getString(R.string.youwin)+""+ countTap);
    }

}
