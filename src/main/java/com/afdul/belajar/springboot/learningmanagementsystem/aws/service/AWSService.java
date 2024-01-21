package com.afdul.belajar.springboot.learningmanagementsystem.aws.service;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.*;

@Service
@Slf4j
public class AWSService {

    @Value("${app.aws.s3.bucket.name}")
    private String bucketName;

    @Value("${app.aws.s3.region}")
    private String awsRegion;

    @Autowired
    private AmazonS3 amazonS3Client;

    // Upload Thumbnail
    public String uploadThumbnail(MultipartFile multipartFile, String filename) throws Exception {
        StringBuilder fileUrl = new StringBuilder();

        try {
            File fileObj = convertMultiPartToFile(multipartFile);
            uploadFileTos3bucket(filename, fileObj);

            // String Builder to define full url :
            // https://bucketname.s3.ap-southeast-1.amazonaws.com/filename

            fileUrl.append("https://");
            fileUrl.append(bucketName);
            fileUrl.append(".s3.");
            fileUrl.append(awsRegion);
            fileUrl.append(".amazonaws.com");
            fileUrl.append("/");
            fileUrl.append(filename);

            Files.delete(fileObj.toPath());

        } catch (Exception e) {
            log.error("Error upload file ", e);
            throw e;
        }

        return fileUrl.toString();
    }


    private void uploadFileTos3bucket(String fileName, File file) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucketName, fileName, file)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public String getVideoUrl(String filename) {
        return getObjectPresignedUrl(filename);
    }


    // Upload Large Video Content
    public void uploadLargeFileToS3Bucket(MultipartFile file, String filename) throws IOException {

        InputStream in = file.getInputStream();

        try {
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(in.available());
            meta.setContentType("video/mp4");

            TransferManager tm = TransferManagerBuilder.standard()
                    .withS3Client(amazonS3Client)
                    .build();

            PutObjectRequest request = new PutObjectRequest(bucketName, filename, in, meta);
            Upload upload = tm.upload(request);

            upload.waitForCompletion();
//            if (upload.isDone()) {
//                tm.shutdownNow();
//            }

        } catch (AmazonServiceException e) {
            System.out.println("Inside exception " + e);

        } catch (SdkClientException e) {
            System.out.println("Couldn't Upload the file " + e);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private String getObjectPresignedUrl(String fileName) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 7); // Generated URL will be valid for 1 Year

        return amazonS3Client.generatePresignedUrl(bucketName, fileName, calendar.getTime(), HttpMethod.GET).toString();
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

}
