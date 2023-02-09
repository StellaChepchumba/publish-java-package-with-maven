package com.imalipay.messaging.library.dtos;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.URL;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentDTO 
{
	@URL(message = "Please enter valid reachable url to file")
	@NotBlank(message = "Please enter file name")
	private String url;
	@NotBlank(message = "Please enter file name")
	private String name;
}
