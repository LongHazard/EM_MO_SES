package com.ncsgroup.ems.controller;

import com.ncsgroup.ems.dto.response.ResponseGeneral;
import com.ncsgroup.ems.dto.request.address.SearchWardRequest;
import com.ncsgroup.ems.dto.response.address.ward.WardPageResponse;
import com.ncsgroup.ems.service.MessageService;
import com.ncsgroup.ems.service.address.WardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.ncsgroup.ems.constanst.EMSConstants.CommonConstants.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/wards")
@RequiredArgsConstructor
public class WardController {

  private final MessageService messageService;
  private final WardService wardService;

  @PostMapping
  public ResponseGeneral<WardPageResponse> list(
        @RequestBody(required = false) SearchWardRequest request,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(search) request: {}, page:{}, size:{}", request, page, size);
    return ResponseGeneral.ofSuccess(messageService.getMessage(SUCCESS, language),
          wardService.search(request, page, size));
  }
}
