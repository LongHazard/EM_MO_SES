package com.ncsgroup.ems.service.person.impl;

import com.ncsgroup.ems.dto.request.person.PersonRequest;
import com.ncsgroup.ems.dto.request.person.PersonSearchRequest;
import com.ncsgroup.ems.dto.request.person.SearchPersonRequest;
import com.ncsgroup.ems.dto.response.identity.IdentityResponse;
import com.ncsgroup.ems.dto.response.person.*;
import com.ncsgroup.ems.entity.person.Person;
import com.ncsgroup.ems.exception.person.*;
import com.ncsgroup.ems.repository.PersonRepository;
import com.ncsgroup.ems.service.person.PersonService;
import com.ncsgroup.ems.service.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static com.ncsgroup.ems.utils.MapperUtils.toDTO;
import static com.ncsgroup.ems.utils.ValidationUtils.isNullOrEmpty;

@Slf4j
public class PersonServiceImpl extends BaseServiceImpl<Person> implements PersonService {
  private final PersonRepository repository;

  public PersonServiceImpl(PersonRepository repository) {
    super(repository);
    this.repository = repository;
  }

  @Override
  public PersonCreateResponse create(PersonRequest request, Long personCardId) {
    log.info("(create) request: {}", request);
    checkUnique(
          request.getEmail(),
          request.getPhoneNumber(),
          request.getStaffCode(),
          request.getFaceId()
    );

    Person person = map(request);
    person.setId(null);
    if (personCardId != null) {
      person.setPersonCardId(personCardId);
    }
    return toDTO(create(person), PersonCreateResponse.class);
  }

  @Override
  public PersonCreateResponse update(Long id, PersonRequest request) {
    log.info("(update) id: {}, personRequest: {} ", id, request);
    Person existedPerson = find(id);
    checkExistedPreUpdate(request, existedPerson);

    Person person = map(request);
    person.setId(id);
    person.setUid(existedPerson.getUid());

    person.setPersonCardId(existedPerson.getPersonCardId());

    return toDTO(create(person), PersonCreateResponse.class);
  }

  @Override
  public Person checkNotFound(Long id) {
    return repository.findById(id).orElseThrow(PersonNotFoundException::new);
  }

  public List<String> findNames(List<Long> personIds) {
    return repository.findNames(personIds);
  }

  @Override
  public Long findIdByUid(String uid) {
    log.info("(findIdByUid) uid: {}", uid);

    checkUidExist(uid);
    return repository.getIdByUid(uid);
  }

  @Override
  public String findNameById(Long id) {
    return repository.findNameById(id);
  }

  private void checkExistedPreUpdate(PersonRequest request, Person person) {
    log.info("(checkExistedPreUpdate) request: {}, person: {}", request, person);
    if (!Objects.equals(request.getEmail(), person.getEmail())
          && repository.existsByEmail(request.getEmail())
    ) {
      throw new EmailAlreadyExistException();
    }

    if (!Objects.equals(request.getPhoneNumber(), person.getPhoneNumber())
          && repository.existsByPhoneNumber(request.getPhoneNumber())
    ) {
      throw new PhoneNumberAlreadyExistException();
    }

    if (!Objects.equals(request.getStaffCode(), person.getStaffCode())
          && repository.existsByStaffCode(request.getStaffCode())
    ) {
      throw new StaffCodeAlreadyExistException();
    }

    if (!Objects.equals(request.getFaceId(), person.getFaceId())
          && repository.existsByFaceId(request.getFaceId())
    ) {
      throw new FaceIdAlreadyExistException();
    }
  }

  private Person find(Long id) {
    return repository.findById(id).orElseThrow(PersonNotFoundException::new);
  }

  @Override
  public PersonPageResponse list(SearchPersonRequest request, int size, int page, boolean isAll) {
    log.info("(list) request: {}, size : {}, page: {}, isAll: {}", request, size, page, isAll);

    Pageable pageable = (isAll) ? null : PageRequest.of(page, size);

    boolean faceIdsIsEmpty = request == null || request.getFaceIds() == null || request.getFaceIds().isEmpty();

    String keyword = (request == null || request.getKeyword() == null) ? null : request.getKeyword();

    List<PersonResponse> personResponses = faceIdsIsEmpty
          ? repository.listByKeyword(keyword, pageable)
          : repository.listBySearch(keyword, request.getFaceIds(), pageable);

    return PersonPageResponse.of(personResponses, faceIdsIsEmpty ? repository.countByKeyword(keyword)
          : repository.countSearch(keyword, request.getFaceIds()));
  }

  @Override
  public PersonPageResponse listByGroup(long groupId, int page, int size) {
    log.info("(listByGroup) groupId:{}, page:{}, size:{}", groupId, page, size);

    Pageable pageable = PageRequest.of(page, size);

    List<PersonResponse> personResponses = repository.listByGroup(groupId, pageable);

    return PersonPageResponse.of(personResponses, repository.countByGroup(groupId));
  }

  @Override
  public Set<String> getDepartment(String keyword) {
    log.info("(getDepartment) keyword: {}", keyword);
    return repository.findDepartment(keyword);
  }

  @Override
  public Set<String> getPosition(String keyword) {
    log.info("(getPosition) keyword: {}", keyword);
    return repository.findPosition(keyword);
  }

  @Override
  public Set<String> getOrg(String keyword) {
    log.info("(getOrg) keyword: {}", keyword);
    return repository.findOrg(keyword);
  }

  @Override
  public Long getIdByVehicle(Long vehicleId) {
    log.info("(getByVehicle) vehicleId:{}", vehicleId);
    return repository.getIdByVehicle(vehicleId);
  }

  @Override
  public void updatePersonCardId(long id, long personCardId) {
    log.info("(updatePersonCardId) personCardId : {}", personCardId);
    repository.updatePersonCardId(id, personCardId);
  }

  public Person find(String uid) {
    log.info("(find) uid : {}", uid);
    return null;
  }

  @Override
  @Transactional
  public void remove(long id) {
    log.info("(remove) id : {}", id);
    find(id);
    repository.remove(id);
  }

  @Override
  public Person findByVehicleIdOrNull(Long vehicleId) {
    log.info("(findByVehicleId) vehicleId : {}", vehicleId);

    return repository.findByVehicleId(vehicleId).orElse(null);
  }

  @Override
  public PagePersonResponse list(PersonSearchRequest request, int page, int size, boolean isAll) {
    log.info("(list) request:{}, page:{}, size:{}, isAll:{}", request, page, size, isAll);

    Pageable pageable = (isAll) ? null : PageRequest.of(page, size);
    String keyword = (request == null || request.getKeyword() == null) ? null : request.getKeyword().toLowerCase();

    return PagePersonResponse.of(repository.list(keyword, pageable), repository.countSearch(keyword));
  }

  @Override
  public List<PersonDTO> list(List<Long> personIds) {
    log.info("(list) personIds:{}", personIds);
    return repository.list(personIds);
  }

  @Override
  public Long getPersonCardId(Long personId) {
    log.info("(getPersonCardId) personId : {}", personId);

    find(personId);

    return repository.getPersonCarId(personId);
  }

  @Override
  public int countVehicle(Long personId) {
    log.info("(countVehicle) personId : {}", personId);

    find(personId);

    return repository.countVehicle(personId);
  }

  @Override
  public PersonCreateResponse detail(Long id) {
    log.info("(detail) id :{}", id);
    find(id);

    return repository.detail(id);
  }

  @Override
  public List<IdentityResponse> getPersonByGroupId(Long groupId) {
    log.info("(getPersonByGroupId) groupId: {}", groupId);

    return repository.getPersonByGroupId(groupId);
  }

  @Override
  public List<IdentityResponse> getPersonByGroupId(List<Long> groupIds) {
    log.info("(getPersonByGroupId) groupId: {}", groupIds);

    return repository.getPersonByGroupId(groupIds);
  }

  public List<IdentityResponse> getByOutsideGroupIds(List<Long> groupIds) {
    log.info("(getByOutsideGroupIds) groupIds: {}", groupIds);

    return repository.getByOutsideGroupIds(groupIds);
  }


  private void checkEmailExist(String email) {
    log.info("(checkEmailExist) email : {}", email);
    if (isNullOrEmpty(email)) return;
    if (repository.existsByEmail(email)) {
      log.error("(checkEmailExist) =============> EmailAlreadyExistException");
      throw new EmailAlreadyExistException();
    }
  }

  private void checkPhoneNumberExist(String phoneNumber) {
    log.info("(checkPhoneNumberExist) phoneNumber : {}", phoneNumber);
    if (isNullOrEmpty(phoneNumber)) return;
    if (repository.existsByPhoneNumber(phoneNumber)) {
      log.error("(checkPhoneNumberExist) =============> PhoneNumberAlreadyExistException");
      throw new PhoneNumberAlreadyExistException();
    }
  }

  private void checkStaffCodeExist(String staffCode) {
    log.info("(checkStaffCodeExist) staffCode : {}", staffCode);
    if (isNullOrEmpty(staffCode)) return;
    if (repository.existsByStaffCode(staffCode)) {
      log.error("(checkStaffCodeExist) =============> StaffCodeAlreadyExistException");
      throw new StaffCodeAlreadyExistException();
    }
  }

  private void checkUidExist(String uid) {
    log.info("(checkUidExist) uid : {}", uid);
    if (isNullOrEmpty(uid)) return;
    if (repository.existsByUid(uid) == false) {
      throw new PersonNotFoundException();
    }
  }


  private void checkFaceIdExist(String faceId) {
    log.info("(checkFaceIdExist) faceId : {}", faceId);
    if (Objects.isNull(faceId)) return;
    if (repository.existsByFaceId(faceId)) {
      log.error("(checkFaceIdExist) =============> FaceIdAlreadyExistException");
      throw new FaceIdAlreadyExistException();
    }
  }

  private void checkPersonCardExist(String personCardId) {
    log.info("(checkPersonCardExist) personCardId : {}", personCardId);
    if (repository.existsByPersonCardId(personCardId)) {
      log.error("(checkPersonCardIdExist) =============> PersonCardIdAlreadyExistException");
      throw new PersonCardAlreadyExistException();
    }
  }

  private void checkUnique(String email, String phoneNumber, String staffCode, String faceId) {
    log.info("(checkUnique) email : {}, phoneNumber : {}, staffCode : {}, faceID : {}", email, phoneNumber, staffCode, faceId);
    checkEmailExist(email);
    checkPhoneNumberExist(phoneNumber);
    checkStaffCodeExist(staffCode);
    checkFaceIdExist(faceId);
  }

  private Person map(PersonRequest request) {
    Person person = new Person();
    person.setName(request.getName());
    person.setUid(UUID.randomUUID().toString());
    person.setEmail(request.getEmail());
    person.setSex(request.getSex());
    person.setPhoneNumber(request.getPhoneNumber());
    person.setStaffCode(request.getStaffCode());
    person.setPosition(request.getPosition());
    person.setDepartment(request.getDepartment());
    person.setFaceId(request.getFaceId());
    return person;
  }
}
