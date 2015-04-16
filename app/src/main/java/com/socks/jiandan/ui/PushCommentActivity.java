package com.socks.jiandan.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.socks.jiandan.R;
import com.socks.jiandan.base.BaseActivity;
import com.socks.jiandan.constant.ToastMsg;
import com.socks.jiandan.model.Commentator;
import com.socks.jiandan.net.Request4PushComment;
import com.socks.jiandan.utils.EditTextShakeHelper;
import com.socks.jiandan.utils.SharedPreUtils;
import com.socks.jiandan.utils.ShowToast;
import com.socks.jiandan.utils.TextUtil;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 发表评论界面
 */
public class PushCommentActivity extends BaseActivity {

	@InjectView(R.id.tv_title)
	TextView tv_title;
	@InjectView(R.id.et_content)
	EditText et_content;

	private String thread_id;
	private String parent_id;
	private String author_name;
	private String author_email;
	private String message;

	private EditText et_name;
	private EditText et_email;

	private View positiveAction;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_push_comment);
		initView();
	}

	@Override
	public void initView() {

		ButterKnife.inject(this);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		String parent_name = getIntent().getStringExtra("parent_name");
		tv_title.setText(TextUtil.isNull(parent_name) ? "回复:" : "回复:" + parent_name);

		thread_id = getIntent().getStringExtra("thread_id");
		parent_id = getIntent().getStringExtra("parent_id");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_push_comment, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case R.id.action_push:

				message = et_content.getText().toString();

				if (TextUtil.isNull(message)) {
					ShowToast.Short(ToastMsg.INPUT_TOO_SHORT);
					new EditTextShakeHelper(this).shake(et_content);
					return true;
				}

				MaterialDialog dialog = new MaterialDialog.Builder(this)
						.title("作为游客留言")
						.customView(R.layout.dialog_commentotar_info, true)
						.positiveText("确定")
						.negativeText(android.R.string.cancel)
						.callback(new MaterialDialog.ButtonCallback() {
							@Override
							public void onPositive(final MaterialDialog dialog) {

								author_name = et_name.getText().toString();
								author_email = et_email.getText().toString();

								SharedPreUtils.setString(PushCommentActivity.this,
										"author_name", author_name);
								SharedPreUtils.setString(PushCommentActivity.this,
										"author_email", author_email);

								HashMap<String, String> requestParams;

								//首次评论
								if (TextUtil.isNull(parent_id)) {
									requestParams = Request4PushComment.getRequestParamsNoParent(thread_id,
											author_name, author_email, message);
								} else {
									//回复别人
									requestParams = Request4PushComment.getRequestParams(thread_id, parent_id,
											author_name, author_email, message);
								}

								executeRequest(new Request4PushComment(Commentator.URL_PUSH_COMMENT,
										requestParams, new Response.Listener<Boolean>() {
									@Override
									public void onResponse(Boolean response) {

										dialog.dismiss();

										if (response) {
											setResult(RESULT_OK);
											finish();
										} else {
											ShowToast.Short(ToastMsg.COMMENT_FAILED);
										}

									}
								}, new Response.ErrorListener() {
									@Override
									public void onErrorResponse(VolleyError error) {
										ShowToast.Short(ToastMsg.COMMENT_FAILED);
										dialog.dismiss();
									}
								}));

							}

							@Override
							public void onNegative(MaterialDialog dialog) {

							}
						}).build();

				et_name = (EditText) (dialog.getCustomView().findViewById(R.id
						.et_name));
				et_email = (EditText) (dialog.getCustomView().findViewById(R.id
						.et_email));
				positiveAction = dialog.getActionButton(DialogAction.POSITIVE);

				et_name.addTextChangedListener(new InputWatcher());
				et_email.addTextChangedListener(new InputWatcher());

				et_name.setText(SharedPreUtils.getString(PushCommentActivity
						.this, "author_name"));
				et_email.setText(SharedPreUtils.getString(PushCommentActivity
						.this, "author_email"));

				dialog.show();

				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private class InputWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

			positiveAction.setEnabled(TextUtil.isEmail(et_email.getText().toString().trim()
			) && !TextUtil.isNull(et_name.getText().toString()));

		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	}


}
