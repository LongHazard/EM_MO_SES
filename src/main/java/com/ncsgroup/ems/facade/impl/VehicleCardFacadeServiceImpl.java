package com.ncsgroup.ems.facade.impl;

import com.ncsgroup.ems.dto.request.image.ImageRequest;
import com.ncsgroup.ems.dto.request.vehicle.card.VehicleCardRequest;
import com.ncsgroup.ems.dto.response.cardtype.CardTypeResponse;
import com.ncsgroup.ems.dto.response.color.ColorResponse;
import com.ncsgroup.ems.dto.response.image.ImageResponse;
import com.ncsgroup.ems.dto.response.vehicle.brand.VehicleBrandResponse;
import com.ncsgroup.ems.dto.response.vehicle.type.VehicleTypeResponseDTO;
import com.ncsgroup.ems.dto.response.vehicle.vehiclecard.VehicleCardPageResponse;
import com.ncsgroup.ems.dto.response.vehicle.vehiclecard.VehicleCardResponse;
import com.ncsgroup.ems.entity.address.Address;
import com.ncsgroup.ems.entity.vehicle.VehicleCard;
import com.ncsgroup.ems.facade.VehicleCardFacadeService;
import com.ncsgroup.ems.service.address.AddressService;
import com.ncsgroup.ems.service.identity.ImageService;
import com.ncsgroup.ems.service.person.CardTypeService;
import com.ncsgroup.ems.service.vehicle.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ncsgroup.ems.utils.MapperUtils.toDTO;
import static com.ncsgroup.ems.utils.MapperUtils.toDTOs;
import static com.ncsgroup.ems.utils.ValidationUtils.isNullOrEmpty;


@Slf4j
@RequiredArgsConstructor
public class VehicleCardFacadeServiceImpl implements VehicleCardFacadeService {
  private final VehicleCardService vehicleCardService;
  private final CardTypeService cardTypeService;
  private final VehicleBrandService vehicleBrandService;
  private final VehicleTypeService vehicleTypeService;
  private final ColorService colorService;
  private final AddressService addressService;
  private final ImageService imageService;
  private final VehicleCardColorService vehicleCardColorService;

  @Override
  @Transactional
  public VehicleCardResponse create(VehicleCardRequest request) {
    log.info("(create) request: {}", request);

    CardTypeResponse cardType = cardTypeService.detail(request.getCardTypeId());
    VehicleBrandResponse vehicleBrand = vehicleBrandService.detail(request.getBrandId());
    VehicleTypeResponseDTO vehicleType = vehicleTypeService.detail(request.getVehicleTypeId());
    colorService.checkNotFound(request.getColorIds());
    List<ColorResponse> colorResponses = findColorList(request.getColorIds());

    VehicleCardResponse vehicleCardResponse = vehicleCardService.create(request);
    if (!isNullOrEmpty(request.getImages())) {
      imageService.addImageToVehicleCard(request.getImages(), vehicleCardResponse.getId());
    }


    VehicleCardResponse vehicleOfCardResponse = setSubResponse(
          vehicleCardResponse,
          cardType,
          vehicleBrand,
          vehicleType,
          imageService.getByRequests(request.getImages()),
          colorResponses
    );

    if (request.getPermanentResident() != null) {
      Address address = addressService.createOfVehicleCard(request.getPermanentResident());
      vehicleOfCardResponse.setPermanentResident(addressService.detail(address.getId()));
      vehicleCardService.updatePermanentResident(address.getId(), vehicleCardResponse.getId());
    }

    return vehicleOfCardResponse;
  }

  @Override
  public VehicleCardPageResponse list(int size, int page, boolean isAll) {
    log.info("(list) size: {}, page: {}, isAll: {}", size, page, isAll);

    VehicleCardPageResponse pageResponse = vehicleCardService.list(size, page, isAll);
    for (VehicleCardResponse response : pageResponse.getVehicleCardResponses()) {
      setSubResponse(
            response,
            (response.getCardType() == null) ? null : cardTypeService.detail(response.getCardType().getId()),
            (response.getVehicleBrand() == null) ? null : vehicleBrandService.detail(response.getVehicleBrand().getId()),
            (response.getVehicleType() == null) ? null : vehicleTypeService.detail(response.getVehicleType().getId()),
            imageService.getAllByVehicleCard(response.getId()),
            findColorList(vehicleCardService.findColorIds(response.getId()))
      );
    }

    return pageResponse;
  }


  @Override
  public VehicleCardResponse detail(Long id) {
    log.info("(detail) id: {}", id);

    VehicleCard vehicleCard = vehicleCardService.find(id);

    CardTypeResponse cardType = cardTypeService.detail(vehicleCard.getCardTypeId());
    VehicleBrandResponse vehicleBrand = vehicleBrandService.detail(vehicleCard.getBrandId());
    VehicleTypeResponseDTO vehicleType = vehicleTypeService.detail(vehicleCard.getVehicleTypeId());
    List<ColorResponse> colorResponses = findColorList(vehicleCardService.findColorIds(id));

    VehicleCardResponse vehicleCardResponse = toDTO(vehicleCard, VehicleCardResponse.class);

    return setSubResponse(
          vehicleCardResponse,
          cardType,
          vehicleBrand,
          vehicleType,
          imageService.getAllByVehicleCard(id),
          colorResponses
    );
  }

  @Override
  public VehicleCardResponse findOrDefault(Long id) {
    log.info("(findOrDefault) id: {}", id);

    VehicleCard vehicleCard = vehicleCardService.findOrDefault(id);

    if (vehicleCard == null) return null;

    CardTypeResponse cardType = cardTypeService.detail(vehicleCard.getCardTypeId());
    VehicleBrandResponse vehicleBrand = vehicleBrandService.detail(vehicleCard.getBrandId());
    VehicleTypeResponseDTO vehicleType = vehicleTypeService.detail(vehicleCard.getVehicleTypeId());
    List<ColorResponse> colorResponses = findColorList(vehicleCardService.findColorIds(id));

    VehicleCardResponse vehicleCardResponse = toDTO(vehicleCard, VehicleCardResponse.class);

    return setSubResponse(
          vehicleCardResponse,
          cardType,
          vehicleBrand,
          vehicleType,
          imageService.getAllByVehicleCard(id),
          colorResponses
    );
  }

  @Override
  @Transactional
  public VehicleCardResponse update(Long vehicleCardId, VehicleCardRequest request) {
    log.info("(update) vehicleCardId: {}, request: {}", vehicleCardId, request);

    VehicleCard vehicleCardExisted = vehicleCardService.find(vehicleCardId);

    this.validateCardCodePreUpdate(
          request.getCardCode(), vehicleCardExisted.getCardCode()
    );

    CardTypeResponse cardType = cardTypeService.detail(request.getCardTypeId());
    VehicleBrandResponse vehicleBrand = vehicleBrandService.detail(request.getBrandId());
    VehicleTypeResponseDTO vehicleType = vehicleTypeService.detail(request.getVehicleTypeId());

    VehicleCardResponse vehicleCardResponse = vehicleCardService.update(vehicleCardId, request);

    this.updateImages(
          vehicleCardResponse.getId(),
          request.getImages()
    );

    this.setImages(
          vehicleCardResponse,
          request.getImages()
    );

    vehicleCardColorService.update(vehicleCardId, request.getColorIds());

    List<ColorResponse> colorResponses = findColorList(request.getColorIds());

    return setSubResponse(
          vehicleCardResponse,
          cardType,
          vehicleBrand,
          vehicleType,
          imageService.getByRequests(request.getImages()),
          colorResponses
    );
  }

  @Override
  public List<ColorResponse> findColorList(List<Long> colorIds) {
    log.info("(checkColorList) list: {}", colorIds);

    if (colorIds == null || colorIds.isEmpty()) return new ArrayList<>();

    return toDTOs(colorService.findByIds(colorIds), ColorResponse.class);
  }

  public VehicleCardResponse setSubResponse(
        VehicleCardResponse vehicleCardResponse,
        CardTypeResponse cardType,
        VehicleBrandResponse vehicleBrand,
        VehicleTypeResponseDTO vehicleType,
        List<ImageResponse> imageResponses,
        List<ColorResponse> colorResponses
  ) {
    log.info("(setSubResponse) start");

    vehicleCardResponse.setCardType(cardType);
    vehicleCardResponse.setVehicleBrand(vehicleBrand);
    vehicleCardResponse.setVehicleType(vehicleType);
    vehicleCardResponse.setImages(imageResponses);
    vehicleCardResponse.setColors(colorResponses);

    return vehicleCardResponse;
  }


  private void validateCardCodePreUpdate(String cardCodeReq, String cardCodeExist) {
    log.debug("(validateCardCodePreUpdate) cardCodeReq: {}, cardCodeExist: {}", cardCodeReq, cardCodeExist);

    if (Objects.nonNull(cardCodeExist) && cardCodeExist.equals(cardCodeReq) ||
          Objects.isNull(cardCodeReq) && Objects.isNull(cardCodeExist)) {
      return;
    }

    vehicleCardService.checkVehicleCardCodeExist(cardCodeReq);
  }

  private void setImages(VehicleCardResponse vehicleCardResponse, List<ImageRequest> imageRequests) {

    imageService.addImageToVehicleCard(
          imageRequests,
          vehicleCardResponse.getId()
    );

    vehicleCardResponse.setImages(
          imageService.getByRequests(imageRequests)
    );
  }

  private void updateImages(Long vehicleCardId, List<ImageRequest> imageRequests) {
    log.info("(updateImages)vehicleId: {}, imageIds: {}", vehicleCardId, imageRequests);

    imageService.update(
          imageService.getIdByVehicleCard(vehicleCardId),
          imageRequests
    );
  }
}
