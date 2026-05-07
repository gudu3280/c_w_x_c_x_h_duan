package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.entity.Pet;
import org.example.service.PetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 宠物档案控制器
 */
@RestController
@RequestMapping("/api/pet")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @GetMapping("/list")
    public Result<List<Pet>> list(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(petService.getList(userId));
    }

    @GetMapping("/{id}")
    public Result<Pet> detail(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(petService.getById(id, userId));
    }

    @PostMapping("/add")
    public Result<?> add(HttpServletRequest request, @RequestBody Pet pet) {
        Long userId = (Long) request.getAttribute("userId");
        petService.add(userId, pet);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<?> update(HttpServletRequest request, @RequestBody Pet pet) {
        Long userId = (Long) request.getAttribute("userId");
        petService.update(userId, pet);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        petService.delete(id, userId);
        return Result.success();
    }
}
