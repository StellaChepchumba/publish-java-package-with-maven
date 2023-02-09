package com.imalipay.messaging.library.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailDTO {
	@NotBlank(message="Please enter email owner name ")
	private String name;
	@Email
	@NotBlank(message="Please enter email")
	private String email;
}
