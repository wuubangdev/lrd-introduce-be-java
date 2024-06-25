package com.wuubangdev.lrd.controller;

import com.wuubangdev.lrd.domain.FileInfo;
import com.wuubangdev.lrd.service.File.FileService;
import com.wuubangdev.lrd.util.ExceptionUtil.FileInvalidException;
import com.wuubangdev.lrd.util.anotation.ApiMessage;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FileController {

	private final FileService fileService;

	public FileController(FileService fileService) {
		this.fileService = fileService;
	}

	@PostMapping("/files")
	@ApiMessage("Tải file lên thành công.")
	public ResponseEntity<FileInfo> upload(@RequestParam(name = "files", required = false) MultipartFile[] files,
	                                       @RequestParam(name = "folder") String folder)
			throws FileInvalidException {
		if (files == null || files.length == 0) {
			throw new FileInvalidException("File tải lên trống.");
		}
		List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
		List<String> fileNames = new ArrayList<>();
		Arrays.stream(files).forEach(file -> {

			String fileName = file.getOriginalFilename();
			// Validate extension
			boolean isValidExtension = allowedExtensions.stream()
					.anyMatch(ext -> {
						assert fileName != null;
						return fileName.toLowerCase().endsWith("." + ext);
					});
			if (!isValidExtension) {
				try {
					throw new FileInvalidException("Định dạng file không đúng.");
				} catch (FileInvalidException e) {
					throw new RuntimeException(e);
				}
			}

			try {
				String finalName = this.fileService.save(file, folder);
				fileNames.add(finalName);
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}
		});
		return ResponseEntity.ok(new FileInfo(folder, fileNames));
	}

	@GetMapping("/files")
	public ResponseEntity<Resource> downloadFile(
			@RequestParam(name = "fileName", required = false) String fileName,
			@RequestParam(name = "folder", required = false) String folder)
			throws FileInvalidException, URISyntaxException, FileNotFoundException {

		if (fileName == null || folder == null)
			throw new FileInvalidException("Tập tin hoặc thư mục không được trống!");
		long fileLength = this.fileService.getFileLength(fileName, folder);
		if (fileLength == 0) {
			throw new FileInvalidException("Tập tin có tên = " + fileName + " khôn tồn tại.");
		}
		// download a file
		Resource resource = this.fileService.load(fileName, folder);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
				.contentLength(fileLength)
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(resource);
	}


}
