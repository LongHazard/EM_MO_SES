package com.ncsgroup.ems.service.person;


import com.ncsgroup.ems.configuration.ServiceConfigurationTest;
import com.ncsgroup.ems.dto.request.person.card.PersonCardRequest;
import com.ncsgroup.ems.dto.response.personcard.PersonCardPageResponse;
import com.ncsgroup.ems.dto.response.personcard.PersonCardResponse;
import com.ncsgroup.ems.entity.person.PersonCard;
import com.ncsgroup.ems.exception.person.card.CardCodeAlreadyExistException;
import com.ncsgroup.ems.exception.person.card.PersonCardNotFoundException;
import com.ncsgroup.ems.repository.PersonCardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@WebMvcTest(PersonCardService.class)
@ContextConfiguration(classes = {
      ServiceConfigurationTest.class
})
public class PersonCardServiceTests {

  @MockBean
  private PersonCardRepository repository;

  @Autowired
  private PersonCardService personCardService;

  @Test
  public void create_InputValid_ReturnPersonCardResponse() {
    PersonCard personCard = this.fakePersonCard();
    PersonCardRequest request = this.fakeRequest();

    when(repository.save(any(PersonCard.class))).thenReturn(personCard);
    when(repository.existsByCardCode(personCard.getCardCode())).thenReturn(false);

    PersonCardResponse response = personCardService.create(request);
    System.out.println("Request: " + request);
    System.out.println("Response: " + response);

    check(request, response);
  }

  @Test
  public void create_InputCardCodeAlreadyExisted_ThrowCardCodeAlreadyExistException() {
    PersonCard personCard = this.fakePersonCard();
    PersonCardRequest request = this.fakeRequest();

    when(repository.save(any(PersonCard.class))).thenReturn(personCard);
    when(repository.existsByCardCode(personCard.getCardCode())).thenReturn(true);

    Assertions.assertThrows(CardCodeAlreadyExistException.class, () ->
          personCardService.create(request)
    );
  }

  @Test
  public void update_InputIsValid_ReturnPersonCardResponse() {
    Long id = this.fakeId();
    PersonCard personCard = this.fakePersonCard();
    PersonCardRequest request = this.fakeRequest();

    when(repository.save(any(PersonCard.class))).thenReturn(personCard);
    when(repository.findById(id)).thenReturn(Optional.of(personCard));
    when(repository.existsByCardCode(request.getCardCode())).thenReturn(false);

    PersonCardResponse response = personCardService.update(id, request);

    System.out.println("Request: " + request);
    System.out.println("Response: " + response);

    check(request, response);
  }

  @Test
  public void update_InputIdNotFound_ThrowPersonCardNotFoundException() {
    Long id = this.fakeId();
    PersonCardRequest request = this.fakeRequest();

    when(repository.findById(id)).thenReturn(Optional.empty());

    Assertions.assertThrows(PersonCardNotFoundException.class, () ->
          personCardService.update(id, request)
    );
  }

  @Test
  public void update_WhenCardCodeHasChangeAndAlreadyExist_ThrowCardCodeAlreadyExistException() {
    Long id = this.fakeId();
    PersonCard personCard = this.fakePersonCard();
    PersonCardRequest request = this.fakeRequest();

    personCard.setCardCode("abcxyz");

    when(repository.save(any(PersonCard.class))).thenReturn(personCard);
    when(repository.findById(id)).thenReturn(Optional.of(personCard));
    when(repository.existsByCardCode(request.getCardCode())).thenReturn(true);

    Assertions.assertThrows(CardCodeAlreadyExistException.class, () ->
          personCardService.update(id, request)
    );

  }

  @Test
  public void retrieve_WhenInputIsValid_ReturnPersonCardResponse() {
    Long id = this.fakeId();
    PersonCardResponse fakeResponse = this.fakeResponse();

    Mockito.when(repository.detail(id)).thenReturn(fakeResponse);

    PersonCardResponse response = personCardService.retrieve(id);

    System.out.println("FakeResponse: " + fakeResponse);
    System.out.println("Response: " + response);

    this.check(fakeResponse, response);
  }

  @Test
  public void list_WhenInputIsValid_ReturnPersonCardPageResponse() {
    PersonCardPageResponse fakeResponses = this.fakeResponses();

    Mockito.when(repository.search(this.keyword(), this.pageable()))
          .thenReturn(fakeResponses.getPersonCards());

    PersonCardPageResponse responses = personCardService.list(
          this.keyword(),
          10,
          0,
          false
    );

    System.out.println("FakeResponses: " + fakeResponses);
    System.out.println("Responses: " + responses);

    this.check(fakeResponses, responses);
  }

  private Long fakeId() {
    return 1L;
  }

  private String keyword() {
    return "keyword";
  }

  private Pageable pageable() {
    return PageRequest.of(0, 10);
  }

  private PersonCard fakePersonCard() {
    PersonCard personCard = new PersonCard();
    personCard.setId(1L);
    personCard.setCardTypeId(2L);
    personCard.setCardCode("123456789");
    personCard.setDateOfBirth("22/12/2001");
    personCard.setSex("male");
    personCard.setNationality("Viet Nam");
    personCard.setIssueOn(1122334455L);
    personCard.setDateOfExpiry(1829408400000L);
    personCard.setPlaceOfIssue("Ha Noi");
    return personCard;
  }


  private PersonCardRequest fakeRequest() {
    PersonCardRequest request = new PersonCardRequest();
    request.setCardTypeId(2L);
    request.setCardCode("123456789");
    request.setDateOfBirth("22/12/2001");
    request.setSex("male");
    request.setNationality("Viet Nam");
    request.setIssueOn(1122334455L);
    request.setDateOfExpiry("22/12/2027");
    request.setPlaceOfIssue("Ha Noi");

    return request;
  }

  private PersonCardResponse fakeResponse() {
    PersonCardResponse response = new PersonCardResponse();
    response.setCardCode("123456789");
    response.setDateOfBirth("22/12/2001");
    response.setSex("male");
    response.setNationality("Viet Nam");
    response.setIssueOn(1122334455L);
    response.setDateOfExpiry("22/12/2027");
    response.setPlaceOfIssue("Ha Noi");

    return response;
  }

  private PersonCardPageResponse fakeResponses() {
    List<PersonCardResponse> personCardResponses = new ArrayList<>();

    personCardResponses.add(this.fakeResponse());
    personCardResponses.add(this.fakeResponse());

    return PersonCardPageResponse.of(
          personCardResponses,
          2
    );
  }


  private void check(PersonCardRequest request, PersonCardResponse response) {
    assertThat(request.getCardCode()).isEqualTo(response.getCardCode());
    assertThat(request.getDateOfBirth()).isEqualTo(response.getDateOfBirth());
    assertThat(request.getSex()).isEqualTo(response.getSex());
    assertThat(request.getNationality()).isEqualTo(response.getNationality());
    assertThat(request.getIssueOn()).isEqualTo(response.getIssueOn());
    assertThat(request.getDateOfExpiry()).isEqualTo(response.getDateOfExpiry());
    assertThat(request.getPlaceOfIssue()).isEqualTo(response.getPlaceOfIssue());
  }

  private void check(PersonCardResponse fakeResponse, PersonCardResponse response) {
    assertThat(fakeResponse.getCardCode()).isEqualTo(response.getCardCode());
    assertThat(fakeResponse.getDateOfBirth()).isEqualTo(response.getDateOfBirth());
    assertThat(fakeResponse.getSex()).isEqualTo(response.getSex());
    assertThat(fakeResponse.getNationality()).isEqualTo(response.getNationality());
    assertThat(fakeResponse.getIssueOn()).isEqualTo(response.getIssueOn());
    assertThat(fakeResponse.getDateOfExpiry()).isEqualTo(response.getDateOfExpiry());
    assertThat(fakeResponse.getPlaceOfIssue()).isEqualTo(response.getPlaceOfIssue());
  }

  private void check(PersonCardPageResponse fakeResponses, PersonCardPageResponse responses) {
    assertThat(Objects.equals(fakeResponses, responses));
  }
}

