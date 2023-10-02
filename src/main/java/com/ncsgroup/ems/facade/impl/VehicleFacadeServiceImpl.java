package com.ncsgroup.ems.facade.impl;

import com.ncsgroup.ems.dto.request.image.ImageRequest;
import com.ncsgroup.ems.dto.request.person.AddPersonToVehicle;
import com.ncsgroup.ems.dto.request.vehicle.FacadeVehicleRequest;
import com.ncsgroup.ems.dto.request.vehicle.VehicleFacadeRequest;
import com.ncsgroup.ems.dto.request.vehicle.VehicleFilter;
import com.ncsgroup.ems.dto.request.vehicle.VehicleRequest;
import com.ncsgroup.ems.dto.request.vehicle.card.VehicleCardRequest;
import com.ncsgroup.ems.dto.response.group.VehicleGroupResponse;
import com.ncsgroup.ems.dto.response.image.ImageResponse;
import com.ncsgroup.ems.dto.response.vehicle.*;
import com.ncsgroup.ems.dto.response.vehicle.vehiclecard.VehicleCardResponse;
import com.ncsgroup.ems.entity.person.Person;
import com.ncsgroup.ems.facade.VehicleCardFacadeService;
import com.ncsgroup.ems.facade.VehicleFacadeService;
import com.ncsgroup.ems.service.identity.GroupService;
import com.ncsgroup.ems.service.identity.IdentityGroupObjectService;
import com.ncsgroup.ems.service.identity.IdentityObjectService;
import com.ncsgroup.ems.service.identity.ImageService;
import com.ncsgroup.ems.service.person.CardTypeService;
import com.ncsgroup.ems.service.person.PersonService;
import com.ncsgroup.ems.service.vehicle.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Slf4j
@RequiredArgsConstructor
public class VehicleFacadeServiceImpl implements VehicleFacadeService {
  private final VehicleBrandService vehicleBrandService;
  private final VehicleService vehicleService;
  private final ImageService imageService;
  private final ColorService colorService;
  private final CardTypeService cardTypeService;
  private final IdentityObjectService identityObjectService;
  private final PersonService personService;
  private final VehicleColorService vehicleColorService;
  private final VehicleCardFacadeService vehicleCardFacadeService;
  private final GroupService groupService;
  private final VehicleTypeService vehicleTypeService;
  private final IdentityGroupObjectService identityGroupObjectService;

  @Override
  @Transactional
  public VehicleFacadeResponse createVehicle(VehicleFacadeRequest request) {
    log.info("(createVehicle) request: {}", request);

    VehicleRequest vehicleRequest = request.getVehicle();
    VehicleCardRequest vehicleCardRequest = request.getVehicleCard();
    VehicleCardResponse vehicleCardResponse = null;

    if (Objects.nonNull(vehicleCardRequest)) {
      vehicleCardResponse = vehicleCardFacadeService.create(vehicleCardRequest);
    }

    vehicleBrandService.find(vehicleRequest.getBrandId());
    vehicleTypeService.find(vehicleRequest.getTypeId());
    VehicleResponse vehicleResponse = vehicleService.create(
          vehicleRequest.getBrandId(),
          vehicleRequest.getTypeId(),
          vehicleRequest.getLicensePlate(),
          vehicleCardResponse == null ? null : vehicleCardResponse.getId(),
          vehicleRequest.getColorIds()
    );

    this.setImage(
          vehicleResponse,
          vehicleRequest.getImages()
    );

    this.setColor(vehicleResponse, request.getVehicle().getColorIds());

    VehicleFacadeResponse vehicleFacadeResponse = new VehicleFacadeResponse();

    this.setProperties(
          vehicleFacadeResponse,
          request.getPersonUid(),
          vehicleResponse,
          vehicleCardResponse,
          request.getGroupIds()
    );

    return vehicleFacadeResponse;
  }

  @Override
  @Transactional
  public FacadeVehicleResponse createVehicleWithMultiPerson(FacadeVehicleRequest request) {
    log.info("(createVehicleWithMultiPerson) request: {}", request);

    VehicleRequest vehicleRequest = request.getVehicle();
    VehicleCardRequest vehicleCardRequest = request.getVehicleCard();
    VehicleCardResponse vehicleCardResponse = null;

    if (Objects.nonNull(vehicleCardRequest)) {
      vehicleCardResponse = vehicleCardFacadeService.create(vehicleCardRequest);
    }

//    Long brandId = vehicleBrandService.checkBrandVehicleNotFound(vehicleRequest.getBrandId());
//    Long typeId = vehicleTypeService.checkTypeVehicleNotFound(vehicleRequest.getTypeId());

    vehicleBrandService.find(vehicleRequest.getBrandId());
    vehicleTypeService.find(vehicleRequest.getTypeId());

    VehicleResponse vehicleResponse = vehicleService.create(
          vehicleRequest.getBrandId(),
          vehicleRequest.getTypeId(),
          vehicleRequest.getLicensePlate(),
          vehicleCardResponse == null ? null : vehicleCardResponse.getId(),
          vehicleRequest.getColorIds()
    );

    this.setImage(
          vehicleResponse,
          vehicleRequest.getImages()
    );

    this.setColor(vehicleResponse, request.getVehicle().getColorIds());

    FacadeVehicleResponse facadeVehicleResponse = new FacadeVehicleResponse();

    this.set(
          facadeVehicleResponse,
          request.getOwnership().getPersonsUid(),
          request.getOwnership().getType(),
          vehicleResponse,
          vehicleCardResponse,
          request.getGroupIds()
    );

    return facadeVehicleResponse;
  }

  private void set(
        FacadeVehicleResponse vehicleFacadeResponse,
        List<String> personsUid,
        Integer type,
        VehicleResponse vehicleResponse,
        VehicleCardResponse vehicleCardResponse,
        List<Long> groupIds
  ) {
    List<Long> personIds = new ArrayList<>();
    if (Objects.nonNull(personsUid) && !personsUid.isEmpty()) {

      Long vehicleId = vehicleResponse.getId();

      for (String uid : personsUid) {
        personIds.add(personService.findIdByUid(uid));
        identityObjectService.insert(vehicleId, personService.findIdByUid(uid), type);
      }
    }

    OwnershipResponse ownerShip = new OwnershipResponse();
    ownerShip.setPersons(personService.list(personIds));
    ownerShip.setType(type);

    vehicleFacadeResponse.setOwnership(ownerShip);
    vehicleFacadeResponse.setVehicle(vehicleResponse);
    vehicleFacadeResponse.setVehicleCard(vehicleCardResponse);

    setGroup(vehicleFacadeResponse, groupIds);
  }

  private void setGroup(FacadeVehicleResponse vehicleFacadeResponse, List<Long> groupIds) {
    log.info("(setGroup)vehicleFacadeResponse: {}, groupIds: {}", vehicleFacadeResponse, groupIds);
    if (Objects.nonNull(groupIds) && !groupIds.isEmpty()) {
      List<VehicleGroupResponse> groups = groupService.addGroupToVehicle(
            vehicleFacadeResponse.getVehicle().getId(), groupIds
      );
      vehicleFacadeResponse.setGroups(groups);
    }
  }


  @Override
  public VehiclePageResponse list(VehicleFilter vehicleFilter, int size, int page, boolean isAll) {
    log.info("(list) vehicleFilter: {}, size : {}, page: {}, isAll: {}", vehicleFilter, size, page, isAll);

    VehiclePageResponse vehiclePageResponse = vehicleService.list(vehicleFilter, size, page, isAll);

    for (VehicleDetail vehicleDetail : vehiclePageResponse.getVehicleDetails()) {
      vehicleDetail.setNameColor(colorService.findName(vehicleDetail.getId()));
      List<ImageResponse> list = new ArrayList<>();
      list.add(imageService.getByVehicleId(vehicleDetail.getId()));
      vehicleDetail.setImages(list);
    }
    return vehiclePageResponse;
  }

  @Override
  public VehicleFacadeResponse detail(Long id) {
    log.info("(detail) id: {}", id);

    VehicleFacadeResponse vehicleFacadeResponse = new VehicleFacadeResponse();

    VehicleResponse vehicleResponse = vehicleService.detail(id);
    this.setImages(vehicleResponse);
    this.setColor(vehicleResponse);

    VehicleCardResponse vehicleCardResponse = Objects.nonNull(vehicleResponse.getVehicleCardId()) ?
          vehicleCardFacadeService.findOrDefault(vehicleResponse.getVehicleCardId()) : null;

    setProperties(
          id,
          vehicleFacadeResponse,
          vehicleResponse,
          vehicleCardResponse
    );

    return vehicleFacadeResponse;
  }

  @Transactional
  @Override
  public VehicleFacadeResponse update(Long vehicleId, VehicleFacadeRequest request) {
    log.info("(update) vehicleId: {}, request: {} ", vehicleId, request);

    VehicleRequest vehicleRequest = request.getVehicle();
    VehicleCardRequest vehicleCardRequest = request.getVehicleCard();
    Long vehicleCardId = Objects.isNull(request.getVehicleCard()) ? null : request.getVehicleCard().getId();

    VehicleCardResponse vehicleCardResponse = this.updateVehicleCard(
          vehicleCardId,
          vehicleCardRequest
    );

    VehicleResponse vehicleResponse = vehicleService.update(
          vehicleId,
          request.getVehicle(),
          Objects.isNull(vehicleCardResponse) ? null : vehicleCardResponse.getId()
    );

    this.updateOwner(
          vehicleId,
          request.getPersonUid()
    );

    this.updateColor(vehicleResponse,
          vehicleId,
          request.getVehicle().getColorIds()
    );

    this.updateImages(
          vehicleId,
          request.getVehicle().getImages()
    );

    this.setImage(
          vehicleResponse,
          Objects.nonNull(vehicleRequest) ? vehicleRequest.getImages() : null
    );

    this.updateGroup(
          vehicleId,
          request.getGroupIds()
    );

    VehicleFacadeResponse vehicleFacadeResponse = new VehicleFacadeResponse();

    this.setProperties(
          vehicleFacadeResponse,
          request.getPersonUid(),
          vehicleResponse,
          vehicleCardResponse,
          request.getGroupIds()
    );

    return vehicleFacadeResponse;

  }

  @Override
  public List<VehicleFacadeResponse> getByPersonId(long personId) {
    log.info("(getByPersonId) personId: {}", personId);

    List<Long> vehicleIds = vehicleService.getByPersonId(personId);

    List<VehicleFacadeResponse> vehicleFacadeResponses = new ArrayList<>();

    for (Long id : vehicleIds) {
      vehicleFacadeResponses.add(detail(id));
    }

    return vehicleFacadeResponses;
  }

  private void setGroup(VehicleFacadeResponse vehicleFacadeResponse, List<Long> groupIds) {
    log.info("(setGroup)vehicleFacadeResponse: {}, groupIds: {}", vehicleFacadeResponse, groupIds);
    if (Objects.nonNull(groupIds) && !groupIds.isEmpty()) {
      List<VehicleGroupResponse> groups = groupService.addGroupToVehicle(
            vehicleFacadeResponse.getVehicle().getId(),
            groupIds
      );
      vehicleFacadeResponse.setGroups(groups);
    }
  }

  private void updateColor(
        VehicleResponse vehicleResponse,
        Long vehicleId,
        List<Long> colorIdsReq
  ) {
    log.debug("(setColorPreUpdate) start");

    vehicleColorService.update(vehicleId, colorIdsReq);

    vehicleResponse.setColors(
          colorService.findNameById(colorIdsReq)
    );

  }

  private void setColor(VehicleResponse vehicleResponse, List<Long> colors) {
    log.info("(setColor) start");
    if (Objects.isNull(colors) || colors.isEmpty()) return;

    colorService.checkNotFound(colors);

    vehicleColorService.create(
          vehicleResponse.getId(),
          colors
    );

    vehicleResponse.setColors(
          colorService.findNameById(colors)
    );

  }

  private void setImage(
        VehicleResponse vehicleResponse,
        List<ImageRequest> vehicleImagesRequest
  ) {

    if (Objects.nonNull(vehicleImagesRequest) && !vehicleImagesRequest.isEmpty()) {
      log.debug("(setImage) vehicleImagesRequest: {}", vehicleImagesRequest);

      imageService.addImageToVehicle(
            vehicleImagesRequest,
            vehicleResponse.getId()
      );

      vehicleResponse.setImages(
            imageService.getByRequests(vehicleImagesRequest)
      );
    }

  }

  private void setProperties(
        VehicleFacadeResponse vehicleFacadeResponse,
        String uidPerson,
        VehicleResponse vehicleResponse,
        VehicleCardResponse vehicleCardResponse,
        List<Long> groupIds
  ) {
    log.info("(setProperties)start");

    if (Objects.nonNull(uidPerson) && !uidPerson.isEmpty()) {
      List<Long> vehicleIds = new ArrayList<>();
      vehicleIds.add(vehicleResponse.getId());

      Long idPerson = personService.findIdByUid(uidPerson);
      AddPersonToVehicle addPersonToVehicle = AddPersonToVehicle.of(idPerson, vehicleIds);
      identityObjectService.create(addPersonToVehicle);

      vehicleFacadeResponse.setUidPerson(uidPerson);
      vehicleFacadeResponse.setNamePerson(personService.findNameById(idPerson));
    }

    vehicleFacadeResponse.setVehicle(vehicleResponse);
    vehicleFacadeResponse.setVehicleCard(vehicleCardResponse);

    setGroup(vehicleFacadeResponse, groupIds);
  }

  private VehicleCardResponse updateVehicleCard(
        Long vehicleCardId,
        VehicleCardRequest vehicleCardRequest
  ) {
    log.debug("(updateVehicleCard) vehicleId: {}, vehicleCardRequest: {}", vehicleCardId, vehicleCardRequest);
    if (Objects.isNull(vehicleCardRequest)) return null;

    return Objects.isNull(vehicleCardId) ?
          vehicleCardFacadeService.create(
                vehicleCardRequest) :
          vehicleCardFacadeService.update(
                vehicleCardId,
                vehicleCardRequest
          );
  }

  private void updateImages(Long vehicleId, List<ImageRequest> imageIds) {
    log.info("(updateImages)vehicleId: {}, imageIds: {}", vehicleId, imageIds);

    imageService.update(
          imageService.getIdByVehicle(vehicleId),
          imageIds
    );

  }

  private void updateOwner(Long vehicleId, String personUid) {
    log.info("(updateOwner)vehicleId: {}, personUid: {}", vehicleId, personUid);

    identityObjectService.update(
          vehicleId,
          personService.findIdByUid(personUid)
    );
  }

  private void updateGroup(
        Long vehicleId,
        List<Long> groupIds
  ) {
    log.info("(updateGroup)vehicleId: {}, groupIds: {}", vehicleId, groupIds);

    identityGroupObjectService.update(vehicleId, groupIds);
  }

  private void setProperties(
        Long vehicleId,
        VehicleFacadeResponse vehicleFacadeResponse,
        VehicleResponse vehicleResponse,
        VehicleCardResponse vehicleCardResponse
  ) {
    log.info("(setProperties)start");

    Person person = personService.findByVehicleIdOrNull(vehicleId);

    if (Objects.nonNull(person)) {
      vehicleFacadeResponse.setUidPerson(person.getUid());
      vehicleFacadeResponse.setNamePerson(person.getName());
    }

    vehicleFacadeResponse.setVehicle(vehicleResponse);
    vehicleFacadeResponse.setVehicleCard(vehicleCardResponse);
    vehicleFacadeResponse.setGroups(groupService.findByVehicleId(vehicleId));
  }

  private void setImages(VehicleResponse vehicleResponse) {
    vehicleResponse.setImages(
          imageService.getAllByVehicle(vehicleResponse.getId())
    );
  }

  private void setColor(VehicleResponse vehicleResponse) {
    vehicleResponse.setColors(
          colorService.findByVehicleId(vehicleResponse.getId())
    );
  }

}

