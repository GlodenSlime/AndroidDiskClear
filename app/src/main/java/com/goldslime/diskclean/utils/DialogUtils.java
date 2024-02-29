package com.goldslime.diskclean.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.goldslime.diskclean.R;

import java.text.DecimalFormat;

public class DialogUtils {

    static DecimalFormat decimalFormat = new DecimalFormat("####,####,####");

    public static void ShowInputDialog(Context context, String title, String hint, String defaultValue, IInputOkClick iInputOkClick) {
        final EditText text = new EditText(context);
        text.setHint(hint);
        text.setText(defaultValue);
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setIcon(R.mipmap.logo)
                .setView(text)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String input = text.getText().toString().trim();
                        if ("".equals(input)) {
                            Toast.makeText(context, "请填写内容", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        iInputOkClick.onInputDone(input);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public static void ShowTipDialog(Context context, String title, String content, IOnClick onOkClick) {
        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(context)
                .setIcon(R.mipmap.logo)//设置标题的图片
                .setTitle(title)//设置对话框的标题
                .setMessage(content)//设置对话框的内容
                //设置对话框的按钮
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("授予权限", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (onOkClick != null) {
                            onOkClick.onOkClick();
                        }
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    public static void ShowSuccess(Context context, String title) {
        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(context)
                .setIcon(R.mipmap.logo)//设置标题的图片
                .setTitle(title)//设置对话框的标题
                .setMessage("")//设置对话框的内容
                //设置对话框的按钮
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    public static void MaxWidthDialog(Dialog dialog) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public interface IOnClick {
        void onOkClick();

        void onCancelClick();
    }

    public interface IInputOkClick {
        void onInputDone(String input);
    }

    public interface IOnNumberSet {
        void onNumberSet(int number);
    }
}
