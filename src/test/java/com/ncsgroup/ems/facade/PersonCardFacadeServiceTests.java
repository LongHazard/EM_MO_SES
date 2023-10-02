package com.ncsgroup.ems.facade;

import com.ncsgroup.ems.configuration.ServiceConfigurationTest;
import com.ncsgroup.ems.dto.request.address.AddressRequest;
import com.ncsgroup.ems.dto.request.person.card.PersonCardRequest;
import com.ncsgroup.ems.dto.response.address.AddressResponse;
import com.ncsgroup.ems.dto.response.cardtype.CardTypeResponse;
import com.ncsgroup.ems.dto.response.personcard.PersonCardPageResponse;
import com.ncsgroup.ems.dto.response.personcard.PersonCardResponse;
import com.ncsgroup.ems.entity.address.Address;
import com.ncsgroup.ems.service.address.AddressService;
import com.ncsgroup.ems.service.person.CardTypeService;
import com.ncsgroup.ems.service.person.PersonCardService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(PersonCardService.class)
@ContextConfiguration(classes = {
      ServiceConfigurationTest.class
})
@Slf4j
public class PersonCardFacadeServiceTests {
  @Autowired
  private PersonCardFacadeService personCardFacadeService;

  @MockBean
  private PersonCardService personCardService;

  @MockBean
  private CardTypeService cardTypeService;

  @MockBean
  private AddressService addressService;

  @Test
  public void create_WhenInputIsValid_ReturnPersonCardResponse() {
    PersonCardRequest mockRequest = this.mockPersonCardRequest();

    Mockito.when(personCardService.create(Mockito.any(PersonCardRequest.class)))
          .thenReturn(this.mockPersonCardResponse());

    Mockito.when(cardTypeService.retrieve(Mockito.anyLong()))
          .thenReturn(this.mockCardTypeResponse());

    Mockito.when(addressService.create(Mockito.any(AddressRequest.class), Mockito.anyLong(), Mockito.anyString()))
          .thenReturn(this.mockAddress());

    Mockito.when(addressService.detail(Mockito.anyLong()))
          .thenReturn(this.mockAddressResponse());

    PersonCardResponse response = personCardFacadeService.create(this.mockPersonCardRequest());

    log.info("mockRequest: {}", mockRequest);
    log.info("Response: {}", response);

    this.check(response, mockRequest, this.mockCardTypeResponse(), this.mockAddressResponse());
  }

  @Test
  public void create_WhenInputIsNull_ReturnNull() {
    PersonCardRequest mockRequest = this.mockPersonCardRequest();

    Mockito.when(personCardService.create(Mockito.any(PersonCardRequest.class)))
          .thenReturn(this.mockPersonCardResponse());

    PersonCardResponse response = personCardFacadeService.create(null);

    log.info("mockRequest: {}", mockRequest);
    log.info("Response: {}", response);

    assertThat(Objects.equals(response, mockRequest));
  }

  @Test
  public void create_WhenInputCardTypeIdIsNull_ReturnPersonCardResponse() {
    PersonCardRequest mockRequest = this.mockPersonCardRequestWithoutCardType();

    Mockito.when(personCardService.create(Mockito.any(PersonCardRequest.class)))
          .thenReturn(this.mockPersonCardResponseWithoutCardType());

    Mockito.when(cardTypeService.retrieve(Mockito.anyLong()))
          .thenReturn(null);

    Mockito.when(addressService.create(Mockito.any(AddressRequest.class), Mockito.anyLong(), Mockito.anyString()))
          .thenReturn(this.mockAddress());

    Mockito.when(addressService.detail(1L))
          .thenReturn(this.mockAddressResponse());

    PersonCardResponse response = personCardFacadeService.create(this.mockPersonCardRequestWithoutCardType());

    log.info("mockResponse: {}", mockRequest);
    log.info("Response: {}", response);

    this.check(response, mockRequest, null, this.mockAddressResponse());
  }

  @Test
  public void create_WhenInputAddressIsNull_ReturnPersonCardResponse() {
    PersonCardRequest mockRequest = this.mockPersonCardRequestWithoutAddress();

    Mockito.when(personCardService.create(Mockito.any(PersonCardRequest.class)))
          .thenReturn(this.mockPersonCardResponseWithoutCardType());

    Mockito.when(cardTypeService.retrieve(1L))
          .thenReturn(this.mockCardTypeResponse());

    PersonCardResponse response = personCardFacadeService.create(this.mockPersonCardRequestWithoutAddress());

    log.info("mockRequest: {}", mockRequest);
    log.info("Response: {}", response);

    this.check(mockRequest, response);
  }

  @Test
  public void update_WhenInputIsValid_ReturnPersonCardResponse() {
    PersonCardRequest mockRequest = this.mockPersonCardRequest();

    Mockito.when(cardTypeService.retrieve(1L))
          .thenReturn(this.mockCardTypeResponse());

    Mockito.when(personCardService.update(mockId(), mockRequest))
          .thenReturn(this.mockPersonCardResponse());

    Mockito.when(addressService.getId(Mockito.anyLong(), Mockito.anyString()))
          .thenReturn(this.mockId());

    Mockito.when(
                addressService.update(
                      Mockito.anyLong(),
                      Mockito.any(AddressRequest.class),
                      Mockito.anyLong(),
                      Mockito.anyString()
                )
          )
          .thenReturn(this.mockAddress());

    Mockito.when(
                addressService.getAddressOfPersonCard(
                      Mockito.anyLong(),
                      Mockito.anyString()
                )
          )
          .thenReturn(this.mockAddressResponse());

    PersonCardResponse response = personCardFacadeService.update(this.mockId(), mockRequest);


    log.info("mockRequest: {}", mockRequest);
    log.info("Response: {}", response);

    this.check(response, mockRequest, this.mockCardTypeResponse(), this.mockAddressResponse());
  }

  @Test
  public void update_WhenInputCardTypeIsNull_ReturnPersonCardResponse() {
    PersonCardRequest mockRequest = this.mockPersonCardRequest();

    Mockito.when(cardTypeService.retrieve(Mockito.anyLong()))
          .thenReturn(null);

    Mockito.when(personCardService.update(mockId(), mockRequest))
          .thenReturn(this.mockPersonCardResponse());

    Mockito.when(addressService.getId(Mockito.anyLong(), Mockito.anyString()))
          .thenReturn(this.mockId());

    Mockito.when(
                addressService.update(
                      Mockito.anyLong(),
                      Mockito.any(AddressRequest.class),
                      Mockito.anyLong(),
                      Mockito.anyString()
                )
          )
          .thenReturn(this.mockAddress());

    Mockito.when(
                addressService.getAddressOfPersonCard(
                      Mockito.anyLong(),
                      Mockito.anyString()
                )
          )
          .thenReturn(this.mockAddressResponse());

    PersonCardResponse response = personCardFacadeService.update(this.mockId(), mockRequest);


    log.info("mockRequest: {}", mockRequest);
    log.info("Response: {}", response);

    this.check(response, mockRequest, null, this.mockAddressResponse());
  }

  @Test
  public void update_WhenInputAddressIsNull_ReturnPersonCardResponse() {
    PersonCardRequest mockRequest = this.mockPersonCardRequest();

    Mockito.when(cardTypeService.retrieve(Mockito.anyLong()))
          .thenReturn(null);

    Mockito.when(personCardService.update(mockId(), mockRequest))
          .thenReturn(this.mockPersonCardResponse());

    Mockito.when(addressService.getId(Mockito.anyLong(), Mockito.anyString()))
          .thenReturn(this.mockId());

    Mockito.when(
                addressService.update(
                      Mockito.anyLong(),
                      Mockito.any(AddressRequest.class),
                      Mockito.anyLong(),
                      Mockito.anyString()
                )
          )
          .thenReturn(this.mockAddress());

    Mockito.when(
                addressService.getAddressOfPersonCard(
                      Mockito.anyLong(),
                      Mockito.anyString()
                )
          )
          .thenReturn(this.mockAddressResponse());

    PersonCardResponse response = personCardFacadeService.update(this.mockId(), mockRequest);


    log.info("mockRequest: {}", mockRequest);
    log.info("Response: {}", response);

    this.check(mockRequest, response);
  }

  @Test
  public void get_WhenInputIsValid_ReturnPersonCardResponse() {
    PersonCardRequest mockRequest = this.mockPersonCardRequest();

    Mockito.when(personCardService.retrieve(Mockito.anyLong()))
          .thenReturn(this.mockPersonCardResponse());

    Mockito.when(addressService.getAddressOfPersonCard(Mockito.anyLong(), Mockito.anyString()))
          .thenReturn(null);

    PersonCardResponse response = personCardFacadeService.get(this.mockId());

    log.info("mockRequest: {}", mockRequest);
    log.info("Response: {}", response);

    this.check(mockRequest, response);
  }

  @Test
  public void get_WhenInputIdIsNull_ReturnPersonCardResponse() {

    Mockito.when(personCardService.retrieve(Mockito.any()))
          .thenReturn(new PersonCardResponse());

    PersonCardResponse response = personCardFacadeService.get(null);

    Assertions.assertTrue(Objects.isNull(response.getPlaceOfOrigin()));
    Assertions.assertTrue(Objects.isNull(response.getPermanentResident()));
  }

  @Test
  public void list_WhenInputIsValid_ReturnPersonCardPageResponse() {
    List<PersonCardResponse> mockPersonCardResponses = new ArrayList<>();
    mockPersonCardResponses.add(this.mockPersonCardResponse());
    mockPersonCardResponses.add(this.mockPersonCardResponse());
    PersonCardPageResponse mockPersonCardPageResponse = PersonCardPageResponse.of(
          mockPersonCardResponses,
          2
    );

    Mockito.when(personCardService.list(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean()))
          .thenReturn(mockPersonCardPageResponse);

    PersonCardPageResponse personCardPageResponse = personCardFacadeService.list(
          "keyword",
          0,
          10,
          false
    );

    Assertions.assertEquals(mockPersonCardPageResponse, personCardPageResponse);
  }

  public Long mockId() {
    return 1L;
  }

  public PersonCardRequest mockPersonCardRequest() {
    return new PersonCardRequest(
          1L,
          "CardCode1",
          "30/04/1945",
          "Male",
          "VietNam",
          this.mockAddressRequest(),
          this.mockAddressRequest(),
          1122334455L,
          "30/04/2045",
          "PT",
          null
    );
  }

  public PersonCardRequest mockPersonCardRequestWithoutCardType() {
    return new PersonCardRequest(
          null,
          "CardCode1",
          "30/04/1945",
          "Male",
          "VietNam",
          this.mockAddressRequest(),
          this.mockAddressRequest(),
          1122334455L,
          "30/04/2045",
          "PT",
          null
    );
  }

  public PersonCardRequest mockPersonCardRequestWithoutAddress() {
    return new PersonCardRequest(
          1L,
          "CardCode1",
          "30/04/1945",
          "Male",
          "VietNam",
          null,
          null,
          1122334455L,
          "30/04/2045",
          "PT",
          null
    );
  }

  private Address mockAddress() {
    return new Address(
          1L,
          "provinceCode",
          "districtCode",
          "wardCode",
          "addressDetail",
          1L,
          "origin",
          false
    );
  }

  private AddressRequest mockAddressRequest() {
    return new AddressRequest(
          "provinceCode",
          "districtCode",
          "wardCode",
          "addressDetail",
          1L,
          "origin"
    );
  }

  private AddressResponse mockAddressResponse() {
    return new AddressResponse(
          "wardName",
          "wardNameEn",
          "wardCodeName",
          "districtName",
          "districtNameEn",
          "districtCodeName",
          "provinceName",
          "provinceNameEn",
          "provinceCodeName"
    );
  }


  public PersonCardResponse mockPersonCardResponse() {
    PersonCardResponse personCardResponse = new PersonCardResponse();

    this.setProperties(personCardResponse);

    return personCardResponse;
  }

  public PersonCardResponse mockPersonCardResponseWithoutCardType() {
    PersonCardResponse personCardResponse = new PersonCardResponse();

    this.setProperties(personCardResponse);
    personCardResponse.setCardType(null);

    return personCardResponse;
  }

  public PersonCardResponse mockPersonCardResponseWithoutAdress() {
    PersonCardResponse personCardResponse = new PersonCardResponse();

    this.setProperties(personCardResponse);
    personCardResponse.setPlaceOfOrigin(null);
    personCardResponse.setPermanentResident(null);

    return personCardResponse;
  }

  public CardTypeResponse mockCardTypeResponse() {
    return new CardTypeResponse(
          1L,
          "identity",
          "CMT",
          "Chung minh thu"
    );
  }


  private void setProperties(PersonCardResponse personCardResponse) {
    personCardResponse.setId(1L);
    personCardResponse.setCardCode("CardCode1");
    personCardResponse.setDateOfBirth("30/04/1945");
    personCardResponse.setSex("Male");
    personCardResponse.setNationality("VietNam");
    personCardResponse.setIssueOn(1122334455L);
    personCardResponse.setDateOfExpiry("30/04/2045");
    personCardResponse.setPlaceOfIssue("PT");
  }

  private void check(PersonCardRequest mockRequest, PersonCardResponse response) {
    Assertions.assertTrue(Objects.equals(mockRequest.getCardCode(), response.getCardCode()));
    Assertions.assertTrue(Objects.equals(mockRequest.getDateOfBirth(), response.getDateOfBirth()));
    Assertions.assertTrue(Objects.equals(mockRequest.getSex(), response.getSex()));
    Assertions.assertTrue(Objects.equals(mockRequest.getNationality(), response.getNationality()));
    Assertions.assertTrue(Objects.equals(mockRequest.getIssueOn(), response.getIssueOn()));
    Assertions.assertTrue(Objects.equals(mockRequest.getDateOfExpiry(), response.getDateOfExpiry()));
    Assertions.assertTrue(Objects.equals(mockRequest.getPlaceOfIssue(), response.getPlaceOfIssue()));
  }

  private void check(
        PersonCardResponse response, PersonCardRequest mockRequest,
        CardTypeResponse mockCardTypeResponse, AddressResponse mockAddressResponse
  ) {

    Assertions.assertTrue(Objects.equals(mockRequest.getCardCode(), response.getCardCode()));
    Assertions.assertTrue(Objects.equals(mockCardTypeResponse, response.getCardType()));
    Assertions.assertTrue(Objects.equals(mockRequest.getDateOfBirth(), response.getDateOfBirth()));
    Assertions.assertTrue(Objects.equals(mockAddressResponse, response.getPlaceOfOrigin()));
    Assertions.assertTrue(Objects.equals(mockAddressResponse, response.getPermanentResident()));
    Assertions.assertTrue(Objects.equals(mockRequest.getSex(), response.getSex()));
    Assertions.assertTrue(Objects.equals(mockRequest.getNationality(), response.getNationality()));
    Assertions.assertTrue(Objects.equals(mockRequest.getIssueOn(), response.getIssueOn()));
    Assertions.assertTrue(Objects.equals(mockRequest.getDateOfExpiry(), response.getDateOfExpiry()));
    Assertions.assertTrue(Objects.equals(mockRequest.getPlaceOfIssue(), response.getPlaceOfIssue()));
  }
}
