/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: S3Service.java
 */

package com.philips.onespace.serviceImpl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.philips.onespace.service.StorageService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class S3Service implements StorageService {
	
	private final AmazonS3 s3client;
	
	public S3Service(@Value("${aws.accessKey}") String accessKey, @Value("${aws.secretKey}") String secretKey) {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        this.s3client = new AmazonS3Client(credentials);
    }
	
	/**
	 * This method uploads given file to S3 cloud storage.
	 * 
	 * @param multipartFile The multipart file
	 * @param bucketName the S3 bucket name
	 * @param fileName the file name
	 */
	@Override
	public boolean uploadFile(MultipartFile multipartFile, String bucketName, String fileName) throws IOException {
		ObjectMetadata data = new ObjectMetadata();
        data.setContentType(multipartFile.getContentType());
        data.setContentLength(multipartFile.getSize());
        s3client.putObject(bucketName, fileName, multipartFile.getInputStream(), data);
        return true;
	}

}
