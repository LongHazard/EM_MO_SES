package com.ncsgroup.ems.service.person.impl;

import com.ncsgroup.ems.dto.request.person.card.PersonCardRequest;
import com.ncsgroup.ems.dto.response.personcard.PersonCardResponse;
import com.ncsgroup.ems.dto.response.personcard.PersonCardPageResponse;
import com.ncsgroup.ems.entity.person.PersonCard;
import com.ncsgroup.ems.exception.person.card.CardCodeAlreadyExistException;
import com.ncsgroup.ems.exception.person.card.PersonCardNotFoundException;
import com.ncsgroup.ems.repository.PersonCardRepository;
import com.ncsgroup.ems.service.person.PersonCardService;
import com.ncsgroup.ems.service.base.BaseServiceImpl;
import com.ncsgroup.ems.utils.DateUtils;
import com.ncsgroup.ems.utils.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.ncsgroup.ems.utils.MapperUtils.*;

@Slf4j
public class PersonCardServiceImpl extends BaseServiceImpl<PersonCard> implements PersonCardService {
  private final PersonCardRepository repository;

  public PersonCardServiceImpl(PersonCardRepository repository) {
    super(repository);
    this.repository = repository;
  }

  @Override
  @Transactional
  public PersonCardResponse create(PersonCardRequest request) {
    log.info("(create) request: {}", request);
    checkCardCodeExist(request.getCardCode());
    PersonCard personCard = map(request);
    PersonCardResponse personCardResponse = toDTO(create(personCard), PersonCardResponse.class);
    personCardResponse.setDateOfBirth(request.getDateOfBirth());
    personCardResponse.setDateOfExpiry(request.getDateOfExpiry());
    return personCardResponse;
  }

  @Override
  @Transactional
  public PersonCardResponse update(Long id, PersonCardRequest request) {
    log.info("(update)id: {}, request : {}", id, request);

    PersonCard existedPersonCard = find(id);
    checkExistPersonCardPreUpdate(existedPersonCard, request);

    PersonCard personCard = map(request);
    personCard.setId(id);

    PersonCardResponse response = toDTO(create(personCard), PersonCardResponse.class);
    response.setDateOfExpiry(request.getDateOfExpiry());

    return response;
  }

  @Override
  public PersonCardResponse retrieve(Long id) {
    log.info("(retrieve)id: {}", id);

    return repository.detail(id);
  }

  @Override
  public PersonCardPageResponse list(String keyword, int size, int page, boolean isAll) {
    log.info("(list)keyword: {}, size : {}, page: {}, isAll: {}", keyword, size, page, isAll);

    Pageable pageable = isAll ? null : PageRequest.of(page, size);

    return PersonCardPageResponse.of(
          repository.search(keyword, pageable),
          repository.countSearch(keyword)
    );
  }

  private void checkCardCodeExist(String cardCode) {
    log.info("(checkCardCodeExist) cardCode : {}", cardCode);
    if (ValidationUtils.isNullOrEmpty(cardCode)) return;
    if (repository.existsByCardCode(cardCode)) {
      log.error("(checkCardCodeExist) ============= CardCodeAlreadyExistException");
      throw new CardCodeAlreadyExistException();
    }
  }

  private PersonCard find(Long id) {
    log.info("(find)id: {}", id);
    return repository.findById(id).orElseThrow(PersonCardNotFoundException::new);
  }


  private void checkExistPersonCardPreUpdate(PersonCard personCard, PersonCardRequest request) {
    log.info("(checkExistPersonCardPreUpdate)start");

    if (!Objects.equals(personCard.getCardCode(), request.getCardCode())) {
      checkCardCodeExist(request.getCardCode());
    }
  }

  private PersonCard map(PersonCardRequest request) {
    PersonCard personCard = new PersonCard();
    personCard.setCardTypeId(request.getCardTypeId());
    personCard.setCardCode(request.getCardCode());
    personCard.setDateOfBirth(request.getDateOfBirth());
    personCard.setSex(request.getSex());
    personCard.setNationality(request.getNationality());
    personCard.setIssueOn(request.getIssueOn());
    personCard.setDateOfExpiry(DateUtils.convertToTimestamp(request.getDateOfExpiry()));
    personCard.setPlaceOfIssue(request.getPlaceOfIssue());
    return personCard;
  }

}
