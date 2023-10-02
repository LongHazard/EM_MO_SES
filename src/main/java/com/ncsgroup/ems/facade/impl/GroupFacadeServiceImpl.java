package com.ncsgroup.ems.facade.impl;

import com.ncsgroup.ems.dto.request.group.GroupRequest;
import com.ncsgroup.ems.dto.response.group.GroupResponse;
import com.ncsgroup.ems.dto.response.identity.IdentityResponse;
import com.ncsgroup.ems.facade.GroupFacadeService;
import com.ncsgroup.ems.service.identity.GroupService;
import com.ncsgroup.ems.service.person.PersonService;
import com.ncsgroup.ems.service.vehicle.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class GroupFacadeServiceImpl implements GroupFacadeService {
  private final GroupService groupService;
  private final PersonService personService;
  private final VehicleService vehicleService;

  @Override
  public GroupResponse create(GroupRequest request) {
    log.info("(createGroup) start");

    List<String> subNames = groupService.findNames(request.getSubIds());
    List<String> personNames = personService.findNames(request.getPersonIds());
    List<String> vehicleLicencePlate = vehicleService.findLicensePlates(request.getVehicleIds());

    GroupResponse groupResponse = groupService.create(request);

    groupResponse.setSubNames(subNames);
    groupResponse.setPersonNames(personNames);
    groupResponse.setVehicleLicencePlate(vehicleLicencePlate);

    return groupResponse;
  }

  //TODO 08-06
  @Override
  public List<GroupResponse> list() {
    List<GroupResponse> groupResponses = groupService.list(0, 0, true).getGroupResponses();
    groupResponses.forEach(item -> {
      item.setIdentities(vehicleService.getByGroupId(item.getId()));
      item.getIdentities().addAll(personService.getPersonByGroupId(item.getId()));
    });

    return groupResponses;
  }

  //TODO
  @Override
  public List<IdentityResponse> getIdentityByGroupId(Long groupId) {
    log.info("(getIdentityByGroupId) groupId:{}", groupId);

    List<Long> groupIds = groupService.getSubGroupIdsById(groupId);

    List<IdentityResponse> vehicleResponse = vehicleService.getByGroupIds(groupIds);
    List<IdentityResponse> personRepose = personService.getPersonByGroupId(groupIds);

    this.getVehicleByPerson(personRepose);

    vehicleResponse.addAll(personRepose);
    return vehicleResponse;
  }

  //TODO
  public List<IdentityResponse> getIdentityUnrelated(Long groupId) {
    log.info("(getIdentityUnrelated) groupId:{}", groupId);

    Long groupIdRoot = groupService.getRootId(groupId);
    List<Long> subGroupIds = groupService.getSubGroupIdsById(groupIdRoot);

    log.debug("(getIdentityUnrelated) groupIdRoot: {}, subGroupIds: {}", groupIdRoot, subGroupIds);
    List<IdentityResponse> vehicleResponse = vehicleService.getByOutsideGroupIds(subGroupIds);
    List<IdentityResponse> personRepose = personService.getByOutsideGroupIds(subGroupIds);

    this.getVehicleByPerson(personRepose);

    vehicleResponse.addAll(personRepose);
    return vehicleResponse;
  }

  private void getVehicleByPerson(List<IdentityResponse> personRepose) {
    personRepose.forEach(
          item -> item.setVehicles(vehicleService.getByPersonId(item.getPersonId()))
    );
  }
}
