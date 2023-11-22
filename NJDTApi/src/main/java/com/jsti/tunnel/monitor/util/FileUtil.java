package com.jsti.tunnel.monitor.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.jsti.system.err.BusinessException;

public class FileUtil {

   

   /**
    * 文件上传到服务器
    * @param path properties文件中配置的路径:storage.file.path
    * @param file 
    * @param moduleName 文件属于的功能模块
    * @return
    */
    public static String saveUploadFile(String path,MultipartFile file, String moduleName){
        if(file==null) return null;
        String dir = path + moduleName + "\\" + UUID.randomUUID().toString() + "\\";
        //初始化文件存储路径
        File fileSaveDir = new File(dir);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdirs();
        }

        String filePath = dir + file.getOriginalFilename();
        FileOutputStream fos = null;
        FileInputStream fis = null;
        try {
            fis = (FileInputStream) file.getInputStream();
            fos = new FileOutputStream(new File(filePath));
            byte[] temp = new byte[1024];
            int i = fis.read(temp);
            while (i != -1) {
                fos.write(temp, 0, temp.length);
                fos.flush();
                i = fis.read(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new BusinessException(20001);
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new BusinessException(20001);
                }
            }
        }
        return filePath;
    }

    
    /**
     * 将文件流输出到浏览器端下载文件
     * @param request
     * @param response
     * @param downloadFileName
     * @param in
     */
    public static void downloadFile(HttpServletRequest request, HttpServletResponse response,String downloadFileName, InputStream in) {
    	response.reset();        
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/x-download");
		// 设置content-disposition响应头控制浏览器以下载的形式打开文件
		if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {    
			try {
				downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO8859-1");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // firefox浏览器    
		} 
		else if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {    
			try {
				downloadFileName = URLEncoder.encode(downloadFileName, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// IE浏览器    
		}
		else if (request.getHeader("User-Agent").toUpperCase().indexOf("CHROME") > 0) {    
			try {
				downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO8859-1");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// 谷歌    
		}  
		response.addHeader("Content-Disposition","attachment;filename=" + downloadFileName);
		
		 
		int len = 0;
		byte[] buffer = new byte[1024];
		OutputStream out;
		try {
			out = response.getOutputStream(); 
			while ((len = in.read(buffer)) > 0) {
				//将缓冲区的数据输出到客户端浏览器
				out.write(buffer,0,len);
			}  
			in.close(); 
			out.flush();
			out.close();
			response.flushBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * * 删除文件
     * @param full_path 文件完整路径
     */
    public static void deleteFile(String full_path) {
    	try {
			File file = new File(full_path);
			if(file.exists()) {
				File parentFile = file.getParentFile();
				file.delete();//删除文件
				if(parentFile.isDirectory() && parentFile.listFiles().length==0) {
					//如果父文件夹为空，删除该文件夹
					parentFile.delete();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }




}
