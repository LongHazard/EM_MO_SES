package com.ncsgroup.ems.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class File {
  private String name;
  private String bucketName;
  private String contentType;
  private String url;

  public static File of(String name, String bucketName, String contentType) {
    return File.of(name, bucketName, contentType, null);
  }

}
