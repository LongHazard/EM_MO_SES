package com.ncsgroup.ems.service.vehiclecard;

import com.ncsgroup.ems.configuration.ServiceConfigurationTest;
import com.ncsgroup.ems.dto.request.vehicle.card.VehicleCardRequest;
import com.ncsgroup.ems.dto.response.vehicle.vehiclecard.VehicleCardPageResponse;
import com.ncsgroup.ems.dto.response.vehicle.vehiclecard.VehicleCardResponse;
import com.ncsgroup.ems.entity.vehicle.VehicleCard;
import com.ncsgroup.ems.exception.vehicle.card.LicensePlateAlreadyExistException;
import com.ncsgroup.ems.exception.vehicle.card.VehicleCardCodeAlreadyExistException;
import com.ncsgroup.ems.exception.vehicle.card.VehicleCardNotFoundException;
import com.ncsgroup.ems.repository.VehicleCardColorRepository;
import com.ncsgroup.ems.repository.VehicleCardRepository;
import com.ncsgroup.ems.service.vehicle.VehicleCardService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(VehicleCardService.class)
@ContextConfiguration(classes = {
      ServiceConfigurationTest.class
})
public class VehicleCardServiceTests {
  @MockBean
  VehicleCardRepository repository;

  @MockBean
  VehicleCardColorRepository vehicleCardColorRepository;

  @Autowired
  VehicleCardService vehicleCardService;


  private VehicleCard getVehicleCard1() {
    VehicleCard vehicleCard = new VehicleCard();
    vehicleCard.setId(1L);
    vehicleCard.setCardTypeId(1L);
    vehicleCard.setCardCode("test card code 1");
    vehicleCard.setBrandId(1L);
    vehicleCard.setVehicleTypeId(1l);
    vehicleCard.setLicensePlate("test license plate 1");
    return vehicleCard;
  }

  private VehicleCard getVehicleCard2() {
    VehicleCard vehicleCard = new VehicleCard();
    vehicleCard.setId(2L);
    vehicleCard.setCardTypeId(2L);
    vehicleCard.setCardCode("test card code 2");
    vehicleCard.setBrandId(2L);
    vehicleCard.setVehicleTypeId(2l);
    vehicleCard.setLicensePlate("test license plate 2");
    return vehicleCard;
  }

  private VehicleCard getVehicleCard3() {
    VehicleCard vehicleCard = new VehicleCard();
    vehicleCard.setId(3L);
    vehicleCard.setCardTypeId(3L);
    vehicleCard.setCardCode("test card code 3");
    vehicleCard.setBrandId(3L);
    vehicleCard.setVehicleTypeId(3l);
    vehicleCard.setLicensePlate("test license plate 3");
    return vehicleCard;
  }

  static VehicleCardRequest request = new VehicleCardRequest(
        null,
        1L,
        "test card code 1",
        1L,
        1L,
        "10/2/2023",
        "test license plate 1",
        null,
        null,
        null
  );

  @Test
  public void createVehicleCard_WhenInputValid_ReturnVehicleCardResponse() {
    VehicleCard vehicleCard = getVehicleCard1();

    Mockito.when(repository.existsByCardCode(request.getCardCode())).thenReturn(false);
    Mockito.when(repository.existsByLicensePlate(request.getLicensePlate())).thenReturn(false);

    when(repository.save(any(VehicleCard.class))).thenReturn(vehicleCard);

    VehicleCardResponse vehicleCardResponse = vehicleCardService.create(request);

    checkResponseTest(vehicleCard, vehicleCardResponse);
  }

  @Test
  public void listVehicleCard_WhenIsAllTrue_ReturnAllVehicleCards() {
    int size = 3;
    int page = 0;
    boolean isAll = true;
    List<VehicleCard> vehicleCards = new ArrayList<>();
    vehicleCards.add(getVehicleCard1());
    vehicleCards.add(getVehicleCard2());

    when(repository.findAll()).thenReturn(vehicleCards);
    when(repository.count()).thenReturn(2L);

    VehicleCardPageResponse response = vehicleCardService.list(size, page, isAll);

    verify(repository, times(1)).findAll();
    verify(repository, times(1)).count();

    checkResponseTest(getVehicleCard1(), response.getVehicleCardResponses().get(0));
    checkResponseTest(getVehicleCard2(), response.getVehicleCardResponses().get(1));
    Assertions.assertEquals(vehicleCards.size(), response.getVehicleCardResponses().size());
    Assertions.assertEquals(2L, response.getCount());
  }

  @Test
  public void listVehicleCard_WhenIsAllFalse_ReturnAllVehicleCards() {
    int size = 3;
    int page = 0;
    boolean isAll = false;
    List<VehicleCard> vehicleCards = new ArrayList<>();
    vehicleCards.add(getVehicleCard1());
    vehicleCards.add(getVehicleCard2());
    vehicleCards.add(getVehicleCard3());

    when(repository.getAll(PageRequest.of(page, size))).thenReturn(vehicleCards);
    when(repository.count()).thenReturn(20L);

    VehicleCardPageResponse response = vehicleCardService.list(size, page, isAll);

    verify(repository, times(1)).getAll(PageRequest.of(page, size));
    verify(repository, times(1)).count();

    checkResponseTest(getVehicleCard1(), response.getVehicleCardResponses().get(0));
    checkResponseTest(getVehicleCard2(), response.getVehicleCardResponses().get(1));
    checkResponseTest(getVehicleCard3(), response.getVehicleCardResponses().get(2));
    Assertions.assertEquals(vehicleCards.size(), response.getVehicleCardResponses().size());
    Assertions.assertEquals(20L, response.getCount());
  }

  @Test
  public void createVehicleCard_WhenInputCardCodeAlreadyExisted_ReturnVehicleCardResponse() {
    VehicleCard vehicleCard = getVehicleCard1();

    when(repository.save(any(VehicleCard.class))).thenReturn(vehicleCard);
    Mockito.when(repository.existsByCardCode(request.getCardCode())).thenReturn(true);

    Assertions.assertThrows(VehicleCardCodeAlreadyExistException.class, () -> {
      vehicleCardService.create(request);
    });
  }

  @Test
  public void createVehicleCard_WhenInputLicensePlateAlreadyExisted_ReturnVehicleCardResponse() {
    VehicleCard vehicleCard = getVehicleCard1();

    when(repository.save(any(VehicleCard.class))).thenReturn(vehicleCard);
    Mockito.when(repository.existsByLicensePlate(request.getLicensePlate())).thenReturn(true);

    Assertions.assertThrows(LicensePlateAlreadyExistException.class, () -> {
      vehicleCardService.create(request);
    });
  }

  @Test
  public void updateVehicleCard_whenInputValid_ReturnVehicleCardResponse() {
    Long id = 2L;
    VehicleCard vehicleCard = getVehicleCard2();

    when(repository.findById(id)).thenReturn(Optional.of(vehicleCard));

    VehicleCard newVehicleCard = new VehicleCard();

    newVehicleCard.setId(id);
    newVehicleCard.setCardTypeId(request.getId());
    newVehicleCard.setCardCode(request.getCardCode());
    newVehicleCard.setBrandId(request.getBrandId());
    newVehicleCard.setVehicleTypeId(request.getVehicleTypeId());
    newVehicleCard.setLicensePlate(request.getLicensePlate());

    when(repository.save(any(VehicleCard.class))).thenReturn(newVehicleCard);

    VehicleCardResponse vehicleCardResponse = vehicleCardService.update(id, request);

    assertEquals(id, vehicleCardResponse.getId());
    assertEquals(request.getCardCode(), vehicleCardResponse.getCardCode());
    assertEquals(request.getLicensePlate(), vehicleCardResponse.getLicensePlate());
  }

  @Test
  public void updateVehicleCard_whenLicensePlateReqIsNull_ReturnVehicleCardResponse() {
    Long id = 2L;
    VehicleCard vehicleCard = getVehicleCard2();
    request.setLicensePlate(null);

    when(repository.findById(id)).thenReturn(Optional.of(vehicleCard));

    VehicleCard newVehicleCard = new VehicleCard();

    newVehicleCard.setId(id);
    newVehicleCard.setCardTypeId(request.getId());
    newVehicleCard.setCardCode(request.getCardCode());
    newVehicleCard.setBrandId(request.getBrandId());
    newVehicleCard.setVehicleTypeId(request.getVehicleTypeId());
    newVehicleCard.setLicensePlate(request.getLicensePlate());

    when(repository.save(any(VehicleCard.class))).thenReturn(newVehicleCard);

    VehicleCardResponse vehicleCardResponse = vehicleCardService.update(id, request);

    assertEquals(id, vehicleCardResponse.getId());
    assertEquals(request.getCardCode(), vehicleCardResponse.getCardCode());
    assertEquals(request.getLicensePlate(), vehicleCardResponse.getLicensePlate());
  }

  @Test
  public void updateVehicleCard_whenLicensePlateReqNotNullAndEqual_ReturnVehicleCardResponse() {
    Long id = 2L;
    VehicleCard vehicleCard = getVehicleCard2();
    request.setLicensePlate(vehicleCard.getLicensePlate());

    when(repository.findById(id)).thenReturn(Optional.of(vehicleCard));

    VehicleCard newVehicleCard = new VehicleCard();

    newVehicleCard.setId(id);
    newVehicleCard.setCardTypeId(request.getId());
    newVehicleCard.setCardCode(request.getCardCode());
    newVehicleCard.setBrandId(request.getBrandId());
    newVehicleCard.setVehicleTypeId(request.getVehicleTypeId());
    newVehicleCard.setLicensePlate(request.getLicensePlate());

    when(repository.save(any(VehicleCard.class))).thenReturn(newVehicleCard);

    VehicleCardResponse vehicleCardResponse = vehicleCardService.update(id, request);

    assertEquals(id, vehicleCardResponse.getId());
    assertEquals(request.getCardCode(), vehicleCardResponse.getCardCode());
    assertEquals(request.getLicensePlate(), vehicleCardResponse.getLicensePlate());
  }

  @Test
  public void updateVehicleCard_whenLicensePlateReqNotNullAndNotEqual_ReturnVehicleCardResponse() {
    Long id = 2L;
    VehicleCard vehicleCard = getVehicleCard2();

    when(repository.findById(id)).thenReturn(Optional.of(vehicleCard));

    VehicleCard newVehicleCard = new VehicleCard();

    newVehicleCard.setId(id);
    newVehicleCard.setCardTypeId(request.getId());
    newVehicleCard.setCardCode(request.getCardCode());
    newVehicleCard.setBrandId(request.getBrandId());
    newVehicleCard.setVehicleTypeId(request.getVehicleTypeId());
    newVehicleCard.setLicensePlate(request.getLicensePlate());

    when(repository.save(any(VehicleCard.class))).thenReturn(newVehicleCard);

    VehicleCardResponse vehicleCardResponse = vehicleCardService.update(id, request);

    assertEquals(id, vehicleCardResponse.getId());
    assertEquals(request.getCardCode(), vehicleCardResponse.getCardCode());
    assertEquals(request.getLicensePlate(), vehicleCardResponse.getLicensePlate());
  }

  @Test
  public void updateVehicleCard_WhenInputIdNotFound_ReturnException() {
    Long id = 1L;

    Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

    Assertions.assertThrows(VehicleCardNotFoundException.class, () -> {
      vehicleCardService.find(id);
    });
  }

  @Test
  public void updateVehicleCard_WhenLicensePlateNotEqualAndExist_ReturnException() {
    Long id = 2L;
    VehicleCard vehicleCard = getVehicleCard2();

    when(repository.findById(id)).thenReturn(Optional.of(vehicleCard));

    Mockito.when(repository.existsByLicensePlate(request.getLicensePlate())).thenReturn(true);

    Assertions.assertThrows(LicensePlateAlreadyExistException.class, () -> {
      vehicleCardService.update(id, request);
    });
  }

  @Test
  public void updateVehicleCard_WhenLicensePlateNotEqualAndNotExist_ReturnVehicleCardResponse() {
    Long id = 2L;
    VehicleCard vehicleCard = getVehicleCard2();
    request.setLicensePlate("new license");

    when(repository.findById(id)).thenReturn(Optional.of(vehicleCard));

    VehicleCard newVehicleCard = new VehicleCard();

    newVehicleCard.setId(id);
    newVehicleCard.setCardTypeId(request.getId());
    newVehicleCard.setCardCode(request.getCardCode());
    newVehicleCard.setBrandId(request.getBrandId());
    newVehicleCard.setVehicleTypeId(request.getVehicleTypeId());
    newVehicleCard.setLicensePlate(request.getLicensePlate());

    when(repository.save(any(VehicleCard.class))).thenReturn(newVehicleCard);

    VehicleCardResponse vehicleCardResponse = vehicleCardService.update(id, request);

    assertEquals(id, vehicleCardResponse.getId());
    assertEquals(request.getCardCode(), vehicleCardResponse.getCardCode());
    assertEquals(request.getLicensePlate(), vehicleCardResponse.getLicensePlate());
  }

  @Test
  public void updatePermanentResident_WhenInputValid_ShouldUpdatePermanentResident() {
    long addressId = 1l;
    long vehicleCardId = 1L;

    vehicleCardService.updatePermanentResident(addressId, vehicleCardId);

    verify(repository, times(1)).updatePermanentResident(addressId, vehicleCardId);
  }

  @Test
  public void findOrDefault_WhenIdIsNull_ShouldFindById() {
    VehicleCard vehicleCard = vehicleCardService.findOrDefault(null);

    verify(repository, never()).findById(null);

    assertEquals(null, vehicleCard);
  }

  @Test
  public void findOrDefault_WhenIdDoesNotExisted_ReturnVehicleCard() {
    Long id = 1l;

    when(repository.findById(id)).thenReturn(Optional.empty());

    VehicleCard vehicleCard = vehicleCardService.findOrDefault(id);

    verify(repository, times(1)).findById(id);

    assertEquals(null, vehicleCard);
  }

  @Test
  public void findOrDefault_WhenIdExisted_ReturnVehicleCard() {
    VehicleCard vehicleCard = getVehicleCard1();

    when(repository.findById(vehicleCard.getId())).thenReturn(Optional.of(vehicleCard));

    VehicleCard vehicleCardFind = vehicleCardService.findOrDefault(vehicleCard.getId());

    verify(repository, times(1)).findById(vehicleCard.getId());

    assertEquals(vehicleCard, vehicleCardFind);
  }

  @Test
  public void findColorIds_WhenInputValid_ReturnColorIds() {
    Long vehicleCardId = 1L;
    List<Long> colorIdsTest = Arrays.asList(
          7L,
          8L
    );

    when(vehicleCardColorRepository.findByVehicleCardId(vehicleCardId)).thenReturn(colorIdsTest);

    List<Long> colorIds = vehicleCardService.findColorIds(vehicleCardId);

    assertEquals(colorIds, colorIdsTest);
  }

  @Test
  public void saveVehicleCardColor_WhenColorIdsIsNull_ShouldSaveAll() {
    Long vehicleCardId = 1L;

    vehicleCardService.saveVehicleCardColor(vehicleCardId, null);

    verify(vehicleCardColorRepository, never()).saveAll(anyList());
  }

  @Test
  public void saveVehicleCardColor_WhenColorIdsIsEmpty_ShouldSaveAll() {
    Long vehicleCardId = 1L;
    List<Long> colorIds = new ArrayList<>();

    vehicleCardService.saveVehicleCardColor(vehicleCardId, colorIds);

    verify(vehicleCardColorRepository, never()).saveAll(anyList());
  }

  @Test
  public void saveVehicleCardColor_WhenInputValid_ShouldSaveAll() {
    Long vehicleCardId = 1L;
    List<Long> colorIds = Arrays.asList(
          7L,
          8L
    );

    vehicleCardService.saveVehicleCardColor(vehicleCardId, colorIds);

    verify(vehicleCardColorRepository, times(1)).saveAll(anyList());
  }

  private void checkResponseTest(VehicleCard vehicleCard,
                                 VehicleCardResponse vehicleCardResponse) {
    assertEquals(vehicleCard.getId(), vehicleCardResponse.getId());
    assertEquals(vehicleCard.getCardCode(), vehicleCardResponse.getCardCode());
    assertEquals(vehicleCard.getLicensePlate(), vehicleCardResponse.getLicensePlate());
    assertEquals(vehicleCard.getRegistrationDate(), vehicleCardResponse.getRegistrationDate());
  }
}

