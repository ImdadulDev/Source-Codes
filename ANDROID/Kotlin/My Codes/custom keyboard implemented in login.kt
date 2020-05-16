package com.android.rpos.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.android.rpos.R
import com.android.rpos.data.db.entities.UserLoginEntity
import com.android.rpos.data.repositories.AuthRepository
import com.android.rpos.helpers.*
import com.android.rpos.ui.datamanagement.models.SalesModeButtonsLayoutModel
import com.android.rpos.data.network.bodyrawjson.UserLoginBodyModel
import com.android.rpos.util.Coroutines
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class Login : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val authRepository: AuthRepository by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // method to disable android soft keyboard
        window.setFlags(
            WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
            WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
        )

        val etUserId = findViewById<TextInputEditText>(R.id.loginUserIDTextInputEditText)
        val etUserPassword = findViewById<TextInputEditText>(R.id.loginPasswordTextInputEditText)

        login_tv_re_enter_id.setOnClickListener {
            login_tv_re_enter_id.visibility = View.GONE
            loginPasswordTextInputLayout.visibility = View.GONE
            loginUserIDTextInputLayout.visibility = View.VISIBLE
            login_tv_header.text = getString(R.string.enter_pass)
            etUserId.requestFocus()

        }

        a1.setOnClickListener {
            if (etUserId.isFocused) etUserId.append("1")
            else if(etUserPassword.isFocused) etUserPassword.append("1")
        }
        b2.setOnClickListener {
            if (etUserId.isFocused) etUserId.append("2")
            else if(etUserPassword.isFocused) etUserPassword.append("2")
        }
        c3.setOnClickListener {
            if (etUserId.isFocused) etUserId.append("3")
            else if(etUserPassword.isFocused) etUserPassword.append("3")
        }
        d4.setOnClickListener {
            if (etUserId.isFocused) etUserId.append("4")
            else if(etUserPassword.isFocused) etUserPassword.append("4")
        }

        e5.setOnClickListener {
            if (etUserId.isFocused) etUserId.append("5")
            else if(etUserPassword.isFocused) etUserPassword.append("5")
        }
        f6.setOnClickListener {
            if (etUserId.isFocused) etUserId.append("6")
            else if(etUserPassword.isFocused) etUserPassword.append("6")
        }
        g7.setOnClickListener {
            if (etUserId.isFocused) etUserId.append("7")
            else if(etUserPassword.isFocused) etUserPassword.append("7")
        }
        h8.setOnClickListener {
            if (etUserId.isFocused) etUserId.append("8")
            else if(etUserPassword.isFocused) etUserPassword.append("8")
        }
        K9.setOnClickListener {
            if (etUserId.isFocused) etUserId.append("9")
            else if(etUserPassword.isFocused) etUserPassword.append("9")
        }
        l0.setOnClickListener {
            if (etUserId.isFocused) etUserId.append("0")
            else if(etUserPassword.isFocused) etUserPassword.append("0")
        }



        back.setOnClickListener {
            var str = ""
            if (etUserId.isFocused){
                str = etUserId.text.toString()
                if (str.isNotEmpty()) {
                    str = str.substring(0, str.length - 1)
                    // Now set this Text to your edit text
                    etUserId.setText(str)
                    //  igboedittext.text=null
                }
            } else if(etUserPassword.isFocused) {
                str = etUserPassword.text.toString()

                if (str.isNotEmpty()) {
                    str = str.substring(0, str.length - 1)
                    // Now set this Text to your edit text
                    etUserPassword.setText(str)
                    //  igboedittext.text=null
                }
            }

        }

        right.setOnClickListener {
            if (etUserId.isFocused) doLoginUserIdValidation(etUserId.text.toString())
            else if(etUserPassword.isFocused) doLoginUserPassValidation(etUserId.text.toString(), etUserPassword.text.toString())
        }


        //initLoginKeysGridView()
    }

    override fun onStart() {
        super.onStart()

        try {
            supportActionBar!!.hide()

            //Disable device keyboard popup
            loginPasswordTextInputEditText.requestFocus()
            loginUserIDTextInputEditText.requestFocus()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                loginUserIDTextInputEditText.showSoftInputOnFocus = false
                loginPasswordTextInputEditText.showSoftInputOnFocus = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun doLoginUserIdValidation(inputUserIdValue: String) {
        if (!isValidUserId(inputUserIdValue)) {
            showSnackBar(
                applicationContext,
                login_root_layout,
                "User ID shouldn't be less than 4 digits",
                getString(R.string.ok)
            )
            loginUserIDTextInputEditText.error = "Enter valid User ID"
            return
        }

        login_tv_re_enter_id.visibility = View.VISIBLE
        loginPasswordTextInputLayout.visibility = View.VISIBLE
        loginUserIDTextInputLayout.visibility = View.GONE
        login_tv_header.text = getString(R.string.enter_pass)
        login_tv_re_enter_id.text = getString(R.string.re_enter_id)
        loginPasswordTextInputEditText.requestFocus()

    }

    private fun doLoginUserPassValidation(inputUserIdValue: String, inputUserPass: String) {

        if (!isValidPassword(inputUserPass)) {
            showSnackBar(
                applicationContext,
                login_root_layout,
                "Password shouldn't be less than 4 digits",
                getString(R.string.ok)
            )
            loginPasswordTextInputEditText.error = "Enter valid Password"
            return
        }

        doLogin(inputUserIdValue, inputUserPass)
    }

    private fun doForgotPassValidation(
        inputUserIdValue: String,
        inputUserPass: String,
        inputUserConfPass: String
    ) {

        if (!isValidUserId(inputUserIdValue)) {
            showSnackBar(
                applicationContext,
                login_root_layout,
                "User ID shouldn't be less than 4 digits",
                getString(R.string.ok)
            )
            loginUserIDTextInputEditText.error = "Enter valid User ID"
            return
        }

        if (!isValidPassword(inputUserPass)) {
            showSnackBar(
                applicationContext,
                login_root_layout,
                "Password shouldn't be less than 4 digits",
                getString(R.string.ok)
            )
            loginPasswordTextInputEditText.error = "Enter valid Password"
            return
        }

        if (!isValidConfirmPassword(inputUserPass, inputUserConfPass)) {
            showSnackBar(
                applicationContext,
                login_root_layout,
                "Old and new password didn't match",
                getString(R.string.ok)
            )
            //loginNewPasswordTextInputEditText.error = "Field can't be empty"
            return
        }

        doLogin(inputUserIdValue, inputUserPass)
    }

    private fun generateSalesModeKeysData(): List<SalesModeButtonsLayoutModel> {
        val listOfButtonsText = mutableListOf<SalesModeButtonsLayoutModel>()

        listOfButtonsText.add(SalesModeButtonsLayoutModel("1", R.color.blue_700))
        listOfButtonsText.add(SalesModeButtonsLayoutModel("2", R.color.blue_700))
        listOfButtonsText.add(SalesModeButtonsLayoutModel("3", R.color.blue_700))
        listOfButtonsText.add(SalesModeButtonsLayoutModel("4", R.color.blue_700))
        listOfButtonsText.add(SalesModeButtonsLayoutModel("5", R.color.blue_700))
        listOfButtonsText.add(SalesModeButtonsLayoutModel("6", R.color.blue_700))
        listOfButtonsText.add(SalesModeButtonsLayoutModel("7", R.color.blue_700))
        listOfButtonsText.add(SalesModeButtonsLayoutModel("8", R.color.blue_700))
        listOfButtonsText.add(SalesModeButtonsLayoutModel("9", R.color.blue_700))
        listOfButtonsText.add(SalesModeButtonsLayoutModel("B", R.color.blue_700))
        listOfButtonsText.add(SalesModeButtonsLayoutModel("0", R.color.blue_700))
        listOfButtonsText.add(SalesModeButtonsLayoutModel("R", R.color.white))

        return listOfButtonsText
    }

    @SuppressLint("HardwareIds")
    private fun doLogin(inputUserIdValue: String, inputUserPass: String) = Coroutines.main {
        login_root_layout.progress_bar_login.show()
        try {

            val intent = Intent(applicationContext, Dashboard::class.java)
            if (authRepository.getUserLoginDataFromDb().isNotEmpty()) {
                startActivity(intent)
                finish()
            } else if (checkNetworkState(applicationContext)) {
                val androidId = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)
                Log.d("----", "android_id: $androidId")
                //android_id: f98b3480ecad4d92

                val userLoginBodyModel =
                    UserLoginBodyModel(inputUserIdValue, androidId, inputUserPass)

                val userLoginResponse = authRepository.getUserLoginResponse(userLoginBodyModel)

                Log.d("----", userLoginResponse.name)
                if (userLoginResponse.status == 1) {
                    val userLoginEntity = UserLoginEntity(
                        userLoginResponse.status.toString(),
                        userLoginResponse.message,
                        userLoginResponse.name,
                        getConvertedTime(),
                        userLoginResponse.operatorid.toString(),
                        userLoginResponse.token,
                        userLoginResponse.workstationid,
                        userLoginResponse.businessGroup[0].functionId,
                        userLoginResponse.businessGroup[0].groupName,
                        userLoginResponse.businessGroup[0].noOfSelections,
                        userLoginResponse.businessgroupFunctions[0],
                        userLoginResponse.businessuits[0],
                        userLoginResponse.businessGroup[0].groupId[0]
                    )

                    authRepository.saveUserLoginData(userLoginEntity)

                    startActivity(intent)
                    finish()
                } else if (userLoginResponse.status == 0) {
                    login_root_layout.snackbar(userLoginResponse.message)
                }

            } else {
                login_root_layout.snackbar(resources.getString(R.string.no_internet_connection))
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        login_root_layout.progress_bar_login.hide()
    }
}


// layout: 
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".ui.activities.Login"
    android:id="@+id/login_root_layout"
    android:background="@drawable/img_background">

    <androidx.core.widget.NestedScrollView
        android:id="@+id/nestedScrollView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:layout_marginBottom="16dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent">

        <LinearLayout
            android:id="@+id/activity_main"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="@color/blue_700"
            android:gravity="center"
            android:orientation="vertical"
            android:paddingStart="40dp"
            android:paddingEnd="40dp">

            <TextView
                android:id="@+id/login_tv_header"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="30dp"
                android:layout_marginBottom="20dp"
                android:gravity="center"
                android:text="@string/enter_your_access_key"
                android:textColor="@android:color/white"
                android:textSize="18sp" />


            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/loginUserIDTextInputLayout"
                style="@style/LoginTextInputLayoutStyle"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_margin="5dp">

                <com.google.android.material.textfield.TextInputEditText
                    android:id="@+id/loginUserIDTextInputEditText"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:hint="@string/user_id" />
            </com.google.android.material.textfield.TextInputLayout>

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/loginPasswordTextInputLayout"
                style="@style/LoginTextInputLayoutStyle"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_margin="5dp"
                android:visibility="gone">

                <com.google.android.material.textfield.TextInputEditText
                    android:id="@+id/loginPasswordTextInputEditText"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:hint="@string/password"
                    android:inputType="numberPassword"/>

            </com.google.android.material.textfield.TextInputLayout>


            <androidx.recyclerview.widget.RecyclerView
                android:id="@+id/login_rv_keypad"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_gravity="center"
                android:layout_marginTop="15dp"
                android:theme="@style/ButtonTheme"
                android:visibility="gone"
                tools:listitem="@layout/content_login_grid_keypad" />

            <LinearLayout
                android:id="@+id/keyboardparentlayout"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical">

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:gravity="center"
                    android:layout_marginTop="10dp"
                    android:orientation="horizontal">

                    <Button
                        android:id="@+id/a1"
                        android:layout_width="@dimen/login_key_size_50_dp"
                        android:layout_height="@dimen/login_key_size_50_dp"
                        style="@style/ButtonTheme"
                        android:text="1"
                        android:textSize="24sp"
                        android:background="@color/blue_300"
                        android:layout_margin="5dp"
                        android:textColor="@color/white"
                        android:fontFamily="@font/gotham_bold"
                        android:elevation="16dp" />

                    <Button
                        android:id="@+id/b2"
                        android:layout_width="@dimen/login_key_size_50_dp"
                        android:layout_height="@dimen/login_key_size_50_dp"
                        android:text="2"
                        android:textSize="24sp"
                        android:background="@color/blue_300"
                        android:layout_margin="5dp"
                        android:textColor="@color/white"
                        android:textStyle="bold"
                        android:elevation="16dp" />

                    <Button
                        android:id="@+id/c3"
                        android:layout_width="@dimen/login_key_size_50_dp"
                        android:layout_height="@dimen/login_key_size_50_dp"
                        android:text="3"
                        android:textSize="24sp"
                        android:background="@color/blue_300"
                        android:layout_margin="5dp"
                        android:textColor="@color/white"
                        android:textStyle="bold"
                        android:elevation="16dp" />

                </LinearLayout>

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:gravity="center"
                    android:orientation="horizontal">

                    <Button
                        android:id="@+id/d4"
                        android:layout_width="@dimen/login_key_size_50_dp"
                        android:layout_height="@dimen/login_key_size_50_dp"
                        android:text="4"
                        android:textSize="24sp"
                        android:background="@color/blue_300"
                        android:layout_margin="5dp"
                        android:textColor="@color/white"
                        android:textStyle="bold"
                        android:elevation="16dp" />

                    <Button
                        android:id="@+id/e5"
                        android:layout_width="@dimen/login_key_size_50_dp"
                        android:layout_height="@dimen/login_key_size_50_dp"
                        android:text="5"
                        android:textSize="24sp"
                        android:background="@color/blue_300"
                        android:layout_margin="5dp"
                        android:textColor="@color/white"
                        android:textStyle="bold"
                        android:elevation="16dp" />

                    <Button
                        android:id="@+id/f6"
                        android:layout_width="@dimen/login_key_size_50_dp"
                        android:layout_height="@dimen/login_key_size_50_dp"
                        android:text="6"
                        android:textSize="24sp"
                        android:background="@color/blue_300"
                        android:layout_margin="5dp"
                        android:textColor="@color/white"
                        android:textStyle="bold"
                        android:elevation="16dp" />

                </LinearLayout>

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:gravity="center"
                    android:orientation="horizontal">

                    <Button
                        android:id="@+id/g7"
                        android:layout_width="@dimen/login_key_size_50_dp"
                        android:layout_height="@dimen/login_key_size_50_dp"
                        android:text="7"
                        android:textSize="24sp"
                        android:background="@color/blue_300"
                        android:layout_margin="5dp"
                        android:textColor="@color/white"
                        android:textStyle="bold"
                        android:elevation="16dp"/>

                    <Button
                        android:id="@+id/h8"
                        android:layout_width="@dimen/login_key_size_50_dp"
                        android:layout_height="@dimen/login_key_size_50_dp"
                        android:layout_margin="5dp"
                        android:background="@color/blue_300"
                        android:text="8"
                        android:textSize="24sp"
                        android:textColor="@color/white"
                        android:elevation="16dp"
                        android:textStyle="bold" />

                    <Button
                        android:id="@+id/K9"
                        android:layout_width="@dimen/login_key_size_50_dp"
                        android:layout_height="@dimen/login_key_size_50_dp"
                        android:text="9"
                        android:textSize="24sp"
                        android:background="@color/blue_300"
                        android:layout_margin="5dp"
                        android:textColor="@color/white"
                        android:elevation="16dp"
                        android:textStyle="bold"/>

                </LinearLayout>

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginBottom="16dp"
                    android:gravity="center"
                    android:orientation="horizontal">

                    <ImageButton
                        android:id="@+id/back"
                        android:layout_width="55dp"
                        android:layout_height="43dp"
                        android:scaleType="fitXY"
                        android:background="@drawable/ic_login_key_cancel"
                        android:layout_margin="5dp"
                        android:padding="2dp"
                        android:elevation="3dp"
                        android:contentDescription="@string/dummy_image_desc" />

                    <Button
                        android:id="@+id/l0"
                        android:layout_width="@dimen/login_key_size_50_dp"
                        android:layout_height="@dimen/login_key_size_50_dp"
                        android:text="0"
                        android:textSize="24sp"
                        android:background="@color/blue_300"
                        android:layout_margin="5dp"
                        android:textColor="@color/white"
                        android:fontFamily="@font/gotham_bold"
                        android:elevation="16dp" />

                    <ImageView
                        android:id="@+id/right"
                        android:layout_width="52dp"
                        android:layout_height="40dp"
                        android:src="@drawable/ic_login_key_right"
                        android:scaleType="fitXY"
                        android:layout_margin="5dp"
                        android:elevation="16dp" />

                </LinearLayout>


            </LinearLayout>


            <TextView
                android:id="@+id/login_tv_re_enter_id"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="10dp"
                android:layout_marginBottom="20dp"
                android:gravity="center"
                android:text="@string/re_enter_id"
                android:textColor="@color/white"
                android:textSize="18sp"
                android:visibility="gone"/>
        </LinearLayout>


    </androidx.core.widget.NestedScrollView>

    <ProgressBar
        android:id="@+id/progress_bar_login"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal|center_vertical"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        android:visibility="gone"/>

</androidx.constraintlayout.widget.ConstraintLayout>

//Theme
<style name="TextInputLayoutAppearance" parent="Theme.MaterialComponents">
        <!-- reference our hint & error styles -->
        <item name="hintTextAppearance">@style/HintText</item>
        <item name="errorTextAppearance">@style/ErrorText</item>
        <item name="android:textColor">@color/white</item>
        <item name="android:textColorHint">@color/white</item>
        <item name="colorControlNormal">@color/white</item>
        <item name="colorControlActivated">@color/blue_700</item>
        <item name="colorControlHighlight">@color/green_100</item>
        <item name="colorOnBackground">@color/blue_700</item>
    </style>

    <style name="TextInputTheme" parent="Theme.MaterialComponents">
        <item name="android:fontFamily">@font/gotham_book</item>
        <item name="colorPrimary">@color/login_btn</item>
        <item name="colorAccent">@color/login_btn</item>
    </style>
