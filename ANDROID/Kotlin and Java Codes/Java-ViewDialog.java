package com.android.rpos.ui.inflateViews;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.android.rpos.R;

import java.util.Objects;

public class ViewDialog {

    private Activity activity;
    private Dialog dialog;
    //..we need the context else we can not create the dialog so get context in constructor
    public ViewDialog(Activity activity) {
        this.activity = activity;
    }

    public void showDialog() {

        try {
            dialog  = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //...set cancelable false so that it's never get hidden
            dialog.setCancelable(false);
            //...that's the layout i told you will inflate later
            dialog.setContentView(R.layout.custom_loading_layout);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            //...initialize the imageView form infalted layout
            dialog.findViewById(R.id.custom_loading);

        /*
        it was never easy to load gif into an ImageView before Glide or Others library
        and for doing this we need DrawableImageViewTarget to that ImageView
        */
            //GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gifImageView);

            //...now load that gif which we put inside the drawble folder here with the help of Glide

        /*Glide.with(activity)
                .load(R.drawable.img_loading)
                .placeholder(R.drawable.img_loading)
                .centerCrop()
                .into(gifImageView);*/

            //...finaly show it
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //..also create a method which will hide the dialog when some work is done
    public void hideDialog(){
        dialog.dismiss();
    }

}


// layout xml code
<?xml version="1.0" encoding="utf-8"?>
<ProgressBar
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/custom_loading"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

</ProgressBar>

// how to call 

//Showing progressBar
    val viewDialog = ViewDialog(this);
    viewDialog.showDialog()

    val handler = Handler()
    handler.postDelayed(Runnable {
        //...here i'm waiting 1 seconds before hiding the custom dialog
        //...you can do whenever you want or whenever your work is done
        
        )

        viewDialog.hideDialog()
    }, 1000)


    }
