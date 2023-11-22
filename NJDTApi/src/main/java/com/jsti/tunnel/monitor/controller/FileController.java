package com.jsti.tunnel.monitor.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jsti.tunnel.monitor.util.FileUtil;

@RestController
@RequestMapping(value = "/file")
public class FileController extends BaseController {
	
    @Value("${storage.file.path}")
    String storageFilePath;
   

	/**
	 * 文件上传
	 * @param req
	 * @param multiReq
	 * @return
	 */
	@RequestMapping(value = "/upload")
	public String UploadFile(HttpServletRequest req, MultipartHttpServletRequest multiReq) {
		String fileFullPath = FileUtil.saveUploadFile(storageFilePath,multiReq.getFile("file"),"file");
		Map<String, String> data=new HashMap<>();
		data.put("path",fileFullPath);
		return returnResult(0, "上传成功", data);
	}

	/**
	 * 文件批量上传
	 * @param request
	 */
	@RequestMapping(value = "/batch/upload", method = RequestMethod.POST)
	@ResponseBody
	public String handleFileUpload(HttpServletRequest request) {
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
		List<String> filesPath = new ArrayList<>();
		for(MultipartFile file : files){
			filesPath.add(FileUtil.saveUploadFile(storageFilePath,file,"file"));
		}
		Map<String, Object> data=new HashMap<>();
		data.put("path",filesPath);
		return returnResult(0, "上传成功", data);
	}


	/**
	 * 文件下载
	 * @param res
	 * @param req
	 */
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public void download(HttpServletResponse res, HttpServletRequest req) {
		String filePath = req.getParameter("filePath");
		try {
			filePath = URLDecoder.decode(filePath,"utf-8");//中文解码
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String fileName = filePath.substring(filePath.lastIndexOf("\\")+1);
		res.setHeader("content-type", "application/octet-stream");
		res.setContentType("application/octet-stream");
		res.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		byte[] buff = new byte[1024];
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
			os = res.getOutputStream();
			bis = new BufferedInputStream(new FileInputStream(new File(filePath)));
			int i = bis.read(buff);
			while (i != -1) {
				os.write(buff, 0, buff.length);
				os.flush();
				i = bis.read(buff);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

 

}
