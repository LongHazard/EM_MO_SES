package com.ncsgroup.ems.service.vehicle.impl;

import com.ncsgroup.ems.dto.request.vehicle.type.VehicleTypeRequest;
import com.ncsgroup.ems.dto.response.vehicle.type.VehicleTypeResponseDTO;
import com.ncsgroup.ems.entity.vehicle.VehicleType;
import com.ncsgroup.ems.exception.vehicle.type.VehicleTypeAlreadyExistException;
import com.ncsgroup.ems.exception.vehicle.type.VehicleTypeNotFoundException;
import com.ncsgroup.ems.repository.VehicleTypeRepository;
import com.ncsgroup.ems.service.vehicle.VehicleTypeService;
import com.ncsgroup.ems.service.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.ncsgroup.ems.utils.MapperUtils.*;

@Slf4j
public class VehicleTypeServiceImpl extends BaseServiceImpl<VehicleType> implements VehicleTypeService {
  private final VehicleTypeRepository repository;

  public VehicleTypeServiceImpl(VehicleTypeRepository repository) {
    super(repository);
    this.repository = repository;
  }

  @Override
  public VehicleTypeResponseDTO create(VehicleTypeRequest request) {
    log.info("(create) request: {}", request);

    checkNameExist(request.getName());
    VehicleType vehicleType = toEntity(request, VehicleType.class);

    return toDTO(create(vehicleType), VehicleTypeResponseDTO.class);
  }

  @Override
  public void delete(Long id) {
    log.info("(delete) id: {}", id);
    repository.delete(find(id));
  }

  @Override
  public List<VehicleTypeResponseDTO> list(String keyword) {
    log.info("(list) keyword: {}", keyword);

    return repository.search(keyword);
  }

  @Override
  public VehicleType find(Long id) {
    log.info("(find) id: {}", id);
    return (id == null) ? null : repository.findById(id).orElseThrow(VehicleTypeNotFoundException::new);
  }

  @Override
  public VehicleTypeResponseDTO detail(Long id) {
    log.info("(detail) id: {}", id);
    return (id == null) ? null : toDTO(find(id), VehicleTypeResponseDTO.class);
  }

//  @Override
//  public Long checkTypeVehicleNotFound(Long id) {
//    log.info("(checkTypeVehicleNotFound) id: {}", id);
//    if (id == null || id == 0) {
//      return null;
//    }
//    return repository.findById(id).orElseThrow(() -> new VehicleTypeNotFoundException()).getId();
//  }

  @Override
  public void checkNameExist(String name) {
    log.info("(checkNameExist) name : {}", name);

    if (repository.existsByName(name)) {
      log.error("(checkNameExist) ============= VehicleTypeAlreadyExistException");
      throw new VehicleTypeAlreadyExistException();
    }
  }


}
