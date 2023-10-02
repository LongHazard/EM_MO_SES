package com.ncsgroup.ems.facade;

import com.ncsgroup.ems.configuration.ServiceConfigurationTest;
import com.ncsgroup.ems.dto.request.address.AddressRequest;
import com.ncsgroup.ems.dto.request.image.ImageRequest;
import com.ncsgroup.ems.dto.request.person.PersonFacadeRequest;
import com.ncsgroup.ems.dto.request.person.PersonRequest;
import com.ncsgroup.ems.dto.request.person.SearchPersonRequest;
import com.ncsgroup.ems.dto.request.person.card.PersonCardRequest;
import com.ncsgroup.ems.dto.response.address.AddressResponse;
import com.ncsgroup.ems.dto.response.group.IdentityGroupResponse;
import com.ncsgroup.ems.dto.response.image.ImageResponse;
import com.ncsgroup.ems.dto.response.person.PersonFacadeResponse;
import com.ncsgroup.ems.dto.response.person.PersonPageResponse;
import com.ncsgroup.ems.dto.response.person.PersonResponse;
import com.ncsgroup.ems.dto.response.personcard.PersonCardResponse;
import com.ncsgroup.ems.service.identity.GroupService;
import com.ncsgroup.ems.service.identity.ImageService;
import com.ncsgroup.ems.service.person.PersonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import com.ncsgroup.ems.dto.response.person.PersonCreateResponse;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@WebMvcTest(PersonFacadeService.class)
@ContextConfiguration(classes = {
      ServiceConfigurationTest.class
})
public class PersonFacadeServiceTests {

  @MockBean
  private PersonService personService;

  @MockBean
  private PersonCardFacadeService personCardFacadeService;

  @MockBean
  private ImageService imageService;

  @MockBean
  private GroupService groupService;

  @Autowired
  private PersonFacadeService personFacadeService;

  private static final AddressRequest addressRequest = new AddressRequest(
        "26",
        "246",
        "08761",
        "Huyen Lap Thach, Tinh Vinh Phuc"
  );


  private static final AddressResponse addressResponse = new AddressResponse(
        "Lap Thach",
        "Lap Thach",
        "lap_thach",
        "Lap Thach",
        "Lap Thach",
        "lap_thach",
        "Vinh Phuc",
        "Vinh Phuc",
        "vinh_phuc"
  );

  @Test
  public void create_InputValid_ThenReturnPersonFacadeResponse() {

    PersonFacadeRequest request = new PersonFacadeRequest();
    request.setPerson(fakePersonRequest());
    request.setPersonCard(fakePersonCardRequest());
    request.setGroupIds(List.of(1L, 2L));

    PersonRequest personRequest = request.getPerson();
    PersonCardRequest personCardRequest = request.getPersonCard();

    PersonCreateResponse personCreateResponse = fakePersonCreateResponse();
    PersonCardResponse personCardResponse = fakePersonCardResponse();


    when(personCardFacadeService.create(personCardRequest)).thenReturn(personCardResponse);
    when(personService.create(personRequest, personCardResponse.getId())).thenReturn(personCreateResponse);
    when(imageService.getAllByPerson(personCreateResponse.getId())).thenReturn(fakeImagesResponse());
    when(imageService.getAllByPersonCard(personCardResponse.getId())).thenReturn(fakeImagesResponse());
    when(groupService.getById(List.of(1L, 2L))).thenReturn(fakeGroups());

    PersonFacadeResponse personFacadeResponse = new PersonFacadeResponse(
          fakePersonCreateResponse(),
          fakePersonCardResponse(),
          fakeGroups()
    );

    PersonFacadeResponse returnFacadeResponse = personFacadeService.createPerson(request);

    System.out.println(personFacadeResponse.getPersonCard());
    System.out.println(returnFacadeResponse.getPersonCard());

    check(personFacadeResponse.getPerson(), returnFacadeResponse.getPerson());
    check(personFacadeResponse.getPersonCard(), returnFacadeResponse.getPersonCard());
    checkGroup(personFacadeResponse.getGroups(), returnFacadeResponse.getGroups());



    verify(imageService, times(1)).addImageToPersonCard(personRequest.getImages(), personCardResponse.getId());
    verify(groupService).findIdsExist(request.getGroupIds());
    verify(groupService).deleteIObyPersonId(1L);
    verify(groupService).save(1L, request.getGroupIds());
  }

  @Test
  public void create_InputPersonCardIsNull_ThenReturnPersonFacadeResponse() {
    PersonFacadeRequest request = new PersonFacadeRequest();
    request.setPerson(fakePersonRequest());
    request.setPersonCard(null);
    request.setGroupIds(List.of(1L, 2L));

    PersonRequest personRequest = request.getPerson();
    PersonCardRequest personCardRequest = request.getPersonCard();

    PersonCreateResponse personCreateResponse = fakePersonCreateResponse();
    PersonCardResponse personCardResponse = fakePersonCardResponse();


    when(personCardFacadeService.create(personCardRequest)).thenReturn(personCardResponse);
    when(personService.create(personRequest, null)).thenReturn(personCreateResponse);
    when(imageService.getAllByPerson(personCreateResponse.getId())).thenReturn(fakeImagesResponse());
    when(imageService.getAllByPersonCard(personCardResponse.getId())).thenReturn(fakeImagesResponse());
    when(groupService.getById(List.of(1L, 2L))).thenReturn(fakeGroups());

    PersonFacadeResponse personFacadeResponse = new PersonFacadeResponse(
          fakePersonCreateResponse(),
          null,
          fakeGroups()
    );

    PersonFacadeResponse returnFacadeResponse = personFacadeService.createPerson(request);

    System.out.println(personFacadeResponse.getPersonCard());
    System.out.println(returnFacadeResponse.getPersonCard());

    check(personFacadeResponse.getPerson(), returnFacadeResponse.getPerson());
    checkGroup(personFacadeResponse.getGroups(), returnFacadeResponse.getGroups());
    Assertions.assertNull(personFacadeResponse.getPersonCard());
    Assertions.assertNull(returnFacadeResponse.getPersonCard());

    System.out.println(personFacadeResponse.getGroups());
    System.out.println(returnFacadeResponse.getGroups());

    verify(imageService, times(1)).addImageToPerson(personRequest.getImages(), personCreateResponse.getId());
    verify(groupService).findIdsExist(request.getGroupIds());
    verify(groupService).deleteIObyPersonId(1L);
    verify(groupService).save(1L, request.getGroupIds());
  }

  @Test
  public void create_InputGroupIsNull_ThenReturnPersonFacadeResponse() {
    PersonFacadeRequest request = new PersonFacadeRequest();
    request.setPerson(fakePersonRequest());
    request.setPersonCard(fakePersonCardRequest());
    request.setGroupIds(null);

    PersonRequest personRequest = request.getPerson();
    PersonCardRequest personCardRequest = request.getPersonCard();

    PersonCreateResponse personCreateResponse = fakePersonCreateResponse();
    PersonCardResponse personCardResponse = fakePersonCardResponse();


    when(personCardFacadeService.create(personCardRequest)).thenReturn(personCardResponse);
    when(personService.create(personRequest, personCardResponse.getId())).thenReturn(personCreateResponse);
    when(imageService.getAllByPerson(personCreateResponse.getId())).thenReturn(fakeImagesResponse());
    when(imageService.getAllByPersonCard(personCardResponse.getId())).thenReturn(fakeImagesResponse());

    PersonFacadeResponse personFacadeResponse = new PersonFacadeResponse(
          fakePersonCreateResponse(),
          fakePersonCardResponse(),
          new ArrayList<>()
    );

    PersonFacadeResponse returnFacadeResponse = personFacadeService.createPerson(request);

    System.out.println(personFacadeResponse.getPersonCard());
    System.out.println(returnFacadeResponse.getPersonCard());

    check(personFacadeResponse.getPerson(), returnFacadeResponse.getPerson());
    check(personFacadeResponse.getPersonCard(), returnFacadeResponse.getPersonCard());
    assertThat(personFacadeResponse.getGroups()).isEmpty();
    assertThat(returnFacadeResponse.getGroups()).isEmpty();

    System.out.println(personFacadeResponse.getGroups());
    System.out.println(returnFacadeResponse.getGroups());

    verify(imageService, times(1)).addImageToPerson(personRequest.getImages(), personCreateResponse.getId());
  }

  @Test
  public void create_InputPersonCardImageIsNull_ThenReturnPersonFacadeResponse() {
    PersonFacadeRequest request = new PersonFacadeRequest();
    request.setPerson(fakePersonRequest());
    request.setPersonCard(fakePersonCardRequest());
    request.setGroupIds(null);

    PersonRequest personRequest = request.getPerson();
    PersonCardRequest personCardRequest = request.getPersonCard();

    PersonCreateResponse personCreateResponse = fakePersonCreateResponse();
    PersonCardResponse personCardResponse = fakePersonCardResponse();


    when(personCardFacadeService.create(personCardRequest)).thenReturn(personCardResponse);
    when(personService.create(personRequest, personCardResponse.getId())).thenReturn(personCreateResponse);
    when(imageService.getAllByPerson(personCreateResponse.getId())).thenReturn(fakeImagesResponse());
    when(imageService.getAllByPersonCard(personCardResponse.getId())).thenReturn(fakeImagesResponse());

    PersonFacadeResponse personFacadeResponse = new PersonFacadeResponse(
          fakePersonCreateResponse(),
          fakePersonCardResponse(),
          new ArrayList<>()
    );

    PersonFacadeResponse returnFacadeResponse = personFacadeService.createPerson(request);

    System.out.println(personFacadeResponse.getPersonCard());
    System.out.println(returnFacadeResponse.getPersonCard());

    check(personFacadeResponse.getPerson(), returnFacadeResponse.getPerson());
    check(personFacadeResponse.getPersonCard(), returnFacadeResponse.getPersonCard());
    assertThat(personFacadeResponse.getGroups()).isEmpty();
    assertThat(returnFacadeResponse.getGroups()).isEmpty();

    System.out.println(personFacadeResponse.getGroups());
    System.out.println(returnFacadeResponse.getGroups());

    verify(imageService, times(1)).addImageToPerson(personRequest.getImages(), personCreateResponse.getId());
  }

  @Test
  public void getPerson_InputValid_ThenReturnPersonFacadeResponse() {

    PersonCreateResponse personCreateResponse = fakePersonCreateResponse();
    PersonCardResponse personCardResponse = fakePersonCardResponse();


    when(personService.detail(1L)).thenReturn(fakePersonCreateResponse());
    when(personService.getPersonCardId(1L)).thenReturn(1L);
    when(personCardFacadeService.get(1L)).thenReturn(fakePersonCardResponse());
    when(imageService.getAllByPersonCard(personCardResponse.getId())).thenReturn(fakeImagesResponse());
    when(imageService.getAllByPerson(personCreateResponse.getId())).thenReturn(fakeImagesResponse());

    when(groupService.getByPersonId(1L)).thenReturn(List.of(1L,2L));
    when(groupService.getById(List.of(1L,2L))).thenReturn(fakeGroups());

    PersonFacadeResponse personFacadeResponse = new PersonFacadeResponse(
          fakePersonCreateResponse(),
          fakePersonCardResponse(),
          fakeGroups()
    );

    PersonFacadeResponse returnFacadeResponse = personFacadeService.getPerson(1L);

    System.out.println(personFacadeResponse.getPersonCard());
    System.out.println(returnFacadeResponse.getPersonCard());

    check(personFacadeResponse.getPerson(), returnFacadeResponse.getPerson());
    check(personFacadeResponse.getPersonCard(), returnFacadeResponse.getPersonCard());
    checkGroup(personFacadeResponse.getGroups(), returnFacadeResponse.getGroups());

    System.out.println(personFacadeResponse.getGroups());
    System.out.println(returnFacadeResponse.getGroups());

  }

  @Test
  public void getPerson_InputValidAndPersonCardIdIsNull_ThenReturnPersonFacadeResponse() {

    PersonCreateResponse personCreateResponse = fakePersonCreateResponse();

    when(personService.detail(1L)).thenReturn(fakePersonCreateResponse());
    when(personService.getPersonCardId(1L)).thenReturn(null);
    when(personCardFacadeService.get(1L)).thenReturn(fakePersonCardResponse());
    when(imageService.getAllByPerson(personCreateResponse.getId())).thenReturn(fakeImagesResponse());
    when(groupService.getByPersonId(1L)).thenReturn(List.of(1L,2L));
    when(groupService.getById(List.of(1L,2L))).thenReturn(fakeGroups());

    PersonFacadeResponse personFacadeResponse = new PersonFacadeResponse(
          fakePersonCreateResponse(),
          null,
          fakeGroups()
    );

    PersonFacadeResponse returnFacadeResponse = personFacadeService.getPerson(1L);

    System.out.println(personFacadeResponse.getPersonCard());
    System.out.println(returnFacadeResponse.getPersonCard());

    check(personFacadeResponse.getPerson(), returnFacadeResponse.getPerson());
    Assertions.assertNull(personFacadeResponse.getPersonCard());
    Assertions.assertNull(returnFacadeResponse.getPersonCard());
    checkGroup(personFacadeResponse.getGroups(), returnFacadeResponse.getGroups());

  }


  @Test
  public void listPersons_InputValid_ThenReturnPersonPageResponse() {

    List<PersonResponse> personResponses = new ArrayList<>();
    personResponses.add(new PersonResponse());
    PersonPageResponse personPageResponse = PersonPageResponse.of(List.of(fakePersonResponse()), 1);


    SearchPersonRequest request = new SearchPersonRequest("keyword", List.of("fid1","fid2"));
    when(personService.list(request,10,0,false)).thenReturn(personPageResponse);
    when(personService.countVehicle(1L)).thenReturn(0);
    when(imageService.getByPersonId(1L)).thenReturn(fakeImagesResponse().get(0));

    PersonPageResponse returnPersonPageResponse = personFacadeService.listPersons(request, 10, 0, false);

    assertThat(personPageResponse.getCountPersons()).isEqualTo(returnPersonPageResponse.getCountPersons());
    assertThat(personPageResponse.getPersons().get(0)).isEqualTo(returnPersonPageResponse.getPersons().get(0));
  }

  @Test
  public void listPersons_InputValidIsAllIsTrue_ThenReturnPersonPageResponse() {

    List<PersonResponse> personResponses = new ArrayList<>();
    personResponses.add(new PersonResponse());
    PersonPageResponse personPageResponse = PersonPageResponse.of(List.of(fakePersonResponse()), 1);


    SearchPersonRequest request = new SearchPersonRequest("keyword", List.of("fid1","fid2"));
    when(personService.list(request,10,0,true)).thenReturn(personPageResponse);
    when(personService.countVehicle(1L)).thenReturn(0);
    when(imageService.getByPersonId(1L)).thenReturn(fakeImagesResponse().get(0));

    PersonPageResponse returnPersonPageResponse = personFacadeService.listPersons(request, 10, 0, true);

    assertThat(personPageResponse.getCountPersons()).isEqualTo(returnPersonPageResponse.getCountPersons());
    assertThat(personPageResponse.getPersons().get(0)).isEqualTo(returnPersonPageResponse.getPersons().get(0));
  }

  @Test
  public void listPersonsByGroup_InputValid_ThenReturnPersonPageResponse() {

    List<PersonResponse> personResponses = new ArrayList<>();
    personResponses.add(new PersonResponse());

    PersonPageResponse personPageResponse = PersonPageResponse.of(List.of(fakePersonResponse()), 1);

    when(personService.listByGroup(1L,0,10)).thenReturn(personPageResponse);
    when(imageService.getByPersonId(1L)).thenReturn(fakeImagesResponse().get(0));

    PersonPageResponse returnPersonPageResponse = personFacadeService.listPersonsByGroup(1L, 0, 10);

    assertThat(personPageResponse.getCountPersons()).isEqualTo(returnPersonPageResponse.getCountPersons());
    assertThat(personPageResponse.getPersons().get(0)).isEqualTo(returnPersonPageResponse.getPersons().get(0));
  }

  @Test
  public void updatePerson_InputValid_ThenReturnPersonFacadeResponse() {

    Long updateId = 1L;
    PersonFacadeRequest personFacadeRequest = new PersonFacadeRequest();

    personFacadeRequest.setGroupIds(List.of(1L,2L));

    PersonRequest personRequest = fakePersonRequest();
    PersonCardRequest personCardRequest = fakePersonCardRequest();

    personFacadeRequest.setPerson(personRequest);
    personFacadeRequest.setPersonCard(personCardRequest);

    PersonCreateResponse personCreateResponse = fakePersonCreateResponse();
    PersonCardResponse personCardResponse = fakePersonCardResponse();

    when(personService.update(updateId, personRequest)).thenReturn(personCreateResponse);
    when(imageService.getByRequests(personRequest.getImages())).thenReturn(fakeImagesResponse());
    when(imageService.getIdByPerson(personCreateResponse.getId())).thenReturn(List.of(1L,2L));
    when(personService.getPersonCardId(updateId)).thenReturn(2L);
    when(personCardFacadeService.update(2L, personCardRequest)).thenReturn(personCardResponse);
    when(imageService.getAllByPersonCard(personCardResponse.getId())).thenReturn(fakeImagesResponse());
    when(groupService.getById(personFacadeRequest.getGroupIds())).thenReturn(fakeGroups());


    PersonFacadeResponse returnPersonFacadeResponse = personFacadeService.updatePerson(updateId, personFacadeRequest);
    PersonFacadeResponse personFacadeResponse = new PersonFacadeResponse(
          fakePersonCreateResponse(),
          fakePersonCardResponse(),
          fakeGroups()
    );

    check(personFacadeResponse.getPerson(), returnPersonFacadeResponse.getPerson());
    check(personFacadeResponse.getPersonCard(), returnPersonFacadeResponse.getPersonCard());
    checkGroup(personFacadeResponse.getGroups(), returnPersonFacadeResponse.getGroups());


    verify(imageService, times(1)).addImageToPersonCard(personRequest.getImages(), personCardResponse.getId());
    verify(imageService).update(List.of(1L,2L), personFacadeRequest.getPerson().getImages());
    verify(imageService).addImageToPerson(personRequest.getImages(), personCreateResponse.getId());
    verify(groupService).findIdsExist(personFacadeRequest.getGroupIds());
    verify(groupService).deleteIObyPersonId(1L);
    verify(groupService).save(1L, personFacadeRequest.getGroupIds());
  }


  @Test
  public void updatePerson_InputPersonCardIdsNull_ThenReturnPersonFacadeResponse() {

    Long updateId = 1L;
    PersonFacadeRequest personFacadeRequest = new PersonFacadeRequest();

    personFacadeRequest.setGroupIds(List.of(1L,2L));

    PersonRequest personRequest = fakePersonRequest();
    PersonCardRequest personCardRequest = fakePersonCardRequest();

    personFacadeRequest.setPerson(personRequest);
    personFacadeRequest.setPersonCard(personCardRequest);

    PersonCreateResponse personCreateResponse = fakePersonCreateResponse();
    PersonCardResponse personCardResponse = fakePersonCardResponse();

    when(personService.update(updateId, personRequest)).thenReturn(personCreateResponse);
    when(imageService.getByRequests(personRequest.getImages())).thenReturn(fakeImagesResponse());
    when(imageService.getIdByPerson(personCreateResponse.getId())).thenReturn(List.of(1L,2L));
    when(personService.getPersonCardId(updateId)).thenReturn(null);
    when(personCardFacadeService.create(personCardRequest)).thenReturn(personCardResponse);
    when(imageService.getAllByPersonCard(personCardResponse.getId())).thenReturn(fakeImagesResponse());
    when(groupService.getById(personFacadeRequest.getGroupIds())).thenReturn(fakeGroups());


    PersonFacadeResponse returnPersonFacadeResponse = personFacadeService.updatePerson(updateId, personFacadeRequest);
    PersonFacadeResponse personFacadeResponse = new PersonFacadeResponse(
          fakePersonCreateResponse(),
          fakePersonCardResponse(),
          fakeGroups()
    );

    check(personFacadeResponse.getPerson(), returnPersonFacadeResponse.getPerson());
    check(personFacadeResponse.getPersonCard(), returnPersonFacadeResponse.getPersonCard());
    checkGroup(personFacadeResponse.getGroups(), returnPersonFacadeResponse.getGroups());


    verify(imageService, times(1)).addImageToPersonCard(personRequest.getImages(), personCardResponse.getId());
    verify(imageService).update(List.of(1L,2L), personFacadeRequest.getPerson().getImages());
    verify(imageService).addImageToPerson(personRequest.getImages(), personCreateResponse.getId());
    verify(groupService).findIdsExist(personFacadeRequest.getGroupIds());
    verify(groupService).deleteIObyPersonId(1L);
    verify(groupService).save(1L, personFacadeRequest.getGroupIds());
  }

  @Test
  public void getPersonByVehicle_InputValid_ThenReturnPersonFacadeResponse() {


    PersonCreateResponse personCreateResponse = fakePersonCreateResponse();
    PersonCardResponse personCardResponse = fakePersonCardResponse();

    PersonFacadeResponse personFacadeResponse = new PersonFacadeResponse(
          fakePersonCreateResponse(),
          fakePersonCardResponse(),
          fakeGroups()
    );
    when(personService.getIdByVehicle(11L)).thenReturn(1L);
    when(personService.detail(1L)).thenReturn(fakePersonCreateResponse());
    when(personService.getPersonCardId(1L)).thenReturn(1L);
    when(personCardFacadeService.get(1L)).thenReturn(fakePersonCardResponse());
    when(imageService.getAllByPersonCard(personCardResponse.getId())).thenReturn(fakeImagesResponse());
    when(imageService.getAllByPerson(personCreateResponse.getId())).thenReturn(fakeImagesResponse());

    when(groupService.getByPersonId(1L)).thenReturn(List.of(1L,2L));
    when(groupService.getById(List.of(1L,2L))).thenReturn(fakeGroups());

    PersonFacadeResponse returnPersonFacadeResponse = personFacadeService.getPersonByVehicle(11L);

    check(personFacadeResponse.getPerson(), returnPersonFacadeResponse.getPerson());
    check(personFacadeResponse.getPersonCard(), returnPersonFacadeResponse.getPersonCard());
    checkGroup(personFacadeResponse.getGroups(), returnPersonFacadeResponse.getGroups());

  }



  private PersonCardRequest fakePersonCardRequest() {
    PersonCardRequest request = new PersonCardRequest();
    request.setCardTypeId(2L);
    request.setCardCode("123456789");
    request.setDateOfBirth("22/12/2001");
    request.setSex("male");
    request.setNationality("Viet Nam");
    request.setIssueOn(1122334455L);
    request.setDateOfExpiry("22/12/2027");
    request.setPlaceOfIssue("Ha Noi");
    request.setPermanentResident(addressRequest);
    request.setPlaceOfOrigin(addressRequest);

    request.setImages(fakeImageSRequest());

    return request;
  }

  private PersonResponse fakePersonResponse() {
    return new PersonResponse(
          1L,
          "John Doe",
          "68fce692-c4d3-47f1-9073-a68c07f48773",
          "Male",
          "john.doe@example.com",
          "0987975814",
          "ABC123",
          "Manager",
          "Sales",
          "123321123"
    );
  }

  private PersonCardResponse fakePersonCardResponse() {
    PersonCardResponse response = new PersonCardResponse();
    response.setId(2L);
    response.setCardCode("123456789");
    response.setDateOfBirth("22/12/2001");
    response.setSex("male");
    response.setNationality("Viet Nam");
    response.setIssueOn(1122334455L);
    response.setDateOfExpiry("22/12/2027");
    response.setPlaceOfIssue("Ha Noi");
    response.setPlaceOfOrigin(addressResponse);
    response.setPermanentResident(addressResponse);

    response.setImages(fakeImagesResponse());

    return response;
  }

  private PersonRequest fakePersonRequest() {
    PersonRequest request = new PersonRequest(
          "John Doe",
          "john.doe@example.com",
          "Male",
          "0987975814",
          "ABC123",
          "Manager",
          "Sales",
          "123321123"
    );

    request.setImages(fakeImageSRequest());

    return request;
  }

  private PersonCreateResponse fakePersonCreateResponse() {

    PersonCreateResponse personCreateResponse = new PersonCreateResponse(
          1L,
          "John Doe",
          "68fce692-c4d3-47f1-9073-a68c07f48773",
          "Male",
          "john.doe@example.com",
          "0987975814",
          "ABC123",
          "Manager",
          "Sales",
          "123321123"
    );

    personCreateResponse.setImages(fakeImagesResponse());

    return personCreateResponse;
  }

  private List<ImageRequest> fakeImageSRequest() {
    List<ImageRequest> imageRequests = new ArrayList<>();
    imageRequests.add(new ImageRequest(1L, "front"));
    imageRequests.add(new ImageRequest(2L, "back"));
    return imageRequests;
  }

  private List<ImageResponse> fakeImagesResponse() {
    List<ImageResponse> imageResponses = new ArrayList<>();
    imageResponses.add(new ImageResponse(1L, "url1", "ct1", "front"));
    imageResponses.add(new ImageResponse(2L, "url2", "ct2", "back"));

    return imageResponses;
  }

  private List<IdentityGroupResponse> fakeGroups() {
    List<IdentityGroupResponse> groups = new ArrayList<>();
    groups.add(new IdentityGroupResponse(1L, "Thanh Xuan"));
    groups.add(new IdentityGroupResponse(2L, "Ha Dong"));

    return groups;
  }

  private void check(PersonCreateResponse response1, PersonCreateResponse response2) {
    assertThat(response1.getId()).isEqualTo(response2.getId());
    assertThat(response1.getName()).isEqualTo(response2.getName());
    assertThat(response1.getSex()).isEqualTo(response2.getSex());
    assertThat(response1.getEmail()).isEqualTo(response2.getEmail());
    assertThat(response1.getPhoneNumber()).isEqualTo(response2.getPhoneNumber());
    assertThat(response1.getStaffCode()).isEqualTo(response2.getStaffCode());
    assertThat(response1.getPosition()).isEqualTo(response2.getPosition());
    assertThat(response1.getDepartment()).isEqualTo(response2.getDepartment());
    assertThat(response1.getFaceId()).isEqualTo(response2.getFaceId());

    checkListImage(response1.getImages(), response2.getImages());
  }

  private void check(PersonCardResponse response1, PersonCardResponse response2) {
    assertThat(response1.getId()).isEqualTo(response2.getId());
    assertThat(response1.getCardType()).isEqualTo(response2.getCardType());
    assertThat(response1.getCardCode()).isEqualTo(response2.getCardCode());
    assertThat(response1.getDateOfBirth()).isEqualTo(response2.getDateOfBirth());
    assertThat(response1.getSex()).isEqualTo(response2.getSex());
    assertThat(response1.getNationality()).isEqualTo(response2.getNationality());
    assertThat(response1.getIssueOn()).isEqualTo(response2.getIssueOn());
    assertThat(response1.getDateOfExpiry()).isEqualTo(response2.getDateOfExpiry());
    assertThat(response1.getPlaceOfIssue()).isEqualTo(response2.getPlaceOfIssue());

    checkListImage(response1.getImages(), response2.getImages());

  }

  private void checkGroup(List<IdentityGroupResponse> g1, List<IdentityGroupResponse> g2) {
    assertThat(g1.size()).isEqualTo(g2.size());

    for (int i = 0; i < g1.size(); i++) {
      assertThat(g1.get(i).getId()).isEqualTo(g2.get(i).getId());
      assertThat(g1.get(i).getName()).isEqualTo(g2.get(i).getName());
    }
  }

  private void checkImageResponse(ImageResponse i1, ImageResponse i2) {
    assertThat(i1.getId()).isEqualTo(i2.getId());
    assertThat(i1.getUrl()).isEqualTo(i2.getUrl());
    assertThat(i1.getContentType()).isEqualTo(i2.getContentType());
    assertThat(i2.getType()).isEqualTo(i2.getType());
  }

  private void checkListImage(List<ImageResponse> l1, List<ImageResponse> l2) {
    assertThat(l1.size()).isEqualTo(l2.size());

    for (int i=0; i<l1.size(); i++) {
      checkImageResponse(l1.get(i), l2.get(i));
    }
  }
}
