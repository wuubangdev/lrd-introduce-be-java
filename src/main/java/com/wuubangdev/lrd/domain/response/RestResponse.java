package com.wuubangdev.lrd.domain.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RestResponse<T> {
	private int statusCode;
	private String error;
	private Object message;// message co the la string hoac arrayList
	private T data;
}
