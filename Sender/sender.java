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
		
		InetAddress host = InetAddress.getByName("24.214.242.190"); //Get IP address of host
      	Socket clientSocket = new Socket(host, 10004); //port number 
      	DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());
		
		FileWriter writer = new FileWriter("results_100mb.txt");
		
		byte [] sendData = new byte[8192]; //packet size
     	int numTimes = 1; //number of times to send file
      	long startTime, endTime, tGap,totalTime;
		totalTime = 0;
		
		File fs = new File("100mb.txt"); 
		FileInputStream fin = new FileInputStream(fs);
		
		int fileLength = (int)fs.length();
		int numBytes = 0;
		
		while (true)
      	{
			startTime = System.currentTimeMillis();
			if (fileLength < 8192) 
				numBytes = fin.read(sendData, 0, fileLength);
			else 
				numBytes = fin.read(sendData, 0, 8192);

			
			while(numBytes != -1)
			{
				 outToServer.write(sendData, 0, numBytes);
				 numBytes = fin.read(sendData, 0, 8192);
			}

			
			endTime = System.currentTimeMillis();
          	tGap = endTime - startTime;
          	totalTime = totalTime + tGap;
          	System.out.println("I am done for "+numTimes+ "th time sending with time: "  + tGap + "ms");
			writer.write("I am done for "+numTimes+ "th time sending with time: "  + tGap + "\n");
          	fin.close();
			clientSocket.close();
			
			
          	if(numTimes == 5)break;
			numTimes++;
          	clientSocket = new Socket(host, 10004);
          	outToServer = new DataOutputStream(clientSocket.getOutputStream());
          	fs = new File("100mb.txt");
          	fin = new FileInputStream(fs);
          	fileLength = (int)fs.length();
		}

         System.out.println("I am done sending the file " + numTimes +" times, and the average time is: " + (double)totalTime/numTimes + "ms");
		 System.out.println("I am done sending the file " + numTimes +" times, and the total time is: " + (double)totalTime+ "ms");
		
		 writer.write("I am done sending the file " + numTimes +" times, and the average time is: " + (double)totalTime/numTimes + "ms \n");
		 writer.write("I am done sending the file " + numTimes +" times, and the total time is: " + (double)totalTime+ "ms \n");
		 writer.close();



				



        


    }
}
