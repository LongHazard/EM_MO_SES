package com.ncsgroup.ems.service.vehicle;

import com.ncsgroup.ems.configuration.ServiceConfigurationTest;
import com.ncsgroup.ems.dto.request.vehicle.type.VehicleTypeRequest;
import com.ncsgroup.ems.dto.response.vehicle.type.VehicleTypeResponseDTO;
import com.ncsgroup.ems.entity.vehicle.VehicleBrand;
import com.ncsgroup.ems.entity.vehicle.VehicleType;
import com.ncsgroup.ems.exception.vehicle.type.VehicleTypeAlreadyExistException;
import com.ncsgroup.ems.exception.vehicle.type.VehicleTypeNotFoundException;
import com.ncsgroup.ems.repository.VehicleTypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(VehicleTypeService.class)
@ContextConfiguration(classes = {ServiceConfigurationTest.class})
public class VehicleTypeServiceTests {
  @MockBean
  VehicleTypeRepository repository;

  @Autowired
  VehicleTypeService service;

  @Test
  public void create_WhenInputValid_ReturnVehicleTypeResponseDTO() {
    VehicleTypeRequest request = new VehicleTypeRequest(
          "plane",
          "may bay",
          "plane"
    );

    VehicleType vehicleType = new VehicleType(
          5L,
          "plane",
          "may bay",
          "plane"
    );

    when(repository.existsByName(request.getName())).thenReturn(false);
    when(repository.save(any(VehicleType.class))).thenReturn(vehicleType);

    VehicleTypeResponseDTO response = service.create(request);

    Assertions.assertEquals(vehicleType.getId(), response.getId());
    Assertions.assertEquals(vehicleType.getName(), response.getName());
  }

  @Test
  public void create_WhenInputNameExisted_ReturnException() {
    VehicleTypeRequest request = new VehicleTypeRequest(
          "plane",
          "may bay",
          "plane"
    );

    when(repository.existsByName(request.getName())).thenReturn(true);

    Assertions.assertThrows(VehicleTypeAlreadyExistException.class, () -> {
      service.create(request);
    });

    verify(repository, never()).save(any(VehicleType.class));
  }

  @Test
  public void checkNameExist_WhenInputValid_ThenPass() {
    String name = "test";

    when(repository.existsByName(name)).thenReturn(false);

    Assertions.assertDoesNotThrow(() -> {
      service.checkNameExist(name);
    }, String.valueOf(VehicleTypeAlreadyExistException.class));
  }


  @Test
  public void checkNameExist_WhenInputNameExisted_ReturnException() {
    String name = "test";

    when(repository.existsByName(name)).thenReturn(true);

    Assertions.assertThrows(VehicleTypeAlreadyExistException.class, () -> {
      service.checkNameExist(name);
    });
  }

  @Test
  public void delete_WhenInputValid_ShouldDelete() {
    VehicleType vehicleType = new VehicleType(
          5L,
          "plane",
          "may bay",
          "plane"
    );

    when(repository.findById(vehicleType.getId())).thenReturn(Optional.of(vehicleType));

    service.delete(vehicleType.getId());

    verify(repository, times(1)).delete(vehicleType);
  }

  @Test
  public void delete_WhenInputIdNotFound_ReturnException() {
    Long id = 5L;

    when(repository.findById(id)).thenReturn(Optional.empty());

    Assertions.assertThrows(VehicleTypeNotFoundException.class, () -> {
      service.delete(id);
    });
  }

  @Test
  public void list_WhenInputKeyword_ReturnListTypeResponse() {
    String keyword = "test";

    service.list(keyword);

    verify(repository, times(1)).search(keyword);
  }

  @Test
  public void list_WhenInputKeywordIsNull_ReturnListTypeResponse() {
    service.list(null);

    verify(repository, times(1)).search(null);
  }

  @Test
  public void find_WhenInputIdIsNull_ReturnNull() {
    VehicleType vehicleType = service.find(null);

    Assertions.assertNull(vehicleType);

    verify(repository, never()).findById(null);
  }

  @Test
  public void find_WhenInputValid_ReturnVehicleBrand() {
    VehicleType vehicleType = new VehicleType(
          5L,
          "plane",
          "may bay",
          "plane"
    );

    when(repository.findById(vehicleType.getId())).thenReturn(Optional.of(vehicleType));

    VehicleType typeFind = service.find(vehicleType.getId());

    Assertions.assertEquals(vehicleType.getId(), typeFind.getId());
    Assertions.assertEquals(vehicleType.getName(), typeFind.getName());
  }

  @Test
  public void find_WhenInputIdNotFound_ReturnException() {
    Long id = 5L;

    when(repository.findById(id)).thenReturn(Optional.empty());

    Assertions.assertThrows(VehicleTypeNotFoundException.class, () -> {
      service.find(id);
    });
  }

  @Test
  public void detail_WhenInputIdIsNull_ReturnNull() {
    VehicleTypeResponseDTO response = service.detail(null);

    Assertions.assertNull(response);

    verify(repository, never()).findById(null);
  }

  @Test
  public void detail_WhenInputValidAndNotExisted_ReturnVehicleBrandResponse() {
    Long id = 5L;

    when(repository.findById(id)).thenReturn(Optional.empty());

    Assertions.assertThrows(VehicleTypeNotFoundException.class, () -> {
      service.detail(id);
    });
  }

  @Test
  public void detail_WhenInputValidAndExisted_ReturnVehicleBrandResponse() {
    VehicleType vehicleType = new VehicleType(
          5L,
          "plane",
          "may bay",
          "plane"
    );

    when(repository.findById(vehicleType.getId())).thenReturn(Optional.of(vehicleType));

    VehicleTypeResponseDTO response = service.detail(vehicleType.getId());

    Assertions.assertEquals(vehicleType.getId(), response.getId());
    Assertions.assertEquals(vehicleType.getName(), response.getName());
  }
}