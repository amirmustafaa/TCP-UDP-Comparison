import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileWriter;

public class sender {

    public static void main(String args[]) throws Exception {
		
		InetAddress host = InetAddress.getLocalHost();
		//InetAddress host = InetAddress.getByName("168.26.127.108");
      	Socket clientSocket = new Socket(host, 6788);
      	DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());
		
		FileWriter writer = new FileWriter("results.txt");
		
		byte [] sendData = new byte[8192];
		int numLines = 0;
     	int m =0;
      	long startTime, endTime, tGap,totalTime;
		totalTime = 0;
		
		File fs = new File("1kb.txt");
		FileInputStream fin = new FileInputStream(fs);
		
		int fileLength = (int)fs.length();
		int numBytes = 0;
		
		while (true)
      	{
			
			if (fileLength < 8192) 
				numBytes = fin.read(sendData, 0, fileLength);
			else 
				numBytes = fin.read(sendData, 0, 8192);

			startTime = System.currentTimeMillis();
			while(numBytes != -1)
			{
				 outToServer.write(sendData, 0, numBytes);
				 numBytes = fin.read(sendData, 0, 8192);
			}

			
			endTime = System.currentTimeMillis();
          	tGap = endTime - startTime;
          	totalTime = totalTime + tGap;
          	System.out.println("I am done for "+m+ "th time sending with time: "  + tGap + "ms");
			writer.write("I am done for "+m+ "th time sending with time: "  + tGap + "\n");
          	fin.close();
			clientSocket.close();
			
			m++;
          	if(m == 10) break;
          	clientSocket = new Socket(host, 6788);
          	outToServer = new DataOutputStream(clientSocket.getOutputStream());
          	fs = new File("1kb.txt");
          	fin = new FileInputStream(fs);
          	fileLength = (int)fs.length();
		}

         System.out.println("I am done sending the file " + m +" times, and the average time is: " + (double)totalTime/m + "ms");
		
		 writer.write("I am done sending the file " + m +" times, and the average time is: " + (double)totalTime/m + "ms \n");
		 writer.close();



				



        


    }
}
