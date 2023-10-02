package com.ncsgroup.ems.service.identity;

import com.ncsgroup.ems.configuration.ServiceConfigurationTest;
import com.ncsgroup.ems.dto.request.group.GroupRequest;
import com.ncsgroup.ems.dto.response.group.GroupPageResponse;
import com.ncsgroup.ems.dto.response.group.GroupResponse;
import com.ncsgroup.ems.dto.response.group.IdentityGroupResponse;
import com.ncsgroup.ems.dto.response.group.VehicleGroupResponse;
import com.ncsgroup.ems.entity.identity.Group;
import com.ncsgroup.ems.exception.group.GroupIdNotFoundException;
import com.ncsgroup.ems.exception.group.GroupNameAlreadyExistException;
import com.ncsgroup.ems.exception.group.SubGroupAlreadyExistParentIdException;
import com.ncsgroup.ems.repository.GroupRepository;
import com.ncsgroup.ems.repository.IdentityObjectGroupRepository;
import com.ncsgroup.ems.service.person.PersonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@WebMvcTest(PersonService.class)
@ContextConfiguration(classes = {
      ServiceConfigurationTest.class
})
public class GroupServiceTests {
  @Autowired
  private GroupService groupService;
  @MockBean
  private GroupRepository groupRepository;
  @MockBean
  private IdentityObjectGroupRepository identityObjectGroupRepository;

  @Test
  public void createGroup_WhenInputGroupRequestValid_ReturnGroupResponse() {
    GroupRequest request = this.getRequest();
    Group group = new Group();
    this.setGroup(group);

    Mockito.when(groupRepository.save(any(Group.class))).thenReturn(group);

    GroupResponse groupResponse = groupService.create(request);

    check(group, groupResponse);
  }

  @Test
  public void createGroup_WhenInputParentIdIsNotNull_ThrowGroupIdNotFoundException() {
    GroupRequest request = this.getRequest2();
    Group group = new Group();
    this.setGroup2(group);

    Mockito.when(groupRepository.save(any(Group.class))).thenReturn(group);
    Mockito.when(groupRepository.existsById(request.getParentId())).thenReturn(false);

    Assertions.assertThrows(
          GroupIdNotFoundException.class,
          () -> groupService.create(request)
    );
  }

  @Test
  public void createGroup_WhenInputGroupNameAlreadyExist_ThrowGroupNameAlreadyExistException() {
    GroupRequest request = this.getRequest();
    Group group = new Group();
    this.setGroup(group);

    Mockito.when(groupRepository.save(any(Group.class))).thenReturn(group);
    Mockito.when(groupRepository.existsById(request.getParentId())).thenReturn(true);
    Mockito.when(groupRepository.existsByName(request.getName())).thenReturn(true);

    Assertions.assertThrows(
          GroupNameAlreadyExistException.class,
          () -> groupService.create(request)
    );
  }

  @Test
  public void createGroup_WhenInputSubIdsIsNotNullAndSubIdHaveNotParent_ThrowGroupIdNotFoundException() {
    GroupRequest request = this.getRequest3();
    Group group = new Group();
    this.setGroup(group);

    Mockito.when(groupRepository.save(any(Group.class))).thenReturn(group);
    Mockito.when(groupRepository.existsById(request.getParentId())).thenReturn(true);
    Mockito.when(groupRepository.existsByName(request.getName())).thenReturn(false);
    Mockito.when(groupRepository.findById(request.getSubIds().get(0))).thenReturn(Optional.of(new Group()));
    Mockito.when(groupRepository.findListIdExist(request.getSubIds())).thenReturn(false);

    Assertions.assertThrows(
          GroupIdNotFoundException.class,
          () -> groupService.create(request)
    );
  }

  @Test
  public void createGroup_WhenInputSubIdsIsNotNullAndSubIdHaveParent_ThrowSubGroupAlreadyExistParentIdException() {
    GroupRequest request = this.getRequest3();
    Group group = new Group();
    this.setGroup(group);

    Mockito.when(groupRepository.save(any(Group.class))).thenReturn(group);
    Mockito.when(groupRepository.existsById(request.getParentId())).thenReturn(true);
    Mockito.when(groupRepository.existsByName(request.getName())).thenReturn(false);
    Mockito.when(groupRepository.findListIdExist(request.getSubIds())).thenReturn(true);
    Mockito.when(groupRepository.findById(request.getSubIds().get(0))).thenReturn(Optional.of(new Group()));
    Mockito.when(groupRepository.checkExistParentIds(request.getSubIds())).thenReturn(true);


    Assertions.assertThrows(
          SubGroupAlreadyExistParentIdException.class,
          () -> groupService.create(request)
    );
  }

  @Test
  public void createGroup_WhenInputSubIdNotFound_ThrowGroupIdNotFoundException() {
    GroupRequest request = this.getRequest3();
    Group group = new Group();
    this.setGroup(group);

    Mockito.when(groupRepository.save(any(Group.class))).thenReturn(group);
    Mockito.when(groupRepository.existsById(request.getParentId())).thenReturn(true);
    Mockito.when(groupRepository.existsByName(request.getName())).thenReturn(false);
    Mockito.when(groupRepository.findListIdExist(request.getSubIds())).thenReturn(true);
    Mockito.when(groupRepository.findById(request.getSubIds().get(0))).thenReturn(Optional.empty());
    Mockito.when(groupRepository.checkExistParentIds(request.getSubIds())).thenReturn(false);

    Assertions.assertThrows(
          GroupIdNotFoundException.class,
          () -> groupService.create(request)
    );
  }

  @Test
  public void save_WhenInputIsValid_ReturnSuccess() {
    long personId = 1L;
    List<Long> groupIds = new ArrayList<>();
    groupIds.add(2L);
    groupIds.add(3L);

    groupService.save(personId, groupIds);

    verify(identityObjectGroupRepository, times(1)).saveAll(anyList());
  }

  @Test
  public void listGroup_whenInputIsAllIsTrue_ReturnListGroupPageResponse() {
    List<GroupResponse> groupResponses = new ArrayList<>();
    groupResponses.add(new GroupResponse());
    groupResponses.add(new GroupResponse());

    Mockito.when(groupRepository.findAll()).thenReturn(Arrays.asList(new Group(), new Group()));
    Mockito.when(groupRepository.count()).thenReturn(2L);

    GroupPageResponse groupPageResponse = groupService.list(10, 0, true);

    assertThat(Objects.equals(groupResponses, groupPageResponse.getGroupResponses()));
    assertThat(groupPageResponse.getCount().equals(2L));
  }

  @Test
  public void listGroup_whenInputIsAllIsFalse_ReturnListGroupPageResponse() {
    List<GroupResponse> groupResponses = new ArrayList<>();
    groupResponses.add(new GroupResponse());
    groupResponses.add(new GroupResponse());

    Mockito.when(groupRepository.getSubGroupIdsById(PageRequest.of(0, 10)))
          .thenReturn(groupResponses);

    Mockito.when(groupRepository.count()).thenReturn(2L);

    GroupPageResponse groupPageResponse = groupService.list(10, 0, false);

    assertThat(Objects.equals(groupResponses, groupPageResponse.getGroupResponses()));
    assertThat(groupPageResponse.getCount().equals(2L));
  }

  @Test
  public void updateGroup_whenInputIdNotFound_ThrowGroupIdNotFoundException() {
    Long id = this.getId();
    GroupRequest request = this.getRequest();
    Group group = new Group();
    this.setGroup(group);

    Mockito.when(groupRepository.findById(id)).thenReturn(Optional.empty());

    Assertions.assertThrows(
          GroupIdNotFoundException.class,
          () -> groupService.update(id, request)
    );
  }

  @Test
  public void updateGroup_whenCheckEditFieldIsFalseAndInPutGroupNameIsAlreadyExist_ReturnGroupResponse() {
    GroupRequest request = this.getRequest();
    Group group = new Group();
    this.setGroup(group);
    Long id = group.getId();

    Mockito.when(groupRepository.findById(id)).thenReturn(Optional.of(group));
    Mockito.when(groupRepository.existsByName(request.getName())).thenReturn(true);

    Assertions.assertThrows(
          GroupNameAlreadyExistException.class,
          () -> groupService.update(id, request)
    );
  }

  @Test
  public void updateGroup_whenCheckEditFieldIsFalse_ReturnGroupResponse() {
    GroupRequest request = this.getRequest2();
    Group group = new Group();
    this.setGroup2(group);
    Long id = group.getId();


    Mockito.when(groupRepository.findById(id)).thenReturn(Optional.of(group));
    Mockito.when(groupRepository.save(any(Group.class))).thenReturn(group);

    GroupResponse groupResponse = groupService.update(id, request);

    check(group, groupResponse);
  }

  private Long getId() {
    return 1L;
  }

  @Test
  public void getSubGroup_ReturnListGroupResponse() {
    List<GroupResponse> groups = new ArrayList<>();
    groups.add(new GroupResponse());
    groups.add(new GroupResponse());

    Mockito.when(groupRepository.getSubGroups()).thenReturn(groups);

    List<GroupResponse> groupResponses = groupService.getSubGroups();

    assertThat(Objects.equals(groupResponses, groups));
    assertThat(groupResponses.size() == 2);

  }

  @Test
  public void addGroupToVehicle_whenInputIsValid_ReturnListVehicleGroupResponse() {
    Long vehicleId = 1L;
    List<Long> groupIds = Arrays.asList(1L, 2L);
    List<Group> groups = new ArrayList<>();
    groups.add(new Group());
    groups.add(new Group());
    List<VehicleGroupResponse> vehicleGroups = new ArrayList<>();
    vehicleGroups.add(new VehicleGroupResponse());
    vehicleGroups.add(new VehicleGroupResponse());

    Mockito.when(groupRepository.findAllById(groupIds)).thenReturn(groups);

    List<VehicleGroupResponse> vehicleGroupResponses = groupService.addGroupToVehicle(vehicleId, groupIds);

    assertThat(Objects.equals(vehicleGroups, vehicleGroupResponses));
  }

  @Test
  public void addGroupToVehicle_whenInputIsValid_ThrowGroupIdNotFoundException() {
    Long vehicleId = 1L;
    List<Long> groupIds = Arrays.asList(1L, 2L);
    List<Group> groups = new ArrayList<>();
    groups.add(new Group());

    Mockito.when(groupRepository.findAllById(groupIds)).thenReturn(groups);

    Assertions.assertThrows(
          GroupIdNotFoundException.class,
          () -> groupService.addGroupToVehicle(vehicleId, groupIds)
    );
  }

  @Test
  public void findByVehicleId_WhenInputIsValid_ReturnListVehicleGroupResponse() {
    List<VehicleGroupResponse> vehicleGroups = new ArrayList<>();
    vehicleGroups.add(new VehicleGroupResponse());
    vehicleGroups.add(new VehicleGroupResponse());
    Long vehicleId = 1L;

    Mockito.when(groupRepository.find(vehicleId))
          .thenReturn(Arrays.asList(new VehicleGroupResponse(), new VehicleGroupResponse()));

    List<VehicleGroupResponse> vehicleGroupResponses = groupService.findByVehicleId(vehicleId);

    assertThat(Objects.equals(vehicleGroupResponses, vehicleGroups));
    assertThat(vehicleGroupResponses.size() == 2);
  }

  @Test
  public void detail_WhenInputIsValid_ReturnVehicleGroupResponse() {
    VehicleGroupResponse vehicleGroup = this.getVehicleGroupResponse();
    Long vehicleId = 1L;

    Mockito.when(groupRepository.get(vehicleId)).thenReturn(vehicleGroup);

    VehicleGroupResponse vehicleGroupResponse = groupService.detail(vehicleId);

    assertThat(Objects.equals(vehicleGroup, vehicleGroupResponse));
  }

  @Test
  public void detail_WhenInputIsValid_ReturnNull() {
    VehicleGroupResponse vehicleGroup = new VehicleGroupResponse();
    Long vehicleId = 1L;

    Mockito.when(groupRepository.get(vehicleId)).thenReturn(new VehicleGroupResponse());

    VehicleGroupResponse vehicleGroupResponse = groupService.detail(vehicleId);

    assertThat(Objects.equals(vehicleGroup, vehicleGroupResponse));
  }

  @Test
  public void getById_WhenInputIsValid_ReturnListIdentityGroupResponse() {
    List<IdentityGroupResponse> identityGroups = new ArrayList<>();
    identityGroups.add(this.getIdentityGroupResponse());
    identityGroups.add(this.getIdentityGroupResponse());
    List<Long> ids = Arrays.asList(1L, 2L);

    Mockito.when(groupRepository.getById(ids)).thenReturn(identityGroups);

    List<IdentityGroupResponse> identityGroupResponses = groupService.getById(ids);

    assertThat(Objects.equals(identityGroups, identityGroupResponses));
  }

  @Test
  public void find_WhenInputIsValid_ReturnGroup() {
    Long id = 1L;
    Group group = new Group();
    this.setGroup(group);

    Mockito.when(groupRepository.findById(id)).thenReturn(Optional.of(group));
    Group existGroup = groupService.find(id);

    assertThat(Objects.equals(group, existGroup));
  }

  @Test
  public void findNames_WhenInputIsValid_ReturnListGroupName() {
    List<Long> groupIds = Arrays.asList(1L, 2L);
    List<String> groupNames = Arrays.asList("name1", "name2");

    Mockito.when(groupRepository.findNames(groupIds)).thenReturn(groupNames);

    List<String> existGroupNames = groupService.findNames(groupIds);

    assertThat(Objects.equals(groupNames, existGroupNames));

  }

  @Test
  public void getSubGroupIdsById_WhenInputIdVali_ReturnListSubGroupId() {
    Long id = this.getId();
    List<Long> ids = Arrays.asList(1L, 2L);

    Mockito.when(groupRepository.getSubGroupIdsById(id)).thenReturn(ids);

    List<Long> existIds = groupService.getSubGroupIdsById(id);

    assertThat(Objects.equals(ids, existIds));
  }

  @Test
  public void getRootId_WhenInputIdVali_ReturnRootId() {
    Long id = this.getId();
    Long rootId = 2L;

    Mockito.when(groupRepository.getRootGroupId(id)).thenReturn(rootId);

    Long existRootId = groupService.getRootId(id);

    assertThat(Objects.equals(rootId, existRootId));
  }

  @Test
  public void remove_WhenInputIsValid_ReturnSuccess() {
    Long id = 1L;

    groupService.remove(id);

    verify(groupRepository, times(1)).remove(id);
  }

  @Test
  public void deleteIObyPersonId_WhenInputIsValid_ReturnSuccess() {
    Long persionId = 1L;

    groupService.deleteIObyPersonId(persionId);

    verify(identityObjectGroupRepository, times(1)).deleteByPersonId(persionId);
  }

  private GroupRequest getRequest() {
    return new GroupRequest(
          null,
          "Group_2",
          "Description cua Group 1",
          new ArrayList<>(),
          new ArrayList<>(),
          new ArrayList<>()
    );
  }

  private GroupRequest getRequest2() {
    return new GroupRequest(
          1L,
          "Group_2",
          "Description cua Group 1",
          new ArrayList<>(),
          new ArrayList<>(),
          new ArrayList<>()
    );
  }

  private GroupRequest getRequest3() {
    return new GroupRequest(
          1L,
          "Group_2",
          "Description cua Group 1",
          Arrays.asList(1L, 2L),
          new ArrayList<>(),
          new ArrayList<>()
    );
  }

  private void setGroup(Group group) {
    group.setParentId(null);
    group.setId(2L);
    group.setName("Group 2");
    group.setDescription("Description cua Group 1");
  }

  private void setGroup2(Group group) {
    group.setParentId(1L);
    group.setId(2L);
    group.setName("Group 2");
    group.setDescription("Description cua Group 1");
  }

  private void check(Group group, GroupResponse response) {
    assertThat(Objects.equals(group.getId(), response.getId()));
    assertThat(Objects.equals(group.getParentId(), response.getParentId()));
    assertThat(Objects.equals(group.getName(), response.getName()));
    assertThat(Objects.equals(group.getDescription(), response.getDescription()));
  }

  private VehicleGroupResponse getVehicleGroupResponse() {
    return new VehicleGroupResponse(1L, "name1");
  }

  private IdentityGroupResponse getIdentityGroupResponse() {
    return new IdentityGroupResponse(1L, "name1");
  }
}
