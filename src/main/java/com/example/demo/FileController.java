package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController {
	@GetMapping("/{fileName}")
	public ResponseEntity<ByteArrayResource> getCsvFile(@PathVariable(value = "fileName") String fileName)
			throws IOException {
		File file = new File("C:\\Users\\Админ\\eclipse-workspace\\FinAnalys\\" + fileName);
		byte[] arr = Files.readAllBytes(file.toPath());
		ByteArrayResource resource = new ByteArrayResource(arr);
//		MediaType mediaType = new MediaType();
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
//              .contentType(mediaType) //
//              // Content-Lengh
//              .contentLength(data.length) //
				.body(resource);
	}
}
