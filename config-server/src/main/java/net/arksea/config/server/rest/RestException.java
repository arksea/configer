package net.arksea.config.server.rest;

import org.springframework.http.HttpStatus;

public class RestException extends RuntimeException {
	private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
	private String label = "";
	private String detail = "";
	public RestException(final String message) {
		super(message);
	}
	public RestException(final HttpStatus status, final String message) {
		super(message);
		this.status = status;
	}
	public RestException(final String message, final Throwable ex) {
		super(message, ex);
	}
	public RestException(final String message, final String label, final Throwable ex) {
		super(message, ex);
		this.label = label;
	}
	public RestException(final HttpStatus status, final String message, final Throwable ex) {
		super(message, ex);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
}
