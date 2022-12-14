package com.sxjs.common.service;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SessionThread extends Thread {
	private Socket _clientSocket = null;
	private final int BUFFER_MAX = 81920;
	private DataHandle _dataHandle = null;
	private MyLog _myLog = new MyLog(getClass().getName());
	private Context context;

	public SessionThread(Socket clientSocket, Context context) {
		this._clientSocket = clientSocket;
		this.context=context;
	}

	public void closeSocket() {
		if (_clientSocket == null) {
			return;
		}
		try {
			_clientSocket.close();
		} catch (IOException e) {
			_myLog.e(e.getMessage());
		}
	}

	public void run() {
		try {

			InputStream socketInput = _clientSocket.getInputStream();
			byte[] buffer = new byte[BUFFER_MAX];
			socketInput.read(buffer);
			_dataHandle = new DataHandle(buffer);
			byte[] content = _dataHandle.fetchContent(context);

			sendResponse(_clientSocket, content);

		} catch (Exception e) {
			_myLog.l(Log.DEBUG, "Exception in TcpListener");
		}
	}

	private void sendResponse(Socket clientSocket, byte[] content) {
		try {
			OutputStream socketOut = clientSocket.getOutputStream();

			byte[] header = _dataHandle.fetchHeader(content.length);

			socketOut.write(header);
			socketOut.write(content);

			socketOut.close();
			clientSocket.close();
		} catch (Exception e) {
		}
	}
}
