package com.ncsgroup.ems.controller;

import com.ncsgroup.ems.dto.response.ResponseGeneral;
import com.ncsgroup.ems.dto.request.vehicle.type.VehicleTypeRequest;
import com.ncsgroup.ems.dto.response.vehicle.type.VehicleTypeResponseDTO;
import com.ncsgroup.ems.service.MessageService;
import com.ncsgroup.ems.service.vehicle.VehicleTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ncsgroup.ems.constanst.EMSConstants.CommonConstants.*;
import static com.ncsgroup.ems.constanst.EMSConstants.MessageCode.CREATE_VEHICLE_TYPE_SUCCESS;
import static com.ncsgroup.ems.constanst.EMSConstants.MessageCode.DELETE_VEHICLE_TYPE_SUCCESS;

@RequestMapping("/api/v1/vehicle/type")
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class VehicleTypeController {
  private final MessageService messageService;
  private final VehicleTypeService vehicleTypeService;

  @PostMapping
  public ResponseGeneral<VehicleTypeResponseDTO> create(
        @RequestBody VehicleTypeRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language) {
    log.info("(create) request: {}", request);
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(CREATE_VEHICLE_TYPE_SUCCESS, language),
          vehicleTypeService.create(request));
  }

  @DeleteMapping("{id}")
  public ResponseGeneral<Void> delete(
        @PathVariable Long id,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(delete)id: {}", id);

    vehicleTypeService.delete(id);

    return ResponseGeneral.ofSuccess(
          messageService.getMessage(DELETE_VEHICLE_TYPE_SUCCESS, language)
    );
  }

  @GetMapping
  public ResponseGeneral<List<VehicleTypeResponseDTO>> list(
        @RequestParam(required = false) String keyword,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(list) keyword: {}", keyword);

    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          vehicleTypeService.list(keyword)
    );
  }
}

