package com.xinder.file.config;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Xinder
 * @date 2023-01-15 12:27
 */
public class QiNiuUtil {

    /**
     * 上传本地文件
     * @param localFilePath 本地文件完整路径
     * @param key 文件云端存储的名称
     * @param override 是否覆盖同名同位置文件
     * @return
     */
    public static boolean upload(String localFilePath, String key, boolean override){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(QiNiuConfig.getInstance().getZone());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        Auth auth = Auth.create(QiNiuConfig.ACCESS_KEY, QiNiuConfig.getInstance().getSecretKey());
        String upToken;
        if(override){
            upToken = auth.uploadToken(QiNiuConfig.getInstance().getBucket(), key);//覆盖上传凭证
        }else{
            upToken = auth.uploadToken(QiNiuConfig.getInstance().getBucket());
        }
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
            return true;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
                return false;
            }
            return false;
        }
    }


    /**
     * 获取文件访问地址
     * @param fileName 文件云端存储的名称
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String fileUrl(String fileName) throws UnsupportedEncodingException {
        String encodedFileName = URLEncoder.encode(fileName, "utf-8");
        String publicUrl = String.format("%s/%s", QiNiuConfig.getInstance().getDomainOfBucket(), encodedFileName);
        Auth auth = getAuth();
        long expireInSeconds = QiNiuConfig.getInstance().getExpireInSeconds();
        if (-1 == expireInSeconds) {
            return auth.privateDownloadUrl(publicUrl);
        }
        return auth.privateDownloadUrl(publicUrl, expireInSeconds);
    }

    public static Auth getAuth(){
//        Auth auth = Auth.create(QiNiuConfig.getInstance().getAccessKey(), QiNiuConfig.getInstance().getSecretKey());
        Auth auth = Auth.create(QiNiuConfig.ACCESS_KEY, QiNiuConfig.getInstance().getSecretKey());
        return auth;
    }


}
