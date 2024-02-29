package com.goldslime.diskclean.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

import com.blankj.utilcode.util.StringUtils;

public class RangeUtil {
    public static int checkRange(int value, int min, int max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    public static void setRange(AppCompatEditText appCompatEditText, int min, int max, int defaultValue) {
        appCompatEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtils.isEmpty(s)) {
                    return;
                }
                try {
                    int value = Integer.parseInt(s + "");
                    if (value > max) {
                        appCompatEditText.setText(String.valueOf(max));
                        appCompatEditText.setSelection(String.valueOf(max).length());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        appCompatEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        if (TextUtils.isEmpty(appCompatEditText.getText())) {
                            appCompatEditText.setText(String.valueOf(defaultValue));
                        } else if (Integer.parseInt(appCompatEditText.getText().toString()) < min) {
                            appCompatEditText.setText(String.valueOf(min));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void setRange(AppCompatEditText appCompatEditText, float min, float max, float defaultValue) {
        appCompatEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) return;

                float value = Float.parseFloat(s.toString());
                if (value > max) {
                    appCompatEditText.setText(String.valueOf(max));
                    return;
                }
                if (!s.toString().matches("[0-9]+\\.?[0-9]?")) {
                    value = (float) (Math.round(value * 10)) / 10;
                    appCompatEditText.setText(String.valueOf(value));
                }

            }
        });
        appCompatEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        if (TextUtils.isEmpty(appCompatEditText.getText())) {
                            appCompatEditText.setText(String.valueOf(defaultValue));
                        } else if (Float.parseFloat(appCompatEditText.getText().toString()) < min) {
                            appCompatEditText.setText(String.valueOf(min));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
