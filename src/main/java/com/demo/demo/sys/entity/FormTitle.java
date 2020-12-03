package com.demo.demo.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 牟欢
 * @since 2020-06-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FormTitle implements Serializable {

    @TableId(value="id",type= IdType.AUTO)
    private Integer id;

    private Integer formId;

    private Integer titleId;

    private Integer autoIndex;

    private Integer alive;

    private Integer width;


}
