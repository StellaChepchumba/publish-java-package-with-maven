package com.imalipay.messaging.core.models;

import java.time.ZonedDateTime;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.imalipay.messaging.library.dtos.DeliveryMode;
import com.vladmihalcea.hibernate.type.json.JsonType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "routes")
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonType.class)
})
public class Routes 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer routeId;
	@ManyToOne
	@JoinColumn(name = "vendor_code")
	private Vendors vendor;
	private String countryCode;
	@Enumerated(EnumType.STRING)
	private DeliveryMode deliveryMode; 

	@Type(type = "json")
	@Convert(disableConversion = true)
	@Column(columnDefinition = "jsonb")
	private Map<String, String> extraData;
	
	private ZonedDateTime createdAt;
}
