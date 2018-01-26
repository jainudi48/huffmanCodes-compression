//Kaushil Ruparelia CS610 9169 prp

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class Write {
	private static BufferedOutputStream outputStream;
	
	private static int buffer;
	private static int bufferSize;
	
	public Write() {
		outputStream = new BufferedOutputStream(System.out);
	}
	
	public Write(String filename) {
		try {
			outputStream = new BufferedOutputStream(new FileOutputStream(filename));
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	public void write(boolean bit) {
		
		buffer <<= 1;
		if (bit) buffer |= 1;

		bufferSize++;
        if (bufferSize == 8) writeBuffer();
		
	}
	
	public void write(char ch) {
		writeByte(ch);
	}

	public void write(int number) {
        writeByte((number >>> 24) & 0xff);
        writeByte((number >>> 16) & 0xff);
        writeByte((number >>>  8) & 0xff);
        writeByte((number >>>  0) & 0xff);
	}
	
	
	private void writeByte(int myByte) {
		if (bufferSize == 0) {
			try {
				outputStream.write(myByte);
			} catch (IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		else {
			//Print bitwise
			for (int i = 0; i < 8; i++) {
	            boolean bit = ((myByte >>> (7-i)) & 1) == 1;
	            write(bit);
	        }
		}
	}

	private static void writeBuffer() {
		
		if(bufferSize == 0) 
			return;
		
		if (bufferSize > 0) {
			buffer<<= 8 - bufferSize;
		}
		
		try {
			outputStream.write(buffer);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		bufferSize = 0;
		buffer = 0;
	}
	
	public void close() {
		
		writeBuffer();
		try {
			outputStream.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		try {
			outputStream.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
