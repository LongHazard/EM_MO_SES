package com.ncsgroup.ems.controller;

import com.ncsgroup.ems.dto.response.ResponseGeneral;
import com.ncsgroup.ems.dto.request.address.SearchDistrictRequest;
import com.ncsgroup.ems.dto.response.address.district.DistrictPageResponse;
import com.ncsgroup.ems.service.address.DistrictService;
import com.ncsgroup.ems.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.ncsgroup.ems.constanst.EMSConstants.CommonConstants.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/districts")
@RequiredArgsConstructor
public class DistrictController {

  private final MessageService messageService;
  private final DistrictService districtService;

  @PostMapping
  public ResponseGeneral<DistrictPageResponse> list(
        @RequestBody(required = false) SearchDistrictRequest request,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(search) request: {}, page:{}, size:{}", request, page, size);
    return ResponseGeneral.ofSuccess(messageService.getMessage(SUCCESS, language),
          districtService.search(request, page, size));
  }
}
