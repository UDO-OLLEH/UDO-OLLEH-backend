package com.udoolleh.backend.provider.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@SpringBootTest
@ActiveProfiles("test")
public class S3ServiceTests {
    @Autowired
    private S3Service s3Service;
    @Value("${cloud.aws.s3.bucket.url}")
    private String s3BucketUrl;

    @DisplayName("S3 파일 업로드 및 삭제 테스트")
    @Transactional
    @Test
    void S3ServiceTest(){
        MultipartFile mockMultipartFile = new MockMultipartFile("file", "test1.png",
                "image/png", "test data".getBytes());
        String url = "";
        try {
            url = s3Service.upload(mockMultipartFile, "test");
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("s3업로드 실패");
        }
        System.out.println(url+"추가완료");
        s3Service.deleteFile(url.substring(s3BucketUrl.length()+1));
    }
}
