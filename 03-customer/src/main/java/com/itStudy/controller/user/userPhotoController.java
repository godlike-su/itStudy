package com.itStudy.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.itStudy.entity.User;
import com.itStudy.service.UserPhotoService;
import com.itStudy.spring.AfRestData;
import com.itStudy.spring.AfRestError;
import com.itStudy.util.FileStore;
import com.itStudy.util.Global;
import com.itStudy.util.MyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

@Controller
@RequestMapping("/user")
public class userPhotoController
{
    @Autowired
    private UserPhotoService userPhotoService;

    @PostMapping("/profile/setPhoto.do")
    public Object setPhoto(@RequestBody JSONObject jreq) throws Exception
    {
        FileStore tmpStore = Global.getTmpStore();

        //base64图片文件
        String base64Data = jreq.getString("content");
        String name = jreq.getString("name");
        String type = jreq.getString("type");
        Long size = jreq.getLong("size");
        Long lastModified = jreq.getLong("lastModified");

        if (base64Data != null && base64Data.length() > 0)
        {
            if(size > 500000)
            {
                return new AfRestError("传输失败，文件不能大于500k");
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


                User user = jreq.getObject("user", User.class);

                File tmpFile = new File(tmpFileurl);
                // 头像的正式URL
                String url = userPhotoService.usePhoto(user, tmpFile, suffix);

                //转换成签到显示的样式
//                String tmpUrl = "/photo/tmp/" + tmpName;
                return new AfRestData(url);
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
