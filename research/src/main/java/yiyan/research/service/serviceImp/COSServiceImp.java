package yiyan.research.service.serviceImp;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectResult;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import yiyan.research.config.COSConfig;
import yiyan.research.mapper.WorkDetailMapper;
import yiyan.research.service.COSService;

import java.net.URL;

@Service
public class COSServiceImp extends COSService {

    @Value("${aliyun.bucketName}")
    private String bucketName;
    @Override
    public String uploadFile(MultipartFile file, String path) {
        COSClient cosClient=new COSConfig().init();
        try{
            PutObjectResult res=cosClient.putObject(bucketName,path,file.getInputStream(),null);
            URL url=cosClient.getObjectUrl(bucketName,path);
            System.out.println(url);
            return url.toString();
        }catch ( Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public String uploadAuthorAvatar(MultipartFile file,String path){
        COSClient cosClient=new COSConfig().init();
        try{
            PutObjectResult res=cosClient.putObject(bucketName,path,file.getInputStream(),null);
            URL url=cosClient.getObjectUrl(bucketName,path);
            return url.toString();
        }catch ( Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
