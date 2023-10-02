package com.ncsgroup.ems.service.identity;

import com.ncsgroup.ems.configuration.ServiceConfigurationTest;
import com.ncsgroup.ems.entity.identity.IdentityObjectGroup;
import com.ncsgroup.ems.repository.IdentityObjectGroupRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@WebMvcTest(IdentityObjectGroupTest.class)
@ContextConfiguration(classes = {
      ServiceConfigurationTest.class
})
public class IdentityObjectGroupTest {
  @Autowired
  IdentityGroupObjectService identityGroupObjectService;

  @MockBean
  IdentityObjectGroupRepository repository;

  @Test
  public void update_WhenInputValid_ReturnVoid() {
    Long vehicleId = 1l;
    List<Long> personIds = Arrays.asList(1l);
    List<IdentityObjectGroup> identityObjectGroups = Arrays.asList(
          IdentityObjectGroup.of(1l, 1l));
    Mockito.when(repository.saveAll(identityObjectGroups)).thenReturn(null);
    identityGroupObjectService.update(vehicleId, personIds);

    verify(repository, times(1)).deleteAllByVehicleId(vehicleId);
    verify(repository, times(1)).saveAll(identityObjectGroups);
  }

  @Test
  public void update_WhenGroupIdsEmpty_ReturnVoid() {
    Long vehicleId = 1l;
    List<Long> personIds = new ArrayList<>();

    identityGroupObjectService.update(vehicleId, personIds);

    verify(repository, times(1)).deleteAllByVehicleId(vehicleId);
    verifyNoMoreInteractions(repository);
  }

  @Test
  public void update_WhenGroupIdsIsNull_ReturnVoid() {
    Long vehicleId = 1l;
    List<Long> personIds = null;

    identityGroupObjectService.update(vehicleId, personIds);

    verify(repository, times(1)).deleteAllByVehicleId(vehicleId);
    verifyNoMoreInteractions(repository);
  }


}
