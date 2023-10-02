package com.ncsgroup.ems.controller;

import com.ncsgroup.ems.dto.response.ResponseGeneral;
import com.ncsgroup.ems.dto.response.address.province.ProvincePageResponse;
import com.ncsgroup.ems.service.MessageService;
import com.ncsgroup.ems.service.address.ProvinceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.ncsgroup.ems.constanst.EMSConstants.CommonConstants.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/provinces")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProvinceController {

  private final ProvinceService provinceService;
  private final MessageService messageService;

  @GetMapping
  public ResponseGeneral<ProvincePageResponse> list(
        @RequestParam(name = "keyword", required = false) String keyword,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(search) keyword: {}, page:{}, size:{}", keyword, page, size);
    return ResponseGeneral.ofSuccess(messageService.getMessage(SUCCESS, language),
          provinceService.search(keyword, page, size));
  }
}
