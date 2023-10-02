package com.ncsgroup.ems.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncsgroup.ems.configuration.ServiceConfigurationTest;
import com.ncsgroup.ems.dto.request.address.AddressRequest;
import com.ncsgroup.ems.dto.request.image.ImageRequest;
import com.ncsgroup.ems.dto.request.person.PersonFacadeRequest;
import com.ncsgroup.ems.dto.request.person.PersonRequest;
import com.ncsgroup.ems.dto.request.person.PersonSearchRequest;
import com.ncsgroup.ems.dto.request.person.SearchPersonRequest;
import com.ncsgroup.ems.dto.request.person.card.PersonCardRequest;
import com.ncsgroup.ems.dto.response.address.AddressResponse;
import com.ncsgroup.ems.dto.response.group.IdentityGroupResponse;
import com.ncsgroup.ems.dto.response.image.ImageResponse;
import com.ncsgroup.ems.dto.response.person.*;
import com.ncsgroup.ems.dto.response.personcard.PersonCardResponse;
import com.ncsgroup.ems.dto.response.vehicle.VehicleFacadeResponse;
import com.ncsgroup.ems.dto.response.vehicle.VehicleResponse;
import com.ncsgroup.ems.dto.response.vehicle.vehiclecard.VehicleCardResponse;
import com.ncsgroup.ems.exception.person.EmailAlreadyExistException;
import com.ncsgroup.ems.exception.person.PersonNotFoundException;
import com.ncsgroup.ems.exception.person.PhoneNumberAlreadyExistException;
import com.ncsgroup.ems.facade.PersonFacadeService;
import com.ncsgroup.ems.facade.VehicleFacadeService;
import com.ncsgroup.ems.service.MessageService;
import com.ncsgroup.ems.service.person.PersonService;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import static com.ncsgroup.ems.constanst.EMSConstants.MessageCode.CREATE_PERSON_SUCCESS;
import static com.ncsgroup.ems.constanst.EMSConstants.CommonConstants.*;

@WebMvcTest(PersonController.class)
@ContextConfiguration(classes = ServiceConfigurationTest.class)
public class PersonControllerTests {

  private static final String END_POINT_PATH = "/api/v1/persons";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private PersonFacadeService facadeService;

  @MockBean
  private MessageService messageService;

  @Autowired
  private PersonController personController;

  @MockBean
  private PersonService personService;

  @MockBean
  private VehicleFacadeService vehicleFacadeService;


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
  public void create_InputIsNull_Return400BadRequest() throws Exception {

    String bodyContent = objectMapper.writeValueAsString(null);

    Mockito.when(messageService.getMessage(CREATE_PERSON_SUCCESS, "en"))
          .thenReturn("create person successfully");

    mockMvc.perform(post(END_POINT_PATH).contentType("application/json").
                content(bodyContent)).andExpect(status().isBadRequest())
          .andDo(print());
  }

  @Test
  public void create_InputValid_Return201CREATED() throws Exception {
    PersonFacadeRequest request = new PersonFacadeRequest();
    PersonRequest personRequest = new PersonRequest();
    personRequest.setName("Nguyen Minh Hieu");
    request.setPerson(personRequest);


    PersonFacadeResponse response = new PersonFacadeResponse();
    PersonCreateResponse personCreateResponse = new PersonCreateResponse();
    personCreateResponse.setName("Nguyen Minh Hieu");
    response.setPerson(personCreateResponse);
    response.setPersonCard(null);

    String bodyContent = objectMapper.writeValueAsString(request);

    System.out.println(objectMapper.writeValueAsString(response));
    Mockito.when(facadeService.createPerson(request)).thenReturn(response);
    Mockito.when(messageService.getMessage(CREATE_PERSON_SUCCESS, "en"))
          .thenReturn("create person successfully");


    MvcResult mvcResult = mockMvc.perform(post(END_POINT_PATH).contentType("application/json").
                content(bodyContent))
          .andExpect(status().isOk())
          .andDo(print())
          .andReturn();

    String actual = mvcResult.getResponse().getContentAsString();
    String expect = objectMapper.writeValueAsString(personController.create(request, "en"));

    Assertions.assertThat(actual).isEqualTo(expect);
  }

  @Test
  public void create_InputEmailInValid_Return400BadRequest() throws Exception {
    PersonFacadeRequest request = new PersonFacadeRequest();
    PersonRequest personRequest = new PersonRequest();
    personRequest.setName("Nguyen Minh Hieu");
    personRequest.setEmail("hieu123");
    request.setPerson(personRequest);

    String bodyContent = objectMapper.writeValueAsString(request);


    Mockito.when(messageService.getMessage(CREATE_PERSON_SUCCESS, "en"))
          .thenReturn("create person successfully");


    mockMvc.perform(post(END_POINT_PATH).contentType("application/json").
                content(bodyContent))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.data.detail", CoreMatchers.is("Invalid Email")))
          .andDo(print())
          .andReturn();
  }

  @Test
  public void create_InputPhoneNumberInvalid_Return400BadRequest() throws Exception {
    PersonFacadeRequest request = new PersonFacadeRequest();
    PersonRequest personRequest = new PersonRequest();
    personRequest.setName("Nguyen Minh Hieu");
    personRequest.setPhoneNumber("113");
    request.setPerson(personRequest);

    String bodyContent = objectMapper.writeValueAsString(request);


    Mockito.when(messageService.getMessage(CREATE_PERSON_SUCCESS, "en"))
          .thenReturn("create person successfully");


    mockMvc.perform(post(END_POINT_PATH).contentType("application/json").
                content(bodyContent))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.data.detail", is("Invalid Phone Number")))
          .andDo(print())
          .andReturn();
  }

  @Test
  public void create_InputEmailAlreadyExisted_Return400BadRequest() throws Exception {
    PersonFacadeRequest request = new PersonFacadeRequest();
    PersonRequest personRequest = new PersonRequest();
    personRequest.setName("Nguyen Minh Hieu");
    personRequest.setPhoneNumber("0987928314");
    personRequest.setEmail("hieunm123.ptit@gmail.com");
    request.setPerson(personRequest);

    String bodyContent = objectMapper.writeValueAsString(request);


    Mockito.when(messageService.getMessage(CREATE_PERSON_SUCCESS, "en"))
          .thenReturn("create person successfully");

    Mockito.when(facadeService.createPerson(request))
          .thenThrow(new EmailAlreadyExistException());


    mockMvc.perform(post(END_POINT_PATH).contentType("application/json").
                content(bodyContent))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.data.code", is("com.ncsgroup.ems.exception.person.EmailAlreadyExistException")))
          .andDo(print())
          .andReturn();
  }

  @Test
  public void create_InputPhoneNumberAlreadyExisted_Return400BadRequest() throws Exception {
    PersonFacadeRequest request = new PersonFacadeRequest();
    PersonRequest personRequest = new PersonRequest();
    personRequest.setName("Nguyen Minh Hieu");
    personRequest.setPhoneNumber("0987928314");
    personRequest.setEmail("hieunm123.ptit@gmail.com");
    request.setPerson(personRequest);

    String bodyContent = objectMapper.writeValueAsString(request);


    Mockito.when(messageService.getMessage(CREATE_PERSON_SUCCESS, "en"))
          .thenReturn("create person successfully");

    Mockito.when(facadeService.createPerson(request))
          .thenThrow(new PhoneNumberAlreadyExistException());


    mockMvc.perform(post(END_POINT_PATH).contentType("application/json").
                content(bodyContent))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.data.code", is("com.ncsgroup.ems.exception.person.PhoneNumberAlreadyExistException")))
          .andExpect(jsonPath("$.message", is("Bad Request")))
          .andDo(print())
          .andReturn();
  }


  @Test
  public void list_InputValid_ThenReturn_PersonPageResponse() throws Exception {

    PersonResponse person1 = fakePersonResponse();
    PersonResponse person2 = fakePersonResponse1();
    PersonPageResponse personPageResponse = PersonPageResponse.of(List.of(person1, person2), 2);

    SearchPersonRequest request = new SearchPersonRequest("abc", null);

    String bodyContent = objectMapper.writeValueAsString(request);

    when(facadeService.listPersons(request, 10, 0, false)).thenReturn(personPageResponse);
    when(messageService.getMessage(SUCCESS, "en"))
          .thenReturn("success");

    mockMvc.perform(post(END_POINT_PATH + "/search").contentType("application/json")
                .content(bodyContent))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data.persons[0].uid", CoreMatchers.is("68fce692-c4d3-47f1-9073-a68c07f48773")))
          .andExpect(jsonPath("$.data.persons[1].uid", CoreMatchers.is("db4656c1-e3af-43ee-8886-921037278960")))
          .andDo(print());
  }

  @Test
  public void listAll_InputValid_ThenReturn_PersonPageResponse() throws Exception {

    PersonDTO person1 = new PersonDTO(1L, "68fce692-c4d3-47f1-9073-a68c07f48773", "Ronaldo");
    PersonDTO person2 = new PersonDTO(2L, "db4656c1-e3af-43ee-8886-921037278960", "Messi");
    PagePersonResponse response = PagePersonResponse.of(List.of(person1, person2), 2);

    PersonSearchRequest request = new PersonSearchRequest("abc");

    String bodyContent = objectMapper.writeValueAsString(request);

    when(personService.list(request, 0, 10, false)).thenReturn(response);
    when(messageService.getMessage(SUCCESS, "en"))
          .thenReturn("success");

    mockMvc.perform(post(END_POINT_PATH + "/list").contentType("application/json")
                .content(bodyContent))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data.persons[0].uid", CoreMatchers.is("68fce692-c4d3-47f1-9073-a68c07f48773")))
          .andExpect(jsonPath("$.data.persons[1].uid", CoreMatchers.is("db4656c1-e3af-43ee-8886-921037278960")))
          .andExpect(jsonPath("$.data.persons[0].name", CoreMatchers.is("Ronaldo")))
          .andExpect(jsonPath("$.data.persons[1].name", CoreMatchers.is("Messi")))
          .andDo(print());
  }

  @Test
  public void listByGroup_InputValid_ThenReturn_PersonPageResponse() throws Exception {

    PersonResponse person1 = fakePersonResponse();
    PersonResponse person2 = fakePersonResponse1();
    PersonPageResponse personPageResponse = PersonPageResponse.of(List.of(person1, person2), 2);

    SearchPersonRequest request = new SearchPersonRequest("abc", null);

    String bodyContent = objectMapper.writeValueAsString(request);

    when(facadeService.listPersonsByGroup(1L, 0, 10)).thenReturn(personPageResponse);
    when(messageService.getMessage(SUCCESS, "en"))
          .thenReturn("success");

    mockMvc.perform(get(END_POINT_PATH + "/group/1").contentType("application/json")
                .content(bodyContent))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data.persons[0].uid", CoreMatchers.is("68fce692-c4d3-47f1-9073-a68c07f48773")))
          .andExpect(jsonPath("$.data.persons[1].uid", CoreMatchers.is("db4656c1-e3af-43ee-8886-921037278960")))
          .andDo(print());
  }

  @Test
  public void get_InputNotFound_ThenReturn404NotFound() throws Exception {
    when(facadeService.getPerson(1L)).thenThrow(new PersonNotFoundException());

    mockMvc.perform(get(END_POINT_PATH + "/1"))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.data.code", CoreMatchers.is("com.ncsgroup.ems.exception.person.PersonNotFoundException")))
          .andDo(print());
  }

  @Test
  public void get_InputValid_ThenReturn200OK() throws Exception {
    PersonFacadeResponse personFacadeResponse = new PersonFacadeResponse(
          fakePersonCreateResponse(),
          fakePersonCardResponse()
    );

    when(facadeService.getPerson(1L)).thenReturn(personFacadeResponse);
    when(messageService.getMessage(SUCCESS, "en"))
          .thenReturn("success");

    mockMvc.perform(get(END_POINT_PATH + "/1"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data.person.uid", CoreMatchers.is("68fce692-c4d3-47f1-9073-a68c07f48773")))
          .andExpect(jsonPath("$.data.person_card.card_code", CoreMatchers.is("123456789")))
          .andDo(print());

  }

  @Test
  public void update_InputInValid_ThenReturn400BadRequest() throws Exception {
    PersonFacadeRequest request = new PersonFacadeRequest(
          fakePersonRequest(),
          fakePersonCardRequest(),
          List.of(1L, 2L)
    );

    PersonFacadeResponse response = new PersonFacadeResponse(
          fakePersonCreateResponse(),
          fakePersonCardResponse()
    );


    String bodyContent = objectMapper.writeValueAsString(request);

    when(facadeService.updatePerson(1L, request)).thenReturn(response);
    when(messageService.getMessage(SUCCESS, "en"))
          .thenReturn("success");

    MvcResult result = mockMvc.perform(put(END_POINT_PATH + "/1").contentType("application/json")
                .content(bodyContent))
          .andExpect(status().isOk())
          .andDo(print())
          .andReturn();


    String expected = objectMapper.writeValueAsString(personController.update(1L, request,"en"));
    String actual = result.getResponse().getContentAsString();

    Assertions.assertThat(actual).isEqualTo(expected);

  }

  @Test
  public void update_InputIdNotFound_ThenReturn404NotFound() throws Exception {
    PersonFacadeRequest request = new PersonFacadeRequest(
          fakePersonRequest(),
          fakePersonCardRequest(),
          List.of(1L, 2L)
    );

    String bodyContent = objectMapper.writeValueAsString(request);

    when(facadeService.updatePerson(1L, request)).thenThrow(new PersonNotFoundException());

    mockMvc.perform(put(END_POINT_PATH + "/1").contentType("application/json").content(bodyContent))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.data.code", is("com.ncsgroup.ems.exception.person.PersonNotFoundException")))
          .andDo(print());
  }

  @Test
  public void update_InputEmailAlreadyExisted_Return400BadRequest() throws Exception {
    PersonFacadeRequest request = new PersonFacadeRequest(
          fakePersonRequest(),
          fakePersonCardRequest(),
          List.of(1L, 2L)
    );

    String bodyContent = objectMapper.writeValueAsString(request);

    Mockito.when(facadeService.updatePerson(1L ,request))
          .thenThrow(new EmailAlreadyExistException());


    mockMvc.perform(put(END_POINT_PATH + "/1").contentType("application/json").
                content(bodyContent))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.data.code", is("com.ncsgroup.ems.exception.person.EmailAlreadyExistException")))
          .andDo(print())
          .andReturn();
  }

  @Test
  public void update_InputPhoneNumberAlreadyExisted_Return400BadRequest() throws Exception {
    PersonFacadeRequest request = new PersonFacadeRequest(
          fakePersonRequest(),
          fakePersonCardRequest(),
          List.of(1L, 2L)
    );

    String bodyContent = objectMapper.writeValueAsString(request);

    Mockito.when(facadeService.updatePerson(1L ,request))
          .thenThrow(new PhoneNumberAlreadyExistException());


    mockMvc.perform(put(END_POINT_PATH + "/1").contentType("application/json").
                content(bodyContent))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.data.code", is("com.ncsgroup.ems.exception.person.PhoneNumberAlreadyExistException")))
          .andDo(print())
          .andReturn();
  }

  @Test
  public void getListDepartment_InputValid_ThenReturn200OK() throws Exception{

    Set<String> sets = new HashSet<>();
    sets.add("dp1");
    sets.add("dp2");

    when(personService.getDepartment("keyword")).thenReturn(sets);

    mockMvc.perform(get(END_POINT_PATH+"/department").param("keyword","keyword"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data.[0]", is("dp1")))
          .andExpect(jsonPath("$.data.[1]", is("dp2")))
          .andDo(print());
  }

  @Test
  public void getListPosition_InputValid_ThenReturn200OK() throws Exception{

    Set<String> sets = new HashSet<>();
    sets.add("position1");
    sets.add("position2");

    when(personService.getPosition("keyword")).thenReturn(sets);

    mockMvc.perform(get(END_POINT_PATH+"/position").param("keyword","keyword"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data.[0]", is("position1")))
          .andExpect(jsonPath("$.data.[1]", is("position2")))
          .andDo(print());
  }

  @Test
  public void getListOrg_InputValid_ThenReturn200OK() throws Exception{

    Set<String> sets = new HashSet<>();
    sets.add("org1");
    sets.add("org2");

    when(personService.getOrg("keyword")).thenReturn(sets);

    mockMvc.perform(get(END_POINT_PATH+"/org").param("keyword","keyword"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data.[0]", is("org1")))
          .andExpect(jsonPath("$.data.[1]", is("org2")))
          .andDo(print());
  }

  @Test
  public void getByPersonId_InputValid_ThenReturn200OK() throws Exception {

    VehicleFacadeResponse response = new VehicleFacadeResponse();
    response.setUidPerson("72816ebe-bf98-4839-a819-7a34a75ee075");
    response.setNamePerson("Nguyen Minh Hieu");
    response.setVehicle(fakeVehicleResponse());
    response.setVehicleCard(fakeVehicleCardResponse());

    List<VehicleFacadeResponse> responses = List.of(response);

    when(vehicleFacadeService.getByPersonId(1L)).thenReturn(responses);

    mockMvc.perform(get(END_POINT_PATH+"/1/vehicles"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data.[0].uid_person", is("72816ebe-bf98-4839-a819-7a34a75ee075")))
          .andExpect(jsonPath("$.data.[0].name_person", is("Nguyen Minh Hieu")))
          .andExpect(jsonPath("$.data.[0].vehicle.license_plate", is("88D1-15888")))
          .andExpect(jsonPath("$.data.[0].vehicle_card.license_plate", is("88D1-88888")))
          .andDo(print());
  }

  @Test
  public void getByPersonId_InputIdNotFound_ThenReturn404NotFound() throws Exception {

    when(vehicleFacadeService.getByPersonId(1L)).thenThrow(new PersonNotFoundException());

    mockMvc.perform(get(END_POINT_PATH+"/1/vehicles"))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.data.code", is("com.ncsgroup.ems.exception.person.PersonNotFoundException")))
          .andDo(print());
  }

  @Test
  public void delete_InputIdNotFound_ThenReturn404NotFound() throws Exception {

    doThrow(new PersonNotFoundException()).when(personService).remove(1L);

    mockMvc.perform(delete(END_POINT_PATH+"/1"))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.data.code", is("com.ncsgroup.ems.exception.person.PersonNotFoundException")))
          .andDo(print());
  }

  @Test
  public void delete_InputValid_ThenReturn200OK() throws Exception {

    when(messageService.getMessage(SUCCESS, "en"))
          .thenReturn("success");

    mockMvc.perform(delete(END_POINT_PATH+"/1"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.message", is("success")))
          .andDo(print());
  }

  private VehicleResponse fakeVehicleResponse() {
    VehicleResponse response = new VehicleResponse();
    response.setId(1L);
    response.setBrandId(2L);
    response.setTypeId(3L);
    response.setLicensePlate("88D1-15888");
    response.setVehicleCardId(4L);

    return response;
  }

  private VehicleCardResponse fakeVehicleCardResponse() {
    VehicleCardResponse response = new VehicleCardResponse();
    response.setId(1L);
    response.setCardType(null);
    response.setCardCode("abc@123");
    response.setLicensePlate("88D1-88888");

    return response;
  }

  private PersonResponse fakePersonResponse() {
    return new PersonResponse(
          1L,
          "68fce692-c4d3-47f1-9073-a68c07f48773",
          "John Doe",
          "Male",
          "john.doe@example.com",
          "0987975814",
          "ABC123",
          "Manager",
          "Sales",
          "123321123"
    );
  }

  private PersonResponse fakePersonResponse1() {
    return new PersonResponse(
          2L,
          "db4656c1-e3af-43ee-8886-921037278960",
          "Mohamed Salad",
          "Male",
          "salad.mohamed@gmail.com",
          "0283975814",
          "ABC321",
          "Employee",
          "IT",
          "123456789"
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
}