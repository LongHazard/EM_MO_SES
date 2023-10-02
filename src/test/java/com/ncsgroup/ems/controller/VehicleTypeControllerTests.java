package com.ncsgroup.ems.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncsgroup.ems.configuration.ServiceConfigurationTest;
import com.ncsgroup.ems.dto.request.vehicle.type.VehicleTypeRequest;
import com.ncsgroup.ems.dto.response.vehicle.type.VehicleTypeResponseDTO;
import com.ncsgroup.ems.exception.vehicle.type.VehicleTypeAlreadyExistException;
import com.ncsgroup.ems.service.MessageService;
import com.ncsgroup.ems.service.vehicle.VehicleTypeService;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static com.ncsgroup.ems.constanst.EMSConstants.CommonConstants.SUCCESS;
import static com.ncsgroup.ems.constanst.EMSConstants.MessageCode.CREATE_VEHICLE_TYPE_SUCCESS;
import static com.ncsgroup.ems.constanst.EMSConstants.MessageCode.DELETE_VEHICLE_TYPE_SUCCESS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = ServiceConfigurationTest.class)
@WebMvcTest(VehicleTypeController.class)
public class VehicleTypeControllerTests {

  private static final String END_POINT_PATH = "/api/v1/vehicle/type";

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  VehicleTypeController vehicleTypeController;
  @MockBean
  private VehicleTypeService vehicleTypeService;
  @MockBean
  private MessageService messageService;

  private VehicleTypeRequest getVehicleTypeRequest() {
    return new VehicleTypeRequest("car", "o to", "xe 4 cho ");
  }

  private VehicleTypeResponseDTO getVehicleTypeResponseDTO() {
    return new VehicleTypeResponseDTO(1l, "car");
  }

  String language = "en";

  @Test
  public void create_WhenInputValid_ReturnVehicleTypeResponseDTO() throws Exception {
    VehicleTypeRequest request = getVehicleTypeRequest();
    VehicleTypeResponseDTO response = getVehicleTypeResponseDTO();

    String bodyContent = objectMapper.writeValueAsString(request);

    String successMessage = "Create vehicle type successfully";

    Mockito.when(vehicleTypeService.create(request)).thenReturn(response);
    Mockito.when(messageService.getMessage(CREATE_VEHICLE_TYPE_SUCCESS, "en"))
          .thenReturn(successMessage);

    mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent)
                .header("LANGUAGE", "en"))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(successMessage))
          .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(response.getId()))
          .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(response.getName()));
  }

  @Test
  public void create_WhenNameAlreadyExisted_Return400BadRequest() throws Exception {
    VehicleTypeRequest request = getVehicleTypeRequest();

    String bodyContent = objectMapper.writeValueAsString(request);

    String errorMessage = "Vehicle Type already exist";

    Mockito.when(vehicleTypeService.create(request)).thenThrow(new VehicleTypeAlreadyExistException());

    mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent)
                .header("LANGUAGE", "en"))
          .andExpect(MockMvcResultMatchers.status().isBadRequest())
          .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
          .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad Request"))
          .andExpect(MockMvcResultMatchers.jsonPath("$.data.code").value("com.ncsgroup.ems.exception.vehicle_type.VehicleTypeAlreadyExistException"))
          //  .andExpect(MockMvcResultMatchers.jsonPath("$.data.detail").value("Vehicle Type already exist"))
          .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(Matchers.notNullValue()));
  }

  @Test
  public void list_WhenInputValid_ReturnsListVehicleTypeResponseDTO() throws Exception {
    String keyword = "car";

    List<VehicleTypeResponseDTO> vehicleTypes = Arrays.asList(
          new VehicleTypeResponseDTO(1l, "Car")
    );

    Mockito.when(vehicleTypeService.list(keyword)).thenReturn(vehicleTypes);
    Mockito.when(messageService.getMessage(SUCCESS, language)).thenReturn("success");

    mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH)
                .param("keyword", keyword)
                .header("LANGUAGE", language))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
          .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"))
          .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
          .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()").value(vehicleTypes.size()))
          .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(vehicleTypes.get(0).getId()))
          .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value(vehicleTypes.get(0).getName()));

  }

  @Test
  public void delete_WhenInputValid() throws Exception {
    Long id = 1L;
    String language = "en";

    Mockito.doNothing().when(vehicleTypeService).delete(id);

    Mockito.when(messageService.getMessage(DELETE_VEHICLE_TYPE_SUCCESS, language)).thenReturn("Delete vehicle type successfully");

    // When
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/vehicle/type/{id}", id)
                .header("LANGUAGE", language))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
          .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Delete vehicle type successfully"))
          .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
          .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(Matchers.notNullValue()));

    Mockito.verify(vehicleTypeService).delete(id);
  }
}







