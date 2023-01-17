package com.xinder.file.config;

import com.qiniu.common.Zone;
import lombok.Data;

import java.util.Properties;

/**
 * @author Xinder
 * @date 2023-01-15 12:25
 */
@Data
public class QiNiuConfig {

    public static final String ACCESS_KEY = "0OHdkTxmT_nnOMhVVqSre9Kqo-sSPJYIu-0RhHdz";
    private String secretKey = "tuHYk95xUWWstg7OPVQYDS290CkUA7ubLTUTVgIc";
    private String bucket;
    private Zone zone;
    private String domainOfBucket;
    private long expireInSeconds;

    private static QiNiuConfig instance = new QiNiuConfig();

    private QiNiuConfig(){
        Properties prop = new Properties();
        try {
            prop.load(QiNiuConfig.class.getResourceAsStream("/qiniu.properties"));
//            accessKey = prop.getProperty("qiniu.access-key");
//            secretKey = prop.getProperty("qiniu.secret-key");
//            bucket = prop.getProperty("qiniu.bucket");
            domainOfBucket = prop.getProperty("qiniu.domain-of-bucket");
            expireInSeconds = Long.parseLong(prop.getProperty("qiniu.expire-in-seconds"));
            String zoneName = prop.getProperty("qiniu.zone");
            if(zoneName.equals("zone0")){
                zone = Zone.zone0();
            }else if(zoneName.equals("zone1")){
                zone = Zone.zone1();
            }else if(zoneName.equals("zone2")){
                zone = Zone.zone2();
            }else if(zoneName.equals("zoneNa0")){
                zone = Zone.zoneNa0();
            }else if(zoneName.equals("zoneAs0")){
                zone = Zone.zoneAs0();
            }else{
                throw new Exception("Zone对象配置错误！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static QiNiuConfig getInstance(){
        return instance;
    }
//    public static void main(String[] args) {
//        System.out.println(QiNiuConfig.getInstance().getAccessKey());
//    }

}
