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
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MeetingMenu implements Serializable {

    @TableId(value="id",type= IdType.AUTO)
    private Integer id;

    private String text;

    private Integer parentId;

    private Integer meetingId;

    private String checkList;

    private String fileUpJobNumbers;

    private String attendJobNumbers;

    private Integer formId;

    private Integer alive;

    private String depIds;

    /**
     *   注意： 如果有有参构造函数，在使用mybatisPlus查询结果用本类接收时，必须要有默认构造函数
     */

    /**
     * 默认构造函数
     *
     */
    public MeetingMenu(){ }

    /**
     * 有参构造函数
     *
     * @param templateMenu 模板目录
     * @param meetingId 会议Id
     */
    public MeetingMenu(TemplateMenu templateMenu,Integer meetingId){
        this.text = templateMenu.getText();
        this.parentId = templateMenu.getParentId();
        this.meetingId = meetingId;
        this.checkList = templateMenu.getCheckList();
        this.fileUpJobNumbers = templateMenu.getFileUpJobNumbers();
        this.attendJobNumbers = templateMenu.getAttendJobNumbers();
        this.formId = templateMenu.getFormId();
        this. alive = templateMenu.getAlive();
        this.depIds = templateMenu.getDepIds();
    }


}
