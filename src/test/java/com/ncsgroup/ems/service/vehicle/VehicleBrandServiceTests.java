package com.ncsgroup.ems.service.vehicle;

import com.ncsgroup.ems.configuration.ServiceConfigurationTest;
import com.ncsgroup.ems.dto.request.vehicle.brand.VehicleBrandRequest;
import com.ncsgroup.ems.dto.response.vehicle.brand.VehicleBrandResponse;
import com.ncsgroup.ems.entity.vehicle.VehicleBrand;
import com.ncsgroup.ems.exception.vehicle.brand.VehicleBrandAlreadyExistException;
import com.ncsgroup.ems.exception.vehicle.brand.VehicleBrandNotFoundException;
import com.ncsgroup.ems.repository.VehicleBrandRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(VehicleBrandService.class)
@ContextConfiguration(classes = {ServiceConfigurationTest.class})
public class VehicleBrandServiceTests {
  @MockBean
  VehicleBrandRepository repository;

  @Autowired
  VehicleBrandService service;

  @Test
  public void create_WhenInputValid_ReturnVehicleBrandResponse() {
    VehicleBrandRequest request = new VehicleBrandRequest("phantom");
    VehicleBrand vehicleBrand = new VehicleBrand(
          15L,
          "phantom"
    );

    when(repository.existsByName(request.getName())).thenReturn(false);
    when(repository.save(any(VehicleBrand.class))).thenReturn(vehicleBrand);

    VehicleBrandResponse response = service.create(request);

    Assertions.assertEquals(vehicleBrand.getId(), response.getId());
    Assertions.assertEquals(vehicleBrand.getName(), response.getName());
  }

  @Test
  public void create_WhenInputNameExisted_ReturnException() {
    VehicleBrandRequest request = new VehicleBrandRequest("phantom");

    when(repository.existsByName(request.getName())).thenReturn(true);

    Assertions.assertThrows(VehicleBrandAlreadyExistException.class, () -> {
      service.create(request);
    });
  }

  @Test
  public void list_WhenInputKeyword_ReturnListBrandResponse() {
    String keyword = "test";

    service.list(keyword);

    verify(repository, times(1)).listBySearch(keyword);
  }

  @Test
  public void list_WhenInputKeywordIsNull_ReturnListBrandResponse() {
    service.list(null);

    verify(repository, times(1)).listBySearch(null);
  }

  @Test
  public void remove_WhenInputValid_ShouldDelete() {
    VehicleBrand vehicleBrand = new VehicleBrand(
          15L,
          "phantom"
    );

    when(repository.findById(vehicleBrand.getId())).thenReturn(Optional.of(vehicleBrand));

    service.remove(vehicleBrand.getId());

    verify(repository, times(1)).delete(vehicleBrand);
  }

  @Test
  public void find_WhenInputIdIsNull_ReturnNull() {
    VehicleBrand vehicleBrand = service.find(null);

    Assertions.assertNull(vehicleBrand);

    verify(repository, never()).findById(null);
  }

  @Test
  public void find_WhenInputValid_ReturnVehicleBrand() {
    VehicleBrand vehicleBrand = new VehicleBrand(
          15L,
          "phantom"
    );

    when(repository.findById(vehicleBrand.getId())).thenReturn(Optional.of(vehicleBrand));

    VehicleBrand brandFind = service.find(vehicleBrand.getId());

    Assertions.assertEquals(vehicleBrand.getId(), brandFind.getId());
    Assertions.assertEquals(vehicleBrand.getName(), brandFind.getName());
  }

  @Test
  public void find_WhenInputIdNotFound_ReturnException() {
    Long id = 15L;

    when(repository.findById(id)).thenReturn(Optional.empty());

    Assertions.assertThrows(VehicleBrandNotFoundException.class, () -> {
      service.find(id);
    });
  }

  @Test
  public void detail_WhenInputIdIsNull_ReturnNull() {
    VehicleBrandResponse response = service.detail(null);

    Assertions.assertNull(response);

    verify(repository, never()).findById(null);
  }

  @Test
  public void detail_WhenInputValidAndNotExisted_ReturnVehicleBrandResponse() {
    Long id = 15L;

    when(repository.findById(id)).thenReturn(Optional.empty());

    Assertions.assertThrows(VehicleBrandNotFoundException.class, () -> {
      service.detail(id);
    });
  }

  @Test
  public void detail_WhenInputValidAndExisted_ReturnVehicleBrandResponse() {
    VehicleBrand vehicleBrand = new VehicleBrand(
          15L,
          "phantom"
    );

    when(repository.findById(vehicleBrand.getId())).thenReturn(Optional.of(vehicleBrand));

    VehicleBrandResponse response = service.detail(vehicleBrand.getId());

    Assertions.assertEquals(vehicleBrand.getId(), response.getId());
    Assertions.assertEquals(vehicleBrand.getName(), response.getName());
  }
}