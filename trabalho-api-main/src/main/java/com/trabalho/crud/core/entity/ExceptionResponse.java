package com.trabalho.crud.core.entity;

public class ExceptionResponse {

	private String message;

	private String status;

	private ExceptionResponse(Builder builder) {
		this.message = builder.message;
		this.status = builder.status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private String message;

		private String status;

		public Builder message(String message) {
			this.message = message;
			return this;
		}

		public Builder status(String status) {
			this.status = status;
			return this;
		}

		public ExceptionResponse build() {
			return new ExceptionResponse(this);
		}

	}

}
