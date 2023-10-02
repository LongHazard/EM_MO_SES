package com.ncsgroup.ems.controller;

import com.ncsgroup.ems.dto.request.color.ColorRequest;
import com.ncsgroup.ems.dto.response.ResponseGeneral;
import com.ncsgroup.ems.dto.response.color.ColorResponse;
import com.ncsgroup.ems.service.vehicle.ColorService;
import com.ncsgroup.ems.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ncsgroup.ems.constanst.EMSConstants.CommonConstants.*;
import static com.ncsgroup.ems.constanst.EMSConstants.MessageCode.CREATE_COLOR_SUCCESS;
import static com.ncsgroup.ems.constanst.EMSConstants.MessageCode.DELETE_COLOR_SUCCESS;

@Slf4j
@RestController
@RequestMapping("/api/v1/colors")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ColorController {
  private final MessageService messageService;
  private final ColorService colorService;

  @PostMapping
  public ResponseGeneral<ColorResponse> create(
        @RequestBody ColorRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(create)request: {}", request);

    return ResponseGeneral.ofCreated(
          messageService.getMessage(CREATE_COLOR_SUCCESS, language),
          colorService.create(request)
    );
  }

  @GetMapping
  public ResponseGeneral<List<ColorResponse>> list(
        @RequestParam(required = false) String keyword,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(list)keyword: {}", keyword);

    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          colorService.list(keyword)
    );
  }

  @DeleteMapping("{id}")
  public ResponseGeneral<Void> delete(
        @PathVariable Long id,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(delete)id: {}", id);

    colorService.remove(id);

    return ResponseGeneral.ofSuccess(
          messageService.getMessage(DELETE_COLOR_SUCCESS, language)
    );
  }
}
