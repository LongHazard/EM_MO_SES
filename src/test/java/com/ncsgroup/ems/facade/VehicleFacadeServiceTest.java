package com.ncsgroup.ems.facade;

import com.ncsgroup.ems.configuration.ServiceConfigurationTest;
import com.ncsgroup.ems.dto.request.image.ImageRequest;
import com.ncsgroup.ems.dto.request.vehicle.VehicleFacadeRequest;
import com.ncsgroup.ems.dto.request.vehicle.VehicleFilter;
import com.ncsgroup.ems.dto.request.vehicle.VehicleRequest;
import com.ncsgroup.ems.dto.request.vehicle.card.VehicleCardRequest;
import com.ncsgroup.ems.dto.response.cardtype.CardTypeResponse;
import com.ncsgroup.ems.dto.response.color.ColorResponse;
import com.ncsgroup.ems.dto.response.group.VehicleGroupResponse;
import com.ncsgroup.ems.dto.response.image.ImageResponse;
import com.ncsgroup.ems.dto.response.vehicle.VehicleDetail;
import com.ncsgroup.ems.dto.response.vehicle.VehicleFacadeResponse;
import com.ncsgroup.ems.dto.response.vehicle.VehiclePageResponse;
import com.ncsgroup.ems.dto.response.vehicle.VehicleResponse;
import com.ncsgroup.ems.dto.response.vehicle.brand.VehicleBrandResponse;
import com.ncsgroup.ems.dto.response.vehicle.type.VehicleTypeResponseDTO;
import com.ncsgroup.ems.dto.response.vehicle.vehiclecard.VehicleCardResponse;
import com.ncsgroup.ems.entity.person.Person;
import com.ncsgroup.ems.entity.vehicle.Color;
import com.ncsgroup.ems.service.identity.GroupService;
import com.ncsgroup.ems.service.identity.IdentityGroupObjectService;
import com.ncsgroup.ems.service.identity.IdentityObjectService;
import com.ncsgroup.ems.service.identity.ImageService;
import com.ncsgroup.ems.service.person.CardTypeService;
import com.ncsgroup.ems.service.person.PersonService;
import com.ncsgroup.ems.service.vehicle.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;

@WebMvcTest(VehicleFacadeService.class)
@ContextConfiguration(classes = {ServiceConfigurationTest.class})
public class VehicleFacadeServiceTest {
  @Autowired
  VehicleFacadeService vehicleFacadeService;
  @MockBean
  VehicleService vehicleService;
  @MockBean
  VehicleBrandService vehicleBrandService;
  @MockBean
  VehicleTypeService vehicleTypeService;
  @MockBean
  VehicleCardFacadeService vehicleCardFacadeService;
  @MockBean
  ImageService imageService;
  @MockBean
  ColorService colorService;
  @MockBean
  IdentityObjectService identityObjectService;
  @MockBean
  PersonService personService;
  @MockBean
  CardTypeService cardTypeService;
  @MockBean
  IdentityGroupObjectService identityGroupObjectService;
  @MockBean
  VehicleColorService vehicleColorService;
  @MockBean
  GroupService groupService;

  List<Long> colorIds = Arrays.asList(1l);

  private Color getColor() {
    return new Color(1l, "red");
  }

  List<String> nameColors = Arrays.asList("red");
  List<Long> groupIds = Arrays.asList(1l);
  List<ImageRequest> imageRequests = Arrays.asList(new ImageRequest(1l, "front"));

  List<ImageResponse> imageResponses = Arrays.asList(new ImageResponse(1l, "http:image1", "", "front"));

  List<ColorResponse> colorResponses = Arrays.asList(new ColorResponse(1l, "red"));

  /**
   * REQUEST
   */

  List<VehicleGroupResponse> groupResponses = Arrays.asList(new VehicleGroupResponse(1l, "Ha Noi"));

  private VehicleRequest getVehicleRequest() {
    return new VehicleRequest(
          1l,
          1l,
          "29x5-12345",
          imageRequests,
          colorIds
    );
  }

  private VehicleCardRequest getVehicleCardRequest() {
    return new VehicleCardRequest(
          1l,
          1l,
          "123",
          1l,
          1l,
          "12/12/2022",
          "29x4-12345",
          null,
          null,
          colorIds
    );
  }

  private VehicleFacadeRequest getVehicleFacadeRequest() {
    return new VehicleFacadeRequest(
          "c7e9a52b-55c4-4bb7-92e2-23213a063a2c",
          getVehicleRequest(),
          getVehicleCardRequest(),
          groupIds
    );
  }

  /**
   * RESPONSE
   */

  private Person getPerson() {
    return new Person(
          "Long",
          "c7e9a52b-55c4-4bb7-92e2-23213a063a2c",
          "nguyenlongvan@gmail.com",
          "men",
          "0399166551",
          null,
          null,
          null,
          null,
          null,
          false
    );
  }

  private VehicleResponse getVehicleResponse() {
    return new VehicleResponse(
          1l,
          1l,
          1l,
          "29x5-12345",
          1l,
          "Long",
          nameColors,
          imageResponses
    );
  }

  static VehicleBrandResponse vehicleBrandResponse = new VehicleBrandResponse(
        1l,
        "lamborghini"
  );

  static VehicleTypeResponseDTO vehicleTypeResponseDTO = new VehicleTypeResponseDTO(
        1l,
        "motor"
  );

  static CardTypeResponse cardTypeResponse = new CardTypeResponse(
        1l,
        "cccd",
        "citizen's identity card",
        "chung minh nhan dan"
  );

  private VehicleCardResponse getVehicleCardResponse() {
    VehicleCardResponse vehicleCardResponse = new VehicleCardResponse();
    vehicleCardResponse.setId(1l);
    vehicleCardResponse.setCardType(cardTypeResponse);
    vehicleCardResponse.setCardCode("123");
    vehicleCardResponse.setVehicleBrand(vehicleBrandResponse);
    vehicleCardResponse.setVehicleType(vehicleTypeResponseDTO);
    vehicleCardResponse.setRegistrationDate(56545454L);
    vehicleCardResponse.setLicensePlate("29x4-12345");
    vehicleCardResponse.setPermanentResident(null);
    vehicleCardResponse.setImages(null);
    vehicleCardResponse.setColors(colorResponses);
    return vehicleCardResponse;
  }

  private VehicleFacadeResponse getVehicleFacadeResponse() {
    VehicleFacadeResponse vehicleFacadeResponse = new VehicleFacadeResponse();
    vehicleFacadeResponse.setUidPerson("c7e9a52b-55c4-4bb7-92e2-23213a063a2c");
    vehicleFacadeResponse.setNamePerson("Long");
    vehicleFacadeResponse.setVehicle(getVehicleResponse());
    vehicleFacadeResponse.setVehicleCard(getVehicleCardResponse());
    vehicleFacadeResponse.setGroups(groupResponses);

    return vehicleFacadeResponse;
  }

  private void compareResponseAndRequest(VehicleFacadeRequest request,
                                         VehicleFacadeResponse response) {
    assertEquals(request.getPersonUid(), response.getUidPerson());
    assertEquals(request.getVehicle().getBrandId(), response.getVehicle().getBrandId());
    assertEquals(request.getVehicle().getTypeId(), response.getVehicle().getTypeId());
    assertEquals(request.getVehicle().getLicensePlate(), response.getVehicle().getLicensePlate());
    assertEquals(request.getVehicleCard().getId(), response.getVehicleCard().getId());
    assertEquals(request.getVehicleCard().getVehicleTypeId(), response.getVehicleCard().getVehicleType().getId());
    assertEquals(request.getVehicleCard().getBrandId(), response.getVehicleCard().getVehicleBrand().getId());
    assertEquals(request.getVehicleCard().getCardCode(), response.getVehicleCard().getCardCode());
    assertEquals(request.getGroupIds().get(0), response.getGroups().get(0).getId());
    assertEquals(request.getVehicle().getImages().get(0).getId(), request.getVehicle().getImages().get(0).getId());
    assertEquals(request.getVehicle().getImages().get(0).getType(), request.getVehicle().getImages().get(0).getType());
//    if (request.getPermanentResident() != null){
//      Assertions.assertEquals(request.getPermanentResident().getProvinceCode(),
//            vehicleCardResponse.getPermanentResident().getProvinces().getProvinceCodeName());
//      Assertions.assertEquals(request.getPermanentResident().getDistrictCode(),
//            vehicleCardResponse.getPermanentResident().getDistricts().getDistrictCodeName());
//      Assertions.assertEquals(request.getPermanentResident().getWardCode(),
//            vehicleCardResponse.getPermanentResident().getWards().getWardCodeName());
//    }
//    Assertions.assertEquals(request.getImages().size(), vehicleCardResponse.getImages().size());
//    if (request.getColorIds() != null){
//      Assertions.assertEquals(request.getColorIds().size(), vehicleCardResponse.getColors().size());
//    }
  }


  @Test
  public void createVehicle_WhenInputValid_ReturnVehicleFacadeResponse() {
    VehicleFacadeRequest vehicleFacadeRequest = getVehicleFacadeRequest();
    VehicleFacadeResponse vehicleFacadeResponse = getVehicleFacadeResponse();

    Mockito.when(vehicleCardFacadeService.create(vehicleFacadeRequest.getVehicleCard())).thenReturn(vehicleFacadeResponse.getVehicleCard());
    Mockito.when(vehicleService.create(
          vehicleFacadeRequest.getVehicle().getBrandId(),
          vehicleFacadeRequest.getVehicle().getTypeId(),
          vehicleFacadeRequest.getVehicle().getLicensePlate(),
          vehicleFacadeRequest.getVehicleCard().getId(),
          vehicleFacadeRequest.getVehicle().getColorIds()
    )).thenReturn(vehicleFacadeResponse.getVehicle());

    Mockito.when(imageService.getByRequests(vehicleFacadeRequest.getVehicle().getImages())).thenReturn(vehicleFacadeResponse.getVehicle().getImages());
    Mockito.when(colorService.findNameById(vehicleFacadeRequest.getVehicle().getColorIds())).thenReturn(vehicleFacadeResponse.getVehicle().getColors());
    Mockito.when(personService.findIdByUid(vehicleFacadeRequest.getPersonUid())).thenReturn(1l);
    Mockito.when(personService.findNameById(1l)).thenReturn(vehicleFacadeResponse.getNamePerson());
    Mockito.when(groupService.addGroupToVehicle(vehicleFacadeResponse.getVehicle().getId(), groupIds)).thenReturn(vehicleFacadeResponse.getGroups());

    VehicleFacadeResponse response = vehicleFacadeService.createVehicle(vehicleFacadeRequest);
    compareResponseAndRequest(vehicleFacadeRequest, response);

  }

  @Test
  public void createVehicle_WhenVehicleCardIsNull_ReturnVehicleFacadeResponse() {
    VehicleFacadeRequest request = new VehicleFacadeRequest(
          "c7e9a52b-55c4-4bb7-92e2-23213a063a2c",
          getVehicleRequest(),
          null,
          groupIds
    );
    VehicleFacadeResponse vehicleFacadeResponse = new VehicleFacadeResponse();
    vehicleFacadeResponse.setUidPerson("c7e9a52b-55c4-4bb7-92e2-23213a063a2c");
    vehicleFacadeResponse.setNamePerson("Long");
    vehicleFacadeResponse.setVehicle(getVehicleResponse());
    vehicleFacadeResponse.setVehicleCard(null);
    vehicleFacadeResponse.setGroups(groupResponses);

    Mockito.when(vehicleService.create(
          request.getVehicle().getBrandId(),
          request.getVehicle().getTypeId(),
          request.getVehicle().getLicensePlate(),
          null,
          request.getVehicle().getColorIds()
    )).thenReturn(vehicleFacadeResponse.getVehicle());

    Mockito.when(imageService.getByRequests(request.getVehicle().getImages())).thenReturn(vehicleFacadeResponse.getVehicle().getImages());
    Mockito.when(colorService.findNameById(request.getVehicle().getColorIds())).thenReturn(vehicleFacadeResponse.getVehicle().getColors());
    Mockito.when(personService.findIdByUid(request.getPersonUid())).thenReturn(1l);
    Mockito.when(personService.findNameById(1l)).thenReturn(vehicleFacadeResponse.getNamePerson());
    Mockito.when(groupService.addGroupToVehicle(vehicleFacadeResponse.getVehicle().getId(), groupIds)).thenReturn(vehicleFacadeResponse.getGroups());

    VehicleFacadeResponse response = vehicleFacadeService.createVehicle(request);
    assertEquals(request.getPersonUid(), response.getUidPerson());
    assertEquals(request.getVehicle().getBrandId(), response.getVehicle().getBrandId());
    assertEquals(request.getVehicle().getTypeId(), response.getVehicle().getTypeId());
    assertEquals(request.getVehicle().getLicensePlate(), response.getVehicle().getLicensePlate());
  }

  @Test
  public void createVehicle_WhenUidPersonIsNull_ReturnVehicleFacadeResponse() {
    VehicleFacadeRequest vehicleFacadeRequest = new VehicleFacadeRequest(
          null,
          getVehicleRequest(),
          getVehicleCardRequest(),
          groupIds
    );
    VehicleFacadeResponse vehicleFacadeResponse = new VehicleFacadeResponse();
    vehicleFacadeResponse.setUidPerson(null);
    vehicleFacadeResponse.setNamePerson(null);
    vehicleFacadeResponse.setVehicle(getVehicleResponse());
    vehicleFacadeResponse.setVehicleCard(getVehicleCardResponse());
    vehicleFacadeResponse.setGroups(groupResponses);

    Mockito.when(vehicleCardFacadeService.create(vehicleFacadeRequest.getVehicleCard())).thenReturn(vehicleFacadeResponse.getVehicleCard());
    Mockito.when(vehicleService.create(
          vehicleFacadeRequest.getVehicle().getBrandId(),
          vehicleFacadeRequest.getVehicle().getTypeId(),
          vehicleFacadeRequest.getVehicle().getLicensePlate(),
          vehicleFacadeRequest.getVehicleCard().getId(),
          vehicleFacadeRequest.getVehicle().getColorIds()
    )).thenReturn(vehicleFacadeResponse.getVehicle());

    Mockito.when(imageService.getByRequests(vehicleFacadeRequest.getVehicle().getImages())).thenReturn(vehicleFacadeResponse.getVehicle().getImages());
    Mockito.when(colorService.findNameById(vehicleFacadeRequest.getVehicle().getColorIds())).thenReturn(vehicleFacadeResponse.getVehicle().getColors());
    Mockito.when(personService.findIdByUid(vehicleFacadeRequest.getPersonUid())).thenReturn(null);
    Mockito.when(personService.findNameById(null)).thenReturn(null);
    Mockito.when(groupService.addGroupToVehicle(vehicleFacadeResponse.getVehicle().getId(), groupIds)).thenReturn(vehicleFacadeResponse.getGroups());

    VehicleFacadeResponse vehicleFacadeResponse1 = vehicleFacadeService.createVehicle(vehicleFacadeRequest);
    compareResponseAndRequest(vehicleFacadeRequest, vehicleFacadeResponse1);
  }

  @Test
  public void createVehicle_WhenGroupIdsIsNull_ReturnVehicleFacadeResponse() {
    VehicleFacadeRequest request = new VehicleFacadeRequest(
          "c7e9a52b-55c4-4bb7-92e2-23213a063a2c",
          getVehicleRequest(),
          getVehicleCardRequest(),
          null
    );

    VehicleFacadeResponse vehicleFacadeResponse = new VehicleFacadeResponse();
    vehicleFacadeResponse.setUidPerson("c7e9a52b-55c4-4bb7-92e2-23213a063a2c");
    vehicleFacadeResponse.setNamePerson("Long");
    vehicleFacadeResponse.setVehicle(getVehicleResponse());
    vehicleFacadeResponse.setVehicleCard(getVehicleCardResponse());
    vehicleFacadeResponse.setGroups(null);

    Mockito.when(vehicleCardFacadeService.create(request.getVehicleCard())).thenReturn(vehicleFacadeResponse.getVehicleCard());
    Mockito.when(vehicleService.create(
          request.getVehicle().getBrandId(),
          request.getVehicle().getTypeId(),
          request.getVehicle().getLicensePlate(),
          request.getVehicleCard().getId(),
          request.getVehicle().getColorIds()
    )).thenReturn(vehicleFacadeResponse.getVehicle());

    Mockito.when(imageService.getByRequests(request.getVehicle().getImages())).thenReturn(vehicleFacadeResponse.getVehicle().getImages());
    Mockito.when(colorService.findNameById(request.getVehicle().getColorIds())).thenReturn(vehicleFacadeResponse.getVehicle().getColors());
    Mockito.when(personService.findIdByUid(request.getPersonUid())).thenReturn(1l);
    Mockito.when(personService.findNameById(1l)).thenReturn(vehicleFacadeResponse.getNamePerson());
    Mockito.when(groupService.addGroupToVehicle(vehicleFacadeResponse.getVehicle().getId(), groupIds)).thenReturn(vehicleFacadeResponse.getGroups());

    VehicleFacadeResponse response = vehicleFacadeService.createVehicle(request);
    assertEquals(request.getVehicle().getBrandId(), response.getVehicle().getBrandId());
    assertEquals(request.getVehicle().getTypeId(), response.getVehicle().getTypeId());
    assertEquals(request.getVehicle().getLicensePlate(), response.getVehicle().getLicensePlate());
    assertEquals(request.getVehicleCard().getId(), response.getVehicleCard().getId());
    assertEquals(request.getVehicleCard().getVehicleTypeId(), response.getVehicleCard().getVehicleType().getId());
    assertEquals(request.getVehicleCard().getBrandId(), response.getVehicleCard().getVehicleBrand().getId());
    assertEquals(request.getVehicleCard().getCardCode(), response.getVehicleCard().getCardCode());
    assertEquals(request.getVehicle().getImages().get(0).getId(), request.getVehicle().getImages().get(0).getId());
    assertEquals(request.getVehicle().getImages().get(0).getType(), request.getVehicle().getImages().get(0).getType());
  }


  @Test
  public void update_WhenInputIsValid_ReturnVehicleFacadeResponse() {
    VehicleFacadeRequest mockVehicleFacadeRequest = this.mockVehicleFacadeRequest();

    Mockito.when(
                vehicleCardFacadeService.update(
                      Mockito.anyLong(),
                      Mockito.any(VehicleCardRequest.class)
                )
          )
          .thenReturn(this.mockVehicleCardResponse());

    Mockito.when(
                vehicleService.update(
                      Mockito.anyLong(),
                      Mockito.any(VehicleRequest.class),
                      Mockito.anyLong()
                )
          )
          .thenReturn(this.mockVehicleResponse());


    Mockito.when(colorService.findNameById(Mockito.anyList()))
          .thenReturn(this.nameColors);

    Mockito.when(personService.findIdByUid(Mockito.any()))
          .thenReturn(this.mockId());

    Mockito.when(personService.findNameById(Mockito.any()))
          .thenReturn(this.mockPersonName());

    Mockito.when(groupService.addGroupToVehicle(Mockito.anyLong(), Mockito.anyList()))
          .thenReturn(this.mockVehicleGroupResponses());

    Mockito.when(imageService.getByRequests(Mockito.anyList()))
          .thenReturn(this.mockImageResponses());

    VehicleFacadeResponse vehicleFacadeResponse = vehicleFacadeService.update(
          this.mockId(),
          mockVehicleFacadeRequest
    );

    Mockito.verify(identityObjectService, times(1)).update(Mockito.anyLong(), Mockito.anyLong());
    Mockito.verify(vehicleColorService, times(1)).update(Mockito.anyLong(), Mockito.anyList());
    Mockito.verify(imageService, times(1)).update(Mockito.anyList(), Mockito.anyList());
    Mockito.verify(imageService, times(1)).addImageToVehicle(Mockito.anyList(), Mockito.anyLong());

    this.check(
          vehicleFacadeResponse,
          mockVehicleFacadeRequest,
          this.mockPersonUid(),
          this.mockPersonName(),
          this.mockVehicleResponse(),
          this.mockVehicleCardResponse(),
          this.mockVehicleGroupResponses()
    );


  }

  private VehicleFacadeRequest mockVehicleFacadeRequest() {
    return new VehicleFacadeRequest(
          this.mockPersonUid(),
          this.mockVehicleRequest(),
          this.mockVehicleCardRequest(),
          this.mockGroupIds()
    );
  }

  private Long mockId() {
    return 1L;
  }

  private String mockPersonUid() {
    return "person-uid";
  }

  private String mockPersonName() {
    return "person-name";
  }

  private VehicleRequest mockVehicleRequest() {
    return new VehicleRequest(
          1l,
          1l,
          "29x5-12345",
          imageRequests,
          colorIds
    );
  }

  private VehicleCardRequest mockVehicleCardRequest() {
    return new VehicleCardRequest(
          1l,
          1l,
          "123",
          1l,
          1l,
          "12/12/2022",
          "29x4-12345",
          null,
          null,
          colorIds
    );
  }

  private List<Long> mockGroupIds() {
    return List.of(1l, 2L);
  }

  private VehicleCardResponse mockVehicleCardResponse() {
    VehicleCardResponse vehicleCardResponse = new VehicleCardResponse();

    this.setProperties(vehicleCardResponse);

    return vehicleCardResponse;
  }

  private void setProperties(VehicleCardResponse vehicleCardResponse) {
    vehicleCardResponse.setId(1l);
    vehicleCardResponse.setCardType(cardTypeResponse);
    vehicleCardResponse.setCardCode("123");
    vehicleCardResponse.setVehicleBrand(vehicleBrandResponse);
    vehicleCardResponse.setVehicleType(vehicleTypeResponseDTO);
    vehicleCardResponse.setRegistrationDate(56545454L);
    vehicleCardResponse.setLicensePlate("29x4-12345");
    vehicleCardResponse.setPermanentResident(null);
    vehicleCardResponse.setImages(null);
    vehicleCardResponse.setColors(colorResponses);
  }

  private VehicleFacadeResponse mockVehicleFacadeResponse() {
    VehicleFacadeResponse vehicleFacadeResponse = new VehicleFacadeResponse();
    vehicleFacadeResponse.setUidPerson("c7e9a52b-55c4-4bb7-92e2-23213a063a2c");
    vehicleFacadeResponse.setNamePerson("Long");
    vehicleFacadeResponse.setVehicle(getVehicleResponse());
    vehicleFacadeResponse.setVehicleCard(getVehicleCardResponse());
    vehicleFacadeResponse.setGroups(null);
    return vehicleFacadeResponse;
  }


  /**
   * LIST
   */
  Integer page = 0;
  Integer size = 10;

  VehicleDetail vehicleDetail = new VehicleDetail(
        1l,
        "29x5-12345",
        "car",
        "audi",
        "Long",
        nameColors,
        imageResponses
  );

  private VehicleResponse mockVehicleResponse() {
    return new VehicleResponse(
          1l,
          1l,
          1l,
          "29x5-12345",
          1l,
          "Long",
          nameColors,
          imageResponses
    );
  }


  List<VehicleDetail> vehicleDetails = Arrays.asList(vehicleDetail);

  List<Long> vehicleTypeIds = Arrays.asList(1l);

  List<String> licensePlates = Arrays.asList("29x5-12345");


  @Test
  public void list_WhenInputValid_ReturnVehiclePageResponse() {
    VehiclePageResponse vehiclePageResponse = VehiclePageResponse.of(vehicleDetails, 1);
    VehicleFilter vehicleFilter = VehicleFilter.of("29x5", vehicleTypeIds, licensePlates);

    Mockito.when(vehicleService.list(vehicleFilter, page, size, false)).thenReturn(vehiclePageResponse);
    Mockito.when(colorService.findName(vehiclePageResponse.getVehicleDetails().get(0).getId())).thenReturn(
          vehiclePageResponse.getVehicleDetails().get(0).getNameColor());
    Mockito.when(imageService.getByVehicleId(vehiclePageResponse.getVehicleDetails().get(0).getId())).thenReturn(
          vehiclePageResponse.getVehicleDetails().get(0).getImages().get(0)
    );
    VehiclePageResponse response = vehicleFacadeService.list(vehicleFilter, page, size, false);
    assertEquals(vehiclePageResponse.getVehicleDetails().get(0).getNameColor().get(0),
          response.getVehicleDetails().get(0).getNameColor().get(0));
    assertEquals(vehiclePageResponse.getVehicleDetails().get(0).getImages().get(0).getId(),
          response.getVehicleDetails().get(0).getImages().get(0).getId());
    assertEquals(vehiclePageResponse.getVehicleDetails().get(0).getImages().get(0).getType(),
          response.getVehicleDetails().get(0).getImages().get(0).getType());
  }


  /**
   * DETAIL
   **/

  @Test
  public void detail_WhenInputValid_ReturnVehicleFacadeResponse() {
    VehicleResponse vehicleResponse = getVehicleResponse();
    VehicleCardResponse vehicleCardResponse = getVehicleCardResponse();
    Person person = getPerson();
    VehicleFacadeResponse vehicleFacadeRequest = getVehicleFacadeResponse();
    List<VehicleGroupResponse> vehicleGroupResponses = Arrays.asList(new VehicleGroupResponse(1l, "Ha Noi"));

    VehicleFacadeResponse vehicleFacadeResponse = new VehicleFacadeResponse();
    vehicleFacadeResponse.setUidPerson("c7e9a52b-55c4-4bb7-92e2-23213a063a2c");
    vehicleFacadeResponse.setNamePerson("Long");
    vehicleFacadeResponse.setVehicle(vehicleResponse);
    vehicleFacadeResponse.setVehicleCard(vehicleCardResponse);
    vehicleFacadeResponse.setGroups(vehicleGroupResponses);


    Mockito.when(vehicleService.detail(1l)).thenReturn(vehicleResponse);
    Mockito.when(imageService.getAllByVehicle(vehicleResponse.getId())).thenReturn(vehicleResponse.getImages());
    Mockito.when(colorService.findByVehicleId(vehicleResponse.getId())).thenReturn(vehicleResponse.getColors());
    Mockito.when(vehicleCardFacadeService.findOrDefault(vehicleResponse.getVehicleCardId())).thenReturn(vehicleCardResponse);
    Mockito.when(personService.findByVehicleIdOrNull(vehicleResponse.getId())).thenReturn(person);
    Mockito.when(groupService.findByVehicleId(vehicleResponse.getId())).thenReturn(vehicleGroupResponses);

    VehicleFacadeResponse response = vehicleFacadeService.detail(1l);
    assertEquals(vehicleFacadeRequest.getUidPerson(), response.getUidPerson());
    assertEquals(vehicleFacadeRequest.getVehicle().getBrandId(), response.getVehicle().getBrandId());
    assertEquals(vehicleFacadeRequest.getVehicle().getTypeId(), response.getVehicle().getTypeId());
    assertEquals(vehicleFacadeRequest.getVehicle().getLicensePlate(), response.getVehicle().getLicensePlate());
    assertEquals(vehicleFacadeRequest.getVehicleCard().getId(), response.getVehicleCard().getId());
    assertEquals(vehicleFacadeRequest.getVehicleCard().getVehicleType().getId(), response.getVehicleCard().getVehicleType().getId());
    assertEquals(vehicleFacadeRequest.getVehicleCard().getVehicleBrand().getId(), response.getVehicleCard().getVehicleBrand().getId());
    assertEquals(vehicleFacadeRequest.getVehicleCard().getCardCode(), response.getVehicleCard().getCardCode());
    assertEquals(vehicleFacadeRequest.getGroups().get(0).getId(), response.getGroups().get(0).getId());
    assertEquals(vehicleFacadeRequest.getVehicle().getImages().get(0).getId(), response.getVehicle().getImages().get(0).getId());
    assertEquals(vehicleFacadeRequest.getVehicle().getImages().get(0).getType(), response.getVehicle().getImages().get(0).getType());
  }

  private List<ImageResponse> mockImageResponses() {
    return this.imageResponses;
  }

  private List<VehicleGroupResponse> mockVehicleGroupResponses() {
    return List.of(
          new VehicleGroupResponse(1L, "name1"),
          new VehicleGroupResponse(2L, "name2")
    );
  }

  private void check(
        VehicleFacadeResponse vehicleFacadeResponse,
        VehicleFacadeRequest mockVehicleFacadeRequest,
        String mockPersonUid,
        String mockPersonName,
        VehicleResponse mockVehicleResponse,
        VehicleCardResponse mockVehicleCardResponse,
        List<VehicleGroupResponse> mockVehicleGroupResponses
  ) {
    Assertions.assertEquals(vehicleFacadeResponse.getUidPerson(), mockPersonUid);
    Assertions.assertEquals(vehicleFacadeResponse.getNamePerson(), mockPersonName);
    Assertions.assertEquals(vehicleFacadeResponse.getVehicle(), mockVehicleResponse);
    Assertions.assertEquals(vehicleFacadeResponse.getVehicleCard(), mockVehicleCardResponse);
    Assertions.assertEquals(vehicleFacadeResponse.getGroups(), mockVehicleGroupResponses);

  }

  @Test
  public void getByPersonId_WhenInputValid_ReturnListVehicleFacadeResponse() {
    List<Long> vehicleIds = Arrays.asList(1l);
    Person person = getPerson();
    VehicleFacadeResponse vehicleFacadeResponse = getVehicleFacadeResponse();
    List<VehicleFacadeResponse> vehicleFacadeResponses = Arrays.asList(vehicleFacadeResponse);
    VehicleFacadeRequest request = getVehicleFacadeRequest();

    Mockito.when(vehicleService.getByPersonId(1l)).thenReturn(vehicleIds);
    Mockito.when(vehicleService.detail(1l)).thenReturn(vehicleFacadeResponses.get(0).getVehicle());
    Mockito.when(imageService.getAllByVehicle(1l)).thenReturn(vehicleFacadeResponses.get(0).getVehicle().getImages());
    Mockito.when(colorService.findByVehicleId(1l)).thenReturn(vehicleFacadeResponses.get(0).getVehicle().getColors());
    Mockito.when(vehicleCardFacadeService.findOrDefault(1l)).thenReturn(vehicleFacadeResponses.get(0).getVehicleCard());
    Mockito.when(personService.findByVehicleIdOrNull(1l)).thenReturn(person);
    Mockito.when(groupService.findByVehicleId(1l)).thenReturn(vehicleFacadeResponses.get(0).getGroups());

    List<VehicleFacadeResponse> responses = vehicleFacadeService.getByPersonId(1l);

    assertEquals(request.getPersonUid(), responses.get(0).getUidPerson());
    assertEquals(request.getVehicle().getBrandId(), responses.get(0).getVehicle().getBrandId());
    assertEquals(request.getVehicle().getTypeId(), responses.get(0).getVehicle().getTypeId());
    assertEquals(request.getVehicle().getLicensePlate(), responses.get(0).getVehicle().getLicensePlate());
    assertEquals(request.getVehicleCard().getId(), responses.get(0).getVehicleCard().getId());
    assertEquals(request.getVehicleCard().getVehicleTypeId(), responses.get(0).getVehicleCard().getVehicleType().getId());
    assertEquals(request.getVehicleCard().getBrandId(), responses.get(0).getVehicleCard().getVehicleBrand().getId());
    assertEquals(request.getVehicleCard().getCardCode(), responses.get(0).getVehicleCard().getCardCode());
    assertEquals(request.getGroupIds().get(0), responses.get(0).getGroups().get(0).getId());
    assertEquals(request.getVehicle().getImages().get(0).getId(), responses.get(0).getVehicle().getImages().get(0).getId());
    assertEquals(request.getVehicle().getImages().get(0).getType(), responses.get(0).getVehicle().getImages().get(0).getType());

  }



}
