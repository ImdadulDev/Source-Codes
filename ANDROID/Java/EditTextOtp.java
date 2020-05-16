package com.android.rpos.ui.inflateViews;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.android.rpos.R;

public class EditTextOtp extends LinearLayout implements View.OnFocusChangeListener, TextWatcher,
        View.OnKeyListener {

    private EditText etDigit1;
    private EditText etDigit2;
    private EditText etDigit3;
    private EditText etDigit4;
    private EditText etDigitCurrent;

    public EditTextOtp(Context context) {
        super(context);
        initialize(null);
    }

    public EditTextOtp(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public EditTextOtp(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    private void initialize(AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.edit_text_otp, this);
        }

        etDigit1 = findViewById(R.id.etDigit1);
        etDigit2 = findViewById(R.id.etDigit2);
        etDigit3 = findViewById(R.id.etDigit3);
        etDigit4 = findViewById(R.id.etDigit4);

        initFocusListener();
        initTextChangeListener();
        initKeyListener();

    }

    private void initFocusListener() {
        etDigit1.setOnFocusChangeListener(this);
        etDigit2.setOnFocusChangeListener(this);
        etDigit3.setOnFocusChangeListener(this);
        etDigit4.setOnFocusChangeListener(this);
    }

    private void initTextChangeListener() {
        etDigit1.addTextChangedListener(this);
        etDigit2.addTextChangedListener(this);
        etDigit3.addTextChangedListener(this);
        etDigit4.addTextChangedListener(this);
    }

    private void initKeyListener() {
        etDigit1.setOnKeyListener(this);
        etDigit2.setOnKeyListener(this);
        etDigit3.setOnKeyListener(this);
        etDigit4.setOnKeyListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        etDigitCurrent = (EditText) v;
        etDigitCurrent.setSelection(etDigitCurrent.getText().length());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        try {
            if (etDigitCurrent.getText().length() >= 1 && etDigitCurrent != etDigit4) {

                etDigitCurrent.focusSearch(View.FOCUS_RIGHT).requestFocus();

            } else if (etDigitCurrent.getText().length() >= 1 && etDigitCurrent == etDigit4) {

                InputMethodManager imm = (InputMethodManager) getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);

                if (imm != null)
                    imm.hideSoftInputFromWindow(getWindowToken(), 0);

            } else {
                String curretValue = etDigitCurrent.getText().toString();
                if (curretValue.length() <= 0 && etDigitCurrent.getSelectionStart() <= 0) {
                    etDigitCurrent.focusSearch(View.FOCUS_LEFT).requestFocus();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public String getOtp() {
        return makeOtp();
    }

    public void setOtp(String otp) {
        if (otp.length() != 4) return;
        etDigit1.setText(String.valueOf(otp.charAt(0)));
        etDigit2.setText(String.valueOf(otp.charAt(1)));
        etDigit3.setText(String.valueOf(otp.charAt(2)));
        etDigit4.setText(String.valueOf(otp.charAt(3)));
    }

    private String makeOtp() {
        StringBuilder sb = new StringBuilder();
        sb.append(etDigit1.getText().toString());
        sb.append(etDigit2.getText().toString());
        sb.append(etDigit3.getText().toString());
        sb.append(etDigit4.getText().toString());
        return sb.toString();
    }

    public boolean isValid() {
        return makeOtp().length() == 4;
    }

    public EditText getCurrentFocused() {
        return etDigitCurrent;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {

            int id = v.getId();

            switch (id) {
                case R.id.etDigit1:

                    return isKeyDel(etDigit1, keyCode);

                case R.id.etDigit2:

                    return isKeyDel(etDigit2, keyCode);

                case R.id.etDigit3:

                    return isKeyDel(etDigit3, keyCode);

                case R.id.etDigit4:

                    return isKeyDel(etDigit4, keyCode);

                default:
                    return false;
            }

        }

        return false;
    }

    private boolean isKeyDel(EditText etDigit, int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            etDigit.setText(null);
            return true;
        }
        return false;
    }
}



// layout : edit_text_otp
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="horizontal"
    android:padding="10dp">

    <EditText
        android:id="@+id/etDigit1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:background="@drawable/kotak_edit_text_otp"
        android:gravity="center"
        android:inputType="number"
        android:maxLength="1"
        android:maxLines="1"
        android:nextFocusLeft="@+id/etDigit1"
        android:nextFocusRight="@+id/etDigit2" />

    <EditText
        android:id="@+id/etDigit2"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:background="@drawable/kotak_edit_text_otp"
        android:gravity="center"
        android:inputType="number"
        android:maxLength="1"
        android:maxLines="1"
        android:nextFocusLeft="@+id/etDigit1"
        android:nextFocusRight="@+id/etDigit3" />

    <EditText
        android:id="@+id/etDigit3"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:background="@drawable/kotak_edit_text_otp"
        android:gravity="center"
        android:inputType="number"
        android:maxLength="1"
        android:maxLines="1"
        android:nextFocusLeft="@+id/etDigit2"
        android:nextFocusRight="@+id/etDigit4" />

    <EditText
        android:id="@+id/etDigit4"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:background="@drawable/kotak_edit_text_otp"
        android:gravity="center"
        android:inputType="number"
        android:maxLength="1"
        android:maxLines="1"
        android:nextFocusLeft="@+id/etDigit3"
        android:nextFocusRight="@+id/etDigit4" />

</LinearLayout>

// how to use it - In xml
                <com.android.rpos.ui.inflateViews.EditTextOtp
                android:id="@+id/otp_view"
                android:layout_width="450dp"
                android:layout_height="wrap_content"
                android:layout_marginTop="72dp"
                android:inputType="numberPassword"
                android:itemBackground="@drawable/ui_border"
                android:textColor="@android:color/white"/>

in activity: 

        val editTextOtp = findViewById<View>(R.id.otp_view) as EditTextOtp

        // set value
        // set value
        editTextOtp.otp = "1234"
        //get value
        //get value
        editTextOtp.otp
        //get current request
        //get current request
        editTextOtp.currentFocused
        //isValid 4 digit
        //isValid 4 digit
        editTextOtp.isValid
