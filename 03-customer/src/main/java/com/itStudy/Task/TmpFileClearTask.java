package com.itStudy.Task;

import com.itStudy.util.FileStore;
import com.itStudy.util.Global;
import org.apache.commons.io.FileUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;

@Component
public class TmpFileClearTask
{
    FileStore tmpStore = Global.getTmpStore();

    int EXPIRED = 1000 * 3600 * 3; // 3小时以上的临时文件 才清除

    //每隔1个钟检查一次
    @Scheduled(cron = "0 0 * * * ?")
    public void run()
    {
        System.out.println("清理tmpFile文件: " + new Date());
        File tmpDir = tmpStore.getFile("");
        if(! tmpDir.exists()) return;
        File[] files = tmpDir.listFiles();
        if(files == null || files.length ==0)
            return;

        // 清理过期的文件
        long now = System.currentTimeMillis();
        for(File file: files)
        {
            if(now - file.lastModified() > EXPIRED)
            {
                try
                {
                    System.out.println("** 清理过期文件: " + file.getAbsolutePath());
                    FileUtils.deleteQuietly(file);
                }catch (Exception e){}
            }
        }
    }

}
