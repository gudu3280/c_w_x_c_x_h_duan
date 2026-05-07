package org.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.example.entity.*;
import org.example.mapper.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商家服务
 */
@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantMapper merchantMapper;
    private final BoardingSettingMapper boardingSettingMapper;
    private final GroomingServiceMapper groomingServiceMapper;
    private final ServiceCalendarMapper serviceCalendarMapper;

    /**
     * 按关键字搜索商家
     */
    public List<Merchant> searchMerchants(String keyword) {
        LambdaQueryWrapper<Merchant> wrapper = new LambdaQueryWrapper<Merchant>()
                .eq(Merchant::getStatus, 1)
                .and(w -> w.like(Merchant::getName, keyword)
                        .or()
                        .like(Merchant::getTags, keyword)
                        .or()
                        .like(Merchant::getDescription, keyword));
        return merchantMapper.selectList(wrapper);
    }

    /**
     * 查询附近商家
     */
    public List<Merchant> findNearby(double lng, double lat, String type, double radius) {
        List<Merchant> merchants = merchantMapper.findNearby(lng, lat, radius, 50);
        // 按类型筛选（如果指定了type）
        if (type != null && !type.isEmpty()) {
            // 根据服务类型筛选：boarding类型的商家查寄养设置表，grooming的查美容服务表
            if ("boarding".equals(type)) {
                List<Long> boardingMerchantIds = boardingSettingMapper.selectList(null)
                        .stream().map(BoardingSetting::getMerchantId).toList();
                merchants = merchants.stream()
                        .filter(m -> boardingMerchantIds.contains(m.getId()))
                        .toList();
            } else if ("grooming".equals(type)) {
                List<Long> groomingMerchantIds = groomingServiceMapper.selectList(null)
                        .stream().map(GroomingService::getMerchantId).distinct().toList();
                merchants = merchants.stream()
                        .filter(m -> groomingMerchantIds.contains(m.getId()))
                        .toList();
            }
        }
        return merchants;
    }

    /**
     * 获取商家详情
     */
    public Map<String, Object> getDetail(Long merchantId) {
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            return null;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("merchant", merchant);

        // 寄养设置
        BoardingSetting boardingSetting = boardingSettingMapper.selectOne(
                new LambdaQueryWrapper<BoardingSetting>()
                        .eq(BoardingSetting::getMerchantId, merchantId));
        result.put("boardingSetting", boardingSetting);

        // 美容服务列表
        List<GroomingService> groomingServices = groomingServiceMapper.selectList(
                new LambdaQueryWrapper<GroomingService>()
                        .eq(GroomingService::getMerchantId, merchantId));
        result.put("groomingServices", groomingServices);

        return result;
    }

    /**
     * 获取可预约日历
     */
    public List<ServiceCalendar> getCalendar(Long merchantId, String yearMonth) {
        LocalDate startDate;
        LocalDate endDate;
        if (yearMonth != null && !yearMonth.isEmpty()) {
            startDate = LocalDate.parse(yearMonth + "-01");
            endDate = startDate.plusMonths(1).minusDays(1);
        } else {
            startDate = LocalDate.now();
            endDate = startDate.plusDays(30);
        }

        return serviceCalendarMapper.selectList(
                new LambdaQueryWrapper<ServiceCalendar>()
                        .eq(ServiceCalendar::getMerchantId, merchantId)
                        .between(ServiceCalendar::getServiceDate, startDate, endDate)
                        .orderByAsc(ServiceCalendar::getServiceDate));
    }
}
