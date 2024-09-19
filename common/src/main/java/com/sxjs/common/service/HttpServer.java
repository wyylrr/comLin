package com.sxjs.common.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import com.sxjs.common.kits.IP;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Enumeration;

public class HttpServer extends Service implements Runnable {
    private static boolean _isRunning = false;
    private static Thread _serverThread = null;
    private ServerSocket _listenSocket = null;
    private MyLog _myLog = new MyLog(getClass().getName());
    private static int _port = Defaults.getPort();
    private TcpListener _tcpListener = null;
    private static final int WAKE_INTERVAL_MS = 1000;
//    private static Intent intent;

    public HttpServer() {
        try {
            Init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void Init() throws IOException {
        _listenSocket = new ServerSocket();
        _listenSocket.setReuseAddress(true);
        _listenSocket.bind(new InetSocketAddress(_port));
    }

    public static void Start(Context context) {
        if (!_isRunning) {
            _isRunning = true;
            Intent intent = new Intent(context, HttpServer.class);
            context.startService(intent);
        }

    }

    public static void Stop(Context context) {

        if (_isRunning) {
            _isRunning = false;
            Intent intent = new Intent(context, HttpServer.class);
            context.stopService(intent);
        }
    }

    public static boolean isRunning() {
        return _isRunning;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int attempts = 10;
        // The previous server thread may still be cleaning up,
        // wait for it to finish.
        while (_serverThread != null) {
            _myLog.l(Log.WARN, "Won't start, server thread exists");
            if (attempts <= 0) {
                _myLog.l(Log.ERROR, "Server thread already exists");
                return super.onStartCommand(intent, flags, startId);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            attempts--;
        }
        _myLog.l(Log.DEBUG, "Creating server thread");
        _serverThread = new Thread(this);
        _serverThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        if (_tcpListener != null) {
            _tcpListener.quit();
        }

        _myLog.l(Log.INFO, "onDestroy() Stopping server");
        if (_serverThread == null) {
            _myLog.l(Log.WARN, "Stopping with null serverThread");
            return;
        }

        _serverThread.interrupt();
        try {
            _serverThread.join(10000); // wait 10 second for server thread to
            // finish

        } catch (InterruptedException e) {
        }

        if (_serverThread.isAlive()) {
            _myLog.l(Log.WARN, "Server thread failed to exit");
        } else {
            _myLog.d("serverThread joined ok");
            _serverThread = null;
        }

        try {
            if (_listenSocket != null) {
                _listenSocket.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        UiUpdater.updateClients();

        _myLog.d("WebService.onDestroy() finished");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void run() {
        while (_isRunning) {
            UiUpdater.updateClients();
            if (_tcpListener == null) {
                _tcpListener = new TcpListener(_listenSocket, this);
                _tcpListener.start();
            }
            try {

                Thread.sleep(WAKE_INTERVAL_MS);
            } catch (InterruptedException e) {
                _myLog.l(Log.DEBUG, "Thread interrupted");
            }
        }
    }

    public static InetAddress getWifiIp(Context context) {

        if (context == null) {
            throw new NullPointerException("Global context is null");
        }
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            return null;
        }

        int ipAsInt = wifiManager.getConnectionInfo().getIpAddress();
        if (ipAsInt == 0) {
            return null;
        } else {
            return IP.intToInet(ipAsInt);
        }

    }

    public static int getPort() {
        return _port;
    }

    /**
     * 获取本地IP地址
     *
     * @return
     */

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> enNetI = NetworkInterface
                    .getNetworkInterfaces(); enNetI.hasMoreElements(); ) {
                NetworkInterface netI = enNetI.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = netI
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "0.0.0.0";
    }

    public static String getLocalIpAddress1() {
//        try {
//            String ipv4;
//            List<NetworkInterface> nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
//            for (NetworkInterface ni : nilist) {
//                List<InetAddress> ialist = Collections.list(ni.getInetAddresses());
//                for (InetAddress address : ialist) {
//                    if (!address.isLoopbackAddress() &&
//                            InetAddressUtils.isIPv4Address(ipv4 = address.getHostAddress())) {
//                        return ipv4;
//                    }
//                }
//            }
//        } catch (SocketException ex) {
//            ex.printStackTrace();
//        }
        return null;
    }

}
