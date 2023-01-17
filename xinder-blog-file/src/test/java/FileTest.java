import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

/**
 * @author Xinder
 * @date 2023-01-14 17:42
 */
public class FileTest {


    public static void main(String[] args) {
        String accessKey = "0OHdkTxmT_nnOMhVVqSre9Kqo-sSPJYIu-0RhHdz";
        String secretKey = "tuHYk95xUWWstg7OPVQYDS290CkUA7ubLTUTVgIc";
        String bucket = "xinderblog";
        Auth auth = Auth.create(accessKey, secretKey);

//        StringMap putPolicy = new StringMap();
//        putPolicy.put("callbackUrl", "http://api.example.com/qiniu/upload/callback");
//        putPolicy.put("callbackBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
//        putPolicy.put("callbackBodyType", "application/json");
//        long expireSeconds = 3600;
//        String upToken = auth.uploadToken(bucket, null, expireSeconds, putPolicy);
//        System.out.println(upToken);

        String upToken = auth.uploadToken(bucket);
        System.out.println(upToken);

        Region region = new Region.Builder()
                .region("z0")
                .accUpHost("up.qiniup.com")
                .srcUpHost("upload.qiniup.com")
                .iovipHost("iovip.qiniuio.com")
                .rsHost("rs.qiniu.com")
                .rsfHost("rsf.qiniu.com")
                .apiHost("api.qiniu.com")
                .build();

        //构造一个带指定Region对象的配置类
        Configuration cfg = new Configuration(Region.region2());
//        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本

        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "blog/dsfsdfdsfsdf";

        try {
            byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(uploadBytes);
            try {
                Response response = uploadManager.put(byteInputStream, key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (UnsupportedEncodingException ex) {
            //ignore
        }
    }
}
