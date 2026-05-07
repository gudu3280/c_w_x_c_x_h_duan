package org.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.example.common.BusinessException;
import org.example.entity.Pet;
import org.example.mapper.PetMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 宠物档案服务
 */
@Service
@RequiredArgsConstructor
public class PetService {

    private final PetMapper petMapper;

    public List<Pet> getList(Long userId) {
        return petMapper.selectList(
                new LambdaQueryWrapper<Pet>()
                        .eq(Pet::getUserId, userId)
                        .orderByDesc(Pet::getCreateTime));
    }

    public Pet getById(Long id, Long userId) {
        Pet pet = petMapper.selectById(id);
        if (pet == null || !pet.getUserId().equals(userId)) {
            throw new BusinessException("宠物档案不存在");
        }
        return pet;
    }

    public void add(Long userId, Pet pet) {
        pet.setUserId(userId);
        petMapper.insert(pet);
    }

    public void update(Long userId, Pet pet) {
        Pet existing = getById(pet.getId(), userId);
        if (existing == null) {
            throw new BusinessException("宠物档案不存在");
        }
        pet.setUserId(userId);
        petMapper.updateById(pet);
    }

    public void delete(Long id, Long userId) {
        getById(id, userId);
        petMapper.deleteById(id);
    }
}
