package com.ncsgroup.ems.service.person;


import com.ncsgroup.ems.configuration.ServiceConfigurationTest;
import com.ncsgroup.ems.dto.request.person.PersonRequest;
import com.ncsgroup.ems.dto.request.person.SearchPersonRequest;
import com.ncsgroup.ems.dto.response.identity.IdentityResponse;
import com.ncsgroup.ems.dto.response.person.PersonCreateResponse;
import com.ncsgroup.ems.dto.response.person.PersonPageResponse;
import com.ncsgroup.ems.dto.response.person.PersonResponse;
import com.ncsgroup.ems.entity.person.Person;
import com.ncsgroup.ems.exception.person.*;
import com.ncsgroup.ems.repository.PersonRepository;
import com.ncsgroup.ems.utils.MapperUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@WebMvcTest(PersonService.class)
@ContextConfiguration(classes = {
      ServiceConfigurationTest.class
})
public class PersonServiceTests {

  @MockBean
  private PersonRepository repository;

  @Autowired
  private PersonService personService;

  @Test
  public void createPerson_WhenInputPersonRequestValid_ReturnPersonCreateResponse() {
    PersonRequest request = getRequest();
    Person person = new Person();
    setPerson(person);

    Mockito.when(repository.save(any(Person.class))).thenReturn(person);

    PersonCreateResponse response = personService.create(request, null);

    check(person, response);
  }

  @Test
  public void createPerson_WhenInputEmailAlreadyExisted_ReturnPersonCreateResponse() {
    PersonRequest request = getRequest();

    Person person = new Person();
    setPerson(person);

    Mockito.when(repository.save(any(Person.class))).thenReturn(person);
    Mockito.when(repository.existsByEmail(request.getEmail())).thenReturn(true);

    Assertions.assertThrows(EmailAlreadyExistException.class, () -> {
      personService.create(request, null);
    });
  }

  @Test
  public void createPerson_WhenInputPhoneNumberAlreadyExisted_ReturnPersonCreateResponse() {
    PersonRequest request = getRequest();

    Person person = new Person();
    setPerson(person);

    Mockito.when(repository.save(any(Person.class))).thenReturn(person);
    Mockito.when(repository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(true);

    Assertions.assertThrows(PhoneNumberAlreadyExistException.class, () -> {
      personService.create(request, null);
    });
  }

  @Test
  public void createPerson_WhenInputFaceIdAlreadyExisted_ReturnPersonCreateResponse() {
    PersonRequest request = getRequest();

    Person person = new Person();
    setPerson(person);
    Mockito.when(repository.save(any(Person.class))).thenReturn(person);
    Mockito.when(repository.existsByFaceId(request.getFaceId())).thenReturn(true);

    Assertions.assertThrows(FaceIdAlreadyExistException.class, () -> {
      personService.create(request, null);
    });
  }

  @Test
  public void createPerson_WhenInputStaffCodeAlreadyExisted_ReturnPersonCreateResponse() {
    PersonRequest request = getRequest();

    Person person = new Person();
    setPerson(person);

    Mockito.when(repository.save(any(Person.class))).thenReturn(person);
    Mockito.when(repository.existsByStaffCode(request.getStaffCode())).thenReturn(true);

    Assertions.assertThrows(StaffCodeAlreadyExistException.class, () -> {
      personService.create(request, null);
    });
  }

  @Test
  public void createPerson_WhenInputPersonCardIdNotNull_ReturnPersonCreateResponse() {
    PersonRequest request = getRequest();

    Person person = new Person();
    setPerson(person);

    Mockito.when(repository.save(any(Person.class))).thenReturn(person);

    PersonCreateResponse response = personService.create(request, 1L);

    check(person, response);
  }

  @Test
  public void getDetailPerson_WhenInputValid_ReturnPersonResponse() {
    Long id = 1L;
    Person person = new Person();
    setPerson(person);

    PersonCreateResponse personCreateResponse = MapperUtils.toDTO(person, PersonCreateResponse.class);

    Mockito.when(repository.detail(id)).thenReturn(personCreateResponse);
    Mockito.when(repository.findById(id)).thenReturn(Optional.of(new Person()));

    PersonCreateResponse returnPerson = personService.detail(1L);

    Assertions.assertEquals(personCreateResponse, returnPerson);
  }

  @Test
  public void getDetailPerson_WhenInputPersonIdInvalid_ThrowPersonNotFoundException() {

    Long id = 1L;
    Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

    Assertions.assertThrows(PersonNotFoundException.class, () -> {
      personService.detail(1L);
    });

  }

  @Test
  public void listPerson_WhenIsAllTrueAndRequestIsNull_ThenReturnPersonPageResponse() {
    List<PersonResponse> personResponses = new ArrayList<>();
    personResponses.add(new PersonResponse());

    System.out.println(personResponses.size());

    Mockito.when(repository.listByKeyword(null, null)).thenReturn(personResponses);
    Mockito.when(repository.countByKeyword(null)).thenReturn(1);

    PersonPageResponse personPageResponse = personService.list(new SearchPersonRequest(), 10, 0, true);
    Assertions.assertEquals(personResponses, personPageResponse.getPersons());
    assertThat(personPageResponse.getCountPersons()).isEqualTo(1);
  }

  @Test
  public void listPerson_WhenIsAllTrueAndFaceIdsIsNull_ThenReturnPersonPageResponse() {
    List<PersonResponse> personResponses = new ArrayList<>();
    personResponses.add(new PersonResponse());

    System.out.println(personResponses.size());

    Mockito.when(repository.listByKeyword("abc", null)).thenReturn(personResponses);
    Mockito.when(repository.countByKeyword("abc")).thenReturn(1);

    PersonPageResponse personPageResponse = personService.list(
          new SearchPersonRequest("abc", null),
          10,
          0,
          true
    );

    Assertions.assertEquals(personResponses, personPageResponse.getPersons());
    assertThat(personPageResponse.getCountPersons()).isEqualTo(1);
  }

  @Test
  public void listPerson_WhenIsAllTrueAndKeywordIsNull_ThenReturnPersonPageResponse() {
    List<PersonResponse> personResponses = new ArrayList<>();
    personResponses.add(new PersonResponse());

    List<String> faceIds = List.of("f001", "f002");

    Mockito.when(repository.listBySearch(null, faceIds, null)).thenReturn(personResponses);
    Mockito.when(repository.countSearch(null, faceIds)).thenReturn(1);

    PersonPageResponse personPageResponse = personService.list(
          new SearchPersonRequest(null, faceIds),
          10,
          0,
          true
    );

    Assertions.assertEquals(personResponses, personPageResponse.getPersons());
    assertThat(personPageResponse.getCountPersons()).isEqualTo(1);
  }

  @Test
  public void listPerson_WhenIsAllTrueAndRequestNotNull_ThenReturnPersonPageResponse() {
    List<PersonResponse> personResponses = new ArrayList<>();
    personResponses.add(new PersonResponse());

    List<String> faceIds = List.of("f001", "f002");

    Mockito.when(repository.listBySearch("abc", faceIds, null)).thenReturn(personResponses);
    Mockito.when(repository.countSearch("abc", faceIds)).thenReturn(1);

    PersonPageResponse personPageResponse = personService.list(
          new SearchPersonRequest("abc", faceIds),
          10,
          0,
          true
    );

    Assertions.assertEquals(personResponses, personPageResponse.getPersons());
    assertThat(personPageResponse.getCountPersons()).isEqualTo(1);
  }

  @Test
  public void listPerson_whenIsAllFalseAndRequestIsNull_ThenReturnPersonPageResponse() {
    List<PersonResponse> personResponses = new ArrayList<>();
    personResponses.add(new PersonResponse());

    System.out.println(personResponses.size());

    Pageable pageable = PageRequest.of(0, 10);

    Mockito.when(repository.listByKeyword(null, pageable)).thenReturn(personResponses);
    Mockito.when(repository.countByKeyword(null)).thenReturn(1);

    PersonPageResponse personPageResponse = personService.list(new SearchPersonRequest(null, null), 10, 0, false);

    Assertions.assertEquals(personResponses, personPageResponse.getPersons());
    assertThat(personPageResponse.getCountPersons()).isEqualTo(1);
  }

  @Test
  public void listPerson_whenIsAllFalseAndKeyWordNotNullAndFaceIdsIsNull_ThenReturnPersonPageResponse() {
    List<PersonResponse> personResponses = new ArrayList<>();
    personResponses.add(new PersonResponse());

    System.out.println(personResponses.size());

    Pageable pageable = PageRequest.of(0, 10);

    Mockito.when(repository.listByKeyword("abc", pageable)).thenReturn(personResponses);
    Mockito.when(repository.countByKeyword("abc")).thenReturn(1);

    PersonPageResponse personPageResponse = personService.list(new SearchPersonRequest("abc", null), 10, 0, false);

    Assertions.assertEquals(personResponses, personPageResponse.getPersons());
    assertThat(personPageResponse.getCountPersons()).isEqualTo(1);
  }

  @Test
  public void listPerson_whenIsAllFalseAndKeyWordIsNullAndFaceIdsNotNull_ThenReturnPersonPageResponse() {
    List<PersonResponse> personResponses = new ArrayList<>();
    personResponses.add(new PersonResponse());

    System.out.println(personResponses.size());

    Pageable pageable = PageRequest.of(0, 10);
    List<String> faceIds = List.of("f001", "f002");

    Mockito.when(repository.listBySearch(null, faceIds, pageable)).thenReturn(personResponses);
    Mockito.when(repository.countSearch(null, faceIds)).thenReturn(1);

    PersonPageResponse personPageResponse = personService.list(
          new SearchPersonRequest(null, faceIds),
          10,
          0,
          false
    );

    Assertions.assertEquals(personResponses, personPageResponse.getPersons());
    assertThat(personPageResponse.getCountPersons()).isEqualTo(1);
  }

  @Test
  public void listPerson_whenIsAllFalseAndRequestNotNull_ThenReturnPersonPageResponse() {
    List<PersonResponse> personResponses = new ArrayList<>();
    personResponses.add(new PersonResponse());

    System.out.println(personResponses.size());

    Pageable pageable = PageRequest.of(0, 10);
    List<String> faceIds = List.of("f001", "f002");

    Mockito.when(repository.listBySearch("abc", faceIds, pageable)).thenReturn(personResponses);
    Mockito.when(repository.countSearch("abc", faceIds)).thenReturn(1);

    PersonPageResponse personPageResponse = personService.list(
          new SearchPersonRequest("abc", faceIds),
          10,
          0,
          false
    );

    Assertions.assertEquals(personResponses, personPageResponse.getPersons());
    assertThat(personPageResponse.getCountPersons()).isEqualTo(1);
  }

  @Test
  public void getPersonCarId_whenPersonIdNotNull_ThenReturnLong() {

    Mockito.when(repository.getPersonCarId(1L)).thenReturn(2L);
    Mockito.when(repository.findById(1L)).thenReturn(Optional.of(new Person()));

    assertThat(personService.getPersonCardId(1L)).isEqualTo(2L);
  }

  @Test
  public void getPersonCarId_whenPersonIdIsNull_ThenThrowPersonNotFoundException() {

    Mockito.when(repository.getPersonCarId(1L)).thenReturn(2L);
    Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());

    Assertions.assertThrows(PersonNotFoundException.class, () -> {
      personService.getPersonCardId(1L);
    });
  }

  @Test
  public void countVehicle_whenPersonIdNotNull_ThenReturnInt() {
    Mockito.when(repository.countVehicle(1L)).thenReturn(1);
    Mockito.when(repository.findById(1L)).thenReturn(Optional.of(new Person()));

    assertThat(personService.countVehicle(1L)).isEqualTo(1);
  }

  @Test
  public void countVehicle_whenPersonIdIsNull_ThenThrowPersonNotFoundException() {
    Mockito.when(repository.countVehicle(1L)).thenReturn(1);
    Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());

    Assertions.assertThrows(PersonNotFoundException.class, () -> {
      personService.countVehicle(1L);
    });
  }

  @Test
  public void update_whenIdNotFound_ThenThrowPersonNotFoundException() {
    PersonRequest request = getRequest();

    Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());

    Assertions.assertThrows(PersonNotFoundException.class, () -> {
      personService.update(1L, request);
    });
  }

  @Test
  public void update_whenInputValid_ThenReturnPersonCreateResponse() {
    PersonRequest request = getRequestUpdate();
    Person person = new Person();
    setPerson(person);

    Person savedPerson = map(request);

    Mockito.when(repository.findById(person.getId())).thenReturn(Optional.of(person));
    Mockito.when(repository.save(any(Person.class))).thenReturn(savedPerson);

    Mockito.when(repository.existsByEmail(request.getEmail())).thenReturn(false);
    Mockito.when(repository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);
    Mockito.when(repository.existsByStaffCode(request.getStaffCode())).thenReturn(false);
    Mockito.when(repository.existsByFaceId(request.getFaceId())).thenReturn(false);

    PersonCreateResponse personCreateResponse = personService.update(person.getId(), request);

    check(savedPerson, personCreateResponse);
  }

  @Test
  public void update_whenInputEmailInvalid_ThenThrowEmailAlreadyExistedException() {
    PersonRequest request = getRequestUpdate();

    Person person = new Person();
    setPerson(person);

    Person savedPerson = map(request);

    Mockito.when(repository.findById(1L)).thenReturn(Optional.of(person));
    Mockito.when(repository.save(any(Person.class))).thenReturn(savedPerson);

    Mockito.when(repository.existsByEmail(request.getEmail())).thenReturn(true);

    Assertions.assertThrows(EmailAlreadyExistException.class, () -> {
      personService.update(1L, request);
    });

  }

  @Test
  public void update_whenInputPhoneNumberInvalid_ThenThrowPhoneNumberAlreadyExistedException() {
    PersonRequest request = getRequestUpdate();

    Person person = new Person();
    setPerson(person);

    Person savedPerson = map(request);

    Mockito.when(repository.findById(1L)).thenReturn(Optional.of(person));
    Mockito.when(repository.save(any(Person.class))).thenReturn(savedPerson);

    Mockito.when(repository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(true);

    Assertions.assertThrows(PhoneNumberAlreadyExistException.class, () -> {
      personService.update(1L, request);
    });

  }

  @Test
  public void update_whenInputStaffCodeInvalid_ThenThrowStaffCodeAlreadyExistedException() {
    PersonRequest request = getRequestUpdate();

    Person person = new Person();
    setPerson(person);

    Person savedPerson = map(request);

    Mockito.when(repository.findById(1L)).thenReturn(Optional.of(person));
    Mockito.when(repository.save(any(Person.class))).thenReturn(savedPerson);

    Mockito.when(repository.existsByStaffCode(request.getStaffCode())).thenReturn(true);

    Assertions.assertThrows(StaffCodeAlreadyExistException.class, () -> {
      personService.update(1L, request);
    });

  }

  @Test
  public void update_whenInputFaceIdInvalid_ThenThrowFaceIdAlreadyExistedException() {
    PersonRequest request = getRequestUpdate();

    Person person = new Person();
    setPerson(person);

    Person savedPerson = map(request);

    Mockito.when(repository.findById(1L)).thenReturn(Optional.of(person));
    Mockito.when(repository.save(any(Person.class))).thenReturn(savedPerson);

    Mockito.when(repository.existsByFaceId(request.getFaceId())).thenReturn(true);

    Assertions.assertThrows(FaceIdAlreadyExistException.class, () -> {
      personService.update(1L, request);
    });

  }

  @Test
  public void update_whenInputEmailIsNull_ThenReturnPersonCreateResponse() {
    PersonRequest request = getRequestUpdate();
    request.setEmail(null);

    Person person = new Person();
    setPerson(person);

    Person savedPerson = map(request);

    Mockito.when(repository.findById(1L)).thenReturn(Optional.of(person));
    Mockito.when(repository.save(any(Person.class))).thenReturn(savedPerson);

    Mockito.when(repository.existsByEmail(request.getEmail())).thenReturn(true);
    Mockito.when(repository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);
    Mockito.when(repository.existsByStaffCode(request.getStaffCode())).thenReturn(false);
    Mockito.when(repository.existsByFaceId(request.getFaceId())).thenReturn(false);


    PersonCreateResponse personCreateResponse = personService.update(1L, request);

    check(savedPerson, personCreateResponse);

  }

  @Test
  public void update_whenInputPhoneNumberIsNull_ThenReturnPersonCreateResponse() {
    PersonRequest request = getRequestUpdate();
    request.setPhoneNumber(null);

    Person person = new Person();
    setPerson(person);

    Person savedPerson = map(request);

    Mockito.when(repository.findById(1L)).thenReturn(Optional.of(person));
    Mockito.when(repository.save(any(Person.class))).thenReturn(savedPerson);

    Mockito.when(repository.existsByEmail(request.getEmail())).thenReturn(false);
    Mockito.when(repository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(true);
    Mockito.when(repository.existsByStaffCode(request.getStaffCode())).thenReturn(false);
    Mockito.when(repository.existsByFaceId(request.getFaceId())).thenReturn(false);


    PersonCreateResponse personCreateResponse = personService.update(1L, request);

    check(savedPerson, personCreateResponse);

  }

  @Test
  public void update_whenInputStaffCodeIsNull_ThenReturnPersonCreateResponse() {
    PersonRequest request = getRequestUpdate();
    request.setStaffCode(null);

    Person person = new Person();
    setPerson(person);

    Person savedPerson = map(request);

    Mockito.when(repository.findById(1L)).thenReturn(Optional.of(person));
    Mockito.when(repository.save(any(Person.class))).thenReturn(savedPerson);

    Mockito.when(repository.existsByEmail(request.getEmail())).thenReturn(false);
    Mockito.when(repository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);
    Mockito.when(repository.existsByStaffCode(request.getStaffCode())).thenReturn(true);
    Mockito.when(repository.existsByFaceId(request.getFaceId())).thenReturn(false);


    PersonCreateResponse personCreateResponse = personService.update(1L, request);

    check(savedPerson, personCreateResponse);

  }

  @Test
  public void update_whenInputFaceIdIsNull_ThenReturnPersonCreateResponse() {
    PersonRequest request = getRequestUpdate();
    request.setFaceId(null);

    Person person = new Person();
    setPerson(person);

    Person savedPerson = map(request);

    Mockito.when(repository.findById(1L)).thenReturn(Optional.of(person));
    Mockito.when(repository.save(any(Person.class))).thenReturn(savedPerson);

    Mockito.when(repository.existsByEmail(request.getEmail())).thenReturn(false);
    Mockito.when(repository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);
    Mockito.when(repository.existsByStaffCode(request.getStaffCode())).thenReturn(false);
    Mockito.when(repository.existsByFaceId(request.getFaceId())).thenReturn(true);


    PersonCreateResponse personCreateResponse = personService.update(1L, request);

    check(savedPerson, personCreateResponse);

  }

  @Test
  public void update_whenEmailNotChange_ThenReturnPersonCreateResponse() {
    PersonRequest request = getRequestUpdate();

    Person person = new Person();
    setPerson(person);

    request.setEmail(person.getEmail());

    Person savedPerson = map(request);

    Mockito.when(repository.findById(1L)).thenReturn(Optional.of(person));
    Mockito.when(repository.save(any(Person.class))).thenReturn(savedPerson);

    Mockito.when(repository.existsByEmail(request.getEmail())).thenReturn(true);
    Mockito.when(repository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);
    Mockito.when(repository.existsByStaffCode(request.getStaffCode())).thenReturn(false);
    Mockito.when(repository.existsByFaceId(request.getFaceId())).thenReturn(false);


    PersonCreateResponse personCreateResponse = personService.update(1L, request);

    check(savedPerson, personCreateResponse);

  }

  @Test
  public void update_whenPhoneNumberNotChange_ThenReturnPersonCreateResponse() {
    PersonRequest request = getRequestUpdate();

    Person person = new Person();
    setPerson(person);

    request.setPhoneNumber(person.getPhoneNumber());

    Person savedPerson = map(request);

    Mockito.when(repository.findById(1L)).thenReturn(Optional.of(person));
    Mockito.when(repository.save(any(Person.class))).thenReturn(savedPerson);

    Mockito.when(repository.existsByEmail(request.getEmail())).thenReturn(false);
    Mockito.when(repository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(true);
    Mockito.when(repository.existsByStaffCode(request.getStaffCode())).thenReturn(false);
    Mockito.when(repository.existsByFaceId(request.getFaceId())).thenReturn(false);


    PersonCreateResponse personCreateResponse = personService.update(1L, request);

    check(savedPerson, personCreateResponse);

  }

  @Test
  public void update_whenStaffCodeNotChange_ThenReturnPersonCreateResponse() {
    PersonRequest request = getRequestUpdate();

    Person person = new Person();
    setPerson(person);

    request.setStaffCode(person.getStaffCode());

    Person savedPerson = map(request);

    Mockito.when(repository.findById(1L)).thenReturn(Optional.of(person));
    Mockito.when(repository.save(any(Person.class))).thenReturn(savedPerson);

    Mockito.when(repository.existsByEmail(request.getEmail())).thenReturn(false);
    Mockito.when(repository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);
    Mockito.when(repository.existsByStaffCode(request.getStaffCode())).thenReturn(true);
    Mockito.when(repository.existsByFaceId(request.getFaceId())).thenReturn(false);


    PersonCreateResponse personCreateResponse = personService.update(1L, request);

    check(savedPerson, personCreateResponse);

  }

  @Test
  public void update_whenFaceIdNotChange_ThenReturnPersonCreateResponse() {
    PersonRequest request = getRequestUpdate();

    Person person = new Person();
    setPerson(person);

    request.setFaceId(person.getFaceId());

    Person savedPerson = map(request);

    Mockito.when(repository.findById(1L)).thenReturn(Optional.of(person));
    Mockito.when(repository.save(any(Person.class))).thenReturn(savedPerson);

    Mockito.when(repository.existsByEmail(request.getEmail())).thenReturn(false);
    Mockito.when(repository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);
    Mockito.when(repository.existsByStaffCode(request.getStaffCode())).thenReturn(false);
    Mockito.when(repository.existsByFaceId(request.getFaceId())).thenReturn(true);


    PersonCreateResponse personCreateResponse = personService.update(1L, request);

    check(savedPerson, personCreateResponse);

  }

  @Test
  public void checkNotFound_InputIdIsNull_ThenThrowPersonNotFoundException() {
    Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());

    Assertions.assertThrows(PersonNotFoundException.class, () -> {
      personService.checkNotFound(1L);
    });

  }

  @Test
  public void checkNotFound_InputValid_ThenReturnPerson() {

    Person person = new Person();
    setPerson(person);

    Mockito.when(repository.findById(1L)).thenReturn(Optional.of(person));

    Person savedPerson = personService.checkNotFound(1L);

    Assertions.assertEquals(person, savedPerson);

  }

  @Test
  public void findNames_InputValid_ThenReturnListString() {
    List<Long> personIds = List.of(1L, 2L);
    List<String> personNames = List.of("Hieu", "Hieu1");

    Mockito.when(repository.findNames(personIds)).thenReturn(personNames);

    List<String> returnPersonNames = personService.findNames(personIds);

    Assertions.assertEquals(personNames, returnPersonNames);
  }

  @Test
  public void findIdByUid_InputValid_ThenReturnLong() {
    String uid = "6152a8e9-5d35-4739-9e45-3210243b5140";

    when(repository.getIdByUid(uid)).thenReturn(1L);
    when(repository.existsByUid(uid)).thenReturn(true);

    assertThat(personService.findIdByUid(uid)).isEqualTo(1L);
  }

  @Test
  public void findIdByUid_InputInvalid_ThenThrowPersonNotFoundException() {
    String uid = "6152a8e9-5d35-4739-9e45-3210243b5140";

    when(repository.getIdByUid(uid)).thenReturn(1L);
    when(repository.existsByUid(uid)).thenReturn(false);

    Assertions.assertThrows(PersonNotFoundException.class, () -> {
      personService.findIdByUid(uid);
    });
  }

  @Test
  public void findIdByUid_InputIsNull_ThenThrowPersonNotFoundException() {
    String uid = null;

    when(repository.getIdByUid(uid)).thenReturn(1L);
    when(repository.existsByUid(uid)).thenReturn(true);

    assertThat(personService.findIdByUid(uid)).isEqualTo(1L);
  }

  @Test
  public void findNameById_InputNotNull_ThenReturnString() {
    when(repository.findNameById(1L)).thenReturn("Hieu");

    assertThat(personService.findNameById(1L)).isEqualTo("Hieu");
  }

  @Test
  public void getPersonByGroupId_InputValid_ThenReturnListIdentityResponse() {

    List<IdentityResponse> identityResponses = new ArrayList<>();
    identityResponses.add(new IdentityResponse());

    when(repository.getPersonByGroupId(1L)).thenReturn(identityResponses);

    Assertions.assertEquals(personService.getPersonByGroupId(1L), identityResponses);
  }

  @Test
  public void listByGroup_InputValid_ThenReturnPersonPageResponse() {

    Pageable pageable = PageRequest.of(0, 10);

    List<PersonResponse> personResponses = new ArrayList<>();
    personResponses.add(new PersonResponse());

    when(repository.listByGroup(1L, pageable)).thenReturn(personResponses);
    when(repository.countByGroup(1L)).thenReturn(1);

    PersonPageResponse personPageResponse = personService.listByGroup(1L, 0, 10);

    assertThat(personPageResponse.getCountPersons()).isEqualTo(1);
    Assertions.assertEquals(personResponses, personPageResponse.getPersons());
  }

  @Test
  public void getPersonByListGroupId_InputValid_ThenReturnListIdentityResponse() {
    List<IdentityResponse> identityResponses = new ArrayList<>();
    identityResponses.add(new IdentityResponse());

    List<Long> groupIds = List.of(1L, 2L);

    when(repository.getPersonByGroupId(groupIds)).thenReturn(identityResponses);

    List<IdentityResponse> returnResponses = personService.getPersonByGroupId(groupIds);

    Assertions.assertEquals(identityResponses, returnResponses);

  }

  @Test
  public void getDepartment_InputValid_ThenReturnSetString() {

    Set<String> listDepartments = new HashSet<>();
    listDepartments.add("dp1");

    when(repository.findDepartment("keyword")).thenReturn(listDepartments);
    Set<String> returnDepartments = personService.getDepartment("keyword");

    Assertions.assertEquals(listDepartments, returnDepartments);
  }

  @Test
  public void getPosition_InputValid_ThenReturnSetString() {

    Set<String> listPositions = new HashSet<>();
    listPositions.add("po1");

    when(repository.findPosition("keyword")).thenReturn(listPositions);
    Set<String> returnPositions = personService.getPosition("keyword");

    Assertions.assertEquals(listPositions, returnPositions);
  }

  @Test
  public void getOrg_InputValid_ThenReturnSetString() {

    Set<String> listOrg = new HashSet<>();
    listOrg.add("org1");

    when(repository.findOrg("keyword")).thenReturn(listOrg);
    Set<String> returnListOrg = personService.getOrg("keyword");

    Assertions.assertEquals(listOrg, returnListOrg);
  }

  @Test
  public void getByOutsideGroupIds_InputValid_ThenReturnListIdentityResponse() {

    List<IdentityResponse> identityResponses = new ArrayList<>();
    identityResponses.add(new IdentityResponse());

    List<Long> groupIds = List.of(1L, 2L);

    when(repository.getByOutsideGroupIds(groupIds)).thenReturn(identityResponses);

    List<IdentityResponse> returnResponses = personService.getByOutsideGroupIds(groupIds);

    Assertions.assertEquals(identityResponses, returnResponses);
  }

  @Test
  public void getIdByVehicle_InputValid_ThenReturnLong() {

    Long personId = 11L;

    when(repository.getIdByVehicle(1L)).thenReturn(11L);

    assertThat(personId).isEqualTo(personService.getIdByVehicle(1L));
  }

  @Test
  public void updatePersonCardId_InputValid_ThenReturnSuccess() {
    Long id = 12L;
    Long personCardId = 34L;

    personService.updatePersonCardId(id, personCardId);

    verify(repository, times(1)).updatePersonCardId(id, personCardId);
  }

  @Test
  public void remove_InputValid_ReturnSuccess() {
    Long id = 123L;

    Person person = new Person();
    when(repository.findById(id)).thenReturn(Optional.of(person));
    personService.remove(id);

    verify(repository, times(1)).remove(123L);
  }

  @Test
  public void remove_InputIdIsNull_ThrowPersonNotFoundException() {
    Long id = 123L;

    Person person = new Person();
    when(repository.findById(id)).thenReturn(Optional.empty());

    Assertions.assertThrows(PersonNotFoundException.class, () -> {
      personService.remove(id);
    });
  }

  @Test
  public void findByVehicleOrNull_InputValid_ReturnPerson() {
    Long id = 123L;

    Person person = new Person();
    when(repository.findByVehicleId(id)).thenReturn(Optional.of(person));

    Assertions.assertEquals(personService.findByVehicleIdOrNull(123L), person);
  }

  private PersonRequest getRequestUpdate() {
    return new PersonRequest(
          "John Doe 1",
          "john.doe@example1.com",
          "Male",
          "0988975814",
          "ABC1234",
          "Manager",
          "Sales",
          "1233211234"
    );
  }


  private void setPerson(Person person) {
    person.setId(1L);
    person.setName("John Doe");
    person.setEmail("john.doe@example.com");
    person.setSex("Male");
    person.setDepartment("ABC123");
    person.setPhoneNumber("0987975814");
    person.setPosition("Manager");
    person.setDepartment("Sales");
    person.setFaceId("123321123");
    person.setPersonCardId(1L);
  }

  private void check(Person person, PersonCreateResponse response) {
    assertThat(response).isNotNull();
    assertThat(person.getId()).isEqualTo(response.getId());
    assertThat(person.getName()).isEqualTo(response.getName());
    assertThat(person.getEmail()).isEqualTo(response.getEmail());
    assertThat(person.getSex()).isEqualTo(response.getSex());
    assertThat(person.getPhoneNumber()).isEqualTo(response.getPhoneNumber());
    assertThat(person.getPosition()).isEqualTo(response.getPosition());
    assertThat(person.getDepartment()).isEqualTo(response.getDepartment());
    assertThat(person.getFaceId()).isEqualTo(response.getFaceId());
  }

  private PersonRequest getRequest() {
    return new PersonRequest(
          "John Doe",
          "john.doe@example.com",
          "Male",
          "0987975814",
          "ABC123",
          "Manager",
          "Sales",
          "123321123"
    );
  }

  private Person map(PersonRequest request) {
    Person person = new Person();
    person.setName(request.getName());
    person.setUid(UUID.randomUUID().toString());
    person.setEmail(request.getEmail());
    person.setSex(request.getSex());
    person.setPhoneNumber(request.getPhoneNumber());
    person.setStaffCode(request.getStaffCode());
    person.setPosition(request.getPosition());
    person.setDepartment(request.getDepartment());
    person.setFaceId(request.getFaceId());
    return person;
  }

}
