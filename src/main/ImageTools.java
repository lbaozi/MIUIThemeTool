package main;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.UserDefinedFileAttributeView;

import org.apache.commons.codec.digest.DigestUtils;


public class ImageTools {
	/**
	 * 替换文件
	 * @param filePath
	 */
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
	
	/**
	 * 修改文件属性
	 * @param filePath 文件路径
	 */
	public void changeFileAtt(String filePath){
		try {
			
			File file = new File(filePath);
			Path path = file.toPath();
			
			System.out.println("改变前文件MD5,filePath"+DigestUtils.md5Hex(new FileInputStream(filePath)));
			System.out.println("改变前文件MD5,file"+DigestUtils.md5Hex(new FileInputStream(file)));
			
			UserDefinedFileAttributeView userView = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
			ByteBuffer bb = ByteBuffer.wrap("你好啊".getBytes());
			//userView.write("name", bb);
			//userView.write("test", ByteBuffer.wrap("Test".getBytes()));
			
			ByteBuffer value = ByteBuffer.allocate(userView.size("name"));
			userView.read("name", value);
			
			System.out.println(new String(value.array(),"UTF-8"));
			System.out.println("改变后文件MD5,filepath:"+DigestUtils.md5Hex(new FileInputStream(filePath)));
			System.out.println("改变后文件MD5,file"+DigestUtils.md5Hex(new FileInputStream(file)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
