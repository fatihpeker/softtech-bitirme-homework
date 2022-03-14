package tr.softtech.patika.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.softtech.patika.dto.AddNewCategoryRequestDto;
import tr.softtech.patika.dto.GenericResponseDto;
import tr.softtech.patika.service.CategoryKdvService;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/category")
@RequiredArgsConstructor
public class CategoryKdvController {

    private final CategoryKdvService categoryKdvService;

    @Operation(tags = "Add New Category"
    ,description = "You can add new category and kdv rate for your product"
    ,summary = "if you want to add an undefined, different and new , category, please request to developer team for adding a new category type in enum")
    @PostMapping
    public ResponseEntity addNewCategory(@Valid @RequestBody AddNewCategoryRequestDto addNewCategoryRequestDto){
        return new ResponseEntity<>(GenericResponseDto.of(categoryKdvService.addNewCategory(addNewCategoryRequestDto),"Category added"), HttpStatus.CREATED);
    }

}
