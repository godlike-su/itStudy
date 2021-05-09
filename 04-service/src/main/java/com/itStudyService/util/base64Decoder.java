package com.itStudyService.util;

import sun.misc.BASE64Decoder;

import java.io.*;

public class base64Decoder
{
    public static String Readfile2Str(String filePath)
    {
        String fileIOstr;
        try
        {
            BufferedReader in = new BufferedReader(new FileReader(filePath));
            while ((fileIOstr = in.readLine()) != null)
            {
                break;
            }
        } catch (IOException e)
        {
            System.out.println(e);
            fileIOstr = "";
        }
        return fileIOstr;
    }

    public static void Base64ToImg(String filePath, String fileNmae, String fileType)
    {
        String fileIOstr = Readfile2Str(filePath);
        //对内容进行base64解码
        BASE64Decoder decoder = new BASE64Decoder();
        try
        {
            FileOutputStream write = new FileOutputStream(new File(fileNmae + "." + fileType));
            byte[] decodeBytes = decoder.decodeBuffer(fileIOstr);
            write.write(decodeBytes);
            write.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
