package com.ncsgroup.ems.service.vehicle.impl;

import com.ncsgroup.ems.dto.request.color.ColorRequest;
import com.ncsgroup.ems.dto.response.color.ColorResponse;
import com.ncsgroup.ems.entity.vehicle.Color;
import com.ncsgroup.ems.exception.color.ColorAlreadyExistException;
import com.ncsgroup.ems.exception.color.ColorNotFoundException;
import com.ncsgroup.ems.repository.ColorRepository;
import com.ncsgroup.ems.service.vehicle.ColorService;
import com.ncsgroup.ems.service.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

import static com.ncsgroup.ems.utils.MapperUtils.*;

@Slf4j
public class ColorServiceImpl extends BaseServiceImpl<Color> implements ColorService {
  private final ColorRepository repository;

  public ColorServiceImpl(ColorRepository repository) {
    super(repository);
    this.repository = repository;
  }

  @Override
  public ColorResponse create(ColorRequest request) {
    log.info("(create)request: {}", request);

    checkNameExist(request.getName());
    Color color = toEntity(request, Color.class);

    return toDTO(create(color), ColorResponse.class);
  }

  @Override
  public ColorResponse retrieve(Long id) {
    log.info("(retrieve)id: {}", id);

    return toDTO(find(id), ColorResponse.class);
  }

  @Override
  public List<ColorResponse> list(String keyword) {
    log.info("(list)keyword: {}", keyword);

    return repository.search(keyword);
  }

  @Override
  public void remove(Long id) {
    log.info("(remove)id: {}", id);

    repository.delete(find(id));
  }

  @Override
  public Color find(Long id) {
    log.info("(find)id: {}", id);

    return repository.findById(id).orElseThrow(ColorNotFoundException::new);
  }

  @Override
  public List<String> findName(Long vehicleId) {
    log.info("(findName) idVehicle: {}", vehicleId);

    return repository.findNameColor(vehicleId);
  }

  @Override
  public List<String> findNameById(List<Long> colorIds) {
    log.info("(findNameById) idColors: {}", colorIds);

    return repository.findNameColorById(colorIds);
  }

  @Override
  public List<Color> findByIds(List<Long> colorIds) {
    log.info("(findByIds) colorIds: {}", colorIds);

    return repository.findByIdIn(colorIds);
  }

  @Override
  public void checkNotFound(List<Long> colorIds) {
    log.info("(checkNotFound) colorIds: {}", colorIds);

    if (Objects.isNull(colorIds) || colorIds.isEmpty()) return;

    if (colorIds.size() != repository.checkNotFound(colorIds)) {
      throw new ColorNotFoundException();
    }
  }

  @Override
  public List<String> findByVehicleId(Long vehicleId) {
    log.info("(checkNameExist) vehicleId : {}", vehicleId);
    return repository.find(vehicleId);
  }

  private void checkNameExist(String name) {
    log.info("(checkNameExist) name : {}", name);

    if (repository.existsByName(name)) {
      log.error("(checkNameExist) ============= ColorAlreadyExistException");
      throw new ColorAlreadyExistException();
    }
  }
}
