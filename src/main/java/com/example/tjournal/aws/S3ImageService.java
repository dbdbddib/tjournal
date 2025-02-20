package com.example.tjournal.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3ImageService {

    private final AmazonS3 amazonS3;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    // ✅ 이미지 업로드
    public String upload(MultipartFile image) {
        if(image.isEmpty() || Objects.isNull(image.getOriginalFilename())){
            throw new S3Exception(ErrorCode.EMPTY_FILE_EXCEPTION);
        }
        // uploadImage를 호출하여 S3에 저장된 이미지의 public url을 반환한다.
        return this.uploadImage(image);
    }

    // ✅ 파일 확장자 검사 및 업로드
    private String uploadImage(MultipartFile image) {
        this.validateImageFileExtention(image.getOriginalFilename());
        try {
            return this.uploadImageToS3(image);
        } catch (IOException e) {
            throw new S3Exception(ErrorCode.IO_EXCEPTION_ON_IMAGE_UPLOAD);
        }
    }

    // ✅ 확장자 유효성 검사 (확장자 명이 올바른지 확인한다.)
    private void validateImageFileExtention(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new S3Exception(ErrorCode.NO_FILE_EXTENTION);
        }

        // 파일 확장자가 jpg, jpeg, png, gif 중에 속하는지 검증
        String extention = filename.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png", "gif");

        if (!allowedExtentionList.contains(extention)) {
            throw new S3Exception(ErrorCode.INVALID_FILE_EXTENTION);
        }
    }

    // ✅ 직접적으로 S3에 업로드하는 메서드 (이미지를 S3에 업로드하고, S3에 저장된 이미지의 public url을 받아서 서비스 로직에 반환)
    private String uploadImageToS3(MultipartFile image) throws IOException {
        String originalFilename = image.getOriginalFilename(); //원본 파일 명
        String extention = originalFilename.substring(originalFilename.lastIndexOf(".")); //확장자 명

        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename; //변경된 파일 명

        InputStream is = image.getInputStream();
        byte[] bytes = IOUtils.toByteArray(is);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + extention);
        metadata.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        try{
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata);
            amazonS3.putObject(putObjectRequest);
        }catch (Exception e){
            throw new S3Exception(ErrorCode.PUT_OBJECT_EXCEPTION);
        }finally {
            byteArrayInputStream.close();
            is.close();
        }
        return amazonS3.getUrl(bucketName, s3FileName).toString();
    }

    // ✅ S3 이미지 삭제
    public void deleteImageFromS3(String imageAddress){
        String key = getKeyFromImageAddress(imageAddress);
        try{
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        }catch (Exception e){
            throw new S3Exception(ErrorCode.IO_EXCEPTION_ON_IMAGE_DELETE);
        }
    }

    // ✅ 이미지 URL에서 S3 키 추출
    private String getKeyFromImageAddress(String imageAddress){
        try{
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
            return decodingKey.substring(1); // 맨 앞의 '/' 제거
        }catch (MalformedURLException | UnsupportedEncodingException e){
            throw new S3Exception(ErrorCode.IO_EXCEPTION_ON_IMAGE_DELETE);
        }
    }
}