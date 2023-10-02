package com.ncsgroup.ems.repository;

import com.ncsgroup.ems.dto.response.personcard.PersonCardResponse;
import com.ncsgroup.ems.entity.person.PersonCard;
import com.ncsgroup.ems.repository.base.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonCardRepository extends BaseRepository<PersonCard> {

  boolean existsByCardCode(String cardCode);

  @Query(value = "select new com.ncsgroup.ems.dto.response.personcard.PersonCardResponse( " +
        " pc.id, ct.id, ct.identity, ct.name, ct.description , pc.cardCode, pc.dateOfBirth, pc.sex, pc.nationality, " +
        " pc.issueOn, pc.dateOfExpiry, pc.placeOfIssue ) " +
        "from PersonCard pc " +
        "left join CardType ct on pc.cardTypeId = ct.id " +
        "where :keyword is null " +
        "or pc.cardCode like %:keyword%"
  )
  List<PersonCardResponse> search(String keyword, Pageable pageable);

  @Query(value = "select new com.ncsgroup.ems.dto.response.personcard.PersonCardResponse(" +
        " pc.id, ct.id, ct.identity, ct.name, ct.description , pc.cardCode, pc.dateOfBirth, pc.sex, pc.nationality, " +
        " pc.issueOn, pc.dateOfExpiry, pc.placeOfIssue ) " +
        "from PersonCard pc " +
        "left join CardType ct on pc.cardTypeId = ct.id "
  )
  List<PersonCardResponse> getAll();

  @Query(value = "select new com.ncsgroup.ems.dto.response.personcard.PersonCardResponse(" +
        " pc.id, ct.id, ct.identity, ct.name, ct.description , pc.cardCode, pc.dateOfBirth, pc.sex, pc.nationality, " +
        " pc.issueOn, pc.dateOfExpiry, pc.placeOfIssue ) " +
        "from PersonCard pc left join CardType ct on pc.cardTypeId = ct.id where pc.id = :id"
  )
  PersonCardResponse detail(long id);

  @Query(value = "select count(pc) " +
        "from PersonCard pc where :keyword is null " +
        "or pc.cardCode like %:keyword%"
  )
  int countSearch(String keyword);
}
