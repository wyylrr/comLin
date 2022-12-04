package com.sxjs.common.service;

import android.content.Context;
import android.util.Log;

import java.net.ServerSocket;
import java.net.Socket;

public class TcpListener extends Thread {
	private ServerSocket _listenSocket = null;
	private MyLog _myLog = new MyLog(getClass().getName());
	private Context context;

	public TcpListener(ServerSocket listenSocket, Context context) {
		this._listenSocket = listenSocket;
		this.context=context;
	}

	public void quit() {
		try {
			_listenSocket.close(); // if the TcpListener thread is blocked on
									// accept,
									// closing the socket will raise an
									// exception
		} catch (Exception e) {
			_myLog.l(Log.DEBUG, "Exception closing TcpListener listenSocket");
		}
	}

	public void run() {
		try {
			while (true) {
				Socket clientSocket = _listenSocket.accept();
				_myLog.l(Log.INFO, "New connection, spawned thread");
				SessionThread newSession = new SessionThread(clientSocket,context);
				newSession.start();
			}
		} catch (Exception e) {
			_myLog.l(Log.DEBUG, "Exception in TcpListener");
		}
	}

}
