package com.demo.demo.sys.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 牟欢
 * @since 2020-05-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserRole implements Serializable {

    @TableId(value="id",type= IdType.AUTO)
    private Integer id;

    private String jobNumber;

    private Integer roleId;

    private Integer alive;


}
