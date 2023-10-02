package com.ncsgroup.ems.configuration;

import com.ncsgroup.ems.facade.*;
import com.ncsgroup.ems.facade.impl.*;
import com.ncsgroup.ems.repository.*;
import com.ncsgroup.ems.service.MessageService;
import com.ncsgroup.ems.service.MinIOService;
import com.ncsgroup.ems.service.address.AddressService;
import com.ncsgroup.ems.service.address.DistrictService;
import com.ncsgroup.ems.service.address.ProvinceService;
import com.ncsgroup.ems.service.address.WardService;
import com.ncsgroup.ems.service.address.impl.AddressServiceImpl;
import com.ncsgroup.ems.service.address.impl.DistrictServiceImpl;
import com.ncsgroup.ems.service.address.impl.ProvinceServiceImpl;
import com.ncsgroup.ems.service.address.impl.WardServiceImpl;
import com.ncsgroup.ems.service.identity.GroupService;
import com.ncsgroup.ems.service.identity.IdentityGroupObjectService;
import com.ncsgroup.ems.service.identity.IdentityObjectService;
import com.ncsgroup.ems.service.identity.ImageService;
import com.ncsgroup.ems.service.identity.impl.GroupServiceImpl;
import com.ncsgroup.ems.service.identity.impl.IdentityObjectGroupServiceImpl;
import com.ncsgroup.ems.service.identity.impl.IdentityObjectServiceImpl;
import com.ncsgroup.ems.service.identity.impl.ImageServiceImpl;
import com.ncsgroup.ems.service.impl.MessageServiceImpl;
import com.ncsgroup.ems.service.impl.MinIOServiceImpl;
import com.ncsgroup.ems.service.person.CardTypeService;
import com.ncsgroup.ems.service.person.PersonCardService;
import com.ncsgroup.ems.service.person.PersonService;
import com.ncsgroup.ems.service.person.impl.CardTypeServiceImpl;
import com.ncsgroup.ems.service.person.impl.PersonCardServiceImpl;
import com.ncsgroup.ems.service.person.impl.PersonServiceImpl;
import com.ncsgroup.ems.service.vehicle.*;
import com.ncsgroup.ems.service.vehicle.impl.*;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmsServiceConfiguration {

  @Value("${application.minio.bucket-name}")
  private String bucketName;

  @Value("${application.minio.url}")
  private String minioUrl;

  @Value("${application.minio.access-key}")
  private String accessKey;

  @Value("${application.minio.secret-key}")
  private String secretKey;

  @Bean
  public MinioClient minioClient() {
    return MinioClient
          .builder()
          .endpoint(minioUrl)
          .credentials(accessKey, secretKey)
          .build();
  }

  @Bean
  public MinIOService minIOService(MinioClient minioClient) {
    return new MinIOServiceImpl(bucketName, minioClient);
  }

  @Bean
  public VehicleBrandService vehicleBrandService(VehicleBrandRepository repository) {
    return new VehicleBrandServiceImpl(repository);
  }

  @Bean
  public CardTypeService cardTypeService(CardTypeRepository repository) {
    return new CardTypeServiceImpl(repository);
  }

  @Bean
  public ColorService colorService(ColorRepository repository) {
    return new ColorServiceImpl(repository);
  }

  @Bean
  public GroupService groupService(GroupRepository repository,
                                   IdentityObjectGroupRepository identityObjectGroupRepository) {
    return new GroupServiceImpl(repository, identityObjectGroupRepository);
  }

  @Bean
  public GroupFacadeService groupFacadeService(GroupService groupService,
                                               PersonService personService,
                                               VehicleService vehicleService
  ) {
    return new GroupFacadeServiceImpl(groupService, personService, vehicleService);
  }

  @Bean
  public VehicleTypeService vehicleTypeService(VehicleTypeRepository repository) {
    return new VehicleTypeServiceImpl(repository);
  }

  @Bean
  public MessageService messageService(MessageSource messageSource) {
    return new MessageServiceImpl(messageSource);
  }


  @Bean
  public PersonCardService personCardService(PersonCardRepository repository) {
    return new PersonCardServiceImpl(repository);
  }

  @Bean
  PersonService personService(PersonRepository repository) {
    return new PersonServiceImpl(repository);
  }

  @Bean
  public PersonFacadeService personFacadeService(
        PersonService personService,
        PersonCardFacadeService personCardFacadeService,
        ImageService imageService,
        IdentityObjectService identityObjectService,
        VehicleService vehicleService,
        GroupService groupService
  ) {
    return new PersonFacadeServiceImpl(personService,
          personCardFacadeService, imageService, identityObjectService, vehicleService, groupService);
  }

  @Bean
  public PersonCardFacadeService personCardFacadeService(
        PersonCardService personCardService,
        CardTypeService cardTypeService,
        AddressService addressService
  ) {
    return new PersonCardFacadeServiceImpl(personCardService, cardTypeService, addressService);
  }

  @Bean
  public VehicleService vehicleService(
        VehicleRepository repository
  ) {
    return new VehicleServiceImpl(repository);
  }

  @Bean
  public ImageService imageService(ImageRepository repository, MinIOService minIOService) {
    return new ImageServiceImpl(repository, minIOService, bucketName);
  }


  @Bean
  public VehicleCardService vehicleCardService(VehicleCardRepository repository,
                                               VehicleCardColorRepository vehicleCardColorRepository) {
    return new VehicleCardServiceImpl(repository, vehicleCardColorRepository);
  }

  @Bean
  public IdentityObjectService identityObjectService(IdentityObjectRepository repository) {
    return new IdentityObjectServiceImpl((repository));
  }

  @Bean
  public VehicleColorService vehicleColorService(VehicleColorRepository repository) {
    return new VehicleColorServiceImpl(repository);
  }

  @Bean
  public VehicleCardFacadeService vehicleCardFacadeService(
        VehicleCardService vehicleCardService,
        CardTypeService cardTypeService,
        VehicleBrandService vehicleBrandService,
        VehicleTypeService vehicleTypeService,
        ColorService colorService,
        AddressService addressService,
        ImageService imageService,
        VehicleCardColorService vehicleCardColorService
  ) {

    return new VehicleCardFacadeServiceImpl(
          vehicleCardService,
          cardTypeService,
          vehicleBrandService,
          vehicleTypeService,
          colorService,
          addressService,
          imageService,
          vehicleCardColorService
    );
  }

  @Bean
  public VehicleFacadeService vehicleFacadeService(
        VehicleBrandService vehicleBrandService,
        VehicleService vehicleService,
        ImageService imageService,
        ColorService colorService,
        CardTypeService cardTypeService,
        IdentityObjectService identityObjectService,
        PersonService personService,
        VehicleColorService vehicleColorService,
        VehicleCardFacadeService vehicleCardFacadeService,
        GroupService groupService,
        VehicleTypeService vehicleTypeService,
        IdentityGroupObjectService identityGroupObjectService
  ) {
    return new VehicleFacadeServiceImpl(
          vehicleBrandService,
          vehicleService,
          imageService,
          colorService,
          cardTypeService,
          identityObjectService,
          personService,
          vehicleColorService,
          vehicleCardFacadeService,
          groupService,
          vehicleTypeService,
          identityGroupObjectService
    );
  }

  @Bean
  public VehicleCardColorService vehicleCardColorService(VehicleCardColorRepository repository) {
    return new VehicleCardColorServiceImpl(repository);
  }

  @Bean
  public ProvinceService provinceService(ProvinceRepository repository) {
    return new ProvinceServiceImpl(repository);
  }

  @Bean
  public DistrictService districtService(DistrictRepository repository) {
    return new DistrictServiceImpl(repository);
  }

  @Bean
  public WardService wardService(WardRepository repository) {
    return new WardServiceImpl(repository);
  }

  @Bean
  public AddressService addressService(AddressRepository repository) {
    return new AddressServiceImpl(repository);
  }

  @Bean
  public IdentityGroupObjectService identityGroupObjectService(IdentityObjectGroupRepository repository) {
    return new IdentityObjectGroupServiceImpl(repository);
  }

  @Bean
  public IdentityObjectFacadeService identityObjectFacadeService(
        IdentityObjectService identityObjectService,
        PersonService personService,
        VehicleService vehicleService
  ) {
    return new IdentityObjectFacadeServiceImpl(
          identityObjectService,
          personService,
          vehicleService
    );
  }
}
