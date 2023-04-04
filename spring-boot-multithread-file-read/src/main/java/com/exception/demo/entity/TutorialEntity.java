package com.exception.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="TBL_TUTORIAL")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class TutorialEntity {
	@Id
	@GeneratedValue
	private Integer id;
	private String title;
	private String description;
	private boolean published;
}
