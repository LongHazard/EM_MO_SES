package com.ncsgroup.ems.service.vehicle.impl;

import com.ncsgroup.ems.dto.request.vehicle.card.VehicleCardRequest;
import com.ncsgroup.ems.dto.response.vehicle.vehiclecard.VehicleCardPageResponse;
import com.ncsgroup.ems.dto.response.vehicle.vehiclecard.VehicleCardResponse;
import com.ncsgroup.ems.entity.embed.VehicleCardColorId;
import com.ncsgroup.ems.entity.vehicle.VehicleCard;
import com.ncsgroup.ems.entity.vehicle.VehicleCardColor;
import com.ncsgroup.ems.exception.vehicle.card.LicensePlateAlreadyExistException;
import com.ncsgroup.ems.exception.vehicle.card.VehicleCardCodeAlreadyExistException;
import com.ncsgroup.ems.exception.vehicle.card.VehicleCardNotFoundException;
import com.ncsgroup.ems.repository.VehicleCardColorRepository;
import com.ncsgroup.ems.repository.VehicleCardRepository;
import com.ncsgroup.ems.service.vehicle.VehicleCardService;
import com.ncsgroup.ems.service.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ncsgroup.ems.utils.DateUtils.convertToTimestamp;
import static com.ncsgroup.ems.utils.MapperUtils.*;

@Slf4j
public class VehicleCardServiceImpl extends BaseServiceImpl<VehicleCard> implements VehicleCardService {
  private final VehicleCardRepository repository;
  private final VehicleCardColorRepository vehicleCardColorRepository;

  public VehicleCardServiceImpl(VehicleCardRepository repository, VehicleCardColorRepository vehicleCardColorRepository) {
    super(repository);
    this.repository = repository;
    this.vehicleCardColorRepository = vehicleCardColorRepository;
  }

  @Override
  public VehicleCardResponse create(VehicleCardRequest request) {
    log.info("(create) request: {}", request);

    checkVehicleCardCodeExist(request.getCardCode());
    checkLicensePlateExist(request.getLicensePlate());

    VehicleCard vehicleCard = new VehicleCard();
    VehicleCard createVehicleCard = create(setVehicleCard(request, vehicleCard));

    Long vehicleCardId = createVehicleCard.getId();
    saveVehicleCardColor(vehicleCardId, request.getColorIds());

    return toDTO(createVehicleCard, VehicleCardResponse.class);
  }

  @Override
  public VehicleCardPageResponse list(int size, int page, boolean isAll) {
    log.info("(list) size: {}, page: {}, isAll: {}", size, page, isAll);

    List<VehicleCard> vehicleCards = isAll ?
          repository.findAll() : repository.getAll(PageRequest.of(page, size));

    return VehicleCardPageResponse.of(
          toDTOs(vehicleCards, VehicleCardResponse.class),
          repository.count());
  }

  @Override
  public VehicleCardResponse update(Long id, VehicleCardRequest request) {
    log.info("(update) id: {}, request: {}", id, request);

    VehicleCard existedVehicleCard = find(id);
    this.validateLicensePlate(
          request.getLicensePlate(),
          existedVehicleCard.getLicensePlate()
    );

    VehicleCard vehicleCard = new VehicleCard();
    setVehicleCard(request, vehicleCard);
    vehicleCard.setId(id);

    return toDTO(update(vehicleCard), VehicleCardResponse.class);
  }

  @Override
  public void updatePermanentResident(long addressId, long vehicleCardId) {
    log.info("(updatePermanentResident) addressId:{}, id:{}", addressId, vehicleCardId);

    repository.updatePermanentResident(addressId, vehicleCardId);
  }

  @Override
  public void checkVehicleCardCodeExist(String cardCode) {
    log.info("(checkVehicleCardCodeExist) start");

    if (Objects.nonNull(cardCode) && repository.existsByCardCode(cardCode)) {
      log.error("(checkVehicleCardCodeExist) vehicle card code already exists");
      throw new VehicleCardCodeAlreadyExistException();
    }
  }

  @Override
  public void checkLicensePlateExist(String licensePlate) {
    log.info("(checkLicensePlateExist) start");

    if (Objects.nonNull(licensePlate) && repository.existsByLicensePlate(licensePlate)) {
      log.error("(checkLicensePlateExist) license plate already exists");
      throw new LicensePlateAlreadyExistException();
    }
  }

  @Override
  public VehicleCard find(Long id) {
    log.info("(find) id: {}", id);

    return repository.findById(id).orElseThrow(VehicleCardNotFoundException::new);
  }

  @Override
  public VehicleCard findOrDefault(Long id) {
    log.info("(findOrDefault) id: {}", id);

    return Objects.isNull(id) ? null : repository.findById(id).orElse(null);
  }

  @Override
  public List<Long> findColorIds(Long vehicleCardId) {
    log.info("(findColorIds) vehicleCardId: {}", vehicleCardId);

    return vehicleCardColorRepository.findByVehicleCardId(vehicleCardId);
  }

  private VehicleCard setVehicleCard(VehicleCardRequest request, VehicleCard vehicleCard) {

    vehicleCard.setId(null);
    vehicleCard.setCardTypeId(request.getCardTypeId());
    vehicleCard.setCardCode(request.getCardCode());
    vehicleCard.setBrandId(request.getBrandId());
    vehicleCard.setVehicleTypeId(request.getVehicleTypeId());

    if (request.getRegistrationDate() != null) {
      vehicleCard.setRegistrationDate(convertToTimestamp(request.getRegistrationDate()));
    }
    vehicleCard.setLicensePlate(request.getLicensePlate());

    return vehicleCard;
  }

  @Override
  public void saveVehicleCardColor(Long vehicleCardId, List<Long> colorIds) {
    log.info("(saveVehicleCardColor) vehicleCardId: {}, colorIds: {}", vehicleCardId, colorIds);

    if (colorIds == null || colorIds.isEmpty()) {
      return;
    }

    List<VehicleCardColor> vehicleCardColors = new ArrayList<>();
    for (Long colorId : colorIds) {
      VehicleCardColorId id = VehicleCardColorId.of(vehicleCardId, colorId);
      vehicleCardColors.add(new VehicleCardColor(id));
    }
    vehicleCardColorRepository.saveAll(vehicleCardColors);
  }

  private void validateLicensePlate(String licensePlateReq, String licensePlateExist) {
    log.debug("(validateLicensePlate) licensePlateReq: {}, licensePlateExist: {}", licensePlateReq, licensePlateExist);

    if (Objects.nonNull(licensePlateReq) && !licensePlateReq.equals(licensePlateExist)) {
      checkLicensePlateExist(licensePlateReq);
    }
  }
}
