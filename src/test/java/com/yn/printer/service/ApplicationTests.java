package com.yn.printer.service;


import com.amazonaws.ClientConfiguration;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@SpringBootTest
class ApplicationTests {

    public static void main(String[] args) {
        final String accessKey = "JDC_711AC05B56BF7F42EB2E4270BD96";
        final String secretKey = "D7ABEE1C809EE23B9951A1BCF55A90E3";
        final String endpoint = "https://s3.cn-south-1.jdcloud-oss.com";
        ClientConfiguration config = new ClientConfiguration();

        AwsClientBuilder.EndpointConfiguration endpointConfig =
                new AwsClientBuilder.EndpointConfiguration(endpoint, "cn-south-1");

        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);

        AmazonS3 s3 = AmazonS3Client.builder()
                .withEndpointConfiguration(endpointConfig)
                .withClientConfiguration(config)
                .withCredentials(awsCredentialsProvider)
                .build();

        ListObjectsV2Result result = s3.listObjectsV2("mps-oss-south");
        List<S3ObjectSummary> objects = result.getObjectSummaries();

        for (S3ObjectSummary os : objects) {
            String key = os.getKey();
            if (key.endsWith(".jar")) { // 筛选出.jar文件
                String downloadUrl = s3.getUrl("mps-oss-south", key).toString(); // 获取下载地址
                System.out.println("文件名: " + key + ", 下载地址: " + downloadUrl);
            }
        }
    }
}