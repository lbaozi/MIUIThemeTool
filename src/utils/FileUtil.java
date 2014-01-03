package utils;

import java.io.File;

public class FileUtil {
    
    /**
     * 删除文件 或者文件夹
     * @param path
     * @return
     */
    public static boolean delete(String path){
        File file = new File(path);
        boolean flag = true;
        if(file.exists()){
            if(file.isFile()){
                flag = file.delete();
            }else{
                File[] _fileArr = file.listFiles();
                for(int i=0;i<_fileArr.length;i++){
                    if(!(flag = delete(_fileArr[i].getAbsolutePath()))) break;
                }
                flag = flag && file.delete();
            }
        }
        return flag;
    }
    
    /**
     * 删除指定后缀名的文件
     * @param path 文件路径
     * @param filter 要删除文件的后缀名
     */
    public static boolean delete(String path,String filter){
        boolean flag = true;
        File file = new File(path);
        if(file.exists()){
            if(file.isFile() && file.getName().endsWith(filter)){
                flag = file.delete();
            }else if(file.isDirectory()){
                File[] fileArr = file.listFiles();
                for(int i=0;i<fileArr.length;i++){
                    File _file = fileArr[i];
                    if(!(flag = delete(_file.getAbsolutePath(),filter))) break;
                }
            }
        }
        
        return flag;
    }
}
