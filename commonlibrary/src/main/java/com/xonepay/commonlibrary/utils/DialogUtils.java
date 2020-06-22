package com.xonepay.commonlibrary.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.xonepay.commonlibrary.R;
import com.xonepay.commonlibrary.view.ConfirmDialog;
import com.xonepay.commonlibrary.view.CustomDialog;


public class DialogUtils {
    private static ConfirmDialog confirmDialog;
    private static ConfirmDialog confirmDialog2;
    private static CustomDialog customDialog;
    private static CustomProgressDialog progressDialog;
    private static Context saveContext;

    public static void alertInfo(Context context, String msg) {
        confirmDialog = new ConfirmDialog(context, "提示", msg,
                new ConfirmDialog.CustomDialogListener() {
                    @Override
                    public void OnClick(View view) {
                        if (view.getId() == R.id.Dlg_Yes) {
                            confirmDialog.dismiss();// 使对话框消失
                        }
                    }
                });
        confirmDialog.show();
    }

    public static void alertInfo(Context context, String msg, final CallBackFunction callBackFunction) {
        confirmDialog2 = new ConfirmDialog(context, "提示", msg, new ConfirmDialog.CustomDialogListener() {
            @Override
            public void OnClick(View view) {
                if (view.getId() == R.id.Dlg_Yes) {
                    confirmDialog2.dismiss();// 使对话框消失
                    callBackFunction.call();
                }
            }
        });
        confirmDialog2.show();
    }


    public static void confirmInfo(Context context, String title, String msg, final CallBackFunction callBackFunction) {
        customDialog = new CustomDialog(context, R.style.CustomDialog, title,
                msg, context.getString(R.string.setting_exit_ok),
                context.getString(R.string.setting_exit_cancel), new CustomDialog.CustomDialogListener() {
            @Override
            public void OnClick(View view) {
                if (view.getId() == R.id.Dlg_Yes) {
                    customDialog.dismiss();// 使对话框消失
                    callBackFunction.call();
                } else {
                    customDialog.dismiss();
                }
            }
        });
        customDialog.show();
    }

    public interface CallBackFunction {
        void call();
    }

    public static void startProgressDialog(Context context, String msg) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(context);
            progressDialog.setMessage(msg);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {//监听关闭事件，置空，解决点返回按钮关闭时引起的
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }
            });
        }
        try {
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void stopProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
