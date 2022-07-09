package id.co.promise.procurement.catalog.entity;

/**
 * @author F.H.K
 *
 */

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

@Entity
@Table(name = "T2_ATTRIBUTE_OPTION")

@TableGenerator(name = "tableSequence", table = "SEQUENCE",pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE",pkColumnValue = "T2_ATTRIBUTE_OPTION", initialValue = 1, allocationSize = 1)
 
public class AttributeOption implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ATTRIBUTE_OPTION_ID")
	private Integer id;
	
	@Column(name = "NAME")
	private String name;
	
	@Transient
	private String value;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
