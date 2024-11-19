/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: StorageService.java
 */

package com.philips.onespace.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

	/**
	 * This method uploads given file to cloud storage.
	 * 
	 * @param multipartFile The multipart file
	 * @param bucketName the bucket name
	 * @param fileName the file name
	 */
	public boolean uploadFile(MultipartFile multipartFile, String bucketName, String fileName) throws IOException;

}
