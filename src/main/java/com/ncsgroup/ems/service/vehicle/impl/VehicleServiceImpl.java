package com.ncsgroup.ems.service.vehicle.impl;

import com.ncsgroup.ems.dto.request.vehicle.VehicleFilter;
import com.ncsgroup.ems.dto.request.vehicle.VehicleRequest;
import com.ncsgroup.ems.dto.request.vehicle.VehicleSearchRequest;
import com.ncsgroup.ems.dto.response.identity.IdentityResponse;
import com.ncsgroup.ems.dto.response.vehicle.*;
import com.ncsgroup.ems.dto.response.vehicle.vehiclecard.VehicleFilterDetail;
import com.ncsgroup.ems.entity.vehicle.Vehicle;
import com.ncsgroup.ems.exception.vehicle.VehicleLicensePlateAlreadyExistException;
import com.ncsgroup.ems.exception.vehicle.VehicleNotFoundException;
import com.ncsgroup.ems.repository.VehicleRepository;
import com.ncsgroup.ems.service.base.BaseServiceImpl;
import com.ncsgroup.ems.service.vehicle.VehicleService;
import com.ncsgroup.ems.utils.GeneratorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ncsgroup.ems.utils.MapperUtils.*;

@Slf4j
public class VehicleServiceImpl extends BaseServiceImpl<Vehicle> implements VehicleService {
  private final VehicleRepository repository;


  public VehicleServiceImpl(
        VehicleRepository repository
  ) {
    super(repository);
    this.repository = repository;
  }

  @Override
  public VehicleResponse create(
        Long brandId,
        Long typeId,
        String licensePlate,
        Long vehicleCardId,
        List<Long> colorIds
  ) {
    log.info("(create) brandId: {}, typeId: {}, licensePlate: {}, vehicleCardId: {}, colorIds: {}", brandId, typeId, licensePlate, vehicleCardId, colorIds);

    checkLicensePlateExist(licensePlate);
    Vehicle vehicle = Vehicle.from(
          brandId,
          typeId,
          licensePlate,
          vehicleCardId,
          GeneratorUtils.generateUid()
    );
    repository.save(vehicle);
    return MODEL_MAPPER.map(vehicle, VehicleResponse.class);

  }

  @Override
  public VehicleResponse detail(Long id) {
    log.info("9detail)id: {}", id);
    return toDTO(find(id), VehicleResponse.class);
  }


  private void checkLicensePlateExist(String licensePlate) {
    log.info("(checkLicensePlateExist) name : {}", licensePlate);
    if (Objects.nonNull(licensePlate) && repository.existsByLicensePlate(licensePlate)) {
      log.error("(existsByLicensePlate) ============= VehicleLicensePlateAlreadyExistException");
      throw new VehicleLicensePlateAlreadyExistException();
    }
  }


  @Override
  public void remove(long id) {
    log.info("(delete) id: {}", id);
    repository.deleteVehicle(id);
  }

  @Override
  public VehiclePageResponse list(VehicleFilter request, int size, int page, boolean isAll) {
    log.info("(list) vehicleFilter: {}, size : {}, page: {}, isAll: {}", request, size, page, isAll);
    Pageable pageable = (isAll) ? null : PageRequest.of(page, size);
    request.setKeyword((request.getKeyword() != null) ? request.getKeyword() : "");

    if (request.getVehicleTypeIds() == null) {
      request.setVehicleTypeIds(new ArrayList<>());
    }

    if (request.getLicensePlates() == null) {
      request.setLicensePlates(new ArrayList<>());
    }

    List<VehicleDetail> vehicleDetailList;

    vehicleDetailList = convertVehicleDetail(
          repository.search(
                request.getKeyword(),
                request.getVehicleTypeIds(),
                request.getLicensePlates(),
                pageable)
    );

    int total;
    total = repository.total(
          request.getKeyword(),
          request.getVehicleTypeIds(),
          request.getLicensePlates()
    );

    return VehiclePageResponse.of(vehicleDetailList, total);
  }

  @Override
  public Long getVehicleCardId(Long idVehicle) {
    return repository.findVehicleCardId(idVehicle);
  }

  public VehicleLicensePlatePageResponse listLicensePlate(String keyword, int size, int page, boolean isAll) {
    log.info("(listLicensePlate)keyword: {}, size : {}, page: {}, isAll: {}", keyword, size, page, isAll);

    List<VehicleLicensePlateResponse> vehicleLicensePlateResponses = isAll ?
          repository.getAllLicensePlate() : repository.searchLicensePlate(keyword, PageRequest.of(page, size));

    return VehicleLicensePlatePageResponse.of(
          vehicleLicensePlateResponses,
          isAll ? repository.count() : repository.countSearchLicensePlate(keyword)
    );
  }

  @Transactional
  @Override
  public VehicleResponse update(Long id, VehicleRequest request, Long vehicleCardId) {
    log.info("(update)id: {}, request: {}", id, request);

    Vehicle existedVehicle = find(id);

    if (hasChange(existedVehicle, request, vehicleCardId)) {

      setProperties(existedVehicle, request, vehicleCardId);

      return toDTO(update(existedVehicle), VehicleResponse.class);
    }
    return toDTO(existedVehicle, VehicleResponse.class);
  }

  @Override
  public void checkNotFound(List<Long> ids) {
    if (ids.size() != repository.getCount(ids)) {
      throw new VehicleNotFoundException();
    }
  }

  public List<String> findLicensePlates(List<Long> vehicleIds) {
    return repository.findLicensePlates(vehicleIds);
  }

  public List<VehicleDetail> convertVehicleDetail(List<VehicleFilterDetail> vehicleFilterDetails) {
    List<VehicleDetail> vehicleDetails = new ArrayList<>();
    for (VehicleFilterDetail vehicleFilterDetail : vehicleFilterDetails) {
      VehicleDetail vehicleDetail = new VehicleDetail();
      vehicleDetail.setId(vehicleFilterDetail.getId());
      vehicleDetail.setLicensePlate(vehicleFilterDetail.getLicense());
      vehicleDetail.setNameVehicleType(vehicleFilterDetail.getNameType());
      vehicleDetail.setNameVehicleBrand(vehicleFilterDetail.getNameBrand());
      vehicleDetail.setOwner(vehicleFilterDetail.getNamePerson());
      vehicleDetails.add(vehicleDetail);
    }
    return vehicleDetails;
  }

  @Override
  public List<IdentityResponse> getByGroupId(Long groupId) {
    log.info("(getVehicleByGroupId) groupId: {}", groupId);

    return repository.getByGroupId(groupId);
  }

  @Override
  public List<IdentityResponse> getByGroupIds(List<Long> groupIds) {
    log.info("(getVehicleByGroupId) groupIds: {}", groupIds);

    return repository.getByGroupIds(groupIds);
  }

  @Override
  public List<IdentityResponse> getByOutsideGroupIds(List<Long> groupIds) {
    log.info("(getByOutsideGroupIds) groupIds: {}", groupIds);

    return repository.getByOutsideGroupIds(groupIds);
  }

  @Override
  public List<IdentityResponse> getByPersonId(Long personId) {
    log.info("(getVehicleByPersonId) personId: {}", personId);

    return repository.getByPersonId(personId);
  }

  @Override
  public List<VehicleResponse> getByGroupId(VehicleSearchRequest request) {
    log.info("(getByGroupId) request :{}", request);

    String keyword = (request == null) ? null : request.getKeyword();
    long groupId = (request == null) ? 0 : request.getGroupId();

    List<Long> vehicleIds = repository.getByGroupId(keyword, groupId);

    List<VehicleResponse> vehicleDetails = new ArrayList<>();

    for (Long id : vehicleIds) {
      vehicleDetails.add(detail(id));
    }

    return vehicleDetails;
  }


  @Override
  public List<Long> getByPersonId(long personId) {
    log.info("(getVehicleByPersonId) personId : {}", personId);

    return repository.getByPersonId(personId);
  }

  private Vehicle find(Long id) {
    log.info("(find)id : {}", id);

    return repository.findById(id).orElseThrow(VehicleNotFoundException::new);
  }

  private boolean hasChange(Vehicle vehicle, VehicleRequest request, Long vehicleCardId) {
    if (Objects.isNull(vehicleCardId) ||
          !vehicleCardId.equals(vehicle.getVehicleCardId())) {
      return true;
    }

    if (Objects.nonNull(request.getLicensePlate()) &&
          !request.getLicensePlate().equals(vehicle.getLicensePlate())
    ) {
      checkLicensePlateExist(request.getLicensePlate());
      return true;
    }

    if (Objects.nonNull(request.getBrandId()) &&
          !request.getBrandId().equals(vehicle.getBrandId())) {
      return true;
    }

    if (Objects.nonNull(request.getTypeId()) &&
          !request.getTypeId().equals(vehicle.getTypeId())) {
      return true;
    }
    return false;
  }

  private void setProperties(Vehicle vehicle, VehicleRequest request, Long vehicleCardId) {
    log.info("(setProperties)vehicle : {}, request: {}", vehicle, request);

    vehicle.setBrandId(request.getBrandId());
    vehicle.setTypeId(request.getTypeId());
    vehicle.setLicensePlate(request.getLicensePlate());
    vehicle.setVehicleCardId(vehicleCardId);
  }
}
