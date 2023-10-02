package com.ncsgroup.ems.service.vehicle.impl;

import com.ncsgroup.ems.dto.request.vehicle.brand.VehicleBrandRequest;
import com.ncsgroup.ems.dto.response.vehicle.brand.VehicleBrandResponse;
import com.ncsgroup.ems.entity.vehicle.VehicleBrand;
import com.ncsgroup.ems.exception.vehicle.brand.VehicleBrandAlreadyExistException;
import com.ncsgroup.ems.exception.vehicle.brand.VehicleBrandNotFoundException;
import com.ncsgroup.ems.repository.VehicleBrandRepository;
import com.ncsgroup.ems.service.vehicle.VehicleBrandService;
import com.ncsgroup.ems.service.base.BaseServiceImpl;
import com.ncsgroup.ems.utils.MapperUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.ncsgroup.ems.utils.MapperUtils.toDTO;

@Slf4j
public class VehicleBrandServiceImpl extends BaseServiceImpl<VehicleBrand> implements VehicleBrandService {
  private VehicleBrandRepository repository;

  public VehicleBrandServiceImpl(VehicleBrandRepository repository) {
    super(repository);
    this.repository = repository;
  }


  @Override
  public VehicleBrandResponse create(VehicleBrandRequest request) {
    log.info("(create) request: {}", request);
    checkNameExist(request.getName());
    VehicleBrand vehicleBrand = MapperUtils.toEntity(request, VehicleBrand.class);
    return MapperUtils.toDTO(create(vehicleBrand), VehicleBrandResponse.class);
  }

  @Override
  public List<VehicleBrandResponse> list(String keyword) {
    log.info("(list)keyword:{}", keyword);
    return repository.listBySearch(keyword);
  }

  @Override
  public void remove(Long id) {
    log.info("(remove) id :{}", id);
    repository.delete(find(id));
  }

  @Override
  public VehicleBrand find(Long id) {
    log.info("(find) id: {}", id);
    return (id == null) ? null : repository.findById(id).orElseThrow(VehicleBrandNotFoundException::new);
  }

  @Override
  public VehicleBrandResponse detail(Long id){
    log.info("(detail) id: {}", id);
    return (id == null) ? null : toDTO(find(id), VehicleBrandResponse.class);
  }

//  @Override
//  public Long checkBrandVehicleNotFound(Long id) {
//    log.info("(checkBrandVehicleNotFound) brandId: {}", id);
//    if(id == null || id == 0){
//     return null;
//    }
//    return repository.findById(id).orElseThrow(() -> new VehicleBrandNotFoundException()).getId();
//  }

  private void checkNameExist(String name) {
    log.info("(checkNameExist) name : {}", name);
    if (repository.existsByName(name)) {
      log.error("(checkNameExist) ============= VehicleBrandAlreadyExistException");
      throw new VehicleBrandAlreadyExistException();
    }
  }
}
