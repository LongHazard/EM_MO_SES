package com.ncsgroup.ems.service.identity;

import com.ncsgroup.ems.dto.request.group.GroupRequest;
import com.ncsgroup.ems.dto.response.group.GroupPageResponse;
import com.ncsgroup.ems.dto.response.group.GroupResponse;
import com.ncsgroup.ems.dto.response.group.VehicleGroupResponse;
import com.ncsgroup.ems.dto.response.group.IdentityGroupResponse;
import com.ncsgroup.ems.entity.identity.Group;
import com.ncsgroup.ems.service.base.BaseService;

import java.util.List;
import java.util.Set;

public interface GroupService extends BaseService<Group> {
  GroupResponse create(GroupRequest request);

  GroupPageResponse list(int size, int page, boolean isAll);

  GroupResponse update(Long id, GroupRequest request);

  List<GroupResponse> getSubGroups();

  Group find(Long id);

  List<String> findNames(List<Long> groupIds);

  void findIdsExist(List<Long> groupIds);

  List<String> findSubNames(Long parentId);

  List<String> findPerSonNames(Long groupId);

  List<String> findLicensePlates(Long groupId);

  List<VehicleGroupResponse> addGroupToVehicle(Long groupId, List<Long> vehicleId);

  List<VehicleGroupResponse> findByVehicleId(Long vehicleId);

  VehicleGroupResponse detail(Long vehicleId);

  List<IdentityGroupResponse> getById(List<Long> id);

  void save(long personId, List<Long> groupIds);

  List<Long> getSubGroupIdsById(Long id);

  Long getRootId(Long id);

  void addGroupsToVehicle(Long vehicleId, List<Long> groupIds);

  void deleteGroupVehicle(Long vehicleId);

  List<VehicleGroupResponse> findVehicleGroupResponse(List<Long> groupIds);

  void deleteIObyPersonId(Long personId);

  void remove(Long id);

  List<Long> getByPersonId(Long personId);
}
