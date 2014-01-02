package main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		
		ZipFileTools zipFileTool = new ZipFileTools();
		ImageTools imageTool = new ImageTools();
		String zipFilePath,unZipFilePath,savePath;
		
		zipFilePath = "E:/下载/Lonely+girl-1.0.0.3.mtz";
		unZipFilePath = "E:/下载/我爱你，但与你无关-1.0.0.3";
		
		
		zipFilePath = "E:/下载/我爱你，但与你无关-1.0.0.3.mtz";
		
		System.out.println(zipFilePath);
		
		unZipFilePath = zipFilePath.substring(0, zipFilePath.lastIndexOf("."));
		
		System.out.println("*****开始解压主题文件*****");
		zipFileTool.unZip(zipFilePath, 0);
		System.out.println("*****解压主题文件完成*****");
		
		System.out.println("*****开始修改主题信息*****");
		XmlTools xmlTools = new XmlTools(unZipFilePath+"/description.xml");
		Map<String,String> map = new HashMap<>();
		map.put("designer", "LHY");
		map.put("author", "LHY");
		xmlTools.update(map);
		System.out.println("*****修改主题信息完成*****");
		
		
		System.out.println("******开始修改壁纸信息*****");
		File wallPaperDir = new File(unZipFilePath+"/wallpaper");
		File[] fileList = wallPaperDir.listFiles();
		
		for(int i=0;i<fileList.length;i++){
			imageTool.replace(fileList[i].getPath());
		}
		System.out.println("*****修改壁纸信息完成*****");
		
		savePath = zipFilePath.substring(0, zipFilePath.lastIndexOf("/")) + "/test.mtz";
		
		
		
		System.out.println("*****开始重新打包*****");
		zipFileTool.zipMIUI(savePath, unZipFilePath);
		System.out.println("*****重新打包完成*****");
		
	}
}
