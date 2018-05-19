package com.witgather.utils;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;


/**
 * @author 陈康勇
 *	将图片进行编码和解码，用于图片的包装在JSON数据中
 */
public class FileHelper {
	/**
	 * @param msg 经过Base64编码的字符串转换为二进制流
	 * @return
	 */
	public static byte[] decoder(String msg) {
		return Base64.decodeBase64(msg);
	}
	
	/**
	 * @param path 将二进制流转换为字符串，然后使用JSON传输
	 * @return
	 */
	public static String encoder(String path) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
			byte[] buffer = new byte[fis.available()];
			return Base64.encodeBase64String(buffer);
		} catch (IOException e1) {
			e1.printStackTrace();
		}finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
