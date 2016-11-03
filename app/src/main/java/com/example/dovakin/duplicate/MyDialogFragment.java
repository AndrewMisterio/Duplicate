package com.example.dovakin.duplicate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

/**
 * Created by DOVAKIN on 01.11.2016.
 */

public class MyDialogFragment extends DialogFragment {
    private GameActivity GA;
    String result;
    public void setActivity(GameActivity ga){
        GA=ga;
    }
    public void setResult(String res){
        result=res;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String button1String = getResources().getString(R.string.again);;
        String button2String = getResources().getString(R.string.exit);;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.gameover));  // заголовок
        builder.setMessage(result);
        builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                GA.mixItem();
            }
        });
        builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                GA.finish();
            }
        });
        builder.setCancelable(true);

        return builder.create();
    }
}
