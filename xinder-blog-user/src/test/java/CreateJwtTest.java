import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Xinder
 * @date 2023-01-30 21:36
 */
public class CreateJwtTest {

    /***
     * 创建令牌测试
     */
    @Test
    public void testCreateToken() {
        //证书文件路径
        String key_location = "dongyimai.jks";
        //秘钥库密码
        String key_password = "dongyimai";
        //秘钥密码
        String keypwd = "dongyimai";
        //秘钥别名
        String alias = "dongyimai";

        //访问证书路径
        ClassPathResource resource = new ClassPathResource(key_location);

        //创建秘钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource, key_password.toCharArray());

        //读取秘钥对(公钥、私钥)
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, keypwd.toCharArray());

        //获取公钥
        //  RSAPublicKey rsaPublicKey= (RSAPublicKey) keyPair.getPublic();
        //  Base64Encoder base64Encoder = new Base64Encoder();
        //  String encode = base64Encoder.encode(rsaPublicKey.getEncoded());
        //  System.out.println("公钥:-----BEGIN PUBLIC KEY-----"+encode+"-----END PUBLIC KEY-----");

        //获取私钥
        RSAPrivateKey rsaPrivate = (RSAPrivateKey) keyPair.getPrivate();

        //定义Payload
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("user_name", "12313213121312");

        //生成Jwt令牌
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(tokenMap), new RsaSigner(rsaPrivate));

        //取出令牌
        String encoded = jwt.getEncoded();
        System.out.println("token:" + encoded);

    }
}
