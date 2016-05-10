package com.example.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

import com.example.mobilesafe.R;
import com.example.mobilesafe.service.LocationService;
/**
 * �������ն��ŵĹ㲥
 * @author God vision
 *
 */
public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//��ȡ�豸������
		DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		Object[] objects = (Object[]) intent.getExtras().get("pdus");
		for (Object object : objects) {
			SmsMessage message = SmsMessage.createFromPdu((byte[])object);
			String messageBody = message.getMessageBody();
			//���ű�������
			if(messageBody.equals("#*alarm*#")){
				MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
				player.setVolume(1f, 1f);
				player.setLooping(true);
				player.start();
				abortBroadcast();
				//��λ�ֻ���γ��
			}else if(messageBody.equals("#*location*#")){
				context.startService(new Intent(context,LocationService.class));
				abortBroadcast();
				//�����豸�����߽���������ݣ��������û���豸������
			}else if(messageBody.equals("#*wipedata*#")){
				dpm.wipeData(0);
				abortBroadcast();
				//�����豸�����߽���Զ���������������û���豸������
			}else if(messageBody.equals("#*lockscreen*#")){
				dpm.resetPassword("123", 0);
				dpm.lockNow();
				abortBroadcast();
			}
		}
	}

}
