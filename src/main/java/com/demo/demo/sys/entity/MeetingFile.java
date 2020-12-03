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
 * @since 2020-06-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MeetingFile implements Serializable {

    @TableId(value="id",type= IdType.AUTO)
    private Integer id;

    private Integer meetingMenuId;

    private String fileName;

    private String uploadUserName;

    private String uploadJobNumber;

    private String checkList;

    private String depIds;

    private String refuseReason;

    private Integer formId;

    private String formText;

    private Integer alive;

    private String remark;

    private String url;

    private Integer pass;

    private String nowNode;


}
