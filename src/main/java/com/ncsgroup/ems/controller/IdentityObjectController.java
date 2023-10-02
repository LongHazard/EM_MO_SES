package com.ncsgroup.ems.controller;

import com.ncsgroup.ems.dto.request.identity.AddIdentityObjectRequest;
import com.ncsgroup.ems.dto.response.ResponseGeneral;
import com.ncsgroup.ems.facade.IdentityObjectFacadeService;
import com.ncsgroup.ems.service.MessageService;
import com.ncsgroup.ems.service.identity.IdentityObjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.ncsgroup.ems.constanst.EMSConstants.CommonConstants.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/identity")
@CrossOrigin(origins = "*")
public class IdentityObjectController {
  private final MessageService messageService;
  private final IdentityObjectFacadeService identityObjectFacadeService;

  @PostMapping
  public ResponseGeneral<Void> create(
        @RequestBody AddIdentityObjectRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language) {
    log.info("(create) request: {}", request);
    identityObjectFacadeService.insert(request);
    return ResponseGeneral.ofCreated(
          messageService.getMessage(SUCCESS, language)
    );
  }
}
