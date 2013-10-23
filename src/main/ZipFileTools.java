package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.swing.text.StyledEditorKit.ItalicAction;

public class ZipFileTools {
	
	/**
	 * 
	 * @param zipFilePath
	 * @param type
	 * @return 解压成功返回
	 */
	public Boolean unZip(String zipFilePath,int type){
		Boolean flag = false;
		
		try {
			flag = unZip(new ZipFile(zipFilePath),"",type);
		} catch (IOException e) {
			return flag;
		}
		
		return flag;
	}
	
	
	/**
	 * 解压ZIP文件 如果内部包含ZIP文件则递归解压
	 * @param zipFile ZIP文件对象
	 * @param unZipPath 解压路径 如果为空 则默认为解压至压缩包所在路径
	 * @param type 0 表示解压完成后删除源文件 1 表示不删除
	 * @return
	 */
	public Boolean unZip(ZipFile zipFile,String unZipPath,int type){
		String zipFilePath = zipFile.getName();
		//如果解压路径为空 则 解压到当前ZIP文件路径
		if(unZipPath == null || "".equals(unZipPath) || unZipPath.replaceAll(" ", "").equals("")){
			unZipPath = zipFilePath.substring(0, zipFilePath.lastIndexOf("."));
		}
		//如果文件路径结尾不为 / 则添加
		if(!unZipPath.endsWith("/")){
			unZipPath += "/";
		}
		
		try {
			Enumeration<? extends ZipEntry> entrys = zipFile.entries();
			while(entrys.hasMoreElements()){
				ZipEntry entry = entrys.nextElement();
				String entryName = entry.getName();
				InputStream in = zipFile.getInputStream(entry);
				String outPath = (unZipPath+entryName).replaceAll("\\\\", "/");
				File outFile = new File(outPath.substring(0,outPath.lastIndexOf("/")));
				
				//如果路径不存在则创建
				if(!outFile.exists()){
					outFile.mkdirs();
				}
				//如果是文件夹 则无须解压 
				if(new File(outPath).isDirectory()){
					continue;
				}
				
				OutputStream out = new FileOutputStream(outPath);
				byte[] buf = new byte[1024];
				int len;
				
				while((len = in.read(buf)) > 0){
					out.write(buf, 0, len);
				}
				
				in.close();
				out.close();
				
				/*由于MIUI 主题包中仍然包含ZIP文件 所以需要判断是否为ZIP文件 如果是则继续进行解压
				if(isZip(outPath)){
					new File(outPath).renameTo(new File(outPath+".zip"));
					unZip(outPath+".zip",1);
				}*/
				
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally{
			//执行完成后关闭文件
			try {
				zipFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//如果type 为 1 则删除原压缩包
		if(type == 1){
			new File(zipFile.getName()).delete();
		}
		
		return true;
	}
	
	/**
	 * 判断文件是否为ZIP文件
	 * @param filePath 文件路径
	 * @return 是则返回 true 不是则返回 false
	 */
	public Boolean isZip(String filePath){
		try {
			new ZipFile(filePath).close();
		} catch (IOException e) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 压缩ZIP文件 
	 * @param zipFilePath ZIP文件路径
	 * @param sourcePath  源文件路径
	 */
	public void zipMIUI(String zipFilePath,String sourcePath){
		ZipOutputStream out;
		//MIUI 主题包 根目录下除了以下文件外 都需要压缩后 在进行压缩
		String spFileName = "preview,wallpaper,description.xml";
		try {
			out = new ZipOutputStream(new FileOutputStream(zipFilePath));
			File sourceFile = new File(sourcePath);
			File[] files = sourceFile.listFiles();
			for(int i=0;i<files.length;i++){
				String _filePath = files[i].getPath();
				if(spFileName.indexOf(files[i].getName()) == -1){
					File _newFile = new File(_filePath+".bak");
					//重命名文件夹为 .bak
					files[i].renameTo(_newFile);
					File[] _files = _newFile.listFiles();
					ZipOutputStream _out = new ZipOutputStream(new FileOutputStream(_filePath));
					//首先压缩特殊文件
					for(int j=0;j<_files.length;j++){
						zipFiles(_out, "", _files[j].getPath());
					}
					_out.close();
					//压缩临时ZIP文件
					zipFiles(out,"",_filePath);
					//压缩完成后删除临时ZIP文件
					new File(_filePath).delete();
					//还原文件夹的名字
					_newFile.renameTo(files[i]);
				}else{
					zipFiles(out,"",_filePath);
				}
			}
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("*****压缩完成*****");
		
	}
	
	/**
	 * 压缩文件 会将目录内的所有文件压缩到
	 * @param zipFilePath 压缩文件路径
	 * @param sourcePath  源目录路径
	 */
	public void zip(String zipFilePath,String sourcePath){
		try {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFilePath));
			File[] files = new File(sourcePath).listFiles();
			
			for(int i=0;i<files.length;i++){
				zipFiles(out,"",files[i].getPath());
			}
			
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("******压缩完成*****");
	}
	
	/**
	 * 压缩ZIP文件
	 * @param out ZIP文件输出流
	 * @param path ZIP中文件路径
	 * @param sourcePath 源文件路径
	 */
	public void zipFiles(ZipOutputStream out,String path,String sourcePath){
		File sourceFile = new File(sourcePath);
		
		if(!"".equals(path) && !path.endsWith("/")){
			path += "/";
		}
		
		try {
			if(sourceFile.isDirectory()){
				File[] files = sourceFile.listFiles();
				String _fileName = sourceFile.getName();
				
				out.putNextEntry(new ZipEntry(path+_fileName+"/"));
				for(int i=0;i<files.length;i++){
					zipFiles(out,path+_fileName,files[i].getPath());
				}
				out.closeEntry();
			}else{
				FileInputStream in = new FileInputStream(sourcePath);
				out.putNextEntry(new ZipEntry(path+sourceFile.getName()));
				byte[] buf = new byte[1024];
				int len;
				
				while((len = in.read(buf)) > 0 ){
					out.write(buf, 0, len);
				}
				out.closeEntry();
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
