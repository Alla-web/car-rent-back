package de.aittr.car_rent.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {



    // POST -> localhost:8080/files
    @PostMapping
    public void attachImageToCar(Long id, String imageUrl) {
        //fileService.attachImageToCar(id, imageUrl);
    }
}
