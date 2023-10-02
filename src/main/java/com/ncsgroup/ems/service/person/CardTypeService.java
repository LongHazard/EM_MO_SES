package com.ncsgroup.ems.service.person;

import com.ncsgroup.ems.dto.request.cardtype.CardTypeRequest;
import com.ncsgroup.ems.dto.response.cardtype.CardTypeDetail;
import com.ncsgroup.ems.dto.response.cardtype.CardTypeResponse;
import com.ncsgroup.ems.entity.person.CardType;
import com.ncsgroup.ems.service.base.BaseService;

import java.util.List;

public interface CardTypeService extends BaseService<CardType> {
  /**
   * create new card type
   *
   * @param request - info of new card type
   * @return info of new card type after save in database
   */
  CardTypeResponse create(CardTypeRequest request);

  /**
   * delete card type
   *
   * @param id - id of card type which need to delete
   */
  void delete(Long id);

  /**
   * get card type
   *
   * @param id - id of card type
   * @return info of card type exist in database
   * @exception: CardTypeNotFoundException ìf it not found
   */
  CardTypeResponse retrieve(Long id);

  /**
   * List card type
   *
   * @param keyword - keyword to search
   * @return list card type
   */
  List<CardTypeResponse> list(String keyword);

  /**
   * update card type
   *
   * @param id      - id of the card type which need to update
   * @param request - new info of card type
   * @return info of new card type after update in database
   */
  CardTypeResponse update(Long id, CardTypeRequest request);

  /**
   * find card type
   *
   * @param id      - id of the card type which need to find
   * @return infor of card type
   */
  CardType find(Long id);

  /**
   * detail card type
   *
   * @param id      - id of the card type which need to detail
   * @return details of card type
   */
  CardTypeResponse detail(Long id);

  void checkCardTypeExist(CardTypeRequest request);

  CardTypeDetail findCardType(Long idVehicle);
}
