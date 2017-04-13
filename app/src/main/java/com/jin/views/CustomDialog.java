package com.jin.views;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jin.slnews.R;


public class CustomDialog extends AlertDialog implements View.OnClickListener {
	CustomDialogListener cdListener;
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
		TextView dlg_Content = (TextView) findViewById(R.id.Dlg_content);

		dlg_title.setText(this.title);
		dlg_Content.setText(this.content);

		Button dlg_Yes = (Button) findViewById(R.id.Dlg_Yes);
		Button dlg_No = (Button) findViewById(R.id.Dlg_No);
		dlg_Yes.setText(this.yes);
		dlg_No.setText(this.no);
		dlg_Yes.setOnClickListener(this);
		dlg_No.setOnClickListener(this);
	}


	public interface CustomDialogListener {
		void OnClick(View view);
	}

	public void onClick(View view) {
		cdListener.OnClick(view);
	}

}
