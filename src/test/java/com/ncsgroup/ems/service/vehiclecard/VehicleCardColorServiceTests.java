package com.ncsgroup.ems.service.vehiclecard;

import com.ncsgroup.ems.configuration.ServiceConfigurationTest;
import com.ncsgroup.ems.entity.vehicle.VehicleCardColor;
import com.ncsgroup.ems.repository.VehicleCardColorRepository;
import com.ncsgroup.ems.service.vehicle.VehicleCardColorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@WebMvcTest(VehicleCardColorService.class)
@ContextConfiguration(classes = {
      ServiceConfigurationTest.class
})
public class VehicleCardColorServiceTests {
  @MockBean
  VehicleCardColorRepository repository;

  @Autowired
  VehicleCardColorService vehicleCardColorService;

  @Test
  void updateVehicleCardColor_whenColorIdsIsNull_shouldNotDeleteOrSaveVehicleCardColors() {
    Long vehicleCardId = 54L;

    vehicleCardColorService.update(vehicleCardId, null);

    verify(repository, never()).getVehicleCardColorsById_VehicleCardId(vehicleCardId);
    verify(repository, never()).deleteAll(anyList());
    verify(repository, never()).saveAll(anyList());
  }

  @Test
  void updateVehicleCardColor_whenColorIdsIsEmpty_shouldNotDeleteOrSaveVehicleCardColors() {
    Long vehicleCardId = 54L;
    List<Long> colorIds = new ArrayList<>();

    vehicleCardColorService.update(vehicleCardId, colorIds);

    verify(repository, never()).getVehicleCardColorsById_VehicleCardId(vehicleCardId);
    verify(repository, never()).deleteAll(anyList());
    verify(repository, never()).saveAll(anyList());
  }

  @Test
  void updateVehicleCardColor_whenColorIdsExist_shouldNotDeleteOrSaveVehicleCardColors() {
    Long vehicleCardId = 54L;
    List<Long> colorIds = List.of(1L, 2L, 3L);

    List<VehicleCardColor> existedVehicleCardColors = new ArrayList<>();
    existedVehicleCardColors.add(VehicleCardColor.of(vehicleCardId, 1l));
    existedVehicleCardColors.add(VehicleCardColor.of(vehicleCardId, 4L));

    when(repository.getVehicleCardColorsById_VehicleCardId(vehicleCardId))
          .thenReturn(existedVehicleCardColors);

    vehicleCardColorService.update(vehicleCardId, colorIds);

    verify(repository, times(1)).deleteAll(anyList());
    verify(repository, times(1)).saveAll(anyList());
  }
}
