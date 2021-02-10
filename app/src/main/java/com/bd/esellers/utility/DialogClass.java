package com.bd.esellers.utility;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ProgressBar;

import com.bd.esellers.R;

public class DialogClass {
    private Dialog alertDialog;
//    private ProgressBarAnimation animation;

    public DialogClass(Context context) {
        this.alertDialog = new Dialog(context);
    }

    public void showProgressDialog() {
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.loading_progress);
        alertDialog.setCancelable(true);
        final ProgressBar progressBar = alertDialog.findViewById(R.id.circular_progressBar);
//        animation=new ProgressBarAnimation(progressBar, 0, 100);
//        progressBar.setAnimation(animation);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    public void closeProgressDialog(){
        if (alertDialog!=null)
            alertDialog.dismiss();
    }
}
