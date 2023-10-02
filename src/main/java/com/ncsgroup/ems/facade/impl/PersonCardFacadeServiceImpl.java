package com.ncsgroup.ems.facade.impl;

import com.ncsgroup.ems.dto.request.person.card.PersonCardRequest;
import com.ncsgroup.ems.dto.response.address.AddressResponse;
import com.ncsgroup.ems.dto.response.cardtype.CardTypeResponse;
import com.ncsgroup.ems.dto.response.personcard.PersonCardPageResponse;
import com.ncsgroup.ems.dto.response.personcard.PersonCardResponse;
import com.ncsgroup.ems.entity.address.Address;
import com.ncsgroup.ems.facade.PersonCardFacadeService;
import com.ncsgroup.ems.service.address.AddressService;
import com.ncsgroup.ems.service.person.CardTypeService;
import com.ncsgroup.ems.service.person.PersonCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.ncsgroup.ems.constanst.EMSConstants.AddressType.PLACE_OF_ORIGIN_TYPE;
import static com.ncsgroup.ems.constanst.EMSConstants.AddressType.PERMANENT_RESIDENT_TYPE;

@Slf4j
@RequiredArgsConstructor
public class PersonCardFacadeServiceImpl implements PersonCardFacadeService {
  private final PersonCardService personCardService;
  private final CardTypeService cardTypeService;
  private final AddressService addressService;

  @Override
  @Transactional
  public PersonCardResponse create(PersonCardRequest request) {
    log.info("(create)request: {}", request);

    if (Objects.isNull(request)) return null;

    CardTypeResponse cardType = Objects.isNull(request.getCardTypeId()) ? null :
          cardTypeService.retrieve(
                request.getCardTypeId()
          );

    PersonCardResponse personCardResponse = personCardService.create(request);

    personCardResponse.setCardType(cardType);

    this.createAddress(request, personCardResponse);

    return personCardResponse;
  }

  @Override
  @Transactional
  public PersonCardResponse update(Long id, PersonCardRequest request) {
    log.info("(update)id: {}, request: {}", id, request);

    CardTypeResponse cardType = Objects.isNull(request.getCardTypeId()) ? null : cardTypeService.retrieve(
          request.getCardTypeId()
    );

    PersonCardResponse personCardResponse = personCardService.update(id, request);
    AddressResponse addressResponse;

    if (Objects.nonNull(request.getPlaceOfOrigin())) {
      addressService.update(
            addressService.getId(id, PLACE_OF_ORIGIN_TYPE),
            request.getPlaceOfOrigin(),
            id,
            PLACE_OF_ORIGIN_TYPE
      );
      addressResponse = addressService.getAddressOfPersonCard(id, PLACE_OF_ORIGIN_TYPE);
      personCardResponse.setPlaceOfOrigin(addressResponse);
    }

    if (Objects.nonNull(request.getPermanentResident())) {
      addressService.update(
            addressService.getId(id, PERMANENT_RESIDENT_TYPE),
            request.getPermanentResident(),
            id,
            PERMANENT_RESIDENT_TYPE
      );
      addressResponse = addressService.getAddressOfPersonCard(id, PERMANENT_RESIDENT_TYPE);
      personCardResponse.setPermanentResident(addressResponse);
    }


    personCardResponse.setCardType(cardType);

    return personCardResponse;
  }

  @Override
  public PersonCardResponse get(Long id) {
    log.info("(get)id: {}", id);

    PersonCardResponse personCardResponse = personCardService.retrieve(id);
    AddressResponse placeOfOrigin = (Objects.isNull(id)) ? null : addressService.getAddressOfPersonCard(
          id,
          PLACE_OF_ORIGIN_TYPE
    );
    AddressResponse permanentResident = (Objects.isNull(id)) ? null : addressService.getAddressOfPersonCard(
          id,
          PERMANENT_RESIDENT_TYPE
    );
    personCardResponse.setPlaceOfOrigin(placeOfOrigin);
    personCardResponse.setPermanentResident(permanentResident);
    return personCardResponse;
  }

  @Override
  public PersonCardPageResponse list(String keyword, int size, int page, boolean isAll) {
    log.info("(list)keyword: {}, size : {}, page: {}, isAll: {}", keyword, size, page, isAll);

    return personCardService.list(keyword, size, page, isAll);
  }


  private void createAddress(
        PersonCardRequest request,
        PersonCardResponse personCardResponse
  ) {
    log.debug("(createAddress)request: {}, personCardResponse: {}", request, personCardResponse);

    if (Objects.nonNull(request.getPlaceOfOrigin())) {
      Address placeOfOrigin = addressService.create(
            request.getPlaceOfOrigin(),
            personCardResponse.getId(),
            PLACE_OF_ORIGIN_TYPE
      );
      personCardResponse.setPlaceOfOrigin(
            addressService.detail(placeOfOrigin.getId())
      );
    }

    if (Objects.nonNull(request.getPermanentResident())) {
      Address permanentResident = addressService.create(
            request.getPermanentResident(),
            personCardResponse.getId(),
            PERMANENT_RESIDENT_TYPE
      );


      personCardResponse.setPermanentResident(
            addressService.detail(permanentResident.getId())
      );
    }
  }
}
