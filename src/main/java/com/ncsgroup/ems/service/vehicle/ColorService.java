package com.ncsgroup.ems.service.vehicle;

import com.ncsgroup.ems.dto.request.color.ColorRequest;
import com.ncsgroup.ems.dto.response.color.ColorResponse;
import com.ncsgroup.ems.entity.vehicle.Color;
import com.ncsgroup.ems.service.base.BaseService;

import java.util.List;

public interface ColorService extends BaseService<Color> {
  /**
   * create new color
   *
   * @param request - info of new color
   * @return info of new color after save in database
   */
  ColorResponse create(ColorRequest request);

  /**
   * get color
   *
   * @param id - id of color
   * @return info of color
   */
  ColorResponse retrieve(Long id);

  /**
   * List color
   *
   * @param keyword - keyword to search
   * @return list color
   */
  List<ColorResponse> list(String keyword);

  /**
   * remove color
   *
   * @param id - id of color which need to remove
   */
  void remove(Long id);

  Color find(Long id);

  List<String> findName(Long idVehicle);

  List<String> findNameById(List<Long> idColor);

  List<Color> findByIds(List<Long> colorIds);

  void checkNotFound(List<Long> colorIds);

  List<String> findByVehicleId(Long vehicleId);
}
