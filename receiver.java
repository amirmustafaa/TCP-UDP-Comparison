import java.io.*;
import java.net.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.FileWriter;


public class receiver {

    public static void main(String args[]) throws Exception {
		
		int numTimes = 0;
		final int NUM_TIME_SEND = 10;
		FileOutputStream outPut = new FileOutputStream("receivedFile" + numTimes+ ".txt");
    	ServerSocket welcomeSocket = new ServerSocket(6788);
		FileWriter writer = new FileWriter("results2.txt");
		
		System.out.println("I am starting now....");
		
		byte [] receiveData = new byte[8192];
		long startTime, endTime, tGap;
		long totalTime = 0;
		int numBytes;
		Socket connectionSocket;
		
		while(true){
			connectionSocket = welcomeSocket.accept();

			DataInputStream inFromClient = new DataInputStream(connectionSocket.getInputStream());

			numBytes = inFromClient.read(receiveData, 0, 8192);
			startTime = System.currentTimeMillis();
			while(numBytes != -1)
			{

				outPut.write(receiveData, 0, numBytes);
				numBytes = inFromClient.read(receiveData, 0, 8192);
			}
			outPut.close();
        	endTime = System.currentTimeMillis();
        	tGap = endTime - startTime;
			totalTime = totalTime + tGap;
			System.out.println("I am done for "+numTimes+ "th times receiving with time: "  + tGap + " ms");
			writer.write("I am done for "+numTimes+ "th times receiving with time: "  + tGap + " ms \n");
			
        	connectionSocket.close();
        
        	numTimes++;
        	if(numTimes == NUM_TIME_SEND) break;
       	 	outPut = new FileOutputStream("receivedFile" + numTimes+ ".txt");
			
			
		}	
		
    	welcomeSocket.close();
		
		MessageDigest md = MessageDigest.getInstance("MD5");
		int i = 0;
        String hex = checksum("StandardFile1.txt", md);
		String hex1 = checksum("receivedFile" + i + ".txt" , md);
		int numErrors = 0;
		
		
		for(i = 0; i <= 9; i++){
			if(hex.equals(hex1)){
			   System.out.println("File " + i + " has 0 errors");
			}
			else{
			   System.out.println("Error");
			   numErrors++;
			}
			hex1 = checksum("receivedFile" + i + ".txt" , md);
			
			
		}
		
		System.out.println("I am done receiving the file " + numTimes +" times, and the average time is: " + (double)totalTime/numTimes + "ms with " + numErrors+ " Errors.");
		
		writer.write("I am done receiving the file " + numTimes +" times, and the average time is: " + (double)totalTime/numTimes + "ms with " + numErrors+ " Errors. \n");
		writer.close();
 
    }
	
	
	private static String checksum(String filepath, MessageDigest md) throws IOException {

        
        try (InputStream fis = new FileInputStream(filepath)) {
            byte[] buffer = new byte[102400];
            int nread;
            while ((nread = fis.read(buffer)) != -1) {
                md.update(buffer, 0, nread);
            }
        }

        // bytes to hex
        StringBuilder result = new StringBuilder();
        for (byte b : md.digest()) {
            result.append(String.format("%02x", b));
        }
        return result.toString();

    }
}
