package com.ncsgroup.ems.service.identity.impl;

import com.ncsgroup.ems.dto.request.identity.AddIdentityObjectRequest;
import com.ncsgroup.ems.dto.request.identity.PersonVehicleRequest;
import com.ncsgroup.ems.dto.request.person.AddPersonToVehicle;
import com.ncsgroup.ems.entity.embed.IdentityObjectId;
import com.ncsgroup.ems.entity.identity.IdentityObject;
import com.ncsgroup.ems.exception.base.NotFoundException;
import com.ncsgroup.ems.repository.IdentityObjectRepository;
import com.ncsgroup.ems.repository.PersonRepository;
import com.ncsgroup.ems.repository.VehicleRepository;
import com.ncsgroup.ems.service.identity.IdentityObjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class IdentityObjectServiceImpl implements IdentityObjectService {
  private final IdentityObjectRepository repository;

  public IdentityObjectServiceImpl(IdentityObjectRepository repository) {
    this.repository = repository;
  }

  @Override
  public void create(AddPersonToVehicle request) {
    log.info("(create) request: {}", request);
    List<IdentityObject> list = new ArrayList<>();
    for (Long idVehicle : request.getVehicleIds()) {
      IdentityObject identityObject = IdentityObject.from(idVehicle, request.getPersonId());
      list.add(identityObject);
    }
    repository.saveAll(list);
  }

  @Override
  @Transactional
  public void update(Long vehicleId, Long personId) {
    log.info("(update)vehicleId: {}, personId: {}", vehicleId, personId);

    repository.delete(vehicleId);

    if (Objects.nonNull(personId)) {
      IdentityObject updateIdentityObject = IdentityObject.from(vehicleId, personId);
      repository.save(updateIdentityObject);
    }
  }

  @Override
  public void insert(Long vehicleId, Long personId, Integer type) {
    log.info("(insert) vehicleId:{}, personId:{}, type:{}", vehicleId, personId, type);
    repository.insert(vehicleId, personId, type);
  }

  @Override
  public void insert(AddIdentityObjectRequest request) {
    log.info("(insert) request : {}", request);
    List<IdentityObject> identityObjects = new ArrayList<>();
    for (PersonVehicleRequest personVehicle: request.getPersonVehicles()) {
      for (Long vehicleId:personVehicle.getVehicleIds()) {
        identityObjects.add(new IdentityObject(vehicleId, personVehicle.getPersonId(), personVehicle.getType()));
      }
    }
    repository.saveAll(identityObjects);
  }

  private IdentityObject find(IdentityObjectId id) {
    log.info("(find) id: {}", id);
    return repository.findById(id).orElseThrow(NotFoundException::new);
  }

  private IdentityObject findOrNull(Long vehicleId) {
    return repository.findByVehicleId(vehicleId).orElse(null);
  }
}
