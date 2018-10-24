package cn.com.dxn.demo.model.entity;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * @author richard
 */
@Entity
@Data
public class Role {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
	private Integer id;

    @Column(nullable=false, unique=true)
    @NotBlank(message = "角色名称不能为空")
    @NonNull
	private String name;
}
