package com.ncsgroup.ems.service.identity.impl;

import com.ncsgroup.ems.dto.request.group.GroupRequest;
import com.ncsgroup.ems.dto.response.group.GroupPageResponse;
import com.ncsgroup.ems.dto.response.group.GroupResponse;
import com.ncsgroup.ems.dto.response.group.VehicleGroupResponse;
import com.ncsgroup.ems.dto.response.group.IdentityGroupResponse;
import com.ncsgroup.ems.entity.identity.Group;
import com.ncsgroup.ems.entity.identity.IdentityObjectGroup;
import com.ncsgroup.ems.exception.group.GroupIdNotFoundException;
import com.ncsgroup.ems.exception.group.GroupNameAlreadyExistException;
import com.ncsgroup.ems.exception.group.SubGroupAlreadyExistParentIdException;
import com.ncsgroup.ems.repository.GroupRepository;
import com.ncsgroup.ems.repository.IdentityObjectGroupRepository;
import com.ncsgroup.ems.service.identity.GroupService;
import com.ncsgroup.ems.service.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;

import static com.ncsgroup.ems.utils.MapperUtils.*;

@Slf4j
public class GroupServiceImpl extends BaseServiceImpl<Group> implements GroupService {
  private final GroupRepository repository;
  private final IdentityObjectGroupRepository identityObjectGroupRepository;

  public GroupServiceImpl(GroupRepository repository,
                          IdentityObjectGroupRepository identityObjectGroupRepository) {
    super(repository);
    this.repository = repository;
    this.identityObjectGroupRepository = identityObjectGroupRepository;
  }

  @Override
  public GroupResponse create(GroupRequest request) {
    log.info("(create) request: {}", request);
    if (request.getParentId() != null) {
      checkExist(request.getParentId());
    }
    checkGroupNameExist(request.getName());
    if (request.getSubIds() != null && !request.getSubIds().isEmpty()) {
      findIdsExist(request.getSubIds());
      checkExistParentIds(request.getSubIds());
    }
    Group group = toEntity(request, Group.class);
    group.setId(null);
    group = create(group);
    Long groupId = group.getId();

    setParentId(groupId, request.getSubIds());

    List<IdentityObjectGroup> identityObjectGroupList = buildIdentityObjectGroups(
          request.getVehicleIds(),
          request.getPersonIds(),
          groupId
    );

    identityObjectGroupRepository.saveAll(identityObjectGroupList);

    return toDTO(group, GroupResponse.class);
  }

  @Override
  public GroupPageResponse list(int size, int page, boolean isAll) {
    log.info("(list) size: {}, page: {}, isAll: {}", size, page, isAll);
    List<GroupResponse> groups = isAll ?
          toDTOs(repository.findAll(), GroupResponse.class) : repository.getSubGroupIdsById(PageRequest.of(page, size));
    return GroupPageResponse.of(
          groups,
          repository.count()
    );
  }

  @Override
  public GroupResponse update(Long id, GroupRequest request) {
    log.info("(update) id: {}, request: {}", id, request);
    Group existedGroup = find(id);
    if (checkEditField(existedGroup, request)) {
      checkGroupNameExist(request.getName());
      Group group = toEntity(request, Group.class);
      group.setId(id);
      return toDTO(update(group), GroupResponse.class);
    }
    return toDTO(existedGroup, GroupResponse.class);
  }

  @Override
  public List<GroupResponse> getSubGroups() {
    log.info("(getSubGroups) start");
    return repository.getSubGroups();
  }

  public boolean checkEditField(Group group, GroupRequest request) {
    log.info("(checkEditField) group: {}, request: {}", group, request);
    GroupRequest groupCheck = toDTO(group, GroupRequest.class);
    if (groupCheck.equals(request)) {
      return false;
    }
    return true;
  }

  public void checkExistParentIds(List<Long> subIds) {
    log.info("(checkExistParentIds) start");
    if (repository.checkExistParentIds(subIds)) {
      log.error("(checkExistParentIds) subId already exists parentId");
      throw new SubGroupAlreadyExistParentIdException();
    }
  }

  public void checkGroupNameExist(String name) {
    log.info("(checkGroupNameExist) start");
    if (repository.existsByName(name)) {
      log.error("(checkGroupNameExist) group name already exist");
      throw new GroupNameAlreadyExistException();
    }
  }

  @Override
  public void findIdsExist(List<Long> groupIds) {
    log.info("(findIdsExist) start");
    if (!repository.findListIdExist(groupIds)) {
      log.error("(findIdsExist) list ids not found");
      throw new GroupIdNotFoundException();
    }
  }

  @Override
  public List<String> findSubNames(Long parentId) {
    return repository.findSubNames(parentId);
  }

  @Override
  public List<String> findPerSonNames(Long groupId) {
    return repository.findPersonNames(groupId);
  }

  @Override
  public List<String> findLicensePlates(Long groupId) {
    return repository.findLicensePlates(groupId);
  }

  @Override
  @Transactional
  public List<VehicleGroupResponse> addGroupToVehicle(Long vehicleId, List<Long> groupIds) {
    log.info("(addGroupToVehicle)vehicleId: {}, groupIds: {}", vehicleId, groupIds);

    List<Group> groups = list(groupIds);
    checkGroupIdsNotFound(groups.size(), groupIds.size());

    identityObjectGroupRepository.deleteAllByVehicleId(vehicleId);

    List<IdentityObjectGroup> identityObjectGroups = groupIds.stream()
          .map(groupId -> IdentityObjectGroup.of(groupId, vehicleId))
          .collect(Collectors.toList());

    identityObjectGroupRepository.saveAll(identityObjectGroups);

    return toDTOs(groups, VehicleGroupResponse.class);
  }

  @Override
  public List<VehicleGroupResponse> findByVehicleId(Long vehicleId) {
    log.info("(findByVehicleId)vehicleId: {}", vehicleId);
    return repository.find(vehicleId);
  }

  @Override
  public VehicleGroupResponse detail(Long vehicleId) {
    log.info("(detail)vehicleId: {}", vehicleId);

    VehicleGroupResponse vehicleGroupResponse = repository.get(vehicleId);

    return vehicleGroupResponse.getId() == null && vehicleGroupResponse.getName() == null
          ? null : vehicleGroupResponse;
  }

  @Override
  public List<IdentityGroupResponse> getById(List<Long> ids) {
    log.info("(getByIdAndName) id:{}", ids);
    return repository.getById(ids);
  }

  @Override
  @Transactional
  public void save(long personId, List<Long> groupIds) {
    log.info("(addPersonIdAndGroupId) personId: {}, groupIds :{}", personId, groupIds);
    List<IdentityObjectGroup> identityObjectGroups = new ArrayList<>();
    for (Long groupId : groupIds) {
      identityObjectGroups.add(
            new IdentityObjectGroup(groupId, null, personId)
      );
    }
    identityObjectGroupRepository.saveAll(identityObjectGroups);
  }

  @Override
  public void addGroupsToVehicle(Long vehicleId, List<Long> groupIds) {
    log.info("(addGroupsToVehicle) vehicleId: {} groupIds: {}", vehicleId, groupIds);
    List<IdentityObjectGroup> identityObjectGroups = new ArrayList<>();
    for (Long groupId : groupIds) {
      identityObjectGroups.add(IdentityObjectGroup.of(groupId, vehicleId));
    }
    identityObjectGroupRepository.saveAll(identityObjectGroups);
  }

  @Override
  public void deleteGroupVehicle(Long vehicleId) {
    log.info("(deleteGroupVehicle) vehicleId: {}", vehicleId);
    identityObjectGroupRepository.deleteByVehicle(vehicleId);
  }

  @Override
  public List<VehicleGroupResponse> findVehicleGroupResponse(List<Long> groupIds) {
    log.info("(findVehicleGroupResponse) groupIds: {}", groupIds);
    return repository.findVehicleGroupResponse(groupIds);
  }


  @Override
  public void deleteIObyPersonId(Long personId) {
    identityObjectGroupRepository.deleteByPersonId(personId);
  }

  @Override
  public void remove(Long id) {
    log.info("(remove) id: {}", id);
    repository.remove(id);
  }

  @Override
  public List<Long> getByPersonId(Long personId) {
    log.info("(getByPersonId) ");
    return repository.getByPersonId(personId);
  }

  @Override
  public Group find(Long id) {
    log.info("(find) id: {}", id);
    return repository.findById(id).orElseThrow(GroupIdNotFoundException::new);
  }

  public List<Group> list(List<Long> ids) {
    log.info("(list) ids: {}", ids);

    return repository.findAllById(ids);
  }

  @Override
  public List<String> findNames(List<Long> groupIds) {
    return repository.findNames(groupIds);
  }

  public void checkExist(Long groupId) {
    log.info("(checkExist) groupId: {}", groupId);
    if (!repository.existsById(groupId)) {
      log.error("(checkExist) groupId not found");
      throw new GroupIdNotFoundException();
    }
  }


  @Override
  public List<Long> getSubGroupIdsById(Long id) {
    log.info("(getSubGroupIdsById) id: {}", id);

    return repository.getSubGroupIdsById(id);
  }

  @Override
  public Long getRootId(Long id) {
    log.info("(getRootId) id: {}", id);

    return repository.getRootGroupId(id);
  }

  public void setParentId(Long parentId, List<Long> subIds) {
    for (Long subId : subIds) {
      log.info("(findSubId) start");
      Group subGroup = find(subId);
      subGroup.setParentId(parentId);
    }
  }

  private List<IdentityObjectGroup> buildIdentityObjectGroups(
        List<Long> vehicleIds,
        List<Long> personIds,
        Long groupId
  ) {
    log.info("(buildIdentityObjectGroup) start");
    List<IdentityObjectGroup> identityObjectGroups = new ArrayList<>();

    for (Long vehicleId : vehicleIds) {
      identityObjectGroups.add(
            new IdentityObjectGroup(groupId, vehicleId, null)
      );
    }

    for (Long personId : personIds) {
      identityObjectGroups.add(
            new IdentityObjectGroup(groupId, null, personId)
      );
    }

    return identityObjectGroups;
  }

  private void checkGroupIdsNotFound(int updateIdSize, int existIdSize) {
    log.info("(checkGroupIdsNotFound)updateIdSize: {}, existIdSize: {}", updateIdSize, existIdSize);

    if (updateIdSize != existIdSize) {
      throw new GroupIdNotFoundException();
    }
  }
}
