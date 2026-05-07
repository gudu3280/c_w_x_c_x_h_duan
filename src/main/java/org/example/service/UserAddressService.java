package org.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.example.common.BusinessException;
import org.example.entity.UserAddress;
import org.example.mapper.UserAddressMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户地址服务
 */
@Service
@RequiredArgsConstructor
public class UserAddressService {

    private final UserAddressMapper userAddressMapper;

    public List<UserAddress> getList(Long userId) {
        return userAddressMapper.selectList(
                new LambdaQueryWrapper<UserAddress>()
                        .eq(UserAddress::getUserId, userId)
                        .orderByDesc(UserAddress::getIsDefault)
                        .orderByDesc(UserAddress::getUpdateTime));
    }

    public UserAddress getById(Long id, Long userId) {
        UserAddress address = userAddressMapper.selectById(id);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException("地址不存在");
        }
        return address;
    }

    public void add(Long userId, UserAddress address) {
        address.setUserId(userId);
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            clearDefault(userId);
        }
        userAddressMapper.insert(address);
    }

    public void update(Long userId, UserAddress address) {
        UserAddress existing = getById(address.getId(), userId);
        if (existing == null) {
            throw new BusinessException("地址不存在");
        }
        address.setUserId(userId);
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            clearDefault(userId);
        }
        userAddressMapper.updateById(address);
    }

    public void delete(Long id, Long userId) {
        UserAddress address = getById(id, userId);
        if (address == null) {
            throw new BusinessException("地址不存在");
        }
        userAddressMapper.deleteById(id);
    }

    private void clearDefault(Long userId) {
        UserAddress update = new UserAddress();
        update.setIsDefault(0);
        userAddressMapper.update(update,
                new LambdaQueryWrapper<UserAddress>()
                        .eq(UserAddress::getUserId, userId)
                        .eq(UserAddress::getIsDefault, 1));
    }
}
