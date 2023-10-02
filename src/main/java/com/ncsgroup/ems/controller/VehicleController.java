package com.ncsgroup.ems.controller;

import com.ncsgroup.ems.dto.request.vehicle.FacadeVehicleRequest;
import com.ncsgroup.ems.dto.request.vehicle.VehicleFacadeRequest;
import com.ncsgroup.ems.dto.request.vehicle.VehicleFilter;
import com.ncsgroup.ems.dto.request.vehicle.VehicleSearchRequest;
import com.ncsgroup.ems.dto.response.ResponseGeneral;
import com.ncsgroup.ems.dto.response.person.PersonFacadeResponse;
import com.ncsgroup.ems.dto.response.vehicle.*;
import com.ncsgroup.ems.facade.PersonFacadeService;
import com.ncsgroup.ems.facade.VehicleFacadeService;
import com.ncsgroup.ems.service.MessageService;
import com.ncsgroup.ems.service.vehicle.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ncsgroup.ems.constanst.EMSConstants.CommonConstants.*;
import static com.ncsgroup.ems.constanst.EMSConstants.MessageCode.CREATE_VEHICLE_SUCCESS;
import static com.ncsgroup.ems.constanst.EMSConstants.MessageCode.DELETE_VEHICLE_TYPE_SUCCESS;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/vehicles")
public class VehicleController {
  private final VehicleService vehicleService;
  private final VehicleFacadeService vehicleFacadeService;
  private final MessageService messageService;
  private final PersonFacadeService personFacadeService;

  @PostMapping
  public ResponseGeneral<VehicleFacadeResponse> create(
        @RequestBody @Valid VehicleFacadeRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language) {
    log.info("(create) request: {}", request);
    return ResponseGeneral.ofCreated(
          messageService.getMessage(CREATE_VEHICLE_SUCCESS, language),
          vehicleFacadeService.createVehicle(request)
    );
  }


  @DeleteMapping("{id}")
  public ResponseGeneral<Void> delete(
        @PathVariable long id,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(delete)id: {}", id);

    vehicleService.remove(id);

    return ResponseGeneral.ofSuccess(
          messageService.getMessage(DELETE_VEHICLE_TYPE_SUCCESS, language)
    );
  }

  @PostMapping("search")
  public ResponseGeneral<VehiclePageResponse> list(
        @RequestBody(required = false) VehicleFilter vehicleFilter,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "all", defaultValue = "false", required = false) boolean isAll,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(list) vehicleFilter: {}, size : {}, page: {}, isAll: {}", vehicleFilter, size, page, isAll);

    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          vehicleFacadeService.list(vehicleFilter, size, page, isAll)
    );
  }

  @GetMapping("{id}")
  public ResponseGeneral<VehicleFacadeResponse> detail(
        @PathVariable Long id,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(detail) id: {}", id);

    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          vehicleFacadeService.detail(id)
    );
  }

  @PutMapping("{id}")
  public ResponseGeneral<VehicleFacadeResponse> update(
        @PathVariable Long id,
        @RequestBody VehicleFacadeRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(update) id: {} request: {}", id, request);

    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          vehicleFacadeService.update(id, request)
    );
  }

  @GetMapping("license-plate")
  public ResponseGeneral<VehicleLicensePlatePageResponse> listLicensePlate(
        @RequestParam(name = "keyword", required = false) String keyword,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "all", defaultValue = "false", required = false) boolean isAll,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(listLicensePlate)keyword: {}, size : {}, page: {}, isAll: {}", keyword, size, page, isAll);

    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          vehicleService.listLicensePlate(keyword, size, page, isAll)
    );
  }

  @GetMapping("/{id}/persons")
  public ResponseGeneral<PersonFacadeResponse> getByVehicle(
        @PathVariable(name = "id") Long id,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(getByVehicle) id:{}", id);
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          personFacadeService.getPersonByVehicle(id)
    );
  }

  @PostMapping("/group")
  public ResponseGeneral<List<VehicleResponse>> getByGroupId(
        @RequestBody(required = false) VehicleSearchRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(getByGroupId) request :{}", request);
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          vehicleService.getByGroupId(request)
    );
  }

}
