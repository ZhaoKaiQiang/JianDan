package com.socks.jiandan.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.socks.jiandan.R;
import com.socks.jiandan.base.BaseActivity;
import com.socks.jiandan.constant.ToastMsg;
import com.socks.jiandan.model.Commentator;
import com.socks.jiandan.net.Request4PushComment;
import com.socks.jiandan.utils.EditTextShakeHelper;
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

	String thread_id;
	String parent_id;
	String author_name;
	String author_email;
	String message;

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

								EditText et_name = (EditText) (dialog.getCustomView().findViewById(R.id
										.et_name));
								EditText et_email = (EditText) (dialog.getCustomView().findViewById(R.id
										.et_email));

								author_name = et_name.getText().toString();
								author_email = et_email.getText().toString();

								if (TextUtil.isNull(author_name, author_email)) {
									new EditTextShakeHelper(PushCommentActivity.this).shake
											(et_name, et_email);
									ShowToast.Short(ToastMsg.INPUT_TOO_SHORT);
								} else {

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

							}

							@Override
							public void onNegative(MaterialDialog dialog) {

							}
						}).build();

				dialog.show();

				return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
