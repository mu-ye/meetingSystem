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
 * @since 2020-06-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TemplateMenu implements Serializable {

    @TableId(value="id",type= IdType.AUTO)
    private Integer id;

    private String text;

    private Integer parentId;

    private Integer templateId;

    private String checkList;

    private String fileUpJobNumbers;

    private String attendJobNumbers;

    private Integer formId;

    private Integer alive;

    private String depIds;


}
