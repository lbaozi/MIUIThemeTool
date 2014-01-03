package utils;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.IImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;


public class ImageTools {
	
	/**
	 * 修改图片文件信息
	 * @param sourceFile 需要修改的文件
	 */
	public boolean changeExifMetadata(String sourceFilePath){
		boolean flag = false;
		try {
			File sourceFile = new File(sourceFilePath);
			if(sourceFile.exists()){
			    //获取源文件路径
				sourceFilePath = sourceFile.getAbsolutePath();
				//获得 / 的lastIndex 便于分隔
				int lastIndex = sourceFilePath.lastIndexOf("/");
				String filePath = "";
				//得到文件所在的目录
				if(lastIndex != -1) filePath= sourceFilePath.substring(0,lastIndex);
				//将文件转换为jpg格式,防止出现非JPG图片导致的写入文件信息失败的情况
		        BufferedImage img = ImageIO.read(sourceFile);
		        File outImageFile = new File(filePath,"jpg_"+sourceFile.getName());
		        ImageIO.write(img, "jpg", outImageFile);
		        //删除源文件
		        sourceFile.delete();
		        //重命名新生成的JPG文件
		        outImageFile.renameTo(sourceFile);
		        
				File outFile = new File(filePath,"out_"+sourceFile.getName());
				//修改文件信息
				changeExifMetadata(sourceFile,outFile);
				sourceFile.delete();
				outFile.renameTo(sourceFile);
				flag = true;
			}
		} catch (ImageReadException e) {
			e.printStackTrace();
		} catch (ImageWriteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	
	/**
	 * 修改文件属性
	 * @param sourceFile
	 * @param dst
	 * @throws IOException
	 * @throws ImageReadException
	 * @throws ImageWriteException
	 */
	public void changeExifMetadata(final File sourceFile, final File dst)
            throws IOException, ImageReadException, ImageWriteException {
        OutputStream os = null;
        TiffOutputSet outputSet = null;

        //如果没有得到metadata 则为null
        final IImageMetadata metadata = Imaging.getMetadata(sourceFile);
        final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
        if (null != jpegMetadata) {
        	//如果没有的到 Exif metadata 则为null
            final TiffImageMetadata exif = jpegMetadata.getExif();
            
            //从文件中获取原来的属性
            if (null != exif) outputSet = exif.getOutputSet();
        }
        //如果文件没有任何 Exif metadata 则创建空的
        if (null == outputSet) outputSet = new TiffOutputSet();

        
        final double longitude = -74.0;
        final double latitude = 40 + 43 / 60.0; 
        //修改文件的GPS信息
        outputSet.setGPSInDegrees(longitude, latitude);
        
        os = new BufferedOutputStream(new FileOutputStream(dst));
        
        ExifRewriter write = new ExifRewriter();

        write.updateExifMetadataLossless(sourceFile, os,outputSet);
        os.close();
    }
	
}
