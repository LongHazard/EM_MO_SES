package com.ncsgroup.ems.repository;


import com.ncsgroup.ems.dto.response.identity.IdentityResponse;
import com.ncsgroup.ems.dto.response.vehicle.VehicleDetail;
import com.ncsgroup.ems.dto.response.vehicle.VehicleLicensePlateResponse;
import com.ncsgroup.ems.entity.identity.Group;
import com.ncsgroup.ems.entity.identity.IdentityObject;
import com.ncsgroup.ems.entity.identity.IdentityObjectGroup;
import com.ncsgroup.ems.entity.person.Person;
import com.ncsgroup.ems.entity.vehicle.Vehicle;
import com.ncsgroup.ems.entity.vehicle.VehicleBrand;
import com.ncsgroup.ems.entity.vehicle.VehicleType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
//@DirtiesContext
class VehicleRepositoryTest {
  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private VehicleRepository vehicleRepository;

  @Test
  public void createVehicle_WhenInputValid_ReturnVehicleEntity() {
    Vehicle vehicleEntity = mockVehicleEntity();
    vehicleEntity = entityManager.persist(vehicleEntity);
    entityManager.flush();

    Vehicle vehicleSaved = vehicleRepository.save(vehicleEntity);

    assertEquals(vehicleEntity.getId(), vehicleSaved.getId());
    assertEquals(vehicleEntity.getTypeId(), vehicleSaved.getTypeId());
    assertEquals(vehicleEntity.getVehicleCardId(), vehicleSaved.getVehicleCardId());
    assertEquals(vehicleEntity.getUid(), vehicleSaved.getUid());
    assertEquals(vehicleEntity.getLicensePlate(), vehicleSaved.getLicensePlate());
    assertEquals(vehicleEntity.isDeleted(), vehicleSaved.isDeleted());
  }

  @Test
  public void existsByLicensePlate_WhenInputValid_ReturnTrue() {
    Vehicle vehicleEntity = mockVehicleEntity();
    vehicleEntity = entityManager.persist(vehicleEntity);
    entityManager.flush();

    boolean isExists = vehicleRepository.existsByLicensePlate(vehicleEntity.getLicensePlate());

    assertTrue(isExists);
  }

  @Test
  public void existsByLicensePlate_WhenInputValid_ReturnFalse() {
    boolean isExists = vehicleRepository.existsByLicensePlate("abc-def123");

    Assertions.assertFalse(isExists);
  }

  @Test
  public void detailVehicle_WhenInputValid_ReturnVehicleDetail() {
    Vehicle vehicleEntity = entityManager.persistAndFlush(mockVehicleEntity());
    Person personEntity = entityManager.persistAndFlush(mockPersonEntity());

    IdentityObject identityObject = IdentityObject.from(
          vehicleEntity.getId(),
          personEntity.getId()
    );

    entityManager.persistAndFlush(identityObject);

    VehicleBrand vehicleBrand = entityManager.find(VehicleBrand.class, vehicleEntity.getBrandId());
    VehicleType vehicleType = entityManager.find(VehicleType.class, vehicleEntity.getTypeId());
    VehicleDetail vehicleDetail = vehicleRepository.detail(vehicleEntity.getId());

    assertEquals(vehicleEntity.getId(), vehicleDetail.getId());
    assertEquals(vehicleEntity.getLicensePlate(), vehicleDetail.getLicensePlate());
    assertEquals(personEntity.getName(), vehicleDetail.getOwner());
    assertEquals(vehicleBrand.getName(), vehicleDetail.getNameVehicleBrand());
    assertEquals(vehicleType.getName(), vehicleDetail.getNameVehicleType());
  }

  @Test
  public void detailVehicle_WhenInputValid_ReturnNull() {
    VehicleDetail vehicleDetail = vehicleRepository.detail(1L);

    assertNull(vehicleDetail);
  }

  @Test
  public void findVehicleCardId_WhenInputValid_ReturnVehicleCardId() {
    Vehicle vehicleEntity = entityManager.persistAndFlush(mockVehicleEntity());

    Long vehicleCardId = vehicleRepository.findVehicleCardId(vehicleEntity.getId());

    assertEquals(vehicleEntity.getVehicleCardId(), vehicleCardId);
  }

  @Test
  public void findLicensePlates_WhenInputValid_ReturnLicensePlates() {
    Vehicle vehicleEntity = entityManager.persistAndFlush(mockVehicleEntity());

    List<String> licensePlates = vehicleRepository.findLicensePlates(List.of(vehicleEntity.getId()));

    assertEquals(vehicleEntity.getLicensePlate(), licensePlates.get(0));
  }

  @Test
  public void getAllLicensePlate_WhenInputValid_ReturnVehicleLicensePlateResponseList() {
    Vehicle vehicleEntity = entityManager.persistAndFlush(mockVehicleEntity());

    List<VehicleLicensePlateResponse> licensePlates = vehicleRepository.getAllLicensePlate();

    assertEquals(1, licensePlates.size());
  }

  @Test
  public void getAllLicensePlate_WhenInputValid_ReturnEmptyList() {
    List<VehicleLicensePlateResponse> licensePlates = vehicleRepository.getAllLicensePlate();

    assertEquals(0, licensePlates.size());
  }

  @Test
  public void getByGroupId_WhenInputValid_ReturnVehicleList() {
    Vehicle vehicleEntity = entityManager.persistAndFlush(mockVehicleEntity());

    Group group = entityManager.persistAndFlush(mockGroupEntity());
    IdentityObjectGroup identityObjectGroup = entityManager.persistAndFlush(
          mockIdentityObjectGroupEntity(
                vehicleEntity.getId(),
                group.getId()
          )
    );

    List<IdentityResponse> vehicles = vehicleRepository.getByGroupId(group.getId());

    assertEquals(1, vehicles.size());
  }


  private IdentityObjectGroup mockIdentityObjectGroupEntity(Long vehicleId, Long groupId) {
    return IdentityObjectGroup.of(
          groupId,
          vehicleId
    );
  }

  private Group mockGroupEntity() {
    return new Group(null, "test", null);
  }


  private Vehicle mockVehicleEntity() {
    return Vehicle.from(
          1L,
          1L,
          "abc-def123",
          null,
          "vehicle-uid"
    );
  }

  private Person mockPersonEntity() {
    return new Person(
          "person-name",
          "person-uid",
          "person-email",
          "person-sex",
          "person-phone-number",
          "person-staff-code",
          "person-position",
          "person-department",
          "person-face-id",
          null,
          false
    );
  }

//  @AfterEach
//  public void tearDown() {
//    executeDBQuery(dataSource, "DROP SCHEMA schema_name CASCADE;\n" +
//          "CREATE SCHEMA schema_name;");
//  }

}
