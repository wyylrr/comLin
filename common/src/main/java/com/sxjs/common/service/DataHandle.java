package com.sxjs.common.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class DataHandle {
	private String _receiveInfo = "";
	private HttpHeader _httpHeader = null;
	private String _encoding = "utf-8";
	private String _serverName = "Server";
	private String _responseCode = "200";
	private String _contentType = "application/json";

	public DataHandle(byte[] recieveData) {
		try {
			this._receiveInfo = new String(recieveData, _encoding);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_httpHeader = new HttpHeader(_receiveInfo);
	}

	public byte[] fetchContent(Context context) {
		byte[] backData = null;
        JSONObject jsonObject=new JSONObject();
		if (!isSupportMethod()) {
            try {
                jsonObject.put("code",500);
                jsonObject.put("msg","请求参数异常！");
            } catch (JSONException e) {
                e.printStackTrace();
            }
			backData = String.valueOf(jsonObject).getBytes();
			return backData;
		}

		try {
			jsonObject.put("code",200);
			jsonObject.put("msg","请求成功！");
			jsonObject.put("data","");
			Intent intent = new Intent("android.intent.action.http.data");
			intent.putExtra("data",_httpHeader.getBody());
			context.sendBroadcast(intent);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		backData = String.valueOf(jsonObject).getBytes();
		return backData;
	}

	public byte[] fetchHeader(int contentLength) {
		byte[] header = null;
		try {
			header = ("HTTP/1.1 " + _responseCode + "\r\n" 
					+ "Server: "+ _serverName + "\r\n" 
					+ "Content-Length: " + contentLength+ "\r\n"
					+ "Connection: close\r\n" 
					+ "Content-Type: "+ _contentType + ";charset="+_encoding+"\r\n\r\n").getBytes(_encoding);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return header;
	}

	@SuppressLint("DefaultLocale")
	private boolean isSupportMethod() {
		String method = _httpHeader.getMethod();
		String body = _httpHeader.getFileName();
		if (method == null || method.length() <= 0) {
			return false;
		}
		method = method.toUpperCase();
		Log.e("wyy",_httpHeader.getUrl()+",method:"+method+"，body:"+_httpHeader.getBody());

//		if (method.equals("GET") && _httpHeader.getUrl().contains("/face/SendMessage")) {
//			return true;
//		}

		return true;
	}
}
