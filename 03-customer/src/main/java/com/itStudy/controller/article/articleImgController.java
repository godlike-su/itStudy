package com.itStudy.controller.article;

import com.alibaba.fastjson.JSONObject;
import com.itStudy.spring.AfRestData;
import com.itStudy.spring.AfRestError;
import com.itStudy.util.FileStore;
import com.itStudy.util.Global;
import com.itStudy.util.MyUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文章图片上传
 */

@Controller
public class articleImgController
{
    //上传临时图片
    @PostMapping("/user/article/fileUpload.do")
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
        String storePath = jreq.getString("storePath");

        if (base64Data != null && base64Data.length() > 0)
        {
            if(size > 3000000)
            {
                return new AfRestError("传输失败，文件不能大于2m");
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

                if(storePath.equals(null) || storePath.trim().length() < 4)
                {
                    storePath = makeStorePath();
                }
                String imgName = moveTmpToStore(storePath, tmpName);
                //转换成签到显示的样式
                String tmpUrl = "/photo/tmp/" + tmpName;
                JSONObject data = new JSONObject(true);
                data.put("storePath", storePath);
                data.put("imgName", imgName);
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

    // 附件图片存储路径 示例 202001/01/15725791906031/
    public static String makeStorePath()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/dd/");
        return sdf.format(new Date()) + MyUtil.guid2() + "/";
    }

    // 将临时文件存储到Store，并返回存储路径 (此处只存储文件名)
    private String moveTmpToStore(String storePath, String tmpName)
    {
        if (tmpName == null || tmpName.length() == 0) return "";

        File tmpFile = Global.getTmpStore().getFile(tmpName);

        //获取入库图片路径
        File storeDir = Global.getArticleStore().getFile(storePath);
        try
        {
            FileUtils.moveFileToDirectory(tmpFile, storeDir, true);
            //图片-1
            return tmpName;
        } catch (Exception e)
        {
            return tmpName;
        }
    }

}
