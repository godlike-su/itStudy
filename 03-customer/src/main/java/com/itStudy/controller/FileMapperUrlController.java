package com.itStudy.controller;

import com.itStudy.util.FileStore;
import com.itStudy.util.Global;
import com.itStudy.util.MyUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/** 临时图片的显示
 * 映射 /itStudy/tmp/ 到 c:/itStudy/tmp/ 目录
 * 
 *
 */
@Controller
public class FileMapperUrlController
{

	@GetMapping("/photo/**")
	public void ImageUrl(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// requestUri: 例如 /luntan/bbsfile/message/202003/24/15850605784804/15850605388631.jpg
		String requestUri = request.getRequestURI();
		
		//取得文件名
//		String[] prefixArray = requestUri.split("/");
		String prefix = "/photo/";
		int p = requestUri.indexOf(prefix);
		String filePath = requestUri.substring(p + prefix.length());

		//目标文件
		//**************************************************注意是否正确
//		FileStore fileStore = new FileStore("c:/itStudy/", "/itStudy/");
		File targetFile = new FileStore("c:/itStudy/", "/itStudy/").getFile(filePath);
		
		// 检查目标文件是否存在
		if(!targetFile.exists() || ! targetFile.isFile())
		{
			System.out.println("目标文件不存在!" +targetFile);
			response.sendError(404);
			return;
		}
		
		// 根据后缀名，获取 Content-Type : image/jpeg, image/png等
		String suffix = urlSuffix(filePath);
		String contentType = MyUtil.getContentType(suffix);
		
		//应答：设置Content-Type  Content-Length
		long contentLength = targetFile.length();
		response.setContentType(contentType);
		response.setHeader("Content-Length", String.valueOf(contentLength));
		
		//应答：读取目标文件的数据，发送给客户端
		InputStream inputStream = new FileInputStream(targetFile);
		OutputStream outputStream = response.getOutputStream();
		
		try
		{
			streamCopy (inputStream, outputStream);
		}catch(Exception e)
		{
			try{ inputStream.close();} catch(Exception e2){}
		}
		inputStream.close();
		outputStream.close();
	}

	
	// 获取 URL 里的后缀名信息
	private String urlSuffix(String url)
	{
		int p1 = url.lastIndexOf('/');
		if(p1 <0) p1 = 0; // 如果没有/，则从头开始检查
		
		int p2 = url.indexOf('.', p1+1);
		if(p2 > 0)
		{
			return url.substring(p2+1);
		}
		
		return "";
	}

	private long streamCopy(InputStream in, OutputStream out) throws Exception
	{
		long count = 0;
		byte[] buf = new byte[8192];
		while (true)
		{
			int n = in.read(buf);
			if (n < 0)
				break;
			if (n == 0)
				continue;
			out.write(buf, 0, n);

			count += n;
		}
		return count;
	}
}
