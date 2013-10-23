package main;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ImageTools {
	
	public void replace(String filePath){
		try {
			OutputStream out = new FileOutputStream(filePath);
			InputStream in = new FileInputStream("Resources/wallPaper.jpg");
			byte[] buf = new byte[1024];
			int len;
			
			while( (len=in.read(buf)) > 0){
				out.write(buf, 0, len);
			}
			
			in.close();
			out.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
