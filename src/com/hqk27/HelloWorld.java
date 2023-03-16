package com.hqk27;

import java.awt.Button;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class HelloWorld {

	static WebServerThread thread;
	static Button button;
	public static void main(String[] args) {
		Frame f = new Frame("我的窗口");
		//f.setUndecorated(true); // 去掉系统自带窗口边框
		//f.setBackground(new Color(255, 255, 255, 128));
		f.setSize(640, 480);
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Window w = e.getWindow();
				w.setVisible(false);
				w.dispose(); // 释放窗口
				thread.destroy();
			}
		});
		f.setVisible(true);
		Panel p = new Panel();
		f.add(p);
		button = new Button("点此查看IP地址");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					button.setLabel(InetAddress.getLocalHost().getHostAddress());
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				}
				try {
					getLocalHostLANAddress();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		p.add(button);
		thread = new WebServerThread();
		thread.start();
	}
	
	static class WebServerThread extends Thread {
		static final int port = 80;
    	ServerSocket mServerSocket;
		boolean isLooping = true;
		public WebServerThread() {
			
		}
		
		@Override
		public void run() {
			try {
				System.out.println("===server start===");
			    mServerSocket = new ServerSocket(port);
			    //mServerSocket.setReuseAddress(true);
			    /**
			      * 开始接受客户端请求。
			      */
			    while (isLooping) {
			        // 接收客户端套接字。
			        if (!mServerSocket.isClosed()) {
			        	try {
				        	// 阻塞接受客户端。
				            Socket socket = mServerSocket.accept();
				            System.out.println("===server accept===" + socket.getInetAddress().toString());
					        new ServerThread(socket).start();
			            	
			        	} catch (SocketException e) {
			        		break;
			        	}
			        }
			    }
			    if (!mServerSocket.isClosed()) {
        			mServerSocket.close();
        		}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void destroy() {
			isLooping = false;
			try {
				mServerSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	static class ServerThread extends Thread {
		Socket mSocket;
		public ServerThread(Socket socket) {
			mSocket = socket;
		}
		@Override
		public void run() {
			try {
				// 解析请求信息为HttpRequest对象
                HttpRequest request = new HttpRequest(new InputStreamReader(mSocket.getInputStream(), "utf-8"));
                String path = request.getPathInfo();
                //System.out.println("路径: " + path);
                MediaPlayer.playMusic("assets\\piano\\" + path.substring(1).replace('s', '#') + ".wav");
	            // 响应
	            HttpResponse response = new HttpResponse();
	            response.setData("");
	            // 返回数据
	            try {
	            	mSocket.getOutputStream().write(response.getBytes());
	            	mSocket.getOutputStream().flush();
	            } finally {
	            	mSocket.getOutputStream().close();
	            }
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
	            try {
					mSocket.close();
					System.out.println("===socket close===");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void getLocalHostLANAddress() throws Exception {
	    try {
	        InetAddress candidateAddress = null;
	        // 遍历所有的网络接口
	        for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
	            NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
	            // 在所有的接口下再遍历IP
	            for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
	                InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
	                if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
	                    if (inetAddr.isSiteLocalAddress()) {
	                        // 如果是site-local地址，就是它了
	                        System.out.println(inetAddr.getHostAddress());
	                    } else if (candidateAddress == null) {
	                        // site-local类型的地址未被发现，先记录候选地址
	                        candidateAddress = inetAddr;
	                    }
	                }
	            }
	        }
	        if (candidateAddress != null) {
	        	System.out.println(candidateAddress.getHostAddress());
	        }
	        // 如果没有发现 non-loopback地址.只能用最次选的方案
	        InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
	        System.out.println(jdkSuppliedAddress.getHostAddress());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
