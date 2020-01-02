package com.example.administrator.note;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Regist extends Activity {
	EditText name, pwd;
	Button login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regist_activity);
		name = (EditText) findViewById(R.id.name1);
		pwd = (EditText) findViewById(R.id.pwd1);
		login = (Button) findViewById(R.id.login1);
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!name.getText().toString().equals("")
						&& !pwd.getText().toString().equals("")) {
					PrefUtils.setString(Regist.this, "name", name.getText()
							.toString());
					PrefUtils.setString(Regist.this, "pwd", pwd.getText()
							.toString());
					finish();
				} else {
					Toast.makeText(Regist.this, "输入不能为空", 2000).show();
				}
			}
		});
	}
}
