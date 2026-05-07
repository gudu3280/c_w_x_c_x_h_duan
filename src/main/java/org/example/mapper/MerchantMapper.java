package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.entity.Merchant;

import java.util.List;

@Mapper
public interface MerchantMapper extends BaseMapper<Merchant> {

    /**
     * 按距离查询附近商家
     */
    @Select("SELECT *, " +
            "(6371 * ACOS(COS(RADIANS(#{lat})) * COS(RADIANS(lat)) * COS(RADIANS(lng) - RADIANS(#{lng})) + SIN(RADIANS(#{lat})) * SIN(RADIANS(lat)))) AS distance " +
            "FROM merchant WHERE deleted = 0 AND status = 1 " +
            "HAVING distance <= #{radius} " +
            "ORDER BY distance ASC " +
            "LIMIT #{limit}")
    List<Merchant> findNearby(@Param("lng") double lng, @Param("lat") double lat,
                              @Param("radius") double radius, @Param("limit") int limit);
}
