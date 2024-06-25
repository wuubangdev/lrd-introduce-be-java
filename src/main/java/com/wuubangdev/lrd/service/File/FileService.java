package com.wuubangdev.lrd.service.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class FileService {
	@Value("${upload-file.base-uri}")
	private String baseURI;

	public Path initFolder(String folderName) throws URISyntaxException {
		URI uri = new URI(baseURI + "/" + folderName);
		Path path = Paths.get(uri);
		if (!path.toFile().isDirectory()) {
			try {
				Files.createDirectory(path);
			} catch (IOException e) {
				throw new RuntimeException("Không thể tạo thư mục để lưu tập tin!");
			}
		} else {
			System.out.println("Đã tồn tại thư mục, bỏ qua bước khởi tạo.");
		}
		return path;
	}

	public String save(MultipartFile file, String folder) throws URISyntaxException {
		String finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
		Path path = this.initFolder(folder);
		try {
			Files.copy(file.getInputStream(), path.resolve(Objects.requireNonNull(finalName)));
		} catch (Exception e) {
			throw new RuntimeException("Không thể lưu tập tin. Lỗi: " + e.getMessage());
		}
		return finalName;
	}

	public Resource load(String filename, String folder) throws URISyntaxException {
		Path path = this.initFolder(folder);
		try {
			Path file = path.resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Không thể đọc tập tin.");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

	public long getFileLength(String fileName, String folder) throws URISyntaxException {
		URI uri = new URI(baseURI + folder + "/" + fileName);
		Path path = Paths.get(uri);

		File tmpDir = new File(path.toString());

		// file không tồn tại, hoặc file là 1 director => return 0
		if (!tmpDir.exists() || tmpDir.isDirectory())
			return 0;
		return tmpDir.length();
	}

	public void deleteAll(String folder) throws URISyntaxException {
		Path path = this.initFolder(folder);
		FileSystemUtils.deleteRecursively(path.toFile());
	}


	public Stream<Path> loadAll(String folder) throws URISyntaxException {
		Path path = this.initFolder(folder);
		try {
			return Files.walk(path, 1).filter(p -> !p.equals(path)).map(path::relativize);
		} catch (IOException e) {
			throw new RuntimeException("Không thể lấy danh sách tập tin!");
		}
	}
}
