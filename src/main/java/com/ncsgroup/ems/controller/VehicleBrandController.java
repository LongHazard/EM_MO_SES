package com.ncsgroup.ems.controller;

import com.ncsgroup.ems.dto.response.ResponseGeneral;
import com.ncsgroup.ems.dto.request.vehicle.brand.VehicleBrandRequest;
import com.ncsgroup.ems.dto.response.vehicle.brand.VehicleBrandResponse;
import com.ncsgroup.ems.service.MessageService;
import com.ncsgroup.ems.service.vehicle.VehicleBrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ncsgroup.ems.constanst.EMSConstants.CommonConstants.*;
import static com.ncsgroup.ems.constanst.EMSConstants.MessageCode.CREATE_VEHICLE_BRAND_SUCCESS;

@RestController
@RequestMapping("/api/v1/vehicle/brands")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class VehicleBrandController {

  private final VehicleBrandService vehicleBrandService;
  private final MessageService messageService;

  @PostMapping
  public ResponseGeneral<VehicleBrandResponse> create(
        @RequestBody VehicleBrandRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language) {
    log.info("(create)request: {}", request);
    return ResponseGeneral.ofCreated(
          messageService.getMessage(CREATE_VEHICLE_BRAND_SUCCESS, language),
          vehicleBrandService.create(request));
  }

  @GetMapping
  public ResponseGeneral<List<VehicleBrandResponse>> list(
        @RequestParam(required = false) String keyword,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language) {
    log.info("(list)keyword: {}", keyword);
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          vehicleBrandService.list(keyword));
  }

  @DeleteMapping("{id}")
  public ResponseGeneral<Void> delete(
        @PathVariable Long id,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language) {
    log.info("(delete)id: {}", id);
    vehicleBrandService.remove(id);
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language));
  }
}
