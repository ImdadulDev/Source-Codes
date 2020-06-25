package com.android.rpos.ui.inflateViews;

import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.android.rpos.R;

import java.util.ArrayList;
import java.util.Objects;

public class MultiSelectSpinnerDialog {

    private String selectedItems = "";

    public String showMultiSelectSpinnerDialog(Context context, TextView spinnerTitleTv){
        String[] listItems;
        boolean[] checkedItems;
        ArrayList<Integer> mUserItems = new ArrayList<>();
        listItems = context.getResources().getStringArray(R.array.SpinnerList);
        checkedItems = new boolean[listItems.length];

        try {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
            mBuilder.setTitle("Select Option");
            mBuilder.setMultiChoiceItems(listItems, checkedItems, (dialogInterface, position, isChecked) -> {
    //                        if (isChecked) {
    //                            if (!mUserItems.contains(position)) {
    //                                mUserItems.add(position);
    //                            }
    //                        } else if (mUserItems.contains(position)) {
    //                            mUserItems.remove(position);
    //                        }
                if(isChecked){
                    mUserItems.add(position);
                }else{
                    mUserItems.remove((Integer.valueOf(position)));
                }
            });

            mBuilder.setCancelable(false);
            mBuilder.setPositiveButton("Ok", (dialogInterface, which) -> {

                for (int i = 0; i < mUserItems.size(); i++) {
                    selectedItems = selectedItems + listItems[mUserItems.get(i)];
                    if (i != mUserItems.size() - 1) {
                        selectedItems = selectedItems + ", ";
                    }
                }

                spinnerTitleTv.setText(selectedItems);
                //Log.d("Data: ----", selectedItems);
                dialogInterface.dismiss();
            });

            mBuilder.setNegativeButton("Dismiss", (dialogInterface, i) -> dialogInterface.dismiss());

            /* mBuilder.setNeutralButton("All Label", (dialogInterface, which) -> {
                for (int i = 0; i < checkedItems.length; i++) {
                    checkedItems[i] = false;
                    mUserItems.clear();
                    //mItemSelected.setText("");
                }
            });*/

            AlertDialog mDialog = mBuilder.create();
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            WindowManager.LayoutParams wmlp = Objects.requireNonNull(mDialog.getWindow()).getAttributes();

            wmlp.gravity = Gravity.TOP | Gravity.START;
            wmlp.x = 100;   //x position
            wmlp.y = 100;   //y position

            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return selectedItems;
    }
}


// array list
    <string-array name="SpinnerList">
        <item>Large</item>
        <item>Beer</item>
        <item>Fruit</item>
        <item>Blue</item>
        <item>Large Screen</item>
        <item>Perl</item>
    </string-array>
    
// Call from Kotlin class
       viewId.setOnClickListener {
            MultiSelectSpinnerDialog().showMultiSelectSpinnerDialog(
                activity,
                spinnerTitleTv
            )
        }
    
        
