package yiyan.research.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.BasicSessionCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Configuration
@Component
//@ConfigurationProperties(prefix = "aliyun")
public class COSConfig {
    @Value("${aliyun.SecretId}")
    private String SecretId="AKIDeCcv67ypT1dJsLfpaHsLE2gCct7C8WtG";

    @Value("spring.mail.host")
    private String host;
    @Value("${aliyun.SecretKey}")

    private String SecretKey="StHg0BHORG4qWEnEoGVp9StCZwLbm4LT";
    @Value("${aliyun.regionName}")
    private String regionName="ap-beijing";


    @Bean
    public COSClient init(){
        String secretId =SecretId;
        String secretKey = SecretKey;
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        Region region = new Region(regionName);
        ClientConfig clientConfig = new ClientConfig(region);
        clientConfig.setHttpProtocol(HttpProtocol.https);
        System.out.println(" reName :" +region.getRegionName());
        return new COSClient(cred, clientConfig);
    }

}