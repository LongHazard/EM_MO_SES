package com.ncsgroup.ems.service.vehicle;


import com.ncsgroup.ems.configuration.ServiceConfigurationTest;
import com.ncsgroup.ems.dto.request.vehicle.VehicleFilter;
import com.ncsgroup.ems.dto.request.vehicle.VehicleRequest;
import com.ncsgroup.ems.dto.request.vehicle.VehicleSearchRequest;
import com.ncsgroup.ems.dto.response.identity.IdentityResponse;
import com.ncsgroup.ems.dto.response.vehicle.*;
import com.ncsgroup.ems.dto.response.vehicle.vehiclecard.VehicleFilterDetail;
import com.ncsgroup.ems.entity.vehicle.Vehicle;
import com.ncsgroup.ems.exception.vehicle.VehicleLicensePlateAlreadyExistException;
import com.ncsgroup.ems.exception.vehicle.VehicleNotFoundException;
import com.ncsgroup.ems.repository.VehicleRepository;
import com.ncsgroup.ems.utils.GeneratorUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.ArgumentMatchers.any;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@WebMvcTest(VehicleService.class)
@ContextConfiguration(classes = {ServiceConfigurationTest.class})
public class VehicleServiceTest {

  @Autowired
  private VehicleService vehicleService;

  @MockBean
  private VehicleRepository repository;

  public List<VehicleDetail> convertVehicleDetail(List<VehicleFilterDetail> vehicleFilterDetails) {
    List<VehicleDetail> vehicleDetails = new ArrayList<>();
    for (VehicleFilterDetail vehicleFilterDetail : vehicleFilterDetails) {
      VehicleDetail vehicleDetail = new VehicleDetail();
      vehicleDetail.setId(vehicleFilterDetail.getId());
      vehicleDetail.setLicensePlate(vehicleFilterDetail.getLicense());
      vehicleDetail.setNameVehicleType(vehicleFilterDetail.getNameType());
      vehicleDetail.setNameVehicleBrand(vehicleFilterDetail.getNameBrand());
      vehicleDetail.setOwner(vehicleFilterDetail.getNamePerson());
      vehicleDetails.add(vehicleDetail);
    }
    return vehicleDetails;
  }

  @Test
  public void createVehicle_WhenInputValid_ReturnVehicleResponse() {
    String licensePlate = "test";
    Mockito.when(repository.existsByLicensePlate(licensePlate)).thenReturn(false);

    Vehicle vehicle = Vehicle.from(
          1L,
          1L,
          licensePlate,
          1L,
          GeneratorUtils.generateUid()
    );

    Mockito.when(repository.save(vehicle)).thenReturn(vehicle);

    VehicleResponse response = vehicleService.create(
          1L,
          1L,
          licensePlate,
          1L,
          new ArrayList<>()
    );

    assertEquals(vehicle.getBrandId(), response.getBrandId());
    assertEquals(vehicle.getTypeId(), response.getTypeId());
    assertEquals(vehicle.getLicensePlate(), response.getLicensePlate());
    assertEquals(vehicle.getVehicleCardId(), response.getVehicleCardId());
  }

  @Test
  public void createVehicle_WhenLicensePlateNull_ReturnVehicleResponse() {

    Vehicle vehicle = Vehicle.from(
          1L,
          1L,
          null,
          1L,
          GeneratorUtils.generateUid()
    );

    Mockito.when(repository.save(vehicle)).thenReturn(vehicle);

    VehicleResponse response = vehicleService.create(
          1L,
          1L,
          null,
          1L,
          new ArrayList<>()
    );

    assertEquals(vehicle.getBrandId(), response.getBrandId());
    assertEquals(vehicle.getBrandId(), response.getBrandId());
    assertEquals(vehicle.getTypeId(), response.getTypeId());
    assertEquals(vehicle.getLicensePlate(), response.getLicensePlate());
    assertEquals(vehicle.getVehicleCardId(), response.getVehicleCardId());
  }


  @Test
  public void createVehicle_WhenLicensePlateExist_ThrowVehicleLicensePlateAlreadyExistException() {
    String licensePlate = "test";
    Mockito.when(repository.existsByLicensePlate(licensePlate)).thenReturn(true);

    Vehicle vehicle = Vehicle.from(
          1L,
          1L,
          licensePlate,
          1L,
          GeneratorUtils.generateUid()
    );

    Mockito.when(repository.save(vehicle)).thenReturn(vehicle);

    assertThrows(VehicleLicensePlateAlreadyExistException.class, () -> vehicleService.create(
          1L,
          1L,
          licensePlate,
          1L,
          new ArrayList<>())
    );
  }

  @Test
  public void detailVehicle_WhenVehicleExit_ThrowsVehicleNotFoundException() {
    Long vehicleId = 1L;

    Mockito.when(repository.findById(vehicleId)).thenReturn(Optional.empty());

    assertThrows(VehicleNotFoundException.class, () -> vehicleService.detail(vehicleId));

  }

  @Test
  public void detailVehicle_WhenInputValid_ReturnVehicleResponse() {
    Vehicle vehicle = Vehicle.from(
          1L,
          1L,
          "29x5-12560",
          1L,
          GeneratorUtils.generateUid()
    );
    vehicle.setId(1L);
    Mockito.when(repository.findById(vehicle.getId())).thenReturn(Optional.of(vehicle));

    VehicleResponse result = vehicleService.detail(vehicle.getId());

    assertEquals(vehicle.getId(), result.getId());
    assertEquals(vehicle.getBrandId(), result.getBrandId());
    assertEquals(vehicle.getTypeId(), result.getTypeId());
    assertEquals(vehicle.getLicensePlate(), result.getLicensePlate());
    assertEquals(vehicle.getVehicleCardId(), result.getVehicleCardId());

  }

  @Test
  public void removeVehicle_WhenInputValid_ReturnVoid() {
    Long vehicleId = 1L;
    Vehicle vehicle = new Vehicle();
    vehicle.setDeleted(false);
    Mockito.when(repository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
    vehicleService.remove(vehicleId);
    assertFalse(vehicle.isDeleted());
  }


  @Test
  public void getVehicleCardId_WhenInputValid_ReturnLong() {
    Long vehicleCardId = 123L;
    Long vehicleId = 1L;
    Mockito.when(repository.findVehicleCardId(vehicleId)).thenReturn(vehicleCardId);
    Long vehicleCard = vehicleService.getVehicleCardId(vehicleId);
    assertEquals(vehicleCardId, vehicleCard);
  }

  @Test
  public void getVehicleCardId_WhenVehicleDoesNotExist_ReturnNull() {
    Long idVehicle = 1L;
    Mockito.when(repository.findVehicleCardId(idVehicle)).thenReturn(null);

    Long vehicleCardId = vehicleService.getVehicleCardId(idVehicle);

    assertNull(vehicleCardId);
  }


  @Test
  public void checkNotFoundVehicle_whenCountAndIdsSizeAreDifferent_ReturnThrowException() {
    List<Long> vehicleIds = Arrays.asList(1L, 2L, 3L);
    Mockito.when(repository.getCount(vehicleIds)).thenReturn(2);
    assertThrows(VehicleNotFoundException.class, () -> vehicleService.checkNotFound(vehicleIds));
  }

  @Test
  public void checkNotFoundVehicle_whenCountAndIdsSizeAreEqual_ReturnNotThrowException() {
    List<Long> vehicleIds = Arrays.asList(1L, 2L, 3L);
    Mockito.when(repository.getCount(vehicleIds)).thenReturn(vehicleIds.size());
    assertDoesNotThrow(() -> vehicleService.checkNotFound(vehicleIds));
  }

  @Test
  public void findLicensePlates_whenVehicleIdsAreValid_ReturnLicensePlates() {
    List<Long> vehicleIds = Arrays.asList(1L, 2L, 3L);
    List<String> licensePlates = Arrays.asList("ABC-123", "XYZ-789", "DEF-456");
    Mockito.when(repository.findLicensePlates(vehicleIds)).thenReturn(licensePlates);

    List<String> actualLicensePlates = vehicleService.findLicensePlates(vehicleIds);

    assertEquals(licensePlates, actualLicensePlates);
  }

  @Test
  public void findLicensePlates_whenVehicleIdsAreInvalid_ReturnEmptyList() {
    List<Long> vehicleIds = Arrays.asList(1L, 2L, 3L);
    Mockito.when(repository.findLicensePlates(vehicleIds)).thenReturn(Collections.emptyList());

    List<String> actualLicensePlates = vehicleService.findLicensePlates(vehicleIds);

    assertTrue(actualLicensePlates.isEmpty());
  }


  @Test
  public void getByGroupId_WhenInputValid_ReturnListIdentityResponse() {
    Long groupId = 1L;

    List<IdentityResponse> identityResponses = Arrays.asList(new IdentityResponse(
          1L,
          1L,
          1L,
          "XYZ",
          "ZXW",
          new ArrayList<>()
    ));

    Mockito.when(repository.getByGroupId(groupId)).thenReturn(identityResponses);
    List<IdentityResponse> list = vehicleService.getByGroupId(groupId);

    assertEquals(identityResponses.size(), list.size());
    assertEquals(identityResponses.get(0).getVehicleId(), list.get(0).getVehicleId());
    assertEquals(identityResponses.get(0).getPersonId(), list.get(0).getPersonId());
    assertEquals(identityResponses.get(0).getGroupId(), list.get(0).getGroupId());
    assertEquals(identityResponses.get(0).getName(), list.get(0).getName());
    assertEquals(identityResponses.get(0).getType(), list.get(0).getType());
  }

  @Test
  public void getByGroupId_WhenInputValid_ReturnIsEmpty() {
    Long groupId = 1L;

    List<IdentityResponse> identityResponses = Collections.emptyList();

    Mockito.when(repository.getByGroupId(groupId)).thenReturn(identityResponses);
    List<IdentityResponse> list = vehicleService.getByGroupId(groupId);

    assertTrue(list.isEmpty());
  }


  @Test
  public void getByGroupIds_WhenInputValid_ReturnListIdentityResponse() {
    List<Long> groupIds = Arrays.asList(1L);

    List<IdentityResponse> identityResponses = Arrays.asList(new IdentityResponse(
          1L,
          1L,
          1L,
          "XYZ",
          "ZXW",
          new ArrayList<>()
    ));

    Mockito.when(repository.getByGroupIds(groupIds)).thenReturn(identityResponses);
    List<IdentityResponse> list = vehicleService.getByGroupIds(groupIds);

    assertEquals(identityResponses.size(), list.size());
    assertEquals(identityResponses.get(0).getVehicleId(), list.get(0).getVehicleId());
    assertEquals(identityResponses.get(0).getPersonId(), list.get(0).getPersonId());
    assertEquals(identityResponses.get(0).getGroupId(), list.get(0).getGroupId());
    assertEquals(identityResponses.get(0).getName(), list.get(0).getName());
    assertEquals(identityResponses.get(0).getType(), list.get(0).getType());
  }

  @Test
  public void getByGroupIds_WhenInputValid_ReturnIsEmpty() {
    List<Long> groupIds = Arrays.asList(1L);

    List<IdentityResponse> identityResponses = Collections.emptyList();

    Mockito.when(repository.getByGroupIds(groupIds)).thenReturn(identityResponses);
    List<IdentityResponse> list = vehicleService.getByGroupIds(groupIds);

    assertTrue(list.isEmpty());
  }

  @Test
  public void getByOutsideGroupIds_WhenInputValid_ReturnListIdentityResponse() {
    List<Long> groupIds = Arrays.asList(1L);

    List<IdentityResponse> identityResponses = Arrays.asList(new IdentityResponse(
          1L,
          1L,
          1L,
          "XYZ",
          "ZXW",
          new ArrayList<>()
    ));

    Mockito.when(repository.getByOutsideGroupIds(groupIds)).thenReturn(identityResponses);
    List<IdentityResponse> list = vehicleService.getByOutsideGroupIds(groupIds);

    assertEquals(identityResponses.size(), list.size());
    assertEquals(identityResponses.get(0).getVehicleId(), list.get(0).getVehicleId());
    assertEquals(identityResponses.get(0).getPersonId(), list.get(0).getPersonId());
    assertEquals(identityResponses.get(0).getGroupId(), list.get(0).getGroupId());
    assertEquals(identityResponses.get(0).getName(), list.get(0).getName());
    assertEquals(identityResponses.get(0).getType(), list.get(0).getType());
  }

  @Test
  public void getByOutsideGroupIds_WhenInputValid_ReturnIsEmpty() {
    List<Long> groupIds = Arrays.asList(1L);

    List<IdentityResponse> identityResponses = Collections.emptyList();

    Mockito.when(repository.getByOutsideGroupIds(groupIds)).thenReturn(identityResponses);
    List<IdentityResponse> list = vehicleService.getByOutsideGroupIds(groupIds);

    assertTrue(list.isEmpty());
  }

  @Test
  public void getByPersonId_WhenInputValid_ReturnListIdentityResponse() {
    Long personId = 1L;
    List<IdentityResponse> identityResponses = Arrays.asList(new IdentityResponse(
          1L,
          1L,
          1L,
          "XYZ",
          "ZXW",
          new ArrayList<>()
    ));

    Mockito.when(repository.getByPersonId(personId)).thenReturn(identityResponses);
    List<IdentityResponse> list = vehicleService.getByPersonId(personId);

    assertEquals(identityResponses.size(), list.size());
    assertEquals(identityResponses.get(0).getVehicleId(), list.get(0).getVehicleId());
    assertEquals(identityResponses.get(0).getPersonId(), list.get(0).getPersonId());
    assertEquals(identityResponses.get(0).getGroupId(), list.get(0).getGroupId());
    assertEquals(identityResponses.get(0).getName(), list.get(0).getName());
    assertEquals(identityResponses.get(0).getType(), list.get(0).getType());
  }

  @Test
  public void getByPersonId_WhenInputValid_ReturnIsEmpty() {
    Long personId = 1L;

    List<IdentityResponse> identityResponses = Collections.emptyList();

    Mockito.when(repository.getByPersonId(personId)).thenReturn(identityResponses);
    List<IdentityResponse> list = vehicleService.getByPersonId(personId);

    assertTrue(list.isEmpty());
  }


  /**
   *
   */

  Integer page = 0;
  Integer size = 10;
  Pageable pageable = PageRequest.of(page, size);
  List<VehicleFilterDetail> vehicleFilterDetails = Arrays.asList(
        new VehicleFilterDetail() {
          @Override
          public Long getId() {
            return 1L;
          }

          @Override
          public String getLicense() {
            return "29x5-12560";
          }

          @Override
          public String getNameType() {
            return "car";
          }

          @Override
          public String getNameBrand() {
            return "audi";
          }

          @Override
          public String getNamePerson() {
            return "Van Long";
          }
        }
  );

  @Test
  public void listVehicle_WhenInputValid_ReturnVehiclePageResponse() {
    List<Long> vehicleTypeIds = new ArrayList<>();
    vehicleTypeIds.add(1L);
    List<String> licensePlates = new ArrayList<>();
    licensePlates.add("29x5-12560");

    Mockito.when(repository.search(
          "29x5",
          vehicleTypeIds,
          licensePlates, pageable
    )).thenReturn(vehicleFilterDetails);

    Mockito.when(repository.total(
          "29x5",
          vehicleTypeIds,
          licensePlates
    )).thenReturn(1);

    VehicleFilter vehicleFilter = VehicleFilter.of("29x5", vehicleTypeIds, licensePlates);
    VehiclePageResponse vehiclePageResponse = vehicleService.list(vehicleFilter, size, page, false);

    assertEquals(convertVehicleDetail(vehicleFilterDetails), vehiclePageResponse.getVehicleDetails());
    assertEquals(1, vehiclePageResponse.getNumberOfVehicle());

  }

  @Test
  public void listVehicle_WhenIsAllTrue_ReturnVehiclePageResponse() {
    List<Long> vehicleTypeIds = new ArrayList<>();
    vehicleTypeIds.add(1L);
    List<String> licensePlates = new ArrayList<>();
    licensePlates.add("29x5-12560");

    Mockito.when(repository.search(
          "29x5",
          vehicleTypeIds,
          licensePlates, null
    )).thenReturn(vehicleFilterDetails);

    Mockito.when(repository.total(
          "29x5",
          vehicleTypeIds,
          licensePlates
    )).thenReturn(1);

    VehicleFilter vehicleFilter = VehicleFilter.of("29x5", vehicleTypeIds, licensePlates);
    VehiclePageResponse vehiclePageResponse = vehicleService.list(vehicleFilter, size, page, true);

    assertEquals(convertVehicleDetail(vehicleFilterDetails), vehiclePageResponse.getVehicleDetails());
    assertEquals(convertVehicleDetail(vehicleFilterDetails).size(), vehiclePageResponse.getNumberOfVehicle());

  }

  @Test
  public void listVehicle_WhenVehicleFilterNull_ReturnVehiclePageResponse() {
    List<Long> vehicleTypeIds = new ArrayList<>();
    List<String> licensePlates = new ArrayList<>();
    Mockito.when((repository.search("", vehicleTypeIds, licensePlates, pageable))).thenReturn(vehicleFilterDetails);
    Mockito.when(repository.total("", vehicleTypeIds, licensePlates)).thenReturn(1);

    VehiclePageResponse vehiclePageResponse = vehicleService.list(VehicleFilter.of(null, null, null), size, page, false);

    assertEquals(convertVehicleDetail(vehicleFilterDetails), vehiclePageResponse.getVehicleDetails());
    assertEquals(convertVehicleDetail(vehicleFilterDetails).size(), vehiclePageResponse.getNumberOfVehicle());
  }

  @Test
  public void listVehicle_WhenKeywordNull_ReturnVehiclePageResponse() {
    List<Long> vehicleTypeIds = new ArrayList<>();
    vehicleTypeIds.add(1L);
    List<String> licensePlates = new ArrayList<>();
    licensePlates.add("29x5-12560");
    Mockito.when(repository.search("", vehicleTypeIds, licensePlates, pageable)).thenReturn(vehicleFilterDetails);
    Mockito.when(repository.total("", vehicleTypeIds, licensePlates)).thenReturn(1);

    VehicleFilter vehicleFilter = VehicleFilter.of(null, vehicleTypeIds, licensePlates);
    VehiclePageResponse vehiclePageResponse = vehicleService.list(vehicleFilter, size, page, false);

    assertEquals(convertVehicleDetail(vehicleFilterDetails), vehiclePageResponse.getVehicleDetails());
    assertEquals(convertVehicleDetail(vehicleFilterDetails).size(), vehiclePageResponse.getNumberOfVehicle());

  }

  @Test
  public void listVehicle_WhenVehicleTypeIdsNull_ReturnVehiclePageResponse() {
    String keyword = "29-x5";
    List<Long> vehicleTypeIds = new ArrayList<>();
    List<String> licensePlates = new ArrayList<>();
    licensePlates.add("29x5-12560");
    Mockito.when(repository.search(keyword, vehicleTypeIds, licensePlates, pageable)).thenReturn(vehicleFilterDetails);
    Mockito.when(repository.total(keyword, vehicleTypeIds, licensePlates)).thenReturn(1);

    VehicleFilter vehicleFilter = VehicleFilter.of(keyword, null, licensePlates);
    VehiclePageResponse vehiclePageResponse = vehicleService.list(vehicleFilter, size, page, false);

    assertEquals(convertVehicleDetail(vehicleFilterDetails), vehiclePageResponse.getVehicleDetails());
    assertEquals(convertVehicleDetail(vehicleFilterDetails).size(), vehiclePageResponse.getNumberOfVehicle());

  }

  @Test
  public void listVehicle_WhenLicensePlatesNull_ReturnVehiclePageResponse() {
    String keyword = "29-x5";
    List<Long> vehicleTypeIds = new ArrayList<>();
    vehicleTypeIds.add(1L);
    List<String> licensePlates = new ArrayList<>();

    Mockito.when(repository.search(keyword, vehicleTypeIds, licensePlates, pageable)).thenReturn(vehicleFilterDetails);
    Mockito.when(repository.total(keyword, vehicleTypeIds, licensePlates)).thenReturn(1);

    VehicleFilter vehicleFilter = VehicleFilter.of(keyword, vehicleTypeIds, null);
    VehiclePageResponse vehiclePageResponse = vehicleService.list(vehicleFilter, size, page, false);

    assertEquals(convertVehicleDetail(vehicleFilterDetails), vehiclePageResponse.getVehicleDetails());
    assertEquals(convertVehicleDetail(vehicleFilterDetails).size(), vehiclePageResponse.getNumberOfVehicle());

  }

  @Test
  public void getByPersonId_WhenInputValid_ReturnList() {
    long personId = 1L;
    List<Long> vehicleIds = Arrays.asList(1L, 2L);
    Mockito.when(repository.getByPersonId(personId)).thenReturn(vehicleIds);
    List<Long> response = vehicleService.getByPersonId(personId);
    assertEquals(vehicleIds, response);
    assertEquals(vehicleIds.size(), response.size());
  }

  @Test
  public void getByPersonId_WhenInputValid_ReturnIEmpty() {
    long personId = 1L;
    List<Long> vehicleIds = Collections.emptyList();
    Mockito.when(repository.getByPersonId(personId)).thenReturn(vehicleIds);
    List<Long> response = vehicleService.getByPersonId(personId);
    assertTrue(response.isEmpty());
  }

  private VehicleResponse getVehicleResponse() {
    VehicleResponse vehicleResponse = new VehicleResponse();
    vehicleResponse.setId(1L);
    vehicleResponse.setBrandId(1L);
    vehicleResponse.setTypeId(1L);
    vehicleResponse.setLicensePlate("29x5-123456");
    vehicleResponse.setVehicleCardId(2l);
    vehicleResponse.setOwner(null);
    vehicleResponse.setImages(null);
    vehicleResponse.setColors(null);
    return vehicleResponse;
  }

  @Test
  public void getByGroupId_WhenInputValid_ReturnListVehicleResponse() {
    String keyword = "test";
    long groupId = 1L;
    List<Long> vehicleIds = Arrays.asList(1L);
    Vehicle vehicle = new Vehicle();
    setVehicle(vehicle);
    VehicleResponse vehicleResponse1 = getVehicleResponse();

    List<VehicleResponse> vehicleResponse = Arrays.asList(vehicleResponse1);
    Mockito.when(repository.getByGroupId(keyword, groupId)).thenReturn(vehicleIds);
    Mockito.when(repository.findById(1L)).thenReturn(Optional.of(vehicle));

    VehicleSearchRequest request = new VehicleSearchRequest(keyword, groupId);
    List<VehicleResponse> vehicleResponses = vehicleService.getByGroupId(request);

    assertEquals(vehicleResponse, vehicleResponses);
    assertEquals(vehicleResponse.size(),vehicleResponses.size());

  }

  @Test
  public void getByGroupId_WhenKeywordIsNull_ReturnListVehicleResponse() {
    long groupId = 1L;
    List<Long> vehicleIds = Arrays.asList(1L);
    Vehicle vehicle = new Vehicle();
    setVehicle(vehicle);
    VehicleResponse vehicleResponse1 = getVehicleResponse();

    List<VehicleResponse> vehicleResponse = Arrays.asList(vehicleResponse1);
    Mockito.when(repository.getByGroupId(null, groupId)).thenReturn(vehicleIds);
    Mockito.when(repository.findById(1L)).thenReturn(Optional.of(vehicle));

    VehicleSearchRequest request = new VehicleSearchRequest(null, groupId);
    List<VehicleResponse> vehicleResponses = vehicleService.getByGroupId(request);

    assertEquals(vehicleResponse, vehicleResponses);
    assertEquals(vehicleResponse.size(),vehicleResponses.size());

  }

  @Test
  public void getByGroupId_WhenGroupIdZero_ReturnListVehicleResponse() {
    String keyword = "test";
    List<Long> vehicleIds = Arrays.asList(1L);
    Vehicle vehicle = new Vehicle();
    setVehicle(vehicle);
    VehicleResponse vehicleResponse1 = getVehicleResponse();

    List<VehicleResponse> vehicleResponse = Arrays.asList(vehicleResponse1);
    Mockito.when(repository.getByGroupId(keyword, 0)).thenReturn(vehicleIds);
    Mockito.when(repository.findById(1L)).thenReturn(Optional.of(vehicle));

    VehicleSearchRequest request = new VehicleSearchRequest(keyword, 0);
    List<VehicleResponse> vehicleResponses = vehicleService.getByGroupId(request);

    assertEquals(vehicleResponse, vehicleResponses);
    assertEquals(vehicleResponse.size(),vehicleResponses.size());

  }

  /**
   * UPDATE
   */
  private VehicleRequest vehicleRequest() {
    return new VehicleRequest(
          2L,
          2L,
          "29X5-12345"
    );
  }

  private void setVehicle(Vehicle vehicle) {
    vehicle.setId(1L);
    vehicle.setBrandId(1L);
    vehicle.setTypeId(1L);
    vehicle.setLicensePlate("29x5-123456");
    vehicle.setVehicleCardId(2L);
  }

  private void setVehicle2(Vehicle vehicle) {
    vehicle.setId(2L);
    vehicle.setBrandId(1L);
    vehicle.setTypeId(1L);
    vehicle.setLicensePlate("29x5-12345");
    vehicle.setVehicleCardId(2L);
  }

  private Vehicle map(VehicleRequest request, Long vehicleCardId) {
    Vehicle vehicle = new Vehicle();
    vehicle.setBrandId(request.getBrandId());
    vehicle.setUid(UUID.randomUUID().toString());
    vehicle.setLicensePlate(request.getLicensePlate());
    vehicle.setTypeId(request.getTypeId());
    vehicle.setVehicleCardId(vehicleCardId);
    return vehicle;
  }

  private void check(Vehicle vehicle, VehicleResponse vehicleResponse) {
    Assertions.assertThat(vehicleResponse).isNotNull();
    Assertions.assertThat(vehicle.getBrandId()).isEqualTo(vehicleResponse.getBrandId());
    Assertions.assertThat(vehicle.getTypeId()).isEqualTo(vehicleResponse.getTypeId());
    Assertions.assertThat(vehicle.getLicensePlate()).isEqualTo(vehicleResponse.getLicensePlate());
    Assertions.assertThat(vehicle.getVehicleCardId()).isEqualTo(vehicleResponse.getVehicleCardId());
  }

  @Test
  public void updateVehicle_WhenInputValid_ReturnVehicleResponse() {
    Long vehicleCardId = 2L;
    VehicleRequest request = vehicleRequest();
    Vehicle vehicle = new Vehicle();
    setVehicle(vehicle);

    Vehicle savedVehicle = map(request, vehicleCardId);

    Mockito.when(repository.findById(vehicle.getId())).thenReturn(Optional.of(vehicle));
    Mockito.when(repository.save(vehicle)).thenReturn(savedVehicle);
    Mockito.when(repository.existsByLicensePlate(request.getLicensePlate())).thenReturn(false);

    VehicleResponse vehicleResponse = vehicleService.update(vehicle.getId(), request, vehicleCardId);
    check(vehicle, vehicleResponse);

  }

  @Test
  public void updateVehicle_WhenLicensePlateAlreadyExist_ReturnVehicleResponse() {
    Long vehicleCardId = 2L;
    VehicleRequest request = vehicleRequest();
    Vehicle vehicle = new Vehicle();
    setVehicle(vehicle);
    Vehicle savedVehicle = map(request, vehicleCardId);

    System.out.println(vehicle.getLicensePlate());
    System.out.println(request.getLicensePlate());

    System.out.println(Objects.nonNull(request.getLicensePlate()) &&
          !request.getLicensePlate().equals(vehicle.getLicensePlate()));

    Mockito.when(repository.findById(1L)).thenReturn(Optional.of(vehicle));
    Mockito.when(repository.existsByLicensePlate(request.getLicensePlate())).thenReturn(true);
    Mockito.when(repository.save(vehicle)).thenReturn(savedVehicle);

    assertThrows(VehicleLicensePlateAlreadyExistException.class, () -> vehicleService.update(1L, request, vehicleCardId));
  }

  @Test
  public void updateVehicle_WhenVehicleCardIdIsNull_ReturnVehicleResponse() {
    VehicleRequest request = vehicleRequest();
    Vehicle vehicle = new Vehicle();
    setVehicle(vehicle);

    Vehicle savedVehicle = map(request, null);

    Mockito.when(repository.findById(vehicle.getId())).thenReturn(Optional.of(vehicle));
    Mockito.when(repository.save(vehicle)).thenReturn(savedVehicle);
    Mockito.when(repository.existsByLicensePlate(request.getLicensePlate())).thenReturn(false);

    VehicleResponse vehicleResponse = vehicleService.update(vehicle.getId(), request, null);
    check(vehicle, vehicleResponse);
  }

  @Test
  public void updateVehicle_WhenBrandIdIsNull_ReturnVehicleResponse() {
    Long vehicleCardId = 2L;
    VehicleRequest request = new VehicleRequest(
          null,
          2L,
          "29X5-12345");

    Vehicle vehicle = new Vehicle();
    setVehicle(vehicle);

    Vehicle savedVehicle = map(request, vehicleCardId);

    Mockito.when(repository.findById(vehicle.getId())).thenReturn(Optional.of(vehicle));
    Mockito.when(repository.save(vehicle)).thenReturn(savedVehicle);
    Mockito.when(repository.existsByLicensePlate(request.getLicensePlate())).thenReturn(false);

    VehicleResponse vehicleResponse = vehicleService.update(vehicle.getId(), request, vehicleCardId);
    check(vehicle, vehicleResponse);
  }

  @Test
  public void updateVehicle_WhenTypeIdIsNull_ReturnVehicleResponse() {
    Long vehicleCardId = 2L;
    VehicleRequest request = new VehicleRequest(
          2L,
          null,
          "29X5-12345");

    Vehicle vehicle = new Vehicle();
    setVehicle(vehicle);

    Vehicle savedVehicle = map(request, vehicleCardId);

    Mockito.when(repository.findById(vehicle.getId())).thenReturn(Optional.of(vehicle));
    Mockito.when(repository.save(vehicle)).thenReturn(savedVehicle);
    Mockito.when(repository.existsByLicensePlate(request.getLicensePlate())).thenReturn(false);

    VehicleResponse vehicleResponse = vehicleService.update(vehicle.getId(), request, vehicleCardId);
    check(vehicle, vehicleResponse);
  }

  @Test
  public void updateVehicle_WhenLicensePlateIsNull_ReturnVehicleResponse() {
    Long vehicleCardId = 2L;
    VehicleRequest request = new VehicleRequest(
          2L,
          2L,
          null);

    Vehicle vehicle = new Vehicle();
    setVehicle(vehicle);

    Vehicle savedVehicle = map(request, vehicleCardId);

    Mockito.when(repository.findById(vehicle.getId())).thenReturn(Optional.of(vehicle));
    Mockito.when(repository.save(vehicle)).thenReturn(savedVehicle);
    Mockito.when(repository.existsByLicensePlate(request.getLicensePlate())).thenReturn(false);

    VehicleResponse vehicleResponse = vehicleService.update(vehicle.getId(), request, vehicleCardId);
    check(vehicle, vehicleResponse);
  }


  private VehicleLicensePlateResponse getResponse(){
    VehicleLicensePlateResponse response = new VehicleLicensePlateResponse();
    response.setId(1L);
    response.setPersonId(1L);
    response.setLicensePlate("29x5-12345");
    return response;
  }
  @Test
  public void listLicensePlate_WhenInputValid_ReturnVehicleLicensePlatePageResponse(){
    String keyword = "29x5";
    List<VehicleLicensePlateResponse> vehicleLicensePlateResponses = Arrays.asList(getResponse());
    Mockito.when(repository.searchLicensePlate(keyword, pageable)).thenReturn(vehicleLicensePlateResponses);
    Mockito.when(repository.countSearchLicensePlate(keyword)).thenReturn(1L);

    VehicleLicensePlatePageResponse response = vehicleService.listLicensePlate(keyword, size, page, false);

    assertEquals(vehicleLicensePlateResponses, response.getLicensePlates());
    assertEquals(vehicleLicensePlateResponses.size(), response.getNumberOfLicensePlate());
  }

  @Test
  public void listLicensePlate_WhenIsAllTrue_ReturnVehicleLicensePlatePageResponse(){
    String keyword = "29x5";
    List<VehicleLicensePlateResponse> vehicleLicensePlateResponses = Arrays.asList(getResponse());
    Mockito.when(repository.getAllLicensePlate()).thenReturn(vehicleLicensePlateResponses);
    Mockito.when(repository.count()).thenReturn(1L);

    VehicleLicensePlatePageResponse response = vehicleService.listLicensePlate(keyword, size, page, true);

    assertEquals(vehicleLicensePlateResponses, response.getLicensePlates());
    assertEquals(vehicleLicensePlateResponses.size(), response.getNumberOfLicensePlate());
  }

}