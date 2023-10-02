package com.ncsgroup.ems.service.person;

import com.ncsgroup.ems.dto.request.person.card.PersonCardRequest;
import com.ncsgroup.ems.dto.response.personcard.PersonCardPageResponse;
import com.ncsgroup.ems.dto.response.personcard.PersonCardResponse;
import com.ncsgroup.ems.entity.person.PersonCard;
import com.ncsgroup.ems.service.base.BaseService;

public interface PersonCardService extends BaseService<PersonCard> {
  PersonCardResponse create(PersonCardRequest request);

  PersonCardResponse update(Long id, PersonCardRequest request);

  PersonCardResponse retrieve(Long id);

  PersonCardPageResponse list(String keyword, int size, int page, boolean isAll);
}
