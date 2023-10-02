package com.ncsgroup.ems.service.identity.impl;


import com.ncsgroup.ems.dto.File;
import com.ncsgroup.ems.dto.request.image.ImageRequest;
import com.ncsgroup.ems.dto.response.image.ImageResponse;
import com.ncsgroup.ems.entity.identity.Image;
import com.ncsgroup.ems.repository.ImageRepository;
import com.ncsgroup.ems.service.MinIOService;
import com.ncsgroup.ems.service.base.BaseServiceImpl;
import com.ncsgroup.ems.service.identity.ImageService;
import com.ncsgroup.ems.utils.ComparatorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.ncsgroup.ems.constanst.EMSConstants.ImageType.PERSON;
import static com.ncsgroup.ems.constanst.EMSConstants.ImageType.VEHICLE;
import static com.ncsgroup.ems.utils.ValidationUtils.isNullOrEmpty;

@Slf4j
public class ImageServiceImpl extends BaseServiceImpl<Image> implements ImageService {
  private final String imageBucket;
  private final ImageRepository repository;
  private final MinIOService minIOService;

  public ImageServiceImpl(ImageRepository repository, MinIOService minIOService, String imageBucket) {
    super(repository);
    this.repository = repository;
    this.minIOService = minIOService;
    this.imageBucket = imageBucket;
  }


  @Override
  @Transactional
  public List<ImageResponse> upload(List<MultipartFile> fileRequests) {
    log.info("(upload) {} images", fileRequests.size());

    List<Image> images = minIOService
          .putFile(fileRequests, imageBucket)
          .stream().map(this::convertToImage).toList();
    repository.saveAll(images);

    return images
          .stream()
          .map(image -> buildImageResponse(image, imageBucket))
          .toList();
  }

  @Override
  public List<ImageResponse> getAllByVehicle(Long vehicleId) {
    log.info("(getByVehicle) vehicleId: {}", vehicleId);

    return convertToImagesResponse(repository.findAllByVehicleId(vehicleId));
  }

  @Override
  public List<ImageResponse> getAllByPerson(Long personId) {
    log.info("(getByPerson) personId: {}", personId);

    return convertToImagesResponse(repository.findAllByPersonId(personId));
  }

  @Override
  public List<ImageResponse> getAllByVehicleCard(Long vehicleCardId) {
    log.info("(getByVehicleCard) vehicleCardId: {}", vehicleCardId);

    return convertToImagesResponse(repository.findAllByVehicleCardId(vehicleCardId));
  }


  @Override
  public List<ImageResponse> getAllByPersonCard(Long personCardId) {
    log.info("(getByPersonCard) personCardId: {}", personCardId);

    return convertToImagesResponse(repository.findAllByPersonCardId(personCardId));
  }

  @Override
  public ImageResponse getByVehicleId(Long vehicleId) {
    log.info("(getByVehicleId) vehicleId: {}", vehicleId);

    return buildImageResponse(repository.findByVehicleId(vehicleId).orElse(null), imageBucket);
  }


  @Override
  public ImageResponse getByPersonId(Long personId) {
    log.info("(getByPersonId) personId: {}", personId);

    return buildImageResponse(repository.findByPersonId(personId).orElse(null), imageBucket);
  }

  @Override
  public ImageResponse getByVehicleCardId(Long vehicleCardId) {
    log.info("(getByVehicleCardId) vehicleCardId: {}", vehicleCardId);

    return buildImageResponse(repository.findByVehicleCardId(vehicleCardId).orElse(null), imageBucket);
  }

  @Override
  public ImageResponse getByPersonCardId(Long personCardId) {
    log.info("(getByPersonCardId) personCardId: {}", personCardId);

    return buildImageResponse(repository.findByPersonCardId(personCardId).orElse(null), imageBucket);
  }


  @Override
  public void addImageToObject(
        List<Long> imageIds,
        Long vehicleId,
        Long personId,
        Long vehicleCardId,
        Long personCardId,
        String type
  ) {
    log.info("(addRefObject) imageIds: {}, vehicleId: {}, personId: {}, vehicleCardId: {}, personCardId: {}, type: {}",
          imageIds, vehicleId, personId, vehicleCardId, personCardId, type
    );

    repository.updateImageToObject(
          imageIds,
          vehicleId,
          personId,
          vehicleCardId,
          personCardId,
          type
    );
  }

  @Override
  //TODO
  public List<ImageResponse> getByRequests(List<ImageRequest> images) {
    log.info("(getByRequests) image request: {}", images);
    if (isNullOrEmpty(images)) {
      return null;
    }
    List<Long> imageIds = new ArrayList<>();
    for (ImageRequest image : images) {
      imageIds.add(image.getId());
    }
    return convertToImagesResponse(repository.findAllByIdIn(imageIds));
  }

  @Override
  @Transactional
  public void addImageToVehicleCard(
        List<ImageRequest> imageRequests,
        Long vehicleCardId
  ) {
    log.info("(addImageToVehicleCard) imageRequests: {}, vehicleCardId: {}", imageRequests, vehicleCardId);

    for (ImageRequest request : imageRequests) {
      repository.updateImageToObject(
            request.getId(),
            null,
            null,
            vehicleCardId,
            null,
            request.getType()
      );
    }
  }

  @Override
  @Transactional
  public void addImageToVehicle(
        List<ImageRequest> imageRequests,
        Long vehicleId
  ) {
    log.info("(addImageToVehicle) imageRequests: {}, vehicleId: {}", imageRequests, vehicleId);

    repository.updateImageToObject(
          this.getIdByImageRequest(imageRequests),
          vehicleId,
          null,
          null,
          null,
          VEHICLE
    );
  }

  @Override
  public void addImageToPerson(List<ImageRequest> imageRequests, Long personId) {
    log.info("(addImageToPerson) imageRequests: {}, personId: {}", imageRequests, personId);
    repository.updateImageToObject(
          this.getIdByImageRequest(imageRequests),
          null,
          personId,
          null,
          null,
          PERSON
    );
  }

  @Override
  public void addImageToPersonCard(List<ImageRequest> imageRequests, Long personCardId) {
    log.info("(addImageToPersonCard) imageRequests: {}, personCardId: {}", imageRequests, personCardId);

    for (ImageRequest request : imageRequests) {
      repository.updateImageToObject(
            request.getId(),
            null,
            null,
            null,
            personCardId,
            request.getType()
      );
    }
  }

  @Override
  @Transactional
  public void softDelete(Long id) {
    log.info("(softDelete)id: {}", id);

    repository.softDelete(id);
  }

  @Override
  @Transactional
  public void softDelete(List<Long> ids) {
    log.info("(softDelete)ids: {}", ids);

    repository.softDelete(ids);
  }

  @Override
  @Transactional
  public void update(List<Long> existedImagesIds, List<ImageRequest> imageRequests) {
    log.info("(update)existedImagesIds: {}, updateImageIds: {}", existedImagesIds, imageRequests);

    softDelete(
          ComparatorUtils.findUniqueElements(
                existedImagesIds,
                this.getImageIds(imageRequests)
          )
    );
  }

  @Override
  public List<Long> getIdByVehicle(Long vehicleId) {
    log.info("(getAllIdByVehicle)vehicleId: {}", vehicleId);

    return repository.findIdByVehicleId(vehicleId);
  }

  @Override
  public List<Long> getIdByPerson(Long personId) {
    log.info("(getIdByPerson)personId: {}", personId);

    return repository.findIdByPersonId(personId);
  }

  @Override
  public List<Long> getIdByVehicleCard(Long vehicleCardId) {
    log.info("(getIdByVehicleCard)vehicleCardId: {}", vehicleCardId);

    return repository.findIdByVehicleCardId(vehicleCardId);
  }

  @Override
  public List<Long> getIdByPersonCard(Long personCardId) {
    log.info("(getIdByPersonCard)personCardId: {}", personCardId);

    return repository.findIdByPersonCardId(personCardId);
  }

  @Override
  public List<Long> getIdByImageRequest(List<ImageRequest> imageRequests) {
    return imageRequests.stream().map(ImageRequest::getId).toList();
  }

  @Override
  public List<String> getTypeByImageRequest(List<ImageRequest> imageRequests) {
    return imageRequests.stream().map(ImageRequest::getType).toList;
  }

  private Image convertToImage(File file) {
    return Image.builder()
          .name(file.getName())
          .bucketName(imageBucket)
          .contentType(file.getContentType())
          .build();
  }

  private List<ImageResponse> convertToImagesResponse(List<Image> images) {
    log.info("(convertToImagesResponse) images: {}", images);

    return images
          .stream()
          .map(image -> buildImageResponse(image, imageBucket))
          .toList();
  }

  public ImageResponse buildImageResponse(Image image, String imageBucket) {
    log.info("(buildImageResponse) image: {}, imageBucket: {}", image, imageBucket);

    return Objects.nonNull(image) ? ImageResponse.builder()
          .id(image.getId())
          .type(image.getType())
          .contentType(image.getContentType())
          .url(minIOService.getPreSignedUrl(
                image.getName(),
                image.getContentType(),
                TimeUnit.HOURS,
                imageBucket,
                2
          ))
          .build() : null;
  }

  private List<Long> getImageIds(List<ImageRequest> imageRequests) {
    if (Objects.isNull(imageRequests)) return new ArrayList<>();

    return imageRequests
          .stream()
          .map(ImageRequest::getId)
          .collect(Collectors.toList());
  }
}
