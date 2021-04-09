package com.itStudy.service.Impl;

import com.itStudy.dao.UserDao;
import com.itStudy.entity.User;
import com.itStudy.service.UserPhotoService;
import com.itStudy.util.FileStore;
import com.itStudy.util.Global;
import com.itStudy.util.MyUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.*;

@Service
public class UserPhotoServiceImpl implements UserPhotoService
{
    @Autowired
    private UserDao userDao;

    @Override
    public String usePhoto(User user, File tmpImage, String suffix) throws Exception
    {
        //
        FileStore store = Global.getThumbStore();
        // 示例： 000/1_15728601766396.jpg, 保存在数据库的图片路径
        String path = String.format("%03d/%d_%s" + suffix,
                user.getId() / 1000, user.getId(), MyUtil.guid2());

        // 先删除旧的照片
        if (user.getThumb() != null && user.getThumb().length() > 0 && !"/Thumb.png".equals(user.getThumb()))
        {
            File oldFile = store.fileOfUrl(user.getThumb());
            try
            {
                FileUtils.deleteQuietly(oldFile);
            } catch (Exception e)
            {
                System.out.println("** 不能删除旧的头像: " + oldFile);
            }
        }
        File dstFile = store.getFile(path);
        // 保存新的头像图片
        dstFile.getParentFile().mkdirs(); // 创建层次目录
        clipPhoto(tmpImage, dstFile); // 修改图片

//        FileUtils.moveFileToDirectory(tmpImage, dstFile, true);
        try
        {
            //删除缓存区里的图片
            tmpImage.delete();
        } catch (Exception e)
        {
            System.out.println(e);
        }

        user.setThumb(store.getUrl(path));

        //修改路径
        userDao.updateByPrimaryKeySelective(user);

        return user.getThumb();
    }

    // 加入 thumbnailator-0.4.8.jar，图像裁剪
    private static void clipPhoto(File srcFile, File dstFile)
    {
        /*
        // 读取源图，缩小为100x100 (最大尺寸)
        BufferedImage img = Thumbnails.of(srcFile).size(100, 100).asBufferedImage();

        //截取中间的正方形
        int w = img.getWidth();
        int h = img.getHeight();
        int size = w < h ? w : h;   //取最小值
        int x = (w - size) / 2;
        int y = (h - size) / 2;

        //截取
        Thumbnails.of(img)
                .scale(1.0f)
                .sourceRegion(x, y, size, size)
                .outputFormat("jpg")
                .toFile(dstFile);
        */
        // 读取源图，缩小为100x100 (最大尺寸)
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try
        {
            inputStream = new FileInputStream(srcFile);
            outputStream = new FileOutputStream(dstFile);

            byte[] buf = new byte[4096];
            long total = 0;

            while (true)
            {
                int n = inputStream.read(buf);
                if (n <= 0)
                    break;
                outputStream.write(buf, 0, n);
                total += n;
            }
        } catch (Exception e)
        {
        } finally
        {
            try
            {
                outputStream.close();
                inputStream.close();
            } catch (Exception e1)
            {
            }
        }


    }
}
