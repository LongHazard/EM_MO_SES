package com.ncsgroup.ems.controller;

import com.ncsgroup.ems.dto.request.cardtype.CardTypeRequest;
import com.ncsgroup.ems.dto.response.ResponseGeneral;
import com.ncsgroup.ems.dto.response.cardtype.CardTypeResponse;
import com.ncsgroup.ems.service.person.CardTypeService;
import com.ncsgroup.ems.service.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static com.ncsgroup.ems.constanst.EMSConstants.CommonConstants.*;
import static com.ncsgroup.ems.constanst.EMSConstants.MessageCode.*;

@RestController
@RequestMapping("/api/v1/card_types")
@CrossOrigin(origins = "*")
@Slf4j
@AllArgsConstructor
public class CardTypeController {
  private final CardTypeService cardTypeService;
  private final MessageService messageService;

  @PostMapping
  public ResponseGeneral<CardTypeResponse> create(
        @RequestBody CardTypeRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language) {
    log.info("(create) request: {}", request);

    return ResponseGeneral.ofCreated(
          messageService.getMessage(CREATE_CARD_TYPE_SUCCESS, language),
          cardTypeService.create(request));
  }

  @DeleteMapping("/{id}")
  public ResponseGeneral<Void> delete(
        @PathVariable Long id,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(delete) id: {}", id);

    cardTypeService.delete(id);
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(DELETE_CARD_TYPE_SUCCESS, language));
  }

  @GetMapping
  public ResponseGeneral<List<CardTypeResponse>> list(
        @RequestParam(required = false) String keyword,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(list) keyword: {}", keyword);

    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          cardTypeService.list(keyword));

  }

  @PutMapping("/{id}")
  public ResponseGeneral<CardTypeResponse> update(
        @PathVariable Long id,
        @RequestBody CardTypeRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(update) id: {}, request: {}", id, request);

    return ResponseGeneral.ofSuccess(
          messageService.getMessage(UPDATE_CARD_TYPE_SUCCESS, language),
          cardTypeService.update(id, request)
    );
  }
}