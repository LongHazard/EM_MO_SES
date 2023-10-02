package com.ncsgroup.ems.service;


import com.ncsgroup.ems.dto.File;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.concurrent.TimeUnit;

public interface MinIOService {

  File putFile(MultipartFile file);

  File putFile(MultipartFile file, String bucketName);

  List<File> putFile(List<MultipartFile> files, String bucketName);

  List<File> upload(List<MultipartFile> files, String bucket);

  byte[] getFile(String filename);

  byte[] getFile(String filename, String bucketName);

  String getPreSignedUrl(String filename, String contentType);

  String getPreSignedUrl(
        String filename,
        String contentType,
        TimeUnit timeUnit,
        String bucketName,
        int time
  );

  byte[] downloadFile(
        String fileName,
        String bucketName
  );

  boolean remove(String filename, String bucketName);

  String getPreSignURLImage(String filename);
}
