package com.ncsgroup.ems.service.color;

import com.ncsgroup.ems.configuration.ServiceConfigurationTest;
import com.ncsgroup.ems.dto.request.color.ColorRequest;
import com.ncsgroup.ems.dto.response.color.ColorResponse;
import com.ncsgroup.ems.entity.vehicle.Color;
import com.ncsgroup.ems.exception.color.ColorAlreadyExistException;
import com.ncsgroup.ems.exception.color.ColorNotFoundException;
import com.ncsgroup.ems.repository.ColorRepository;
import com.ncsgroup.ems.service.vehicle.ColorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(ColorService.class)
@ContextConfiguration(classes = {
      ServiceConfigurationTest.class
})
public class ColorServiceTests {
  @MockBean
  ColorRepository repository;

  @Autowired
  ColorService colorService;

  static ColorRequest request = new ColorRequest(
        "brown"
  );

  @Test
  public void createColor_WhenInputValid_ReturnColorResponse() {
    Color color = new Color(8L, "brown");

    when(repository.existsByName(request.getName())).thenReturn(false);
    when(repository.save(any(Color.class))).thenReturn(color);

    ColorResponse response = colorService.create(request);

    checkResponseTest(color, response);
  }

  @Test
  public void createColor_WhenInputNameExisted_ReturnException() {
    when(repository.existsByName(request.getName())).thenReturn(true);

    Assertions.assertThrows(ColorAlreadyExistException.class, () -> {
      colorService.create(request);
    });
  }

  @Test
  public void retrieveColor_WhenInputValid_ReturnColorResponse() {
    Color color = new Color(8L, "brown");

    when(repository.findById(color.getId())).thenReturn(Optional.of(color));

    ColorResponse response = colorService.retrieve(color.getId());

    checkResponseTest(color, response);
  }

  @Test
  public void retrieveColor_WhenInputIdNotFound_ReturnException() {
    Color color = new Color(8L, "brown");

    when(repository.findById(color.getId())).thenReturn(Optional.empty());

    Assertions.assertThrows(ColorNotFoundException.class, () -> {
      colorService.retrieve(color.getId());
    });
  }

  @Test
  public void removeColor_whenColorIdsExist_shouldDeleteColor() {
    Color color = new Color(8L, "brown");

    when(repository.findById(color.getId())).thenReturn(Optional.of(color));
    colorService.remove(color.getId());

    verify(repository, times(1)).delete(color);
  }

  @Test
  public void listColor_WhenInputValid_ReturnColorResponse() {
    String keyword = "test";

    List<ColorResponse> colorResponses = colorService.list(keyword);

    verify(repository, times(1)).search(keyword);
  }

  @Test
  public void findName_WhenInputValid_ReturnNameList() {
    Long vehicleId = 8L;
    List<String> nameColors = Arrays.asList(
          "Black",
          "Red"
    );

    when(repository.findNameColor(vehicleId)).thenReturn(nameColors);

    List<String> namList = colorService.findName(vehicleId);

    assertEquals(nameColors, namList);
  }

  @Test
  public void findNameById_WhenInputValid_ReturnNameList() {
    List<Long> idColors = Arrays.asList(
          7L,
          8L
    );

    List<String> nameColors = Arrays.asList(
          "Blue",
          "Brown"
    );


    when(repository.findNameColorById(idColors)).thenReturn(nameColors);

    List<String> namList = colorService.findNameById(idColors);

    assertEquals(nameColors, namList);
  }

  @Test
  public void findByIds_WhenInputValid_ReturnColorList() {
    List<Color> colors = Arrays.asList(
          new Color(7L, "Black"),
          new Color(8L, "Red")
    );

    List<Long> colorIds = Arrays.asList(
          7L,
          8L
    );

    when(repository.findByIdIn(colorIds)).thenReturn(colors);

    List<Color> colorList = colorService.findByIds(colorIds);

    assertEquals(colors, colorList);
  }

  @Test
  public void checkNotFound_WhenColorIdsIsNull_ShouldCheckNotFound() {
    colorService.checkNotFound(null);

    verify(repository, never()).checkNotFound(null);
  }

  @Test
  public void checkNotFound_WhenColorIdsIsEmpty_ShouldCheckNotFound() {
    List<Long> colorIds = new ArrayList<>();

    colorService.checkNotFound(colorIds);

    verify(repository, never()).checkNotFound(colorIds);
  }

  @Test
  public void checkNotFound_WhenColorIdsExistedAndSizeNotEqual_ReturnException() {
    List<Long> idColors = Arrays.asList(
          7L,
          8L
    );

    when(repository.checkNotFound(idColors)).thenReturn(1);

    Assertions.assertThrows(ColorNotFoundException.class, () -> {
      colorService.checkNotFound(idColors);
    });
  }

  @Test
  public void checkNotFound_WhenColorIdsExistedAndSizeEqual_ThenPass() {
    List<Long> idColors = Arrays.asList(
          7L,
          8L
    );

    when(repository.checkNotFound(idColors)).thenReturn(2);

    Assertions.assertDoesNotThrow(() -> {
      colorService.checkNotFound(idColors);
    }, String.valueOf(ColorNotFoundException.class));
  }

  @Test
  public void findByVehicleId_WhenInputValid_ReturnColorList() {
    Long vehicleId = 8L;

    List<String> nameColors = Arrays.asList(
          "Black",
          "Red"
    );

    when(repository.find(vehicleId)).thenReturn(nameColors);

    List<String> namList = colorService.findByVehicleId(vehicleId);

    assertEquals(nameColors, namList);
  }

  private void checkResponseTest(Color color,
                                 ColorResponse colorResponse) {
    assertEquals(color.getId(), colorResponse.getId());
    assertEquals(color.getName(), colorResponse.getName());
  }
}
