package com.wuubangdev.lrd.domain.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ResultPaginateDTO<T> {
	private int page;
	private int size;
	private int totalPage;
	private long totalElement;
	private List<T> result;

	public ResultPaginateDTO(Page<T> page) {
		this.page = page.getNumber() + 1;
		this.size = page.getSize();
		this.totalPage = page.getTotalPages();
		this.totalElement = page.getTotalElements();
		this.result = (List<T>) page.getContent();
	}
}
