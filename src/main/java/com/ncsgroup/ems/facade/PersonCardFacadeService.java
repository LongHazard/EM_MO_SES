package com.ncsgroup.ems.facade;

import com.ncsgroup.ems.dto.request.person.card.PersonCardRequest;
import com.ncsgroup.ems.dto.response.personcard.PersonCardPageResponse;
import com.ncsgroup.ems.dto.response.personcard.PersonCardResponse;

public interface PersonCardFacadeService {
  /**
   * Creates a new PersonCard.
   *
   * @param request The PersonCardRequest containing the information for creating a new PersonCard.
   * @return The PersonCardResponse representing the newly created PersonCard.
   * @exception: CardCodeAlreadyExistException if card code is existed
   */
  PersonCardResponse create(PersonCardRequest request);

  /**
   * Updates an existing PersonCard.
   *
   * @param id      The ID of the PersonCard to be updated.
   * @param request The PersonCardRequest containing the information for updating the PersonCard.
   * @return The PersonCardResponse representing the updated PersonCard.
   */
  PersonCardResponse update(Long id, PersonCardRequest request);

  /**
   * Get PersonCard.
   *
   * @param id The ID of the PersonCard
   * @return The PersonCardResponse representing the PersonCard.
   */
  PersonCardResponse get(Long id);

  /**
   * get list person card
   *
   * @param keyword: keyword to search
   * @param size:    size of page
   * @param page:    page number
   * @param isAll:   condition to get all address
   * @return list of person card
   */
  PersonCardPageResponse list(String keyword, int size, int page, boolean isAll);
}
