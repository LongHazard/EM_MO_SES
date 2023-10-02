package com.ncsgroup.ems.service.identity;

import com.ncsgroup.ems.dto.request.image.ImageRequest;
import com.ncsgroup.ems.dto.response.image.ImageResponse;
import com.ncsgroup.ems.entity.identity.Image;
import com.ncsgroup.ems.service.base.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService extends BaseService<Image> {

  List<ImageResponse> upload(List<MultipartFile> fileRequests);

  void addImageToObject(
        List<Long> imageIds,
        Long vehicleId,
        Long personId,
        Long vehicleCardId,
        Long personCardId,
        String type
  );

  List<ImageResponse> getByRequests(List<ImageRequest> images);

  List<ImageResponse> getAllByVehicle(Long vehicleId);

  List<ImageResponse> getAllByPerson(Long personId);

  List<ImageResponse> getAllByVehicleCard(Long vehicleCardId);

  List<ImageResponse> getAllByPersonCard(Long personCardId);

  ImageResponse getByVehicleId(Long vehicleId);

  ImageResponse getByPersonId(Long personId);

  ImageResponse getByVehicleCardId(Long vehicleCardId);

  ImageResponse getByPersonCardId(Long personCardId);

  List<Long> getIdByImageRequest(List<ImageRequest> imageRequests);

  List<String> getTypeByImageRequest(List<ImageRequest> imageRequests);

  void addImageToVehicleCard(List<ImageRequest> imageRequests, Long vehicleCardId);

  void addImageToVehicle(
        List<ImageRequest> imageRequests,
        Long vehicleId
  );

  void addImageToPerson(List<ImageRequest> imageRequests, Long personId);

  void addImageToPersonCard(List<ImageRequest> imageRequests, Long personCardId);

  void softDelete(Long id);

  void softDelete(List<Long> ids);

  void update(List<Long> existedImagesIds, List<ImageRequest> updateImageIds);

  List<Long> getIdByVehicle(Long vehicleId);

  List<Long> getIdByPerson(Long personId);

  List<Long> getIdByVehicleCard(Long vehicleCardId);

  List<Long> getIdByPersonCard(Long personCardId);
}
