package com.ncsgroup.ems.controller;

import com.ncsgroup.ems.dto.response.ResponseGeneral;
import com.ncsgroup.ems.dto.request.vehicle.card.VehicleCardRequest;
import com.ncsgroup.ems.dto.response.vehicle.vehiclecard.VehicleCardPageResponse;
import com.ncsgroup.ems.dto.response.vehicle.vehiclecard.VehicleCardResponse;
import com.ncsgroup.ems.facade.VehicleCardFacadeService;
import com.ncsgroup.ems.service.MessageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.ncsgroup.ems.constanst.EMSConstants.CommonConstants.*;
import static com.ncsgroup.ems.constanst.EMSConstants.MessageCode.CREATE_VEHICLE_CARD_SUCCESS;
import static com.ncsgroup.ems.constanst.EMSConstants.MessageCode.UPDATE_VEHICLE_CARD_SUCCESS;

@RestController
@RequestMapping("/api/v1/vehicle_cards")
@Slf4j
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class VehicleCardController {
  private final VehicleCardFacadeService vehicleCardFacadeService;
  private final MessageService messageService;

  @PostMapping
  public ResponseGeneral<VehicleCardResponse> create(
        @RequestBody @Valid  VehicleCardRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(create) request: {}", request);
    return ResponseGeneral.ofCreated(
          messageService.getMessage(CREATE_VEHICLE_CARD_SUCCESS, language),
          vehicleCardFacadeService.create(request)
    );
  }

  @GetMapping("/{id}")
  public ResponseGeneral<VehicleCardResponse> get(
        @PathVariable Long id,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(get) id: {}", id);
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          vehicleCardFacadeService.detail(id)
    );
  }

  @GetMapping
  public ResponseGeneral<VehicleCardPageResponse> list(
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "all", defaultValue = "false", required = false) boolean isAll,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(list) size: {}, page: {}, isAll: {}", size, page, isAll);
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          vehicleCardFacadeService.list(size, page, isAll));
  }

  @PutMapping("/{id}")
  public ResponseGeneral<VehicleCardResponse> update(
        @PathVariable Long id,
        @RequestBody @Valid VehicleCardRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(update) id: {}, request: {}", id, request);
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(UPDATE_VEHICLE_CARD_SUCCESS, language),
          vehicleCardFacadeService.update(id, request)
    );
  }
}
