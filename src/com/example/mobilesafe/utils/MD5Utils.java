package com.example.mobilesafe.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * MD5º”√‹
 * @author God vision
 *
 */
public class MD5Utils {
	public static String encode(String password) {
		try {
			MessageDigest instance = MessageDigest.getInstance("MD5");
			byte[] digest = instance.digest(password.getBytes());
			StringBuilder str = new StringBuilder();
			for (byte b : digest) {
				int i = b & 0xff;
				String hexString = Integer.toHexString(i);
				if (hexString.length() < 2) {
					hexString = "0" + hexString;
				}
				str.append(hexString);

			}
			return str.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	public static String getFileMd5(String sourceDir){
		File file = new File(sourceDir);
		try {
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int len = 0;
			MessageDigest messageDigest = MessageDigest.getInstance("md5");
			while((len = fis.read(buffer)) != -1){
				messageDigest.update(buffer, 0, len);
			}
			byte[] digest = messageDigest.digest();
			StringBuilder str = new StringBuilder();
			for (byte b : digest) {
				int i = b & 0xff;
				String hexString = Integer.toHexString(i);
				if (hexString.length() < 2) {
					hexString = "0" + hexString;
				}
				str.append(hexString);

			}
			fis.close();
			return str.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
}
