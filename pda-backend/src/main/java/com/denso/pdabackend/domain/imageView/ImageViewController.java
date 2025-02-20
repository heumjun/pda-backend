package com.denso.pdabackend.domain.imageView;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@Slf4j
@RequestMapping("image-view")
public class ImageViewController {

    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("{path}/{fileName}")
    public ResponseEntity<Resource> dietViewImage(@PathVariable String path,@PathVariable String fileName) throws Exception {
        log.debug("{}",path);
        String filePath = fileDir.concat(path);
        
        Path file = Paths.get(filePath,fileName).toAbsolutePath().normalize();
        Resource resource = new UrlResource(file.toUri());

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // 확장자에 맞는 MIME 타입 지정
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
        
        
    }

    
}
