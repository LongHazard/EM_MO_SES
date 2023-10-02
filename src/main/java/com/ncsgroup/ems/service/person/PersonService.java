package com.ncsgroup.ems.service.person;

import com.ncsgroup.ems.dto.request.person.PersonRequest;
import com.ncsgroup.ems.dto.request.person.PersonSearchRequest;
import com.ncsgroup.ems.dto.request.person.SearchPersonRequest;
import com.ncsgroup.ems.dto.response.identity.IdentityResponse;
import com.ncsgroup.ems.dto.response.person.PagePersonResponse;
import com.ncsgroup.ems.dto.response.person.PersonCreateResponse;
import com.ncsgroup.ems.dto.response.person.PersonDTO;
import com.ncsgroup.ems.dto.response.person.PersonPageResponse;
import com.ncsgroup.ems.entity.person.Person;
import com.ncsgroup.ems.service.base.BaseService;

import java.util.List;
import java.util.Set;

public interface PersonService extends BaseService<Person> {

  PersonCreateResponse create(PersonRequest request, Long personCardId);

  PersonPageResponse list(SearchPersonRequest request, int size, int page, boolean isAll);

  Long getPersonCardId(Long personId);

  int countVehicle(Long personId);

  PersonCreateResponse detail(Long id);

  PersonCreateResponse update(Long id, PersonRequest personRequest);

  Person checkNotFound(Long id);

  List<String> findNames(List<Long> personIds);

  Long findIdByUid(String uid);

  String findNameById(Long id);

  List<IdentityResponse> getPersonByGroupId(Long groupId);

  PersonPageResponse listByGroup(long groupId, int page, int size);

  List<IdentityResponse> getPersonByGroupId(List<Long> groupIds);

  Set<String> getDepartment(String keyword);

  Set<String> getPosition(String keyword);

  Set<String> getOrg(String keyword);

  List<IdentityResponse> getByOutsideGroupIds(List<Long> groupIds);

  Long getIdByVehicle(Long vehicleId);

  void updatePersonCardId(long id, long personCardId);

  Person find(String uid);

  void remove(long id);

  Person findByVehicleIdOrNull(Long vehicleId);

  PagePersonResponse list(PersonSearchRequest request, int page, int size, boolean isAll);

  List<PersonDTO> list(List<Long> personIds);
}
