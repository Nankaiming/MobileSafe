package com.example.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mobilesafe.R;

//��������ʾ����ק�¼�
public class DragViewActivity extends Activity {
	private TextView tvBottom;
	private TextView tvTop;
	private ImageView ivDrag;
	private SharedPreferences mPref;
	long[] mHits = new long[2];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dragview);
		tvBottom = (TextView) findViewById(R.id.tv_bottom);
		tvTop = (TextView) findViewById(R.id.tv_top);
		ivDrag = (ImageView) findViewById(R.id.iv_drag);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		final int winWidth = getWindowManager().getDefaultDisplay().getWidth();
		final int winHeight = getWindowManager().getDefaultDisplay()
				.getHeight();

		int lastX = mPref.getInt("lastX", 0);
		int lastY = mPref.getInt("lastY", 0);

		if (lastX > winHeight / 2) {
			tvTop.setVisibility(View.VISIBLE);
			tvBottom.setVisibility(View.INVISIBLE);
		} else {
			tvTop.setVisibility(View.INVISIBLE);
			tvBottom.setVisibility(View.VISIBLE);
		}

		// onMeasure(����view), onLayout(����λ��), onDraw(����)
		// ivDrag.layout(lastX, lastY, lastX + ivDrag.getWidth(),
		// lastY + ivDrag.getHeight());//�������������,��Ϊ��û�в������,�Ͳ��ܰ���λ��
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivDrag
				.getLayoutParams();
		layoutParams.leftMargin = lastX;
		layoutParams.topMargin = lastY;
		ivDrag.setLayoutParams(layoutParams);

		// ����˫�����м���
		ivDrag.setOnClickListener(new OnClickListener() {

			

			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
				mHits[mHits.length - 1] = System.currentTimeMillis();
				if (mHits[0] > System.currentTimeMillis() - 500) {
					ivDrag.layout(winWidth / 2 - ivDrag.getWidth() / 2,
							ivDrag.getTop(), winWidth / 2 + ivDrag.getWidth()
									/ 2, ivDrag.getBottom());
				}
			}
		});

		// ������ק����
		ivDrag.setOnTouchListener(new OnTouchListener() {

			private int startX;
			private int startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();

					// �����ƶ�ƫ����
					int dx = endX - startX;
					int dy = endY - startY;

					// �����������¾���
					int l = ivDrag.getLeft() + dx;
					int r = ivDrag.getRight() + dx;

					int t = ivDrag.getTop() + dy;
					int b = ivDrag.getBottom() + dy;
					if (l < 0 || r > winWidth || t < 0 || b > winHeight - 30) {
						break;
					}
					if (t > winHeight / 2) {
						tvTop.setVisibility(View.VISIBLE);
						tvBottom.setVisibility(View.INVISIBLE);
					} else {
						tvTop.setVisibility(View.INVISIBLE);
						tvBottom.setVisibility(View.VISIBLE);
					}
					ivDrag.layout(l, t, r, b);
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();

					break;
				case MotionEvent.ACTION_UP:
					Editor edit = mPref.edit();
					edit.putInt("lastX", ivDrag.getLeft());
					edit.putInt("lastY", ivDrag.getTop());
					edit.commit();
					break;

				default:
					break;
				}
				return false;
			}
		});
	}
}
