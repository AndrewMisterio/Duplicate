package com.example.dovakin.duplicate;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by DOVAKIN on 22.10.2016.
 */

public class controller {

    private GameActivity GA;
    private Integer size[][] = {{8,12,20},{15,24,36},{16,48,64}};     //packs size countDuplicate {{2}{3}{4}}
    private Integer cS=1;//count for size
    private List<tile> L= new ArrayList<>();
    private Integer countDuplicate=2;

    private Integer countTap=0, valuetimeout=0;
    private Integer gameStyle=0; //gamestyle

    boolean enebleThread = false;

    public void setEnebleThread(boolean p){
        enebleThread=p;
    }

    controller (GameActivity g, Integer s,int cD, Integer gs){
        gameStyle = gs;
        GA=g;
        countDuplicate=cD+2;
        setSize(s);
    }
    void setSize(Integer s){cS=s;createList();}

    public Integer getColumn(){return (int)Math.sqrt(size[countDuplicate-2][cS]);}

    public List getL(){return L;}

    void createList(){
        L.clear();
        for (int i = 0; i < size[countDuplicate-2][cS]; i++)
            L.add(new tile("" + (int)(i / countDuplicate)));
        mixList();
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
        if(p) GA.dialog(GA.getResources().getString(R.string.youwin));
    }

}
