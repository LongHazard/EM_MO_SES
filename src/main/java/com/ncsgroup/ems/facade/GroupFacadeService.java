package com.ncsgroup.ems.facade;

import com.ncsgroup.ems.dto.request.group.GroupRequest;
import com.ncsgroup.ems.dto.response.group.GroupPageResponse;
import com.ncsgroup.ems.dto.response.group.GroupResponse;
import com.ncsgroup.ems.dto.response.identity.IdentityResponse;

import java.util.List;

public interface GroupFacadeService {
  GroupResponse create(GroupRequest request);

  List<GroupResponse> list();

  List<IdentityResponse> getIdentityByGroupId(Long groupId);

  List<IdentityResponse> getIdentityUnrelated(Long groupId);
}
