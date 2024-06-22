package com.wuubangdev.lrd.util.ExceptionUtil;

import com.wuubangdev.lrd.domain.response.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalException {
	@ExceptionHandler(value = {
			UsernameNotFoundException.class,
			BadCredentialsException.class,
			CheckExistException.class
	})
	public ResponseEntity<RestResponse<Object>> IdException(Exception ex) {
		RestResponse<Object> res = new RestResponse<Object>();
		res.setStatusCode(HttpStatus.BAD_REQUEST.value());
		res.setError("Exception occurs");
		res.setMessage(ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<RestResponse<Object>> validationError(MethodArgumentNotValidException ex) {
		BindingResult bindingResult = ex.getBindingResult();
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();

		RestResponse<Object> res = new RestResponse<>();
		res.setStatusCode(HttpStatus.BAD_REQUEST.value());
		res.setError("Bad Credentials");

		List<String> errors = new ArrayList<>();

		for (FieldError fieldError : fieldErrors) {
			errors.add(fieldError.getDefaultMessage());
		}
		res.setMessage(errors.size() > 1 ? errors : errors.get(0));

		return ResponseEntity.badRequest().body(res);
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<RestResponse<Object>> noResourceFoundException(Exception ex) {

		RestResponse<Object> res = new RestResponse<Object>();
		res.setStatusCode(HttpStatus.NOT_FOUND.value());
		res.setError("Exception occurs.");
		res.setMessage("No resource found exception!!!");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
	}

	// @ExceptionHandler(value = {
	// FileInvalidException.class
	// })
	// public ResponseEntity<RestResponse<Object>> fileException(Exception ex) {
	// RestResponse<Object> res = new RestResponse<Object>();
	// res.setStatusCode(HttpStatus.BAD_REQUEST.value());
	// res.setError(ex.getMessage());
	// res.setMessage("Exception upload file...");
	// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
	// }
	//
	// @ExceptionHandler(value = {
	// PermissionException.class
	// })
	// public ResponseEntity<RestResponse<Object>> permissionException(Exception ex)
	// {
	// RestResponse<Object> res = new RestResponse<Object>();
	// res.setStatusCode(HttpStatus.FORBIDDEN.value());
	// res.setError("Forbidden");
	// res.setMessage(ex.getMessage());
	// return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
	// }

	@ExceptionHandler(Exception.class)
	public ResponseEntity<RestResponse<Object>> handleAllException(Exception ex) {
		RestResponse<Object> res = new RestResponse<Object>();
		res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		res.setMessage(ex.getMessage());
		res.setError("Internal Server Error");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
	}
}
