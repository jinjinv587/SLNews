package com.xonepay.commonlibrary.view;

/**
 * wjq
 */

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xonepay.commonlibrary.R;


public class ConfirmDialog extends AlertDialog implements View.OnClickListener {

    private TextView Dlg_title;//弹窗标题
    private TextView Dlg_Content;//弹窗提示内容
    private Button Dlg_Yes;
    CustomDialogListener cdListener;
    private String title;
    private String content;
    private String yes = "确定";

    public ConfirmDialog(Context context, String title, String content, CustomDialogListener cdListener) {
        super(context, R.style.CustomDialog);
        this.cdListener = cdListener;
        this.title = title;
        this.content = content;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.confirm_dialog);
        this.setCanceledOnTouchOutside(false); // 点击外部不会消失
        InitViews();
    }

    private void InitViews() {
        Dlg_title = (TextView) findViewById(R.id.Dlg_Title);
        Dlg_Content = (TextView) findViewById(R.id.Dlg_content);

        Dlg_title.setText(this.title);
        Dlg_Content.setText(this.content);

        Dlg_Yes = (Button) findViewById(R.id.Dlg_Yes);

        Dlg_Yes.setText(this.yes);

        Dlg_Yes.setOnClickListener(this);

    }

    @Override
    public void setMessage(CharSequence message) {
        super.setMessage(message);
        if (!TextUtils.isEmpty(message)) {
            Dlg_Content.setText(message);//重新设置显示内容
        }
    }

    public interface CustomDialogListener {
        void OnClick(View view);
    }

    public void onClick(View view) {

        cdListener.OnClick(view);
    }

}
