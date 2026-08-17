package com.foi.nloncar.thesis_manager.rest.submission;

import com.foi.nloncar.thesis_manager.config.StorageProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class FileStorageService {

	private final StorageProperties storageProperties;

	public FileStorageService(StorageProperties storageProperties) {
		this.storageProperties = storageProperties;
	}

	public String store(MultipartFile file, String subfolder, String fileName) {
		try {
			Path uploadDir = Path.of(storageProperties.getRoot(), subfolder);
			Files.createDirectories(uploadDir);

			String storedFileName = UUID.randomUUID() + "-" + fileName;
			Path targetPath = uploadDir.resolve(storedFileName);
			file.transferTo(targetPath);

			return targetPath.toString();
		} catch (IOException e) {
			throw new UncheckedIOException("Failed to store file", e);
		}
	}
}
