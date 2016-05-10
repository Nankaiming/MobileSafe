package com.example.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * MD5º”√‹√‹¬Î
 * @author God vision
 *
 */
public class MD5Utils {
	public static String encode(String password) {
		MessageDigest instance;
		try {
			instance = MessageDigest.getInstance("MD5");
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
}
