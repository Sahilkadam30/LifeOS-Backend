package com.life.service.traveltrack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

	private final String UPLOAD_DIR = "uploads/";

	public String uploadFile(MultipartFile file) throws IOException {

	    String uploadDir = "uploads/";

	    // Create folder if not exists
	    File dir = new File(uploadDir);
	    if (!dir.exists()) {
	        dir.mkdirs();
	    }

	    // Generate unique filename
	    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

	    Path filePath = Paths.get(uploadDir + fileName);

	    Files.write(filePath, file.getBytes());

	    // 🔥 IMPORTANT: Return FULL URL
	    return "http://localhost:4550/uploads/" + fileName;
	}
}
