package com.itStudy.controller.analysis;

import com.alibaba.fastjson.JSONObject;
import com.itStudy.spring.AfRestData;
import com.itStudy.spring.AfRestError;
import com.itStudy.util.FileStore;
import com.itStudy.util.Global;
import com.itStudy.util.MyUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * 文章图片上传
 */

@Controller
public class AnalysisImgController
{
    //上传临时图片
    @PostMapping("/analysis/fileUpload.do")
    public Object upload( @RequestBody JSONObject jreq ) throws Exception
    {
        // 临时图片的存储位置
        FileStore tmpStore = Global.getTmpStore();
        //base64图片文件
        String base64Data = jreq.getString("content");
        String name = jreq.getString("name");
        String type = jreq.getString("type");
        Long size = jreq.getLong("size");
        Long lastModified = jreq.getLong("lastModified");

        if (base64Data != null && base64Data.length() > 0)
        {
            if(size > 1024 * 1024 * 2)
            {
                return new AfRestError("传输失败，文件不能大于2M");
            }

            if(!type.split("/")[0].equals("image"))
            {
                return new AfRestError("请传入图片文件!");
            }

            //临时文件名
            String suffix = MyUtil.getSuffix(name);
            String tmpName = MyUtil.guid2() + suffix;
            String tmpFileurl = tmpStore.getAbsoUrl(tmpName);

            String[] d = base64Data.split("base64,");
//            String datatype = d[0];
//            String data = d[1];

            try
            {
                BASE64Decoder decoder = new BASE64Decoder();
                // Base64解码
                byte[] bytes = decoder.decodeBuffer(d[1]);
                for (int i = 0; i < bytes.length; ++i)
                {
                    if (bytes[i] < 0)
                    {// 调整异常数据
                        bytes[i] += 256;
                    }
                }
                // 生成图片
                OutputStream out = new FileOutputStream(tmpFileurl);
                out.write(bytes);
                out.flush();
                out.close();

                //转换成签到显示的样式
                String tmpUrl = "/tmp/" + tmpName;
                JSONObject data = new JSONObject(true);
                data.put("tmpUrl", tmpUrl);
                data.put("name", tmpName);
                return new AfRestData(data);
            } catch (Exception e)
            {
                return new AfRestError("上传失败，写入文件失败!");
            }
        } else
        {
            return new AfRestError("");
        }

    }

}
