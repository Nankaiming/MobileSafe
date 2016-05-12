package com.example.mobilesafe.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.R;
import com.example.mobilesafe.adapter.MyBaseAdapter;
import com.example.mobilesafe.bean.BlackNumberInfo;
import com.example.mobilesafe.db.dao.BlackNumberDao;
import com.example.mobilesafe.utils.ToastUtils;

public class CallSafeActivity2 extends Activity {
	private ListView list_view;
	private BlackNumberDao blackNumberDao;
	private List<BlackNumberInfo> blackNumberInfos;

	private int mStartIndex = 0;
	private int maxCount = 20;
	private int totalNumber;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ll_pgs_bar.setVisibility(View.INVISIBLE);
			if (adapter == null) {
				adapter = new CallSafeAdapter(blackNumberInfos,
						CallSafeActivity2.this);
				list_view.setAdapter(adapter);
			}else{
				adapter.notifyDataSetChanged();
			}
		};
	};
	private CallSafeAdapter adapter;
	private LinearLayout ll_pgs_bar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_safe2);
		initUI();
		initData();
	}

	private void initData() {
		blackNumberDao = new BlackNumberDao(this);
		totalNumber = blackNumberDao.getTotalNumber();
		new Thread() {

			public void run() {
				if (blackNumberInfos == null) {
					blackNumberInfos = blackNumberDao.findPar2(mStartIndex,
							maxCount);
				} else {
					blackNumberInfos.addAll(blackNumberDao.findPar2(
							mStartIndex, maxCount));
				}
				handler.sendEmptyMessage(0);
			};
		}.start();

	}

	private void initUI() {
		list_view = (ListView) findViewById(R.id.list_view);
		ll_pgs_bar = (LinearLayout) findViewById(R.id.ll_pgs_bar);
		ll_pgs_bar.setVisibility(View.VISIBLE);
		list_view.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				switch (scrollState) {
				case SCROLL_STATE_IDLE:
					int lastVisiblePosition = list_view
							.getLastVisiblePosition();
					if (lastVisiblePosition == blackNumberInfos.size() - 1) {
						mStartIndex += maxCount;
						if (mStartIndex >= totalNumber - 1) {
							ToastUtils.showToast(CallSafeActivity2.this,
									"没有更多的数据了。");
							return;
						}

						initData();
					}
					break;

				default:
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});
	}

	private class CallSafeAdapter extends MyBaseAdapter<BlackNumberInfo> {

		public CallSafeAdapter(List<BlackNumberInfo> list, Context context) {
			super(list, context);
			// TODO Auto-generated constructor stub
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			View view;
			ViewHolder viewHolder;
			if (convertView == null) {
				view = View.inflate(CallSafeActivity2.this,
						R.layout.item_call_safe, null);
				viewHolder = new ViewHolder();
				viewHolder.tv_number = (TextView) view
						.findViewById(R.id.tv_number);
				viewHolder.tv_mode = (TextView) view.findViewById(R.id.tv_mode);
				viewHolder.iv_delete = (ImageView) view
						.findViewById(R.id.iv_delete);
				view.setTag(viewHolder);
			} else {
				view = convertView;
				viewHolder = (ViewHolder) view.getTag();
			}
			viewHolder.tv_number.setText(blackNumberInfos.get(position)
					.getNumber());
			String mode = blackNumberInfos.get(position).getMode();
			if (mode.equals("1")) {
				viewHolder.tv_mode.setText("来电拦截+短信拦截");
			} else if (mode.equals("2")) {
				viewHolder.tv_mode.setText("来电拦截");
			} else if (mode.equals("3")) {
				viewHolder.tv_mode.setText("短信拦截");
			}

			viewHolder.iv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					BlackNumberInfo info = blackNumberInfos.get(position);
					boolean result = blackNumberDao.delete(info.getNumber());
					if (result) {
						ToastUtils.showToast(CallSafeActivity2.this, "删除成功");
						blackNumberInfos.remove(info);
						adapter.notifyDataSetChanged();
					} else {
						ToastUtils.showToast(CallSafeActivity2.this, "删除失败");
					}
				}
			});

			return view;
		}

	}

	static class ViewHolder {
		TextView tv_number;
		TextView tv_mode;
		ImageView iv_delete;
	}
	public void addBlackNumber(View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View dialog_view = View.inflate(this, R.layout.dialog_add_black_number, null);
		Button btn_ok = (Button) dialog_view.findViewById(R.id.btn_ok);
		final EditText et_number = (EditText) dialog_view.findViewById(R.id.et_number);

        Button btn_cancel = (Button) dialog_view.findViewById(R.id.btn_cancel);

        final CheckBox cb_phone = (CheckBox) dialog_view.findViewById(R.id.cb_phone);

        final CheckBox cb_sms = (CheckBox) dialog_view.findViewById(R.id.cb_sms);
        
        
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_number = et_number.getText().toString().trim();
                if(TextUtils.isEmpty(str_number)){
                  Toast.makeText(CallSafeActivity2.this,"请输入黑名单号码",Toast.LENGTH_SHORT).show();
                    return;
                }

                String mode = "";

                if(cb_phone.isChecked()&& cb_sms.isChecked()){
                    mode = "1";
                }else if(cb_phone.isChecked()){
                    mode = "2";
                }else if(cb_sms.isChecked()){
                    mode = "3";
                }else{
                    Toast.makeText(CallSafeActivity2.this,"请勾选拦截模式",Toast.LENGTH_SHORT).show();
                    return;
                }
                BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
                blackNumberInfo.setNumber(str_number);
                blackNumberInfo.setMode(mode);
                blackNumberInfos.add(0,blackNumberInfo);
                //把电话号码和拦截模式添加到数据库。
                blackNumberDao.add(str_number,mode);

                if(adapter == null){
                    adapter = new CallSafeAdapter(blackNumberInfos, CallSafeActivity2.this);
                    list_view.setAdapter(adapter);
                }else{
                    adapter.notifyDataSetChanged();
                }

                dialog.dismiss();
            }
        });
        dialog.setView(dialog_view);
        dialog.show();

	}

}
