package com.ncsgroup.ems.service.identity;


import com.ncsgroup.ems.configuration.ServiceConfigurationTest;
import com.ncsgroup.ems.dto.request.identity.AddIdentityObjectRequest;
import com.ncsgroup.ems.dto.request.identity.PersonVehicleRequest;
import com.ncsgroup.ems.dto.request.person.AddPersonToVehicle;
import com.ncsgroup.ems.entity.identity.IdentityObject;
import com.ncsgroup.ems.repository.IdentityObjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.Mockito.*;

import java.util.List;

@WebMvcTest(IdentityObjectService.class)
@ContextConfiguration(classes = {
      ServiceConfigurationTest.class
})
public class IdentityObjectServiceTests {

  @MockBean
  private IdentityObjectRepository repository;

  @Autowired
  private IdentityObjectService identityObjectService;

  @Test
  public void create_InputValid_ThenReturnSuccess() {
    AddPersonToVehicle request = addPersonToVehicle();

    identityObjectService.create(request);

    verify(repository).saveAll(any(List.class));
  }

  @Test
  public void update_InputValid_ThenReturnSuccess() {
    Long vehicleId = 100L;
    Long personId = 1L;

    identityObjectService.update(vehicleId, personId);

    verify(repository).delete(vehicleId);
    verify(repository).save(any(IdentityObject.class));
  }

  @Test
  public void update_InputPersonIdIsNull_ThenReturnSuccess() {
    Long vehicleId = 100L;
    Long personId = null;

    identityObjectService.update(vehicleId, personId);

    verify(repository).delete(vehicleId);
  }

  @Test
  public void insert_InputValid_ThenReturnSuccess() {
    Long vehicleId = 100L;
    Long personId = 1L;
    Integer type = 2;

    identityObjectService.insert(vehicleId, personId, type);

    verify(repository).insert(vehicleId, personId, type);
  }

  @Test
  public void inset_InputRequestValid_ThenReturnSuccess() {
    AddIdentityObjectRequest request = new AddIdentityObjectRequest();
    PersonVehicleRequest personVehicle1 = new PersonVehicleRequest();

    personVehicle1.setVehicleIds(List.of(1L,2L));
    personVehicle1.setPersonId(1L);
    personVehicle1.setType(1);

    PersonVehicleRequest personVehicle2 = new PersonVehicleRequest();

    personVehicle2.setVehicleIds(List.of(3L, 4L));
    personVehicle2.setPersonId(2L);
    personVehicle2.setType(2);

    request.setPersonVehicles(List.of(personVehicle1, personVehicle2));

    System.out.println(request.getPersonVehicles().get(0).getPersonId());
    System.out.println(request.getPersonVehicles().get(1).getPersonId());

    identityObjectService.insert(request);

    verify(repository).saveAll(anyList());
  }

  private AddPersonToVehicle addPersonToVehicle() {
    Long personId = 1L;
    List<Long> vehicleIds = List.of(1L, 2L, 3L);
    return AddPersonToVehicle.of(personId, vehicleIds);

  }
}
