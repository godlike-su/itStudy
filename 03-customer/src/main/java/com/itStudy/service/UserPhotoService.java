package com.itStudy.service;

import com.itStudy.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

public interface UserPhotoService
{
    //上传用户头像
    String usePhoto(User user, File tmpImage, String suffix) throws Exception;
}
