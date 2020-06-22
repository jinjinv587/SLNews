package com.xonepay.commonlibrary.view;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xonepay.commonlibrary.R;


public class CustomDialog extends AlertDialog implements View.OnClickListener {

    CustomDialogListener cdListener;
    private TextView dlg_Content;//显示弹框上的内容组件
    private String title;
    private String content;
    private String yes;
    private String no;

    public CustomDialog(Context context, int theme, String title,
                        String content, String yes, String no,
                        CustomDialogListener cdListener) {
        super(context, theme);
        this.cdListener = cdListener;
        this.title = title;
        this.content = content;
        this.yes = yes;
        this.no = no;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.prompt_dialog);
        this.setCanceledOnTouchOutside(false); // 点击外部不会消失
        InitViews();
    }

    private void InitViews() {
        TextView dlg_title = (TextView) findViewById(R.id.Dlg_Title);
        dlg_Content = (TextView) findViewById(R.id.Dlg_content);

        dlg_title.setText(this.title);
        dlg_Content.setText(this.content);

        Button dlg_Yes = (Button) findViewById(R.id.Dlg_Yes);
        Button dlg_No = (Button) findViewById(R.id.Dlg_No);
        dlg_Yes.setText(this.yes);
        dlg_No.setText(this.no);
        dlg_Yes.setOnClickListener(this);
        dlg_No.setOnClickListener(this);
    }

    public void setGravity(int gravity) {
        dlg_Content.setGravity(gravity);
        final ViewGroup.LayoutParams lp = dlg_Content.getLayoutParams();
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        dlg_Content.setLayoutParams(lp);
    }

    public interface CustomDialogListener {
        public void OnClick(View view);
    }

    public void onClick(View view) {

        cdListener.OnClick(view);
    }

    @Override
    public void setMessage(CharSequence message) {
        super.setMessage(message);
        if (!TextUtils.isEmpty(message)) {
            dlg_Content.setText(message);
        }
    }
}
