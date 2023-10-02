package com.ncsgroup.ems.controller;

import com.ncsgroup.ems.dto.request.person.PersonSearchRequest;
import com.ncsgroup.ems.dto.response.ResponseGeneral;
import com.ncsgroup.ems.dto.request.person.PersonFacadeRequest;
import com.ncsgroup.ems.dto.request.person.SearchPersonRequest;
import com.ncsgroup.ems.dto.response.person.PagePersonResponse;
import com.ncsgroup.ems.dto.response.person.PersonFacadeResponse;
import com.ncsgroup.ems.dto.response.person.PersonPageResponse;
import com.ncsgroup.ems.dto.response.vehicle.VehicleFacadeResponse;
import com.ncsgroup.ems.facade.PersonFacadeService;
import com.ncsgroup.ems.facade.VehicleFacadeService;
import com.ncsgroup.ems.service.MessageService;
import com.ncsgroup.ems.service.person.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static com.ncsgroup.ems.constanst.EMSConstants.CommonConstants.*;
import static com.ncsgroup.ems.constanst.EMSConstants.MessageCode.CREATE_PERSON_SUCCESS;


@Slf4j
@RestController
@RequestMapping("/api/v1/persons")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PersonController {
  private final PersonFacadeService personFacadeService;
  private final MessageService messageService;
  private final PersonService personService;
  private final VehicleFacadeService vehicleFacadeService;

  @PostMapping
  public ResponseGeneral<PersonFacadeResponse> create(
        @RequestBody @Valid PersonFacadeRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(create)request: {}", request);
    return ResponseGeneral.ofCreated(
          messageService.getMessage(CREATE_PERSON_SUCCESS, language),
          personFacadeService.createPerson(request)
    );
  }

  @PostMapping("/search")
  public ResponseGeneral<PersonPageResponse> list(
        @RequestBody(required = false) SearchPersonRequest request,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "all", defaultValue = "false", required = false) boolean isAll,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(list)request: {}, size : {}, page: {}, isAll: {}", request, size, page, isAll);
    return ResponseGeneral.ofSuccess(messageService.getMessage(SUCCESS, language),
          personFacadeService.listPersons(request, size, page, isAll));
  }

  @PostMapping("/list")
  public ResponseGeneral<PagePersonResponse> listAll(
        @RequestBody(required = false) PersonSearchRequest request,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "all", defaultValue = "false", required = false) boolean isAll,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(list)request: {}, size : {}, page: {}, isAll: {}", request, size, page, isAll);
    return ResponseGeneral.ofSuccess(messageService.getMessage(SUCCESS, language),
          personService.list(request, page, size, isAll));
  }

  @GetMapping("/group/{id}")
  public ResponseGeneral<PersonPageResponse> listByGroup(
        @PathVariable(name = "id") long groupId,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(listByGroup) groupId:{}, page:{}, size:{}", groupId, page, size);
    return ResponseGeneral.ofSuccess(messageService.getMessage(SUCCESS, language),
          personFacadeService.listPersonsByGroup(groupId, page, size));
  }

  @GetMapping("{id}")
  public ResponseGeneral<PersonFacadeResponse> get(
        @PathVariable Long id,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(get) id : {}", id);
    return ResponseGeneral.ofSuccess(messageService.getMessage(SUCCESS, language),
          personFacadeService.getPerson(id));
  }

  @PutMapping("{id}")
  public ResponseGeneral<PersonFacadeResponse> update(
        @PathVariable Long id,
        @RequestBody PersonFacadeRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(update) id : {}, request : {}", id, request);
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          personFacadeService.updatePerson(id, request)
    );
  }

  @GetMapping("department")
  public ResponseGeneral<Set<String>> getListDepartment(
        @RequestParam(required = false) String keyword,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(getListDepartment) keyword : {}", keyword);
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          personService.getDepartment(keyword)
    );
  }

  @GetMapping("position")
  public ResponseGeneral<Set<String>> getListPosition(
        @RequestParam(required = false) String keyword,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(getListPosition) keyword : {}", keyword);
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          personService.getPosition(keyword)
    );
  }

  @GetMapping("org")
  public ResponseGeneral<Set<String>> getListOrg(
        @RequestParam(required = false) String keyword,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(getListOrg) keyword : {}", keyword);
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          personService.getOrg(keyword)
    );
  }

  @GetMapping("/{id}/vehicles")
  public ResponseGeneral<List<VehicleFacadeResponse>> getByPersonId(
        @PathVariable long id,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(getByPersonId) id:{}", id);
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          vehicleFacadeService.getByPersonId(id)
    );
  }

  @DeleteMapping("{id}")
  public ResponseGeneral<Void> delete(
        @PathVariable long id,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(delete) id:{}", id);
    personService.remove(id);
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language)
    );
  }
}
