package com.ncsgroup.ems.configuration;


import com.ncsgroup.ems.facade.PersonCardFacadeService;
import com.ncsgroup.ems.facade.PersonFacadeService;
import com.ncsgroup.ems.facade.VehicleCardFacadeService;
import com.ncsgroup.ems.facade.VehicleFacadeService;
import com.ncsgroup.ems.facade.impl.PersonCardFacadeServiceImpl;
import com.ncsgroup.ems.facade.impl.PersonFacadeServiceImpl;
import com.ncsgroup.ems.facade.impl.VehicleCardFacadeServiceImpl;
import com.ncsgroup.ems.facade.impl.VehicleFacadeServiceImpl;
import com.ncsgroup.ems.repository.*;
import com.ncsgroup.ems.service.MessageService;
import com.ncsgroup.ems.service.MinIOService;
import com.ncsgroup.ems.service.address.AddressService;
import com.ncsgroup.ems.service.address.impl.AddressServiceImpl;
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
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;

@TestConfiguration
@EnableJpaRepositories(basePackages = {"com.ncsgroup.ems.repository"},
      entityManagerFactoryRef = "entityManagerFactoryTest",
      transactionManagerRef = "transactionManagerTest ")
@ComponentScan(basePackages = {"com.ncsgroup.ems.repository"})
@EnableTransactionManagement
//@ActiveProfiles("test")
public class ServiceConfigurationTest {

  @Bean
  public DataSource dataSource() {
    EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
    return builder.setType(EmbeddedDatabaseType.H2).build();
  }

  @Value("${application.minio.bucket-name}")
  private String bucketName;

  @Value("${application.minio.url}")
  private String minioUrl;

  @Value("${application.minio.access-key}")
  private String accessKey;

  @Value("${application.minio.secret-key}")
  private String secretKey;

  @Bean
  public EntityManagerFactory entityManagerFactoryTest() throws IOException {

    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setGenerateDdl(true);

    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    factory.setJpaVendorAdapter(vendorAdapter);
    factory.setPackagesToScan("com.ncsgroup.ems.entity");
    factory.setDataSource(dataSource());
    factory.afterPropertiesSet();
    return factory.getObject();
  }

  @Bean
  public PlatformTransactionManager transactionManagerTest() throws IOException {
    JpaTransactionManager txManager = new JpaTransactionManager();
    txManager.setEntityManagerFactory(entityManagerFactoryTest());
    return txManager;
  }

  @Bean

  public VehicleService vehicleServiceTest(
        VehicleRepository repository
  ) {
    return new VehicleServiceImpl(repository);

  }

  @Bean
  public PersonService personServiceTest(PersonRepository repository) {
    return new PersonServiceImpl(repository);
  }

  @Bean
  public VehicleCardService vehicleCardService(VehicleCardRepository repository,
                                               VehicleCardColorRepository vehicleCardColorRepository) {
    return new VehicleCardServiceImpl(repository, vehicleCardColorRepository);
  }

  @Bean
  public GroupService groupService(
        GroupRepository groupRepository,
        IdentityObjectGroupRepository identityObjectGroupRepository
  ) {
    return new GroupServiceImpl(groupRepository, identityObjectGroupRepository);
  }

  @Bean
  public VehicleCardColorService vehicleCardColorService(VehicleCardColorRepository repository) {
    return new VehicleCardColorServiceImpl(repository);
  }

  @Bean
  public ColorService colorService(ColorRepository colorRepository) {
    return new ColorServiceImpl(colorRepository);
  }

  @Bean
  public IdentityObjectService identityObjectService(IdentityObjectRepository repository) {
    return new IdentityObjectServiceImpl(repository);
  }

  @Bean
  public PersonCardService personCardService(PersonCardRepository repository) {
    return new PersonCardServiceImpl(repository);
  }

  @Bean
  public VehicleBrandService vehicleBrandService(VehicleBrandRepository vehicleBrandRepository) {
    return new VehicleBrandServiceImpl(vehicleBrandRepository);
  }

  @Bean
  public CardTypeService cardTypeService(CardTypeRepository repository) {
    return new CardTypeServiceImpl(repository);
  }

  @Bean
  public AddressService addressService(AddressRepository repository) {
    return new AddressServiceImpl(repository);
  }

  @Bean
  public VehicleTypeService vehicleTypeService(VehicleTypeRepository vehicleTypeRepository) {
    return new VehicleTypeServiceImpl(vehicleTypeRepository);
  }

  @Bean
  IdentityGroupObjectService identityGroupObjectService(IdentityObjectGroupRepository repository) {
    return new IdentityObjectGroupServiceImpl(repository);
  }

  @Bean
  VehicleColorService vehicleColorService(VehicleColorRepository repository){
    return new VehicleColorServiceImpl(repository);
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
  public ImageService imageService(ImageRepository repository, MinIOService minIOService) {
    return new ImageServiceImpl(repository, minIOService, bucketName);
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
  public MessageService messageService(MessageSource messageSource) {
    return new MessageServiceImpl(messageSource);
  }
}

