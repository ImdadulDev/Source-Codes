package com.android.rpos.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.android.rpos.R
import com.android.rpos.data.db.entities.UserLoginEntity
import com.android.rpos.data.repositories.AuthRepository
import com.android.rpos.helpers.*
import com.android.rpos.listeners.RecyclerViewTouchListener
import com.android.rpos.ui.datamanagement.adpters.LoginButtonLayoutGridRecyclerAdapter
import com.android.rpos.ui.datamanagement.models.SalesModeButtonsLayoutModel
import com.android.rpos.data.network.bodyrawjson.UserLoginBodyModel
import com.android.rpos.util.Coroutines
import com.android.rpos.util.EqualSpacingItemDecoration
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

        login_tv_change_pass.setOnClickListener {
            if (login_tv_header.text == getString(R.string.change_pass)) {
                login_tv_header.text = getString(R.string.enter_your_access_key)
                login_tv_change_pass.text = getString(R.string.change_password_link_txt)
                loginNewPasswordTextInputLayout.visibility = View.GONE
            } else {
                login_tv_header.text = getString(R.string.change_pass)
                login_tv_change_pass.text = getString(R.string.click_to_login)
                loginNewPasswordTextInputLayout.visibility = View.VISIBLE
            }

        }



        initLoginKeysGridView()
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
                loginNewPasswordTextInputEditText.showSoftInputOnFocus = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //Code for sales mode buttons layout
    private fun initLoginKeysGridView() {

        login_rv_keypad.layoutManager = GridLayoutManager(this, 3)
        //This will for default android divider
        login_rv_keypad.addItemDecoration(
            EqualSpacingItemDecoration(
                20,
                EqualSpacingItemDecoration.GRID
            )
        )

        val buttonsTextListAdapter = LoginButtonLayoutGridRecyclerAdapter()
        login_rv_keypad.adapter = buttonsTextListAdapter
        buttonsTextListAdapter.setButtonList(generateSalesModeKeysData())

        login_rv_keypad.addOnItemTouchListener(
            RecyclerViewTouchListener(
                applicationContext,
                login_rv_keypad,
                object : RecyclerViewTouchListener.ClickListener {
                    override fun onClick(
                        view: View?,
                        position: Int
                    ) {
                        val nPosition = position + 1
                        val inputUserIdValue = loginUserIDTextInputEditText.text.toString().trim()
                        val inputUserPass = loginPasswordTextInputEditText.text.toString().trim()
                        val inputUserConfPass =
                            loginNewPasswordTextInputEditText.text.toString().trim()


                        //Log.d("---- 1 ", "$position")
                        //Log.d("---- 2 ", "${loginUserIDTextInputEditText.isFocused}")

                        when {
                            loginUserIDTextInputEditText.isFocused -> {
                                when (position) {
                                    9 -> {
                                        if (inputUserIdValue.isNotEmpty()) {
                                            loginUserIDTextInputEditText.setText(
                                                inputUserIdValue.substring(
                                                    0,
                                                    inputUserIdValue.length - 1
                                                )
                                            )

                                            loginUserIDTextInputEditText.setSelection(inputUserIdValue.length - 1)
                                        }
                                    }

                                    10 -> {
                                        loginUserIDTextInputEditText.setText(inputUserIdValue + "0")
                                        loginUserIDTextInputEditText.setSelection(inputUserIdValue.length + 1)
                                    }

                                    11 -> {

                                        if (login_tv_header.text == getString(R.string.change_pass)) {
                                            doForgotPassValidation(
                                                inputUserIdValue,
                                                inputUserPass,
                                                inputUserConfPass
                                            )
                                        } else {
                                            doLoginValidation(inputUserIdValue, inputUserPass)
                                        }
                                    }

                                    else -> {
                                        loginUserIDTextInputEditText.setText(inputUserIdValue + "$nPosition")
                                        loginUserIDTextInputEditText.setSelection(inputUserIdValue.length + 1)
                                    }
                                }
                            }
                            loginPasswordTextInputEditText.isFocused -> {
                                when (position) {

                                    9 -> {
                                        if (inputUserPass.isNotEmpty()) {
                                            loginPasswordTextInputEditText.setText(
                                                inputUserPass.substring(
                                                    0,
                                                    inputUserPass.length - 1
                                                )
                                            )

                                            loginPasswordTextInputEditText.setSelection(inputUserPass.length - 1)
                                        }
                                    }

                                    10 -> {
                                        loginPasswordTextInputEditText.setText(inputUserPass + "0")
                                        loginPasswordTextInputEditText.setSelection(inputUserPass.length + 1);
                                    }

                                    11 -> {
                                        if (login_tv_header.text == getString(R.string.change_pass)) {
                                            doForgotPassValidation(
                                                inputUserIdValue,
                                                inputUserPass,
                                                inputUserConfPass
                                            )
                                        } else {
                                            doLoginValidation(inputUserIdValue, inputUserPass)
                                        }
                                    }

                                    else -> {
                                        loginPasswordTextInputEditText.setText(inputUserPass + "$nPosition")
                                        loginPasswordTextInputEditText.setSelection(inputUserPass.length + 1)
                                    }
                                }
                            }
                            loginNewPasswordTextInputEditText.isFocused -> {
                                when (position) {

                                    9 -> {
                                        if (inputUserConfPass.isNotEmpty()) {
                                            loginNewPasswordTextInputEditText.setText(
                                                inputUserConfPass.substring(
                                                    0,
                                                    inputUserConfPass.length - 1
                                                )
                                            )
                                            loginNewPasswordTextInputEditText.setSelection(
                                                inputUserConfPass.length - 1
                                            )
                                        }
                                    }

                                    10 -> {
                                        loginNewPasswordTextInputEditText.setText(inputUserConfPass + "0")
                                        loginNewPasswordTextInputEditText.setSelection(inputUserConfPass.length + 1)
                                    }

                                    11 -> {
                                        if (login_tv_header.text == "Change Password") {
                                            doForgotPassValidation(
                                                inputUserIdValue,
                                                inputUserPass,
                                                inputUserConfPass
                                            )
                                        } else {
                                            doLoginValidation(inputUserIdValue, inputUserPass)
                                        }
                                    }

                                    else -> {
                                        loginNewPasswordTextInputEditText.setText(inputUserConfPass + "$nPosition")
                                        loginNewPasswordTextInputEditText.setSelection(inputUserConfPass.length + 1)
                                    }
                                }
                            }
                        }
                    }


                    override fun onLongClick(view: View?, position: Int) {}
                })
        )
    }

    private fun doLoginValidation(inputUserIdValue: String, inputUserPass: String) {
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
            loginNewPasswordTextInputEditText.error = "Field can't be empty"
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

