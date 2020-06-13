package com.bottomup.android.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bottomup.android.R;
import com.bottomup.android.common.Common;
import com.bottomup.android.common.LocalStorageManager;
import com.bottomup.android.models.UserModel;
import com.bottomup.android.utils.DialogManager;
import com.coreasp.CorePushManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameTxt, emailTxt;
    private TextView genderTxt, addressText, applyTxt1, applyTxt2;

    private ArrayList<String> selectedApplyItems = new ArrayList<>();
    private int fromAc = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Common.currentActivity = this;

        fromAc = getIntent().getIntExtra("fromAc", 0);
        nameTxt = findViewById(R.id.nameTxt);
        emailTxt = findViewById(R.id.emailTxt);
        genderTxt = findViewById(R.id.genderTxt);
        final String[] genderArr = getResources().getStringArray(R.array.gender_arr);
        genderTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(Common.currentActivity);
                DialogManager.showRadioDialog(
                        Common.currentActivity,
                        null,
                        genderArr,
                        null,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                genderTxt.setText(genderArr[which]);
                            }
                        });
            }
        });
        addressText = findViewById(R.id.addressTxt);
        final String[] regionArr = getResources().getStringArray(R.array.region_arr);
        addressText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(Common.currentActivity);
                DialogManager.showRadioDialog(
                        Common.currentActivity,
                        null,
                        regionArr,
                        null,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addressText.setText(regionArr[which]);
                            }
                        });
            }
        });
        applyTxt1 = findViewById(R.id.applyTxt1);
        applyTxt2 = findViewById(R.id.applyTxt2);
        applyTxt2.setVisibility(View.GONE);
        applyTxt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(Common.currentActivity);
                final ArrayList<String> selectedItems = (ArrayList<String>) selectedApplyItems.clone();
                final boolean[] selected_items = new boolean[]{
                        false, // ビジネス
                        false, // 自己啓発
                        false, // スポーツ
                        false, // 子育て
                        false, // 教育
                        false // その他
                };
                final String[] itemList = getResources().getStringArray(R.array.apply_arr);
                int i = 0;
                for (String item : itemList) {
                    if (selectedItems.contains(item)) selected_items[i] = true;
                    i ++;
                }
                DialogManager.showMultiSelectDialog(
                        Common.currentActivity,
                        getResources().getString(R.string.apply_title),
                        itemList,
                        selected_items,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int pos, boolean isChecked) {
                                if (isChecked) {
                                    selectedItems.add(itemList[pos]);
                                } else {
                                    selectedItems.remove(itemList[pos]);
                                }
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                selectedApplyItems = (ArrayList<String>) selectedItems.clone();
                                if (selectedItems.contains("その他")) {
                                    applyTxt2.setVisibility(View.VISIBLE);
                                }else {
                                    applyTxt2.setVisibility(View.GONE);
                                }
                                String selectedStr = "";
                                for (String item : selectedApplyItems) {
                                    selectedStr = selectedStr + item + ",";
                                }
                                applyTxt1.setText(selectedStr.substring(0, selectedStr.length()-1));
                            }
                        });
            }
        });

        findViewById(R.id.registerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBlackItems()) {
//                    if (selectedApplyItems.contains("その他")) {
//                        final ArrayList<String> selectedItems = (ArrayList<String>) selectedApplyItems.clone();
//                        int i = 0;
//                        for (String item : selectedItems) {
//                            if (item.equals("その他")) {
//                                if (applyTxt2.getText().toString().equals("")) {
//                                    selectedApplyItems.remove(i);
//                                }else {
//                                    selectedApplyItems.set(i, applyTxt2.getText().toString());
//                                }
//                                break;
//                            }
//                            i ++;
//                        }
//                    }
                    HashMap<String, List<String>> multiCategoryIds = new HashMap<>();
//                    multiCategoryIds.put("1", Arrays.asList(nameTxt.getText().toString()));
//                    multiCategoryIds.put("2", Arrays.asList(emailTxt.getText().toString()));
                    multiCategoryIds.put("3", Arrays.asList(genderTxt.getText().toString()));
                    multiCategoryIds.put("4", Arrays.asList(addressText.getText().toString()));
                    multiCategoryIds.put("5", selectedApplyItems);
                    CorePushManager.getInstance().setMultiCategoryIds(multiCategoryIds);
                    CorePushManager.getInstance().registerToken();

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("user_name", nameTxt.getText().toString());
                    userMap.put("gender", genderTxt.getText().toString());
                    userMap.put("email", emailTxt.getText().toString());
                    userMap.put("address", addressText.getText().toString());
                    userMap.put("apply", applyTxt1.getText().toString());

                    Intent intent = new Intent(getApplicationContext(), TermsAndUseActivity.class);
                    intent.putExtra("fromAc", fromAc);
                    intent.putExtra("userInfo", userMap);
                    startActivity(intent);
                    finish();
                }
            }
        });

        if (Common.me != null) {
            nameTxt.setText(Common.me.getUserName());
            genderTxt.setText(Common.me.getGender());
            emailTxt.setText(Common.me.getEmail());
            addressText.setText(Common.me.getAddress());
            applyTxt1.setText(Common.me.getApply());
            if (Common.me.getApply().contains("その他")) {
                applyTxt2.setVisibility(View.VISIBLE);
            }else {
                applyTxt2.setVisibility(View.GONE);
            }
        }
    }

    private boolean checkBlackItems() {
        String preStr = "";
        if (nameTxt.getText().toString().equals("")) {
            preStr = getString(R.string.name_title);
            Common.cm.showAlertDlg(getString(R.string.input_err_title), preStr+getString(R.string.input_err_msg),null, null);
            return false;
        }else if (emailTxt.getText().toString().equals("") || !emailTxt.getText().toString().contains("@")) {
            preStr = getString(R.string.email_title);
            Common.cm.showAlertDlg(getString(R.string.input_err_title), preStr+getString(R.string.input_err_msg),null, null);
            return false;
        }
        else if (genderTxt.getText().toString().equals("")) {
            preStr = getString(R.string.gender_title);
            Common.cm.showAlertDlg(getString(R.string.input_err_title), preStr+getString(R.string.input_err_msg),null, null);
            return false;
        }

        if (applyTxt1.getText().toString().equals("")) {
            preStr = getString(R.string.apply_title);
            Common.cm.showAlertDlg(getString(R.string.input_err_title), preStr+getString(R.string.input_err_msg),null, null);
            return false;
        }
//        else {
//            if (selectedApplyItems.contains("その他") && applyTxt2.getText().toString().equals("")) {
//                preStr = getString(R.string.apply_title);
//                Common.cm.showAlertDlg(getString(R.string.input_err_title), preStr+getString(R.string.input_err_msg),null, null);
//                return false;
//            }
//        }
        return true;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
