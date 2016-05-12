package com.example.mobilesafe.activity;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.adapter.MyBaseAdapter;
import com.example.mobilesafe.bean.BlackNumberInfo;
import com.example.mobilesafe.db.dao.BlackNumberDao;
import com.example.mobilesafe.utils.ToastUtils;

public class CallSafeActivity extends Activity {
	private ListView list_view;
	private BlackNumberDao blackNumberDao;
	private List<BlackNumberInfo> blackNumberInfos;
	
	private int mPageSize = 20;
	private int mCurrentPageNum = 0;
	private int totalPageNumber;
	
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			ll_pgs_bar.setVisibility(View.INVISIBLE);
			tv_page_number.setText((mCurrentPageNum+1)+"/"+totalPageNumber);
			adapter = new CallSafeAdapter(blackNumberInfos,CallSafeActivity.this);
			list_view.setAdapter(adapter);
		};
	};
	private CallSafeAdapter adapter;
	private LinearLayout ll_pgs_bar;
	private TextView tv_page_number;
	private EditText et_page_number;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_safe);
		initUI();
		initData();
	}
	
	private void initData() {
		blackNumberDao = new BlackNumberDao(this);
		new Thread(){
			

			public void run() {
				blackNumberInfos = blackNumberDao.findPar(mCurrentPageNum, mPageSize);
				if(blackNumberDao.getTotalNumber() % mPageSize == 0){
					
					totalPageNumber = blackNumberDao.getTotalNumber() / mPageSize;
				}else{
					totalPageNumber = blackNumberDao.getTotalNumber() / mPageSize + 1;
				}
//				System.out.println("+++++"+totalPageNumber+"+++++");
				handler.sendEmptyMessage(0);
			};
		}.start();
		
	}

	private void initUI() {
		list_view = (ListView) findViewById(R.id.list_view);
		ll_pgs_bar = (LinearLayout) findViewById(R.id.ll_pgs_bar);
		ll_pgs_bar.setVisibility(View.VISIBLE);
		et_page_number = (EditText) findViewById(R.id.et_page_number);
		tv_page_number = (TextView) findViewById(R.id.tv_page_number);
	}

	private class CallSafeAdapter extends MyBaseAdapter<BlackNumberInfo> {

		

		public CallSafeAdapter(List<BlackNumberInfo> list, Context context) {
			super(list, context);
			// TODO Auto-generated constructor stub
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view;
			ViewHolder viewHolder;
			if (convertView == null) {
				view = View.inflate(CallSafeActivity.this,
						R.layout.item_call_safe, null);
				viewHolder = new ViewHolder();
				viewHolder.tv_number = (TextView) view.findViewById(R.id.tv_number);
				viewHolder.tv_mode = (TextView) view.findViewById(R.id.tv_mode);
				viewHolder.iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
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
					if(result){
						ToastUtils.showToast(CallSafeActivity.this, "删除成功");
						blackNumberInfos.remove(info);
						adapter.notifyDataSetChanged();
					}else{
						ToastUtils.showToast(CallSafeActivity.this, "删除失败");
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
	
	public void prePage(View view){
		if(mCurrentPageNum <= 0){
			ToastUtils.showToast(this, "已经是第一页了");
			return;
		}
		mCurrentPageNum--;
		initData();
	}
	public void nextPage(View view){
		if(mCurrentPageNum >= totalPageNumber - 1){
			ToastUtils.showToast(this, "已经是最后一页了");
			return;
		}
		mCurrentPageNum++;
		initData();
	}
	public void jump(View view){
		String str_page_number = et_page_number.getText().toString().trim();
		if(TextUtils.isEmpty(str_page_number)){
			ToastUtils.showToast(this, "请输入正确的页码");
		}else{
			int number = Integer.parseInt(str_page_number) - 1;
			if(number >= 0 && number <= totalPageNumber - 1){
				mCurrentPageNum = number;
				initData();
			}else{
				ToastUtils.showToast(this, "请输入正确的页码");
			}
		}
	}
}
