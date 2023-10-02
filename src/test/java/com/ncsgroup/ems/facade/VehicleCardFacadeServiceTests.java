package com.ncsgroup.ems.facade;

import com.ncsgroup.ems.configuration.ServiceConfigurationTest;
import com.ncsgroup.ems.dto.request.address.AddressOfVehicleRequest;
import com.ncsgroup.ems.dto.request.image.ImageRequest;
import com.ncsgroup.ems.dto.request.vehicle.card.VehicleCardRequest;
import com.ncsgroup.ems.dto.response.address.AddressResponse;
import com.ncsgroup.ems.dto.response.cardtype.CardTypeResponse;
import com.ncsgroup.ems.dto.response.color.ColorResponse;
import com.ncsgroup.ems.dto.response.image.ImageResponse;
import com.ncsgroup.ems.dto.response.vehicle.brand.VehicleBrandResponse;
import com.ncsgroup.ems.dto.response.vehicle.type.VehicleTypeResponseDTO;
import com.ncsgroup.ems.dto.response.vehicle.vehiclecard.VehicleCardPageResponse;
import com.ncsgroup.ems.dto.response.vehicle.vehiclecard.VehicleCardResponse;
import com.ncsgroup.ems.entity.address.Address;
import com.ncsgroup.ems.entity.vehicle.Color;
import com.ncsgroup.ems.entity.vehicle.VehicleCard;
import com.ncsgroup.ems.exception.color.ColorNotFoundException;
import com.ncsgroup.ems.exception.vehicle.card.VehicleCardNotFoundException;
import com.ncsgroup.ems.service.address.AddressService;
import com.ncsgroup.ems.service.identity.ImageService;
import com.ncsgroup.ems.service.person.CardTypeService;
import com.ncsgroup.ems.service.vehicle.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@WebMvcTest(VehicleCardFacadeService.class)
@ContextConfiguration(classes = {
      ServiceConfigurationTest.class
})
public class VehicleCardFacadeServiceTests {
  @Autowired
  VehicleCardFacadeService service;

  @MockBean
  VehicleCardService vehicleCardService;

  @MockBean
  CardTypeService cardTypeService;

  @MockBean
  VehicleBrandService vehicleBrandService;

  @MockBean
  VehicleTypeService vehicleTypeService;

  @MockBean
  ColorService colorService;

  @MockBean
  AddressService addressService;

  @MockBean
  ImageService imageService;

  @MockBean
  VehicleCardColorService vehicleCardColorService;

  //Fake Request
  static AddressOfVehicleRequest permanentResident = new AddressOfVehicleRequest(
        "01",
        "001",
        "000001",
        "Ba Dinh, Ha Noi",
        "origin"
  );

  static ImageRequest imageRequest1 = new ImageRequest(
        1L,
        "front"
  );

  static ImageRequest imageRequest2 = new ImageRequest(
        2L,
        "back"
  );

  static List<ImageRequest> imageRequests = Arrays.asList(
        imageRequest1,
        imageRequest2
  );

  static List<Long> colorIds = Arrays.asList(
        1L,
        2L
  );

  static List<Color> colors = Arrays.asList(
        new Color(1L, "black"),
        new Color(2L, "white")
  );

  VehicleCardRequest getVehicleCardRequest() {
    VehicleCardRequest vehicleCardRequest = new VehicleCardRequest();
    vehicleCardRequest.setCardTypeId(1L);
    vehicleCardRequest.setCardCode("005");
    vehicleCardRequest.setBrandId(1L);
    vehicleCardRequest.setVehicleTypeId(1L);
    vehicleCardRequest.setRegistrationDate("21/06/2023");
    vehicleCardRequest.setLicensePlate("18K1-12345");
    vehicleCardRequest.setPermanentResident(permanentResident);
    vehicleCardRequest.setImages(imageRequests);
    vehicleCardRequest.setColorIds(colorIds);
    return vehicleCardRequest;
  }

  //Fake response
  static CardTypeResponse cardTypeResponse = new CardTypeResponse(
        1L,
        "cccd",
        "citizen's identity card",
        "chung minh nhan dan"
  );

  static VehicleBrandResponse vehicleBrandResponse = new VehicleBrandResponse(
        1L,
        "lamborghini"
  );

  static VehicleTypeResponseDTO vehicleTypeResponseDTO = new VehicleTypeResponseDTO(
        1L,
        "motor"
  );

  static List<ColorResponse> colorResponses = Arrays.asList(
        new ColorResponse(1L, "black"),
        new ColorResponse(2L, "white")
  );

  static List<ImageResponse> imageResponses = Arrays.asList(
        new ImageResponse(1L, "http:image1", "", "front"),
        new ImageResponse(2L, "http:image2", "", "back")
  );

  static AddressResponse addressResponse = new AddressResponse(
        "wardName",
        "wardNameEn",
        "000001",
        "districtName",
        "districtNameEn",
        "001",
        "provinceName",
        "provinceNameEn",
        "01"
  );

  static Address address = new Address(
        1L,
        "01",
        "001",
        "000001",
        "Ba Dinh, Ha Noi",
        null,
        "origin",
        false
  );

  static VehicleCard vehicleCard = new VehicleCard(
        1L,
        "005",
        1L,
        1L,
        1040058000000L,
        "18K1-12345",
        75L
  );

  VehicleCardResponse getVehicleCardResponse() {
    VehicleCardResponse vehicleCardResponse = new VehicleCardResponse();
    vehicleCardResponse.setId(1L);
    vehicleCardResponse.setCardCode("005");
    vehicleCardResponse.setRegistrationDate(56545454L);
    vehicleCardResponse.setLicensePlate("18K1-12345");
    return vehicleCardResponse;
  }

  VehicleCardResponse getVehicleCardUpdateResponse() {
    VehicleCardResponse vehicleCardResponse = new VehicleCardResponse();
    vehicleCardResponse.setId(1L);
    vehicleCardResponse.setCardCode("006");
    vehicleCardResponse.setRegistrationDate(56545454L);
    vehicleCardResponse.setLicensePlate("18K1-12345");
    return vehicleCardResponse;
  }

  VehicleCardResponse getVehicleCardResponse2() {
    VehicleCardResponse vehicleCardResponse = new VehicleCardResponse();
    vehicleCardResponse.setId(2L);
    vehicleCardResponse.setCardCode("006");
    vehicleCardResponse.setRegistrationDate(9989989898L);
    vehicleCardResponse.setLicensePlate("18K1-23456");
    return vehicleCardResponse;
  }

  VehicleCardResponse getVehicleCardResponse3() {
    VehicleCardResponse vehicleCardResponse = new VehicleCardResponse();
    vehicleCardResponse.setId(3L);
    vehicleCardResponse.setCardCode("007");
    vehicleCardResponse.setRegistrationDate(65454884L);
    vehicleCardResponse.setLicensePlate("18K1-34567");
    return vehicleCardResponse;
  }

  //Start create test
  @Test
  public void create_WhenInputColorIdsValidAndPermanentResidentNotNull_ReturnVehicleCardResponse() {
    VehicleCardRequest request = getVehicleCardRequest();
    VehicleCardResponse response = getVehicleCardResponse();

    when(cardTypeService.detail(request.getCardTypeId())).thenReturn(cardTypeResponse);
    when(vehicleBrandService.detail(request.getBrandId())).thenReturn(vehicleBrandResponse);
    when(vehicleTypeService.detail(request.getVehicleTypeId())).thenReturn(vehicleTypeResponseDTO);
    doNothing().when(colorService).checkNotFound(request.getColorIds());
    when(colorService.findByIds(colorIds)).thenReturn(colors);
    when(vehicleCardService.create(request)).thenReturn(response);
    when(imageService.getByRequests(request.getImages())).thenReturn(imageResponses);
    when(addressService.createOfVehicleCard(request.getPermanentResident())).thenReturn(address);
    when(addressService.detail(address.getId())).thenReturn(addressResponse);

    VehicleCardResponse vehicleCardResponse = service.create(request);

    compareResponseAndResponseMock(vehicleCardResponse);

    compareResponseAndRequest(vehicleCardResponse, request);

    verify(colorService, times(1)).checkNotFound(request.getColorIds());
  }

  @Test
  public void create_WhenInputColorIdsIsNullAndPermanentResidentNotNull_ReturnVehicleCardResponse() {
    VehicleCardRequest request = getVehicleCardRequest();
    request.setColorIds(null);
    VehicleCardResponse response = getVehicleCardResponse();

    when(cardTypeService.detail(request.getCardTypeId())).thenReturn(cardTypeResponse);
    when(vehicleBrandService.detail(request.getBrandId())).thenReturn(vehicleBrandResponse);
    when(vehicleTypeService.detail(request.getVehicleTypeId())).thenReturn(vehicleTypeResponseDTO);
    doNothing().when(colorService).checkNotFound(null);
    when(vehicleCardService.create(request)).thenReturn(response);
    when(imageService.getByRequests(request.getImages())).thenReturn(imageResponses);
    when(addressService.createOfVehicleCard(request.getPermanentResident())).thenReturn(address);
    when(addressService.detail(address.getId())).thenReturn(addressResponse);

    VehicleCardResponse vehicleCardResponse = service.create(request);

    compareResponseAndResponseMock(vehicleCardResponse);

    compareResponseAndRequest(vehicleCardResponse, request);
  }

  @Test
  public void create_WhenInputColorIdsIsEmptyAndPermanentResidentNotNull_ReturnVehicleCardResponse() {
    VehicleCardRequest request = getVehicleCardRequest();
    request.setColorIds(new ArrayList<>());
    VehicleCardResponse response = getVehicleCardResponse();

    when(cardTypeService.detail(request.getCardTypeId())).thenReturn(cardTypeResponse);
    when(vehicleBrandService.detail(request.getBrandId())).thenReturn(vehicleBrandResponse);
    when(vehicleTypeService.detail(request.getVehicleTypeId())).thenReturn(vehicleTypeResponseDTO);
    doNothing().when(colorService).checkNotFound(null);
    when(vehicleCardService.create(request)).thenReturn(response);
    when(imageService.getByRequests(request.getImages())).thenReturn(imageResponses);
    when(addressService.createOfVehicleCard(request.getPermanentResident())).thenReturn(address);
    when(addressService.detail(address.getId())).thenReturn(addressResponse);

    VehicleCardResponse vehicleCardResponse = service.create(request);

    compareResponseAndResponseMock(vehicleCardResponse);

    compareResponseAndRequest(vehicleCardResponse, request);
  }

  @Test
  public void create_WhenInputColorIdsNotFound_ReturnException() {
    VehicleCardRequest request = getVehicleCardRequest();

    when(cardTypeService.detail(request.getCardTypeId())).thenReturn(cardTypeResponse);
    when(vehicleBrandService.detail(request.getBrandId())).thenReturn(vehicleBrandResponse);
    when(vehicleTypeService.detail(request.getVehicleTypeId())).thenReturn(vehicleTypeResponseDTO);
    doThrow(ColorNotFoundException.class).when(colorService).checkNotFound(request.getColorIds());

    Assertions.assertThrows(ColorNotFoundException.class, () -> {
      service.create(request);
    });

    verify(colorService, times(1)).checkNotFound(request.getColorIds());
  }

  @Test
  public void create_WhenInputColorIdsValidAndPermanentResidentIsNull_ReturnVehicleCardResponse() {
    VehicleCardRequest request = getVehicleCardRequest();
    request.setPermanentResident(null);
    VehicleCardResponse response = getVehicleCardResponse();

    when(cardTypeService.detail(request.getCardTypeId())).thenReturn(cardTypeResponse);
    when(vehicleBrandService.detail(request.getBrandId())).thenReturn(vehicleBrandResponse);
    when(vehicleTypeService.detail(request.getVehicleTypeId())).thenReturn(vehicleTypeResponseDTO);
    doNothing().when(colorService).checkNotFound(request.getColorIds());
    when(colorService.findByIds(colorIds)).thenReturn(colors);
    when(vehicleCardService.create(request)).thenReturn(response);
    when(imageService.getByRequests(request.getImages())).thenReturn(imageResponses);
    when(addressService.createOfVehicleCard(request.getPermanentResident())).thenReturn(address);
    when(addressService.detail(address.getId())).thenReturn(addressResponse);

    VehicleCardResponse vehicleCardResponse = service.create(request);

    compareResponseAndResponseMock(vehicleCardResponse);

    compareResponseAndRequest(vehicleCardResponse, request);

    verify(colorService, times(1)).checkNotFound(request.getColorIds());
  }

  @Test
  public void create_WhenInputColorIdsIsNullAndPermanentResidentIsNull_ReturnVehicleCardResponse() {
    VehicleCardRequest request = getVehicleCardRequest();
    request.setColorIds(null);
    request.setPermanentResident(null);
    VehicleCardResponse response = getVehicleCardResponse();

    when(cardTypeService.detail(request.getCardTypeId())).thenReturn(cardTypeResponse);
    when(vehicleBrandService.detail(request.getBrandId())).thenReturn(vehicleBrandResponse);
    when(vehicleTypeService.detail(request.getVehicleTypeId())).thenReturn(vehicleTypeResponseDTO);
    doNothing().when(colorService).checkNotFound(null);
    when(vehicleCardService.create(request)).thenReturn(response);
    when(imageService.getByRequests(request.getImages())).thenReturn(imageResponses);
    when(addressService.createOfVehicleCard(request.getPermanentResident())).thenReturn(address);
    when(addressService.detail(address.getId())).thenReturn(addressResponse);

    VehicleCardResponse vehicleCardResponse = service.create(request);

    compareResponseAndResponseMock(vehicleCardResponse);

    compareResponseAndRequest(vehicleCardResponse, request);
  }

  @Test
  public void create_WhenInputColorIdsIsEmptyAndPermanentResidentIsNull_ReturnVehicleCardResponse() {
    VehicleCardRequest request = getVehicleCardRequest();
    request.setColorIds(new ArrayList<>());
    request.setPermanentResident(null);
    VehicleCardResponse response = getVehicleCardResponse();

    when(cardTypeService.detail(request.getCardTypeId())).thenReturn(cardTypeResponse);
    when(vehicleBrandService.detail(request.getBrandId())).thenReturn(vehicleBrandResponse);
    when(vehicleTypeService.detail(request.getVehicleTypeId())).thenReturn(vehicleTypeResponseDTO);
    doNothing().when(colorService).checkNotFound(null);
    when(vehicleCardService.create(request)).thenReturn(response);
    when(imageService.getByRequests(request.getImages())).thenReturn(imageResponses);
    when(addressService.createOfVehicleCard(request.getPermanentResident())).thenReturn(address);
    when(addressService.detail(address.getId())).thenReturn(addressResponse);

    VehicleCardResponse vehicleCardResponse = service.create(request);

    compareResponseAndResponseMock(vehicleCardResponse);

    compareResponseAndRequest(vehicleCardResponse, request);
  }
  //End create test

  //Start list test
  @Test
  public void list_WhenInputValid_ReturnVehicleCardPageResponse() {
    int size = 3;
    int page = 0;
    boolean isAll = false;
    Long count = 3L;

    List<VehicleCardResponse> vehicleCardResponses = Arrays.asList(
          new VehicleCardResponse(),
          new VehicleCardResponse(),
          new VehicleCardResponse()
    );

    when(cardTypeService.detail(anyLong())).thenReturn(new CardTypeResponse());
    when(vehicleBrandService.detail(anyLong())).thenReturn(new VehicleBrandResponse());
    when(vehicleTypeService.detail(anyLong())).thenReturn(new VehicleTypeResponseDTO());
    when(imageService.getAllByVehicleCard(anyLong())).thenReturn(new ArrayList<>());
    when(colorService.findByIds(anyList())).thenReturn(colors);

    VehicleCardPageResponse vehicleCardPageResponse = VehicleCardPageResponse.of(vehicleCardResponses, count);
    when(vehicleCardService.list(size, page, isAll)).thenReturn(vehicleCardPageResponse);

    VehicleCardPageResponse response = service.list(size, page, isAll);

    Assertions.assertEquals(vehicleCardPageResponse, response);
  }

  @Test
  public void list_WhenAllIsTrue_ReturnVehicleCardPageResponse() {
    int size = 3;
    int page = 0;
    boolean isAll = true;
    Long count = 2L;

    List<VehicleCardResponse> vehicleCardResponses = Arrays.asList(
          new VehicleCardResponse(),
          new VehicleCardResponse()
    );

    when(cardTypeService.detail(anyLong())).thenReturn(new CardTypeResponse());
    when(vehicleBrandService.detail(anyLong())).thenReturn(new VehicleBrandResponse());
    when(vehicleTypeService.detail(anyLong())).thenReturn(new VehicleTypeResponseDTO());
    when(imageService.getAllByVehicleCard(anyLong())).thenReturn(new ArrayList<>());
    when(colorService.findByIds(anyList())).thenReturn(colors);

    VehicleCardPageResponse vehicleCardPageResponse = VehicleCardPageResponse.of(vehicleCardResponses, count);
    when(vehicleCardService.list(size, page, isAll)).thenReturn(vehicleCardPageResponse);

    VehicleCardPageResponse response = service.list(size, page, isAll);

    Assertions.assertEquals(vehicleCardPageResponse, response);
  }

  @Test
  public void detail_WhenInputValid_ReturnVehicleCardResponse() {
    Long id = 1L;

    when(vehicleCardService.find(id)).thenReturn(vehicleCard);
    when(cardTypeService.detail(vehicleCard.getCardTypeId())).thenReturn(cardTypeResponse);
    when(vehicleBrandService.detail(vehicleCard.getBrandId())).thenReturn(vehicleBrandResponse);
    when(vehicleTypeService.detail(vehicleCard.getVehicleTypeId())).thenReturn(vehicleTypeResponseDTO);
    when(vehicleCardService.findColorIds(id)).thenReturn(colorIds);
    when(colorService.findByIds(colorIds)).thenReturn(colors);
    when(imageService.getAllByVehicleCard(id)).thenReturn(imageResponses);

    VehicleCardResponse vehicleCardResponse = service.detail(id);

    compareResponseAndResponseMock(vehicleCardResponse);

    compareResponseAndVehicleCard(vehicleCardResponse, vehicleCard);
  }

  @Test
  public void detail_WhenInputIdNotFound_ReturnException() {
    Long id = 1L;

    doThrow(VehicleCardNotFoundException.class).when(vehicleCardService).find(id);

    Assertions.assertThrows(VehicleCardNotFoundException.class, () -> {
      service.detail(id);
    });

    verify(vehicleCardService, times(1)).find(id);
  }

  @Test
  public void findOrDefault_WhenInputIsNull_ReturnNull() {
    Long id = null;

    when(vehicleCardService.findOrDefault(id)).thenReturn(null);

    VehicleCardResponse vehicleCardResponse = service.findOrDefault(id);

    Assertions.assertEquals(null, vehicleCardResponse);
    verify(vehicleCardService, times(1)).findOrDefault(id);
  }

  @Test
  public void findOrDefault_WhenInputIsNotFound_ReturnNull() {
    Long id = 1L;

    when(vehicleCardService.findOrDefault(id)).thenReturn(null);

    VehicleCardResponse vehicleCardResponse = service.findOrDefault(id);

    Assertions.assertEquals(null, vehicleCardResponse);
    verify(vehicleCardService, times(1)).findOrDefault(id);
  }

  @Test
  public void findOrDefault_WhenInputValid_ReturnVehicleCardResponse() {
    Long id = 1L;

    when(vehicleCardService.findOrDefault(id)).thenReturn(vehicleCard);
    when(cardTypeService.detail(vehicleCard.getCardTypeId())).thenReturn(cardTypeResponse);
    when(vehicleBrandService.detail(vehicleCard.getBrandId())).thenReturn(vehicleBrandResponse);
    when(vehicleTypeService.detail(vehicleCard.getVehicleTypeId())).thenReturn(vehicleTypeResponseDTO);
    when(vehicleCardService.findColorIds(id)).thenReturn(colorIds);
    when(colorService.findByIds(colorIds)).thenReturn(colors);
    when(imageService.getAllByVehicleCard(id)).thenReturn(imageResponses);

    VehicleCardResponse vehicleCardResponse = service.findOrDefault(id);

    compareResponseAndResponseMock(vehicleCardResponse);
    compareResponseAndVehicleCard(vehicleCardResponse, vehicleCard);
  }

  @Test
  public void update_WhenInputIdNotFound_ReturnException() {
    Long vehicleCardId = 1L;
    VehicleCardRequest request = getVehicleCardRequest();

    doThrow(VehicleCardNotFoundException.class).when(vehicleCardService).find(vehicleCardId);

    Assertions.assertThrows(VehicleCardNotFoundException.class, () -> {
      service.update(vehicleCardId, request);
    });

    verify(vehicleCardService, times(1)).find(vehicleCardId);
  }

//  @Test
//  public void update_WhenInputCardCodeExistNotNullAndEqualCardCodeReq_ReturnVehicleCardResponse() {
//    Long vehicleCardId = 1L;
//    VehicleCardRequest request = getVehicleCardRequest();
//
//    when(vehicleCardService.find(vehicleCardId)).thenReturn(vehicleCard);
//    when(cardTypeService.detail(vehicleCard.getCardTypeId())).thenReturn(cardTypeResponse);
//    when(vehicleBrandService.detail(vehicleCard.getBrandId())).thenReturn(vehicleBrandResponse);
//    when(vehicleTypeService.detail(vehicleCard.getVehicleTypeId())).thenReturn(vehicleTypeResponseDTO);
//    when(vehicleCardService.update(vehicleCardId, request)).thenReturn(getVehicleCardResponse());
//    when(imageService.getByRequests(request.getImages())).thenReturn(imageResponses);
//    when(colorService.findByIds(colorIds)).thenReturn(colors);
//
//    VehicleCardResponse vehicleCardResponse = service.update(vehicleCardId, request);
//
//    compareResponseAndResponseMock(vehicleCardResponse);
//    compareResponseAndRequest(vehicleCardResponse, request);
//
//    verify(vehicleCardService, never()).checkVehicleCardCodeExist(request.getCardCode());
//    verify(imageService, times(1)).getIdByVehicleCard(vehicleCardId);
//    verify(vehicleCardColorService, times(1)).update(vehicleCardId, request.getColorIds());
//  }

  @Test
  public void update_WhenInputCardCodeExistIsNullAndCardCodeReqIsNull_ReturnVehicleCardResponse() {
    Long vehicleCardId = 1L;
    VehicleCardRequest request = getVehicleCardRequest();
    request.setCardCode(null);
    vehicleCard.setCardCode(null);
    VehicleCardResponse vehicleCardResponseWithCardCodeNull = getVehicleCardResponse();
    vehicleCardResponseWithCardCodeNull.setCardCode(null);

    when(vehicleCardService.find(vehicleCardId)).thenReturn(vehicleCard);
    when(cardTypeService.detail(vehicleCard.getCardTypeId())).thenReturn(cardTypeResponse);
    when(vehicleBrandService.detail(vehicleCard.getBrandId())).thenReturn(vehicleBrandResponse);
    when(vehicleTypeService.detail(vehicleCard.getVehicleTypeId())).thenReturn(vehicleTypeResponseDTO);
    when(vehicleCardService.update(vehicleCardId, request)).thenReturn(vehicleCardResponseWithCardCodeNull);
    when(imageService.getByRequests(request.getImages())).thenReturn(imageResponses);
    when(colorService.findByIds(colorIds)).thenReturn(colors);

    VehicleCardResponse vehicleCardResponse = service.update(vehicleCardId, request);

    compareResponseAndResponseMock(vehicleCardResponse);
    compareResponseAndRequest(vehicleCardResponse, request);

    verify(vehicleCardService, never()).checkVehicleCardCodeExist(request.getCardCode());
    verify(imageService, times(1)).getIdByVehicleCard(vehicleCardId);
    verify(vehicleCardColorService, times(1)).update(vehicleCardId, request.getColorIds());
  }

  @Test
  public void update_WhenInputValid_ReturnVehicleCardResponse() {
    Long vehicleCardId = 1L;
    VehicleCardRequest request = getVehicleCardRequest();
    request.setCardCode("006");

    when(vehicleCardService.find(vehicleCardId)).thenReturn(vehicleCard);
    when(cardTypeService.detail(vehicleCard.getCardTypeId())).thenReturn(cardTypeResponse);
    when(vehicleBrandService.detail(vehicleCard.getBrandId())).thenReturn(vehicleBrandResponse);
    when(vehicleTypeService.detail(vehicleCard.getVehicleTypeId())).thenReturn(vehicleTypeResponseDTO);
    when(vehicleCardService.update(vehicleCardId, request)).thenReturn(getVehicleCardUpdateResponse());
    when(imageService.getByRequests(request.getImages())).thenReturn(imageResponses);
    when(colorService.findByIds(colorIds)).thenReturn(colors);

    VehicleCardResponse vehicleCardResponse = service.update(vehicleCardId, request);

    compareResponseAndResponseMock(vehicleCardResponse);
    compareResponseAndRequest(vehicleCardResponse, request);

    verify(vehicleCardService, times(1)).checkVehicleCardCodeExist(request.getCardCode());
    verify(imageService, times(1)).getIdByVehicleCard(vehicleCardId);
    verify(vehicleCardColorService, times(1)).update(vehicleCardId, request.getColorIds());
  }

  //check
  private void compareResponseAndResponseMock(VehicleCardResponse vehicleCardResponse) {
    Assertions.assertEquals(cardTypeResponse, vehicleCardResponse.getCardType());
    Assertions.assertEquals(vehicleBrandResponse, vehicleCardResponse.getVehicleBrand());
    Assertions.assertEquals(vehicleTypeResponseDTO, vehicleCardResponse.getVehicleType());
    if (!vehicleCardResponse.getColors().isEmpty()) {
      Assertions.assertEquals(colorResponses, vehicleCardResponse.getColors());
    }
    Assertions.assertEquals(imageResponses, vehicleCardResponse.getImages());
    if (vehicleCardResponse.getPermanentResident() != null) {
      Assertions.assertEquals(addressResponse, vehicleCardResponse.getPermanentResident());
    }
  }

  private void compareResponseAndRequest(VehicleCardResponse vehicleCardResponse,
                                         VehicleCardRequest request) {
    Assertions.assertEquals(request.getCardTypeId(), vehicleCardResponse.getCardType().getId());
    Assertions.assertEquals(request.getCardCode(), vehicleCardResponse.getCardCode());
    Assertions.assertEquals(request.getBrandId(), vehicleCardResponse.getVehicleBrand().getId());
    Assertions.assertEquals(request.getVehicleTypeId(), vehicleCardResponse.getVehicleType().getId());
    Assertions.assertEquals(request.getLicensePlate(), vehicleCardResponse.getLicensePlate());
    if (request.getPermanentResident() != null &&
          vehicleCardResponse.getPermanentResident() != null) {
      if (request.getPermanentResident() != null) {
        Assertions.assertEquals(request.getPermanentResident().getProvinceCode(),
              vehicleCardResponse.getPermanentResident().getProvinces().getProvinceCodeName());
        Assertions.assertEquals(request.getPermanentResident().getDistrictCode(),
              vehicleCardResponse.getPermanentResident().getDistricts().getDistrictCodeName());
        Assertions.assertEquals(request.getPermanentResident().getWardCode(),
              vehicleCardResponse.getPermanentResident().getWards().getWardCodeName());
      }
      Assertions.assertEquals(request.getImages().size(), vehicleCardResponse.getImages().size());
      if (request.getColorIds() != null) {
        Assertions.assertEquals(request.getColorIds().size(), vehicleCardResponse.getColors().size());
      }
    }
  }

  private void compareResponseAndVehicleCard(VehicleCardResponse vehicleCardResponse,
                                             VehicleCard vehicleCard) {
    Assertions.assertEquals(vehicleCard.getCardTypeId(), vehicleCardResponse.getCardType().getId());
    Assertions.assertEquals(vehicleCard.getCardCode(), vehicleCardResponse.getCardCode());
    Assertions.assertEquals(vehicleCard.getBrandId(), vehicleCardResponse.getVehicleBrand().getId());
    Assertions.assertEquals(vehicleCard.getVehicleTypeId(), vehicleCardResponse.getVehicleType().getId());
    Assertions.assertEquals(vehicleCard.getLicensePlate(), vehicleCardResponse.getLicensePlate());
  }
}
