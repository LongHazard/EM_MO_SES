package com.ncsgroup.ems.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncsgroup.ems.configuration.ServiceConfigurationTest;
import com.ncsgroup.ems.dto.request.vehicle.VehicleFacadeRequest;
import com.ncsgroup.ems.dto.request.vehicle.VehicleFilter;
import com.ncsgroup.ems.dto.request.vehicle.VehicleRequest;
import com.ncsgroup.ems.dto.request.vehicle.VehicleSearchRequest;
import com.ncsgroup.ems.dto.response.person.PersonFacadeResponse;
import com.ncsgroup.ems.dto.response.vehicle.*;
import com.ncsgroup.ems.exception.vehicle.VehicleLicensePlateAlreadyExistException;
import com.ncsgroup.ems.exception.vehicle.VehicleNotFoundException;
import com.ncsgroup.ems.exception.vehicle.brand.VehicleBrandAlreadyExistException;
import com.ncsgroup.ems.exception.vehicle.brand.VehicleBrandNotFoundException;
import com.ncsgroup.ems.exception.vehicle.card.CardCodeAlreadyExistException;
import com.ncsgroup.ems.exception.vehicle.card.LicensePlateAlreadyExistException;
import com.ncsgroup.ems.exception.vehicle.card.VehicleCardCodeAlreadyExistException;
import com.ncsgroup.ems.exception.vehicle.card.VehicleCardNotFoundException;
import com.ncsgroup.ems.facade.PersonFacadeService;
import com.ncsgroup.ems.facade.VehicleFacadeService;
import com.ncsgroup.ems.service.MessageService;
import com.ncsgroup.ems.service.vehicle.VehicleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = ServiceConfigurationTest.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(VehicleController.class)
public class VehicleControllerTests {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private VehicleController vehicleController;
  @MockBean
  private VehicleFacadeService vehicleFacadeService;
  @MockBean
  private VehicleService vehicleService;
  @MockBean
  private PersonFacadeService personFacadeService;
  @MockBean
  private MessageService messageService;

  @Test
  public void create_WhenInputIsValid_Return201AndResponseBody() throws Exception {
    VehicleFacadeRequest request = this.mockVehicleFacadeRequest();

    Mockito.when(vehicleFacadeService.createVehicle(request)).thenReturn(this.mockVehicleFacadeResponse());

    MvcResult mvcResult = mockMvc.perform(
          post("/api/v1/vehicles")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
    ).andReturn();

    String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

    System.out.println(responseBody);

    Assertions.assertEquals(
          responseBody,
          objectMapper.writeValueAsString(
                vehicleController.create(request, null)
          )
    );

  }

  @Test
  public void create_WhenInputIsValid_Return409VehicleBrandAlreadyExistException() throws Exception {
    VehicleFacadeRequest request = this.mockVehicleFacadeRequest();

    Mockito.when(vehicleFacadeService.createVehicle(Mockito.any(VehicleFacadeRequest.class)))
          .thenThrow(new VehicleBrandAlreadyExistException());

    MvcResult mvcResult = mockMvc.perform(
          post("/api/v1/vehicles")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
    ).andReturn();

    String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

    System.out.println(responseBody);

    Assertions.assertTrue(responseBody.contains("409"));
    Assertions.assertTrue(responseBody.contains("com.ncsgroup.ems.exception.vehicle.vehiclebrand.VehicleBrandAlreadyExistException"));
  }

  @Test
  public void create_WhenInputIsValid_Return404VehicleBrandNotFoundException() throws Exception {
    VehicleFacadeRequest request = this.mockVehicleFacadeRequest();

    Mockito.when(vehicleFacadeService.createVehicle(Mockito.any(VehicleFacadeRequest.class)))
          .thenThrow(new VehicleBrandNotFoundException());

    MvcResult mvcResult = mockMvc.perform(
          post("/api/v1/vehicles")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
    ).andReturn();

    String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

    System.out.println(responseBody);

    Assertions.assertTrue(responseBody.contains("404"));
    Assertions.assertTrue(responseBody.contains("com.ncsgroup.ems.exception.vehicle.vehiclebrand.VehicleBrandNotFoundException"));
  }

  @Test
  public void create_WhenInputIsValid_Return409CardCodeAlreadyExistException() throws Exception {
    VehicleFacadeRequest request = this.mockVehicleFacadeRequest();

    Mockito.when(vehicleFacadeService.createVehicle(Mockito.any(VehicleFacadeRequest.class)))
          .thenThrow(new CardCodeAlreadyExistException());

    MvcResult mvcResult = mockMvc.perform(
          post("/api/v1/vehicles")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
    ).andReturn();

    String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

    System.out.println(responseBody);

    Assertions.assertTrue(responseBody.contains("409"));
    Assertions.assertTrue(responseBody.contains("com.ncsgroup.ems.exception.vehicle.vehiclecard.CardCodeAlreadyExistException"));
  }

  @Test
  public void create_WhenInputIsValid_Return409LicensePlateAlreadyExistException() throws Exception {
    VehicleFacadeRequest request = this.mockVehicleFacadeRequest();

    Mockito.when(vehicleFacadeService.createVehicle(Mockito.any(VehicleFacadeRequest.class)))
          .thenThrow(new LicensePlateAlreadyExistException());

    MvcResult mvcResult = mockMvc.perform(
          post("/api/v1/vehicles")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
    ).andReturn();

    String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

    System.out.println(responseBody);

    Assertions.assertTrue(responseBody.contains("409"));
    Assertions.assertTrue(responseBody.contains("com.ncsgroup.ems.exception.vehicle.card.LicensePlateAlreadyExistException"));
  }

  @Test
  public void create_WhenInputIsValid_Return409VehicleCardCodeAlreadyExistException() throws Exception {
    VehicleFacadeRequest request = this.mockVehicleFacadeRequest();

    Mockito.when(vehicleFacadeService.createVehicle(Mockito.any(VehicleFacadeRequest.class)))
          .thenThrow(new VehicleCardCodeAlreadyExistException());

    MvcResult mvcResult = mockMvc.perform(
          post("/api/v1/vehicles")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
    ).andReturn();

    String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

    System.out.println(responseBody);

    Assertions.assertTrue(responseBody.contains("409"));
    Assertions.assertTrue(responseBody.contains("com.ncsgroup.ems.exception.vehicle.vehiclecard.VehicleCardCodeAlreadyExistException"));
  }

  @Test
  public void create_WhenInputIsValid_Return404VehicleCardNotFoundException() throws Exception {
    VehicleFacadeRequest request = this.mockVehicleFacadeRequest();

    Mockito.when(vehicleFacadeService.createVehicle(Mockito.any(VehicleFacadeRequest.class)))
          .thenThrow(new VehicleCardNotFoundException());

    MvcResult mvcResult = mockMvc.perform(
          post("/api/v1/vehicles")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
    ).andReturn();

    String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

    System.out.println(responseBody);

    Assertions.assertTrue(responseBody.contains("404"));
    Assertions.assertTrue(responseBody.contains("com.ncsgroup.ems.exception.vehicle.vehiclecard.VehicleCardNotFoundException"));
  }

  @Test
  public void create_WhenInputIsValid_Return409VehicleLicensePlateAlreadyExistException() throws Exception {
    VehicleFacadeRequest request = this.mockVehicleFacadeRequest();

    Mockito.when(vehicleFacadeService.createVehicle(Mockito.any(VehicleFacadeRequest.class)))
          .thenThrow(new VehicleLicensePlateAlreadyExistException());

    MvcResult mvcResult = mockMvc.perform(
          post("/api/v1/vehicles")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
    ).andReturn();

    String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

    System.out.println(responseBody);

    Assertions.assertTrue(responseBody.contains("409"));
    Assertions.assertTrue(responseBody.contains("com.ncsgroup.ems.exception.vehicle.VehicleLicensePlateAlreadyExistException"));
  }

  @Test
  public void delete_WhenInputIsValid_Return200() throws Exception {

    Mockito.doNothing().when(vehicleService).delete(Mockito.anyLong());

    mockMvc.perform(
                delete("/api/v1/vehicles/{id}", 1L)
                      .contentType("application/json"))
          .andExpect(status().isOk());

  }

  @Test
  public void list_WhenInputIsValid_Return200() throws Exception {
    VehiclePageResponse mockResponse = VehiclePageResponse.of(
          List.of(this.mockVehicleDetail(), this.mockVehicleDetail()),
          2
    );

    Mockito.when(vehicleFacadeService.list(
                Mockito.any(VehicleFilter.class),
                Mockito.anyInt(),
                Mockito.anyInt(),
                Mockito.anyBoolean()
          ))
          .thenReturn(mockResponse);

    MvcResult mvcResult = mockMvc.perform(
          post("/api/v1/vehicles/search")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(this.mockVehicleFilter()))
    ).andReturn();

    String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

    System.out.println(responseBody);

    Assertions.assertEquals(
          responseBody,
          objectMapper.writeValueAsString(
                vehicleController.list(new VehicleFilter(), 10, 0, false, null)
          )
    );
  }

  @Test
  public void detail_WhenInputIsValid_Return200() throws Exception {
    Mockito.when(vehicleFacadeService.detail(Mockito.anyLong()))
          .thenReturn(this.mockVehicleFacadeResponse());

    MvcResult mvcResult = mockMvc.perform(
          get("/api/v1/vehicles/{id}", 1L)
                .contentType("application/json")
    ).andReturn();

    String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

    System.out.println(responseBody);

    Assertions.assertEquals(
          responseBody,
          objectMapper.writeValueAsString(
                vehicleController.detail(1L, null)
          )
    );
  }

  @Test
  public void detail_WhenInputIsValid_Return404VehicleNotFoundException() throws Exception {
    Mockito.when(vehicleFacadeService.detail(Mockito.anyLong()))
          .thenThrow(new VehicleNotFoundException());

    MvcResult mvcResult = mockMvc.perform(
          get("/api/v1/vehicles/{id}", 1L)
                .contentType("application/json")
    ).andReturn();

    String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

    System.out.println(responseBody);

    Assertions.assertTrue(responseBody.contains("404"));
    Assertions.assertTrue(responseBody.contains("com.ncsgroup.ems.exception.vehicle.VehicleNotFoundException"));
  }

  @Test
  public void update_WhenInputIsValid_Return200AndResponseBody() throws Exception {
    VehicleFacadeRequest request = this.mockVehicleFacadeRequest();

    Mockito.when(
                vehicleFacadeService.update(1L, request)
          )
          .thenReturn(this.mockVehicleFacadeResponse());

    MvcResult mvcResult = mockMvc.perform(
          put("/api/v1/vehicles/{id}", 1L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
    ).andReturn();

    String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

    System.out.println(responseBody);

    Assertions.assertEquals(
          responseBody,
          objectMapper.writeValueAsString(
                vehicleController.update(1L, request, null)
          )
    );

  }

  @Test
  public void update_WhenInputIsValid_Return404VehicleNotFoundException() throws Exception {
    VehicleFacadeRequest request = this.mockVehicleFacadeRequest();

    Mockito.when(
                vehicleFacadeService.update(1L, request)
          )
          .thenThrow(new VehicleNotFoundException());

    MvcResult mvcResult = mockMvc.perform(
          put("/api/v1/vehicles/{id}", 1L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
    ).andReturn();

    String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

    System.out.println(responseBody);

    Assertions.assertTrue(responseBody.contains("404"));
    Assertions.assertTrue(responseBody.contains("com.ncsgroup.ems.exception.vehicle.VehicleNotFoundException"));
  }

  @Test
  public void update_WhenInputIsValid_Return404VehicleCardNotFoundException() throws Exception {
    VehicleFacadeRequest request = this.mockVehicleFacadeRequest();

    Mockito.when(
                vehicleFacadeService.update(1L, request)
          )
          .thenThrow(new VehicleCardNotFoundException());

    MvcResult mvcResult = mockMvc.perform(
          put("/api/v1/vehicles/{id}", 1L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
    ).andReturn();

    String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

    System.out.println(responseBody);

    Assertions.assertTrue(responseBody.contains("404"));
    Assertions.assertTrue(responseBody.contains("com.ncsgroup.ems.exception.vehicle.vehiclecard.VehicleCardNotFoundException"));
  }

  @Test
  public void listLicensePlate_WhenInputIsValid_Return200AndResponseBody() throws Exception {
    VehicleLicensePlatePageResponse mockResponse = VehicleLicensePlatePageResponse.of(
          List.of(new VehicleLicensePlateResponse(), new VehicleLicensePlateResponse()),
          2
    );

    Mockito.when(
                vehicleService.listLicensePlate(
                      Mockito.anyString(),
                      Mockito.anyInt(),
                      Mockito.anyInt(),
                      Mockito.anyBoolean()
                )
          )
          .thenReturn(mockResponse);

    MvcResult mvcResult = mockMvc.perform(
          get("/api/v1/vehicles/license-plate")
                .param("keyword", "keyword")
    ).andReturn();

    String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

    System.out.println(responseBody);

    Assertions.assertEquals(
          responseBody,
          objectMapper.writeValueAsString(
                vehicleController.listLicensePlate("keyword", 10, 0, false, null)
          )
    );
  }

  @Test
  public void getByVehicle_WhenInputIsValid_Return200AndResponseBody() throws Exception {
    PersonFacadeResponse mockResponse = new PersonFacadeResponse();

    Mockito.when(
                personFacadeService.getPersonByVehicle(
                      Mockito.anyLong()
                )
          )
          .thenReturn(mockResponse);

    MvcResult mvcResult = mockMvc.perform(
          get("/api/v1/vehicles/{id}/persons", 1L)
    ).andReturn();

    String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

    System.out.println(responseBody);

    Assertions.assertEquals(
          responseBody,
          objectMapper.writeValueAsString(
                vehicleController.getByVehicle(1L, null)
          )
    );
  }

  @Test
  public void getByGroupId_WhenInputIsValid_Return200AndResponseBody() throws Exception {
    List<VehicleResponse> mockResponse = List.of(new VehicleResponse(), new VehicleResponse());

    Mockito.when(
                vehicleService.getByGroupId(
                      Mockito.any(VehicleSearchRequest.class)
                )
          )
          .thenReturn(mockResponse);

    MvcResult mvcResult = mockMvc.perform(
          post("/api/v1/vehicles/group")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(this.mockVehicleSearchRequest()))
    ).andReturn();

    String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

    System.out.println(responseBody);

    Assertions.assertEquals(
          responseBody,
          objectMapper.writeValueAsString(
                vehicleController.getByGroupId(this.mockVehicleSearchRequest(), null)
          )
    );
  }

  private VehicleFacadeRequest mockVehicleFacadeRequest() {
    return new VehicleFacadeRequest(
          null,
          this.mockVehicleRequest(),
          null,
          null
    );
  }

  private VehicleRequest mockVehicleRequest() {
    VehicleRequest vehicleRequest = new VehicleRequest();

    vehicleRequest.setLicensePlate("12345");

    return vehicleRequest;
  }

  private VehicleSearchRequest mockVehicleSearchRequest() {
    return new VehicleSearchRequest("keyword", 1L);
  }

  private VehicleFacadeResponse mockVehicleFacadeResponse() {
    VehicleFacadeResponse vehicleFacadeResponse = new VehicleFacadeResponse();

    vehicleFacadeResponse.setVehicle(this.mockVehicleResponse());

    return vehicleFacadeResponse;
  }

  private VehicleResponse mockVehicleResponse() {
    VehicleResponse response = new VehicleResponse();

    response.setLicensePlate("12345");

    return response;
  }

  private VehicleDetail mockVehicleDetail() {
    VehicleDetail vehicleDetail = new VehicleDetail();

    vehicleDetail.setId(1L);
    vehicleDetail.setLicensePlate("abc");

    return vehicleDetail;
  }

  private VehicleFilter mockVehicleFilter() {
    VehicleFilter vehicleFilter = new VehicleFilter();

    vehicleFilter.setKeyword("keyword");

    return vehicleFilter;
  }
}
