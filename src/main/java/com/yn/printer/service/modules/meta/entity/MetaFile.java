package com.yn.printer.service.modules.meta.entity;

import com.yn.printer.service.common.entity.AuditableModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * This object stores the files.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "META_FILE", indexes = {@Index(columnList = "fileName")})
public class MetaFile extends AuditableModel implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "META_FILE_SEQ")
	@SequenceGenerator(name = "META_FILE_SEQ", sequenceName = "META_FILE_SEQ", allocationSize = 1)
	private Long id;

	@NotNull
	private String fileName;

	@NotNull
	private String filePath;

	private String outerChainPath;

	private Long fileSize = 0L;

	private String fileType;

	private String description;

}
