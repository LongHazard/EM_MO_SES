package com.ncsgroup.ems.service.person.impl;

import com.ncsgroup.ems.dto.request.cardtype.CardTypeRequest;
import com.ncsgroup.ems.dto.response.cardtype.CardTypeDetail;
import com.ncsgroup.ems.dto.response.cardtype.CardTypeResponse;
import com.ncsgroup.ems.dto.response.vehicle.type.VehicleTypeResponseDTO;
import com.ncsgroup.ems.entity.person.CardType;
import com.ncsgroup.ems.exception.cardtype.CardTypeAlreadyExistException;
import com.ncsgroup.ems.exception.cardtype.CardTypeNotFoundException;
import com.ncsgroup.ems.repository.CardTypeRepository;
import com.ncsgroup.ems.service.person.CardTypeService;
import com.ncsgroup.ems.service.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

import static com.ncsgroup.ems.utils.MapperUtils.*;

@Slf4j
public class CardTypeServiceImpl extends BaseServiceImpl<CardType> implements CardTypeService {
  private final CardTypeRepository repository;

  public CardTypeServiceImpl(CardTypeRepository repository) {
    super(repository);
    this.repository = repository;
  }

  @Override
  public CardTypeResponse create(CardTypeRequest request) {
    log.info("(create) request: {}", request);
    checkCardTypeExist(request);
    CardType cardType = toEntity(request, CardType.class);
    return toDTO(create(cardType), CardTypeResponse.class);
  }

  @Override
  public void delete(Long id) {
    log.info("(delete) id: {}", id);
    repository.delete(find(id));
  }

  @Override
  public CardTypeResponse retrieve(Long id) {
    log.info("(retrieve)id: {}", id);
    return toDTO(find(id), CardTypeResponse.class);
  }

  @Override
  public List<CardTypeResponse> list(String keyword) {
    log.info("(list) keyword: {}", keyword);
    List<CardTypeResponse> cardTypeResponses = repository.search(keyword);
    return toDTOs(cardTypeResponses, CardTypeResponse.class);

  }

  @Override
  public CardTypeResponse update(Long id, CardTypeRequest request) {
    log.info("(update) id: {}, request: {}", id, request);
    CardType cardTypeUpdate = find(id);
    if (checkEditField(cardTypeUpdate, request)) {
      checkCardTypeExist(request);
      updateCardType(cardTypeUpdate, request);
      return toDTO(update(cardTypeUpdate), CardTypeResponse.class);
    }
    return toDTO(cardTypeUpdate, CardTypeResponse.class);
  }

  @Override
  public void checkCardTypeExist(CardTypeRequest request) {
    log.info("(checkCardTypeExist) start");
    if (repository.existsByName(request.getName())) {
      log.error("(checkCardTypeExist) card type already exists");
      throw new CardTypeAlreadyExistException();
    }
  }

  @Override
  public CardTypeDetail findCardType(Long idVehicle) {
    return repository.findCardType(idVehicle);
  }

  private boolean checkEditField(CardType cardType, CardTypeRequest request) {
    log.info("(checkEditField) cardType: {}, request: {}", cardType, request);
    if (cardType.getName().equals(request.getName()) &
          cardType.getDescription().equals(request.getDescription())) {
      return false;
    }
    return true;
  }

  @Override
  public CardType find(Long id) {
    log.info("(find) id: {}", id);
    return repository.
          findById(id).
          orElseThrow(() -> new CardTypeNotFoundException());
  }

  @Override
  public CardTypeResponse detail(Long id) {
    log.info("(detail) id: {}", id);
    return (id == null) ? null : toDTO(find(id), CardTypeResponse.class);
  }

  public void updateCardType(CardType cardType, CardTypeRequest request) {
    cardType.setName(request.getName());
    cardType.setDescription(request.getDescription());
  }
}
