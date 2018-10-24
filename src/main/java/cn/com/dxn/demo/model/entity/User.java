package cn.com.dxn.demo.model.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * @author richard
 */
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
	private Integer id;

    @Column(nullable=false, unique=true)
    @NotBlank(message = "用户名不能为空")
    @Size(max=5, message = "用户名长度不能大于5")
	private String username;

    @NotBlank(message = "密码不能为空")
	private String password;

    private String memo;

    private Date lastPasswordResetDate;

    @ManyToMany(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)
    private List<Role> roles;
}
