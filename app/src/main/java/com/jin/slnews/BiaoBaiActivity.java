package com.jin.slnews;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.jin.utils.Constant;
import com.jin.utils.ViewUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class BiaoBaiActivity extends Activity {
	private EditText et_person;
	private EditText et_author;
	private EditText et_content;
	private HttpUtils httpUtils;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_biaobai);
		ViewUtils.editStatusBarColor(this);
		init();
		
	}
	
	private void init() {
		et_person = (EditText) findViewById(R.id.et_person);
		et_author = (EditText) findViewById(R.id.et_author);
		et_content = (EditText) findViewById(R.id.et_content);
	}
	
	public void click(View v) {
		switch (v.getId()) {
		case R.id.btn_fabu:
			String person = et_person.getText().toString().trim();
			String author = et_author.getText().toString().trim();
			String content = et_content.getText().toString().trim();
			if (TextUtils.isEmpty(person) || TextUtils.isEmpty(author)
					|| TextUtils.isEmpty(content)) {
				Toast.makeText(this, "请输入内容！", Toast.LENGTH_LONG).show();
				return;
			}
			httpUtils = new HttpUtils();
			RequestParams params = new RequestParams();
			
			params.addBodyParameter("person", person);
			params.addBodyParameter("author", author);
			params.addBodyParameter("content", content);
			httpUtils.send(HttpMethod.POST, Constant.FA_BIAOBAI_URL, params,
					new RequestCallBack<String>() {
						
						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							if (responseInfo.result.equals("true")) {
								Toast.makeText(BiaoBaiActivity.this, "发布成功！",
										Toast.LENGTH_LONG).show();
								finish();
							}
							if (responseInfo.result.equals("false")) {
								Toast.makeText(BiaoBaiActivity.this, "发布失败！",
										Toast.LENGTH_LONG).show();
							}
						}
						
						@Override
						public void onFailure(HttpException error, String msg) {
						}
					});
			break;
		case R.id.btn_quxiao:
			finish();
			break;
		
		default:
			break;
		}
	}
}
