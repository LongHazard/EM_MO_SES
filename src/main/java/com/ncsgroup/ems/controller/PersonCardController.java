package com.ncsgroup.ems.controller;

import com.ncsgroup.ems.dto.response.ResponseGeneral;
import com.ncsgroup.ems.dto.request.person.card.PersonCardRequest;
import com.ncsgroup.ems.dto.response.personcard.PersonCardPageResponse;
import com.ncsgroup.ems.dto.response.personcard.PersonCardResponse;
import com.ncsgroup.ems.facade.PersonCardFacadeService;
import com.ncsgroup.ems.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.ncsgroup.ems.constanst.EMSConstants.CommonConstants.*;
import static com.ncsgroup.ems.constanst.EMSConstants.MessageCode.CREATE_PERSON_CARD_SUCCESS;
import static com.ncsgroup.ems.constanst.EMSConstants.MessageCode.UPDATE_PERSON_CARD_SUCCESS;

@Slf4j
@RestController
@RequestMapping("/api/v1/person_cards")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PersonCardController {
  private final MessageService messageService;
  private final PersonCardFacadeService personCardFacadeService;

  @PostMapping
  public ResponseGeneral<PersonCardResponse> create(
        @RequestBody @Valid PersonCardRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.warn("(place of Origin =========> {})", request.getPlaceOfOrigin());
    log.info("(create)request: {}", request);
    return ResponseGeneral.ofCreated(
          messageService.getMessage(CREATE_PERSON_CARD_SUCCESS, language),
          personCardFacadeService.create(request)
    );
  }

  @PutMapping("{id}")
  public ResponseGeneral<PersonCardResponse> update(
        @PathVariable Long id,
        @RequestBody PersonCardRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(update)id: {}, request: {}", id, request);

    return ResponseGeneral.ofSuccess(
          messageService.getMessage(UPDATE_PERSON_CARD_SUCCESS, language),
          personCardFacadeService.update(id, request)
    );
  }

  @GetMapping("{id}")
  public ResponseGeneral<PersonCardResponse> get(
        @PathVariable Long id,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(get)id: {}", id);

    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          personCardFacadeService.get(id)
    );
  }

  @GetMapping
  public ResponseGeneral<PersonCardPageResponse> list(
        @RequestParam(name = "keyword", required = false) String keyword,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "all", defaultValue = "false", required = false) boolean isAll,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {

    log.info("(list)keyword: {}, size : {}, page: {}, isAll: {}", keyword, size, page, isAll);

    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          personCardFacadeService.list(keyword, size, page, isAll)
    );
  }

}
