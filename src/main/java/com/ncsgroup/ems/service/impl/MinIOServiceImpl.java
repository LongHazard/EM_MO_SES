package com.ncsgroup.ems.service.impl;


import com.ncsgroup.ems.dto.File;
import com.ncsgroup.ems.service.MinIOService;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.ncsgroup.ems.constanst.MinIOConstants.ContentType.IMAGE;
import static com.ncsgroup.ems.utils.GeneratorUtils.generateFileName;


@Slf4j
public class MinIOServiceImpl implements MinIOService {
  private final String bucketName;

  private final MinioClient minioClient;

  public MinIOServiceImpl(String bucketName, MinioClient minioClient) {
    this.bucketName = bucketName;
    this.minioClient = minioClient;
  }


  public File putFile(MultipartFile file) {
    log.info("(putFile)");

    if (!checkBucketExist(bucketName)) makeBucket(bucketName);
    var fileName = generateFileName(file.getOriginalFilename());
    putFile(file, bucketName, fileName);

    return File.of(fileName, bucketName, file.getContentType());
  }

  public File putFile(MultipartFile file, String bucketName) {
    log.info("(putFile) bucketName: {}", bucketName);

    if (!checkBucketExist(bucketName)) makeBucket(bucketName);
    var fileName = generateFileName(file.getOriginalFilename());
    this.putFile(file, bucketName, fileName);

    return File.of(fileName, bucketName, file.getContentType());
  }

  public List<File> putFile(List<MultipartFile> files, String bucketName) {
    log.info("(putFile) bucketName: {}", bucketName);

    List<File> fileResponse = new ArrayList<>();
    files.forEach(file -> fileResponse.add(
          this.putFile(file, bucketName)
    ));

    return fileResponse;
  }


  public List<File> upload(List<MultipartFile> files, String bucket) {
    log.info("(upload) bucket: {}", bucket);

    if (!checkBucketExist(bucketName)) makeBucket(bucketName);

    List<File> fileResponse = new ArrayList<>();
    files.forEach(
          file -> fileResponse.add(upload(file, bucket, generateFileName(file.getOriginalFilename())))
    );

    return fileResponse;
  }

  public byte[] getFile(String filename) {

    log.info("(getFile) filename: {}", filename);

    try {
      InputStream stream = minioClient.getObject(GetObjectArgs
            .builder()
            .bucket(this.bucketName)
            .object(filename)
            .build()
      );
      return stream.readAllBytes();
    } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
             InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
             XmlParserException e) {
      throw new RuntimeException(e);
    }

  }

  public byte[] getFile(String filename, String bucketName) {
    log.info("(getFile) filename: {}, bucketName: {}", filename, bucketName);

    try {
      InputStream stream  = minioClient.getObject(GetObjectArgs
            .builder()
            .bucket(bucketName)
            .object(filename)
            .build()
      );
      return stream.readAllBytes();
    } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
             InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
             XmlParserException e) {
      throw new RuntimeException(e);
    }

  }

  public String getPreSignedUrl(String filename, String contentType) {

    Map<String, String> reqParams = new HashMap<>();
    reqParams.put("response-content-type", contentType);

    try {
      return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                  .method(Method.GET)
                  .bucket(bucketName)
                  .object(filename)
                  .expiry(2, TimeUnit.DAYS)
                  .extraQueryParams(reqParams)
                  .build());
    } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
             InvalidResponseException | IOException | NoSuchAlgorithmException | XmlParserException |
             ServerException e) {
      throw new RuntimeException(e);
    }
  }

  public String getPreSignURLImage(String filename) {
    log.info("(getPreSignURLImage) filename: {}", filename);

    Map<String, String> reqParams = new HashMap<>();
    reqParams.put("response-content-type", IMAGE);

    try {
      return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                  .method(Method.GET)
                  .bucket(bucketName)
                  .object(filename)
                  .expiry(2, TimeUnit.DAYS)
                  .extraQueryParams(reqParams)
                  .build());
    } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
             InvalidResponseException | IOException | NoSuchAlgorithmException | XmlParserException |
             ServerException e) {
      throw new RuntimeException(e);
    }
  }

  public String getPreSignedUrl(
        String filename,
        String contentType,
        TimeUnit timeUnit,
        String bucketName,
        int time
  ) {
    Map<String, String> reqParams = new HashMap<String, String>();
    reqParams.put("response-content-type", contentType);

    try {
      return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                  .method(Method.GET)
                  .bucket(bucketName)
                  .object(filename)
                  .expiry(time, timeUnit)
                  .extraQueryParams(reqParams)
                  .build());
    } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
             InvalidResponseException | IOException | NoSuchAlgorithmException | XmlParserException |
             ServerException e) {
      throw new RuntimeException(e);
    }
  }

  public byte[] downloadFile(
        String filename,
        String bucketName
  ) {
    log.info("(downloadFile) filename: {}, bucketName: {}", filename, bucketName);

    try {
      return minioClient.getObject(GetObjectArgs
            .builder()
            .bucket(this.bucketName)
            .object(filename)
            .build()
      ).readAllBytes();
    } catch (IOException | ErrorResponseException | InsufficientDataException | InternalException |
             InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | ServerException |
             XmlParserException e) {
      throw new RuntimeException(e);
    }

  }

  public boolean remove(String filename, String bucketName) {
    try {
      minioClient.removeObject(RemoveObjectArgs
            .builder()
            .bucket(bucketName)
            .object(filename)
            .build()
      );
    } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
             InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
             XmlParserException e) {
      throw new RuntimeException(e);
    }

    return true;
  }

  private boolean checkBucketExist(String bucketName) {
    try {
      return minioClient.bucketExists(BucketExistsArgs
            .builder()
            .bucket(bucketName)
            .build()
      );
    } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
             InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
             XmlParserException e) {
      throw new RuntimeException(e);
    }
  }

  private void makeBucket(String bucketName) {
    try {
      minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
    } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
             InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
             XmlParserException e) {
      throw new RuntimeException(e);
    }
  }

  private void putFile(MultipartFile file, String bucketName, String filename) {


    try {
      InputStream inputStream = file.getInputStream();
      minioClient.putObject(PutObjectArgs
            .builder()
            .bucket(bucketName)
            .object(filename)
            .stream(inputStream, file.getSize(), -1)
            .contentType(file.getContentType()).build()
      );
    } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
             InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
             XmlParserException e) {
      throw new RuntimeException(e);
    }
  }


  private File upload(MultipartFile file, String bucket, String filename) {
    try {
      minioClient.uploadObject(
            UploadObjectArgs.builder()
                  .bucket(bucket)
                  .object(filename)
                  .filename(file.getOriginalFilename())
                  .contentType(file.getContentType())
                  .build());
    } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
             InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
             XmlParserException e) {
      throw new RuntimeException(e);
    }

    return File.of(filename, bucketName, file.getContentType());
  }

}
