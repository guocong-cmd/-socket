package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	
	public static void main(String[] args) throws UnsupportedEncodingException, IOException {
		//RELAY-HOST-NOW
		//RELAY-SET-1,1,1

		System.out.println("请输入指令:");
		while(true) {
			//指定目标主机ip与端口
			String host = "192.168.1.254";
			int port = 4196;
			Socket socket = new Socket(host, port);

			//想服务端发送数据
			OutputStream outputStream = socket.getOutputStream();
			//接收用户输入
			Scanner sc = new Scanner(System.in);
			String message = sc.nextLine();
			outputStream.write(message.getBytes("ASCII"));
			outputStream.flush();
			//终止发送数据，客户端现在只接收数据
			//如果不加shutdownOutput，服务器端一直阻塞在read()方法中，导致客户端无法收到服务端回显的数据
			//shutdownOutput只关闭客户端向服务端的输出流，并不会关闭整个Socket的连接
			socket.shutdownOutput();

			//从服务端接收数据
			InputStream inputStream = socket.getInputStream();
			byte[] bytes = new byte[1024];
			int len = 0;
			StringBuilder sb = new StringBuilder();
			while ((len = inputStream.read(bytes)) != -1) {
				sb.append(new String(bytes, 0, len, "utf-8"));
				System.out.println("客户端正在从输入流中读数据");
			}
			System.out.println("来自服务端的回显:" + sb.toString());

			//调用close方法，会直接关闭整个Socket连接
			outputStream.close();
			inputStream.close();
			socket.close();
		}
	}
}