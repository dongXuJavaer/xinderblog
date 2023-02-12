package com.xinder.common.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


/**
 * @author Xinder
 * @date 2023-01-15 13:11
 */
public class FileUtils {

    private static final String ACCESS_KEY = "0OHdkTxmT_nnOMhVVqSre9Kqo-sSPJYIu-0RhHdz";
    private static final String SECRET_KEY = "tuHYk95xUWWstg7OPVQYDS290CkUA7ubLTUTVgIc";
    private static final String BUCKET = "xinderblog";
    private static final String PATH = "blog";
    private static final String domain = "http://rogv8l3fv.hn-bkt.clouddn.com/";

    public static final String FOLDER_ATTACHMENT = "attachment";  // 附件文件夹
    public static final String FOLDER_HEADPIC = "headpic"; // 头像文件夹
    public static final String FOLDER_ING = "img";   // 帖子图片文件夹
    public static final String FOLDER_RESOURCE = "resource ";  //资源文件夹

    /*    ================== 用户文件夹============ */
    public static final String FOLDER_USER_HEAD = "userheadimg";  //用户头像文件夹
    public static final String FOLDER_GROUP_HEAD = "usergroupimg";  //群头像文件夹


    /**
     * @param file ...
     * @return 文件地址
     */
    public static String upload(MultipartFile file, String folder) {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "---";

        String filename = PATH + "/" + folder + "/"
                + date + UUID.randomUUID().toString()
                + "_" + file.getOriginalFilename().replaceAll(" ", "");

        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String upToken = auth.uploadToken(BUCKET);
        //构造一个带指定Region对象的配置类
        Configuration cfg = new Configuration(Region.region2());

        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);

        try {
            Response response = uploadManager.put(file.getInputStream(), filename, upToken, null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return domain + filename;
    }

}
