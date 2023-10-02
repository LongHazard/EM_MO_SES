package com.ncsgroup.ems.controller;

import com.ncsgroup.ems.dto.request.group.GroupRequest;
import com.ncsgroup.ems.dto.response.ResponseGeneral;
import com.ncsgroup.ems.dto.response.group.GroupPageResponse;
import com.ncsgroup.ems.dto.response.group.GroupResponse;
import com.ncsgroup.ems.dto.response.identity.IdentityResponse;
import com.ncsgroup.ems.facade.GroupFacadeService;
import com.ncsgroup.ems.service.MessageService;
import com.ncsgroup.ems.service.identity.GroupService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ncsgroup.ems.constanst.EMSConstants.CommonConstants.*;
import static com.ncsgroup.ems.constanst.EMSConstants.MessageCode.*;

@RestController
@RequestMapping("/api/v1/groups")
@Slf4j
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class GroupController {
  private final GroupFacadeService groupFacadeService;
  private final GroupService groupService;
  private final MessageService messageService;

  @PostMapping
  public ResponseGeneral<GroupResponse> create(
        @RequestBody GroupRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(create) request: {}", request);
    return ResponseGeneral.ofCreated(
          messageService.getMessage(CREATE_GROUP_SUCCESS, language),
          groupFacadeService.create(request)
    );
  }

  @GetMapping("/search")
  public ResponseGeneral<GroupPageResponse> list(
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "all", defaultValue = "false", required = false) boolean isAll,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(list) size: {}, page: {}, isAll: {}", size, page, isAll);
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          groupService.list(size, page, isAll));
  }

  @GetMapping
  public ResponseGeneral<List<GroupResponse>> list(
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {

    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          groupFacadeService.list());
  }

  @GetMapping("/sub")
  public ResponseGeneral<List<GroupResponse>> getSubGroups(
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(getSubGroups)");
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          groupService.getSubGroups());
  }

  @PutMapping("/{id}")
  public ResponseGeneral<GroupResponse> update(
        @PathVariable Long id,
        @RequestBody GroupRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(update) id: {}, request: {}", id, request);
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(UPDATE_GROUP_SUCCESS, language),
          groupService.update(id, request)
    );
  }


  @GetMapping("/{id}/identity")
  public ResponseGeneral<List<IdentityResponse>> listIdentity(
        @PathVariable Long id,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          groupFacadeService.getIdentityByGroupId(id)
    );
  }

  @GetMapping("/{id}/identity-unrelated")
  public ResponseGeneral<List<IdentityResponse>> listIdentityUnrelated(
        @PathVariable Long id,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          groupFacadeService.getIdentityUnrelated(id)
    );
  }

  @DeleteMapping("{id}")
  public ResponseGeneral<Void> delete(
        @PathVariable Long id,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(delete) id:{}", id);
    groupService.remove(id);
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language)
    );
  }
}
