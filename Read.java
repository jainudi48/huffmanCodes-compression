//Kaushil Ruparelia CS610 9169 prp

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Read {
	private static BufferedInputStream inputStream;
	
	private static int buffer;
	private static int bufferSize;
	
	public Read() {
		inputStream = new BufferedInputStream(System.in);
	}
	
	public Read(String filePath) {
		try {
			inputStream = new BufferedInputStream(new FileInputStream(filePath));
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public boolean readBit() throws Exception {

		if (bufferSize == 0) {
			readBuffer();
		} else if (bufferSize == -1) {
			throw new Exception("File ended!");
		}
		
		bufferSize--;
		return  ((buffer >> bufferSize) & 1) == 1;
	}
	
	public void readBuffer() {
		try {
			buffer = inputStream.read();
			if(buffer == -1) {
				return;
			}
			bufferSize = 8;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			buffer = -1;
			bufferSize = -1;
		}
	}
	
	public char readByte() throws Exception {
		if (bufferSize == 0) {
			readBuffer();
			int thisByte = (buffer & 0xFF);
			readBuffer();
			return (char)(thisByte & 0xFF);
		} else if (bufferSize == -1) {
			throw new Exception("File ended!");
		} else if (bufferSize == 8) {
			int thisByte = buffer;
			readBuffer();
			return (char)(thisByte & 0xFF);
		}
		else {
			int oldLength = bufferSize;
			int bits = buffer;
			bits <<= (8-oldLength);
			readBuffer();
			if (bufferSize == -1 || buffer == -1) {
				throw new Exception("File ended!");
			}
			
			bufferSize = oldLength;
			bits |= buffer >>> bufferSize; 
			return (char)(bits & 0xFF);	
		}		
	}

	public char readChar() throws Exception {
		return (char)(readByte() & 0xFF);
	}
	
	public String readString() throws Exception {
		StringBuilder sb = new StringBuilder();
		do {
			sb.append(readChar());
		}while (buffer != -1);
		
		return sb.toString();
	}
	
	public void close() throws IOException {           
		inputStream.close();
	}

	public int readInt() throws Exception {
		//Read Byte by Byte
		int number = 0;
		
		for (int i = 0; i < 4; i++) {
			number<<=8;
			number |= (readByte() & 0xFF); 
		}		
		return number;
	}
}
