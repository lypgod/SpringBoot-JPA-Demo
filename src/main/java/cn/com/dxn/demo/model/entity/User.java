package cn.com.dxn.demo.model.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
	private Integer id;

    @Column(nullable=false, unique=true)
    @NotBlank(message = "用户名不能为空")
    @Size(max=5, message = "用户名长度不能大于5")
	private String userName;

    @NotBlank(message = "密码不能为空")
	private String password;

    private String memo;
}
