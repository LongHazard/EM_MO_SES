package com.ncsgroup.ems.facade;

import com.ncsgroup.ems.dto.request.person.AddPersonToVehicle;
import com.ncsgroup.ems.dto.request.person.PersonFacadeRequest;
import com.ncsgroup.ems.dto.request.person.SearchPersonRequest;
import com.ncsgroup.ems.dto.response.person.PersonFacadeResponse;
import com.ncsgroup.ems.dto.response.person.PersonPageResponse;

public interface PersonFacadeService {
  void addPersonToVehicle(AddPersonToVehicle request);

  PersonFacadeResponse createPerson(PersonFacadeRequest request);

  PersonFacadeResponse getPerson(Long id);

  PersonPageResponse listPersons(SearchPersonRequest request, int size, int page, boolean isAll);

  PersonPageResponse listPersonsByGroup(long groupId, int page, int size);

  PersonFacadeResponse updatePerson(Long id, PersonFacadeRequest request);

  PersonFacadeResponse getPersonByVehicle(Long vehicleId);

}
