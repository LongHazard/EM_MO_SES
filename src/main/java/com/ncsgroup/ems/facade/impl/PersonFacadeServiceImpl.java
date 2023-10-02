package com.ncsgroup.ems.facade.impl;

import com.ncsgroup.ems.dto.request.image.ImageRequest;
import com.ncsgroup.ems.dto.request.person.AddPersonToVehicle;
import com.ncsgroup.ems.dto.request.person.PersonFacadeRequest;
import com.ncsgroup.ems.dto.request.person.PersonRequest;
import com.ncsgroup.ems.dto.request.person.SearchPersonRequest;
import com.ncsgroup.ems.dto.request.person.card.PersonCardRequest;
import com.ncsgroup.ems.dto.response.person.PersonCreateResponse;
import com.ncsgroup.ems.dto.response.person.PersonFacadeResponse;
import com.ncsgroup.ems.dto.response.person.PersonPageResponse;
import com.ncsgroup.ems.dto.response.person.PersonResponse;
import com.ncsgroup.ems.dto.response.personcard.PersonCardResponse;
import com.ncsgroup.ems.facade.PersonCardFacadeService;
import com.ncsgroup.ems.facade.PersonFacadeService;

import com.ncsgroup.ems.service.identity.GroupService;
import com.ncsgroup.ems.service.identity.IdentityObjectService;
import com.ncsgroup.ems.service.identity.ImageService;
import com.ncsgroup.ems.service.person.PersonService;
import com.ncsgroup.ems.service.vehicle.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class PersonFacadeServiceImpl implements PersonFacadeService {
  private final PersonService personService;

  private final PersonCardFacadeService personCardFacadeService;

  private final ImageService imageService;

  private final IdentityObjectService identityObjectService;

  private final VehicleService vehicleService;

  private final GroupService groupService;

  @Override
  @Transactional
  public PersonFacadeResponse createPerson(PersonFacadeRequest request) {
    log.info("(create) request: {}", request);

    PersonRequest personRequest = request.getPerson();
    PersonCardRequest personCardRequest = request.getPersonCard();

    PersonCardResponse personCardResponse = null;

    if (Objects.nonNull(personCardRequest)) {
      personCardResponse = personCardFacadeService.create(personCardRequest);
      addImageForPersonCard(personCardRequest, personCardResponse);
    }

    PersonCreateResponse personCreateResponse = personService.create(
          personRequest,
          (personCardResponse == null) ? null : personCardResponse.getId()
    );

    addImageForPerson(
          personRequest,
          personCreateResponse
    );

    setGroup(
          personCreateResponse.getId(),
          request.getGroupIds()
    );

    return new PersonFacadeResponse(
          personCreateResponse,
          personCardResponse,
          groupService.getById(request.getGroupIds())
    );
  }

  @Override
  public PersonFacadeResponse getPerson(Long id) {
    log.info("(getPerson) id : {}", id);

    PersonCreateResponse personCreateResponse = personService.detail(id);
    PersonCardResponse personCardResponse = null;

    Long personCardId = personService.getPersonCardId(id);

    if (Objects.nonNull(personCardId)) {
      personCardResponse = personCardFacadeService.get(personCardId);
      personCardResponse.setImages(
            imageService.getAllByPersonCard(personCardResponse.getId())
      );
    }

    personCreateResponse.setImages(
          imageService.getAllByPerson(personCreateResponse.getId())
    );

    return new PersonFacadeResponse(
          personCreateResponse,
          personCardResponse,
          groupService.getById(groupService.getByPersonId(id))
    );
  }

  @Override
  public PersonPageResponse listPersons(SearchPersonRequest request, int size, int page, boolean isAll) {
    log.info("(list) request: {}, size : {}, page: {}, isAll: {}", request, size, page, isAll);

    PersonPageResponse personPageResponse = personService.list(request, size, page, isAll);

    Long personId;

    for (PersonResponse personResponse : personPageResponse.getPersons()) {
      personId = personResponse.getId();

      personResponse.setCountVehicle(
            personService.countVehicle(personId)
      );

      personResponse.setImages(
            imageService.getByPersonId(personId)
      );
    }
    return personPageResponse;
  }

  @Override
  public PersonPageResponse listPersonsByGroup(long groupId, int page, int size) {
    log.info("(listPersonByGroup) groupId:{}, page:{}, size:{}", groupId, page, size);

    PersonPageResponse personPageResponse = personService.listByGroup(groupId, page, size);

    Long personId;

    for (PersonResponse personResponse : personPageResponse.getPersons()) {
      personId = personResponse.getId();

      personResponse.setImages(
            imageService.getByPersonId(personId)
      );
    }

    return personPageResponse;
  }

  @Override
  @Transactional
  public PersonFacadeResponse updatePerson(Long id, PersonFacadeRequest request) {
    log.info("(updatePerson) id: {}, request: {} ", id, request);

    PersonRequest personRequest = request.getPerson();
    PersonCardRequest personCardRequest = request.getPersonCard();

    PersonCardResponse personCardResponse = null;

    PersonCreateResponse personCreateResponse = personService.update(id, personRequest);

    this.updateImages(
          id,
          personRequest.getImages()
    );

    this.setImage(
          personCreateResponse,
          personRequest.getImages()
    );

    if (Objects.nonNull(personCardRequest)) {
      Long personCardId = personService.getPersonCardId(id);

      personCardResponse = (Objects.nonNull(personCardId))
            ? personCardFacadeService.update(personService.getPersonCardId(id), personCardRequest)
            : personCardFacadeService.create(personCardRequest);

      personService.updatePersonCardId(personCreateResponse.getId(), personCardResponse.getId());

      if ((Objects.nonNull(personCardId)))
        this.updateImagesForPersonCard(
              personCardId,
              personCardRequest.getImages()
        );

      this.addImageForPersonCard(personCardRequest, personCardResponse);

    }

    setGroup(
          personCreateResponse.getId(),
          request.getGroupIds()
    );

    return new PersonFacadeResponse(
          personCreateResponse,
          personCardResponse,
          groupService.getById(request.getGroupIds())
    );
  }

  @Override
  public PersonFacadeResponse getPersonByVehicle(Long vehicleId) {
    log.info("(getPersonByVehicle) vehicleID :{}", vehicleId);
    return personService.getIdByVehicle(vehicleId) == null ? null : getPerson(personService.getIdByVehicle(vehicleId));
  }

  @Override
  public void addPersonToVehicle(AddPersonToVehicle request) {
    personService.checkNotFound(
          request.getPersonId()
    );

    vehicleService.checkNotFound(
          request.getVehicleIds()
    );

    identityObjectService.create(request);
  }

  public void setGroup(Long personId, List<Long> groupIds) {
    if (groupIds != null) {
      groupService.findIdsExist(groupIds);
      groupService.deleteIObyPersonId(personId);
      groupService.save(personId, groupIds);
    }
  }

  private void addImageForPersonCard(
        PersonCardRequest personCardRequest,
        PersonCardResponse personCardResponse
  ) {
    log.info("(addImageForPerson) personCardRequest:{}, personCardResponse:{}", personCardRequest, personCardResponse);

    if (Objects.nonNull(personCardRequest.getImages())) {
      imageService.addImageToPersonCard(
            personCardRequest.getImages(),
            personCardResponse.getId()
      );
      personCardResponse.setImages(
            imageService.getAllByPersonCard(personCardResponse.getId())
      );
    }

  }

  private void addImageForPerson(
        PersonRequest personRequest,
        PersonCreateResponse personCreateResponse
  ) {
    log.info("(addImageForPersonCard) personRequest:{}, personCreateResponse:{}", personRequest, personCreateResponse);
    if (Objects.nonNull(personRequest.getImages())) {
      imageService.addImageToPerson(
            personRequest.getImages(),
            personCreateResponse.getId()
      );

      personCreateResponse.setImages(
            imageService.getAllByPerson(personCreateResponse.getId())
      );
    }
  }

  private void updateImages(Long personId, List<ImageRequest> imageRequests) {
    log.info("(updateImages)vehicleId: {}, imageRequests: {}", personId, imageRequests);

    imageService.update(
          imageService.getIdByPerson(personId),
          imageRequests
    );
  }

  private void updateImagesForPersonCard(Long personCardId, List<ImageRequest> imageRequests) {
    log.info("(updateImagesForPersonCard)personCardId: {}, imageRequests: {}", personCardId, imageRequests);

    imageService.update(
          imageService.getIdByPersonCard(personCardId),
          imageRequests
    );
  }

  private void setImage(
        PersonCreateResponse personCreateResponse,
        List<ImageRequest> images
  ) {

    if (Objects.nonNull(images) && !images.isEmpty()) {
      log.debug("(setImage) images: {}", images);

      imageService.addImageToPerson(
            images,
            personCreateResponse.getId()
      );

      personCreateResponse.setImages(
            imageService.getByRequests(images)
      );
    }
  }
}
