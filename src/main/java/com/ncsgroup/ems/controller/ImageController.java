package com.ncsgroup.ems.controller;


import com.ncsgroup.ems.dto.response.ResponseGeneral;
import com.ncsgroup.ems.dto.response.image.ImageResponse;
import com.ncsgroup.ems.service.identity.ImageService;
import com.ncsgroup.ems.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.ncsgroup.ems.constanst.EMSConstants.CommonConstants.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ImageController {
  private final MessageService messageService;
  private final ImageService imageService;


  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseGeneral<List<ImageResponse>> upload(
        @RequestPart(name = "files", required = false) MultipartFile[] files,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(upload) {} images", files.length);

    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          imageService.upload(List.of(files))
    );
  }

  @DeleteMapping("{id}")
  public ResponseGeneral<Void> delete(
        @PathVariable Long id,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(delete) id: {}", id);
    imageService.softDelete(id);
    return ResponseGeneral.ofSuccess(messageService.getMessage(SUCCESS, language));
  }
}
