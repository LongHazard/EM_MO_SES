package com.ncsgroup.ems.facade;

import com.ncsgroup.ems.dto.request.vehicle.card.VehicleCardRequest;
import com.ncsgroup.ems.dto.response.color.ColorResponse;
import com.ncsgroup.ems.dto.response.vehicle.vehiclecard.VehicleCardPageResponse;
import com.ncsgroup.ems.dto.response.vehicle.vehiclecard.VehicleCardResponse;

import java.util.List;

public interface VehicleCardFacadeService {
  VehicleCardResponse create(VehicleCardRequest request);

  VehicleCardResponse detail(Long id);

  VehicleCardResponse findOrDefault(Long id);

  VehicleCardPageResponse list(int size, int page, boolean isAll);

  VehicleCardResponse update(Long id, VehicleCardRequest request);

  List<ColorResponse> findColorList(List<Long> colorIds);
}
