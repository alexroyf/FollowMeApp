package com.example.followme.activties;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.followme.R;

public class LogIn extends Activity implements OnClickListener {

	private static final int DELAY = 2000; // 2s

	// Views
	private EditText mUserName;
	private EditText mPassword;
	private Button mLoginEnter;

	// Dialogs
	private ProgressDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.log_in);
		mUserName = (EditText) findViewById(R.id.text1_login);
		mPassword = (EditText) findViewById(R.id.text2_login);
		mLoginEnter = (Button) findViewById(R.id.login_button);

		mLoginEnter.setOnClickListener(this);

		mDialog = new ProgressDialog(this);
		mDialog.setMessage(getString(R.string.please_wait));

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_button:
			if (mUserName.getText().toString().equals("")
					|| mPassword.getText().toString().equals("")) {
				Toast.makeText(this, getString(R.string.wrong_login_message),
						Toast.LENGTH_SHORT).show();
				return;
			}

			mDialog.show();
			Handler handler = new Handler();
			Runnable run = new Runnable() {

				@Override
				public void run() {
					mDialog.dismiss();
					startActivity(new Intent(getBaseContext(),
							MainActivity.class));
				}
			};

			handler.postDelayed(run, DELAY);
			break;
		default:
			break;
		}

	}

}
