package com.yn.printer.service.modules.operation.entity;

import com.yn.printer.service.common.entity.AuditableModel;
import com.yn.printer.service.modules.member.entity.Member;
import com.yn.printer.service.modules.meta.enums.PaperType;
import com.yn.printer.service.modules.operation.enums.ConsumableType;
import com.yn.printer.service.modules.operation.enums.OperationsType;
import com.yn.printer.service.modules.operation.enums.ProcessingStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(name = "OPERATION_TASK_LIST", indexes = { @Index(columnList = "code"), @Index(columnList = "personnel") })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class TaskList extends AuditableModel implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OPERATION_TASK_LIST_SEQ")
	@SequenceGenerator(
			name = "OPERATION_TASK_LIST_SEQ",
			sequenceName = "OPERATION_TASK_LIST_SEQ",
			allocationSize = 1)
	private Long id;


	@ManyToOne(
			fetch = FetchType.LAZY,
			cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "code")
	private DevicesList code;


	private String storeName;

	@Enumerated(value = EnumType.STRING)
	private OperationsType taskType;

	@Enumerated(value = EnumType.STRING)
	private ProcessingStatus taskStatus = ProcessingStatus.PS0;


	private String address;


	private Integer additionsNumber = 0;


	@ManyToOne(
			fetch = FetchType.LAZY,
			cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "personnel")
	private Member personnel;


	private String phoneNumber;


	private LocalDateTime generateTime;


	private LocalDateTime completionTime;


	private Boolean readed = Boolean.FALSE;


	private String message;

	@Enumerated(value = EnumType.STRING)
	private PaperType paperType;

	@Enumerated(value = EnumType.STRING)
	private ConsumableType consumable;


	private String attrs;

	private String information;
}
