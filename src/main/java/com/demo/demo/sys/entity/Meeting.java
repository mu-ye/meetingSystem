package com.demo.demo.sys.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author 牟欢
 * @since 2020-06-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Meeting implements Serializable {

    @TableId(value="id",type= IdType.AUTO)
    private Integer id;

    @DateTimeFormat(pattern = "yyyy-MM-dd 'T' hh:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd 'T' hh:mm:ss")
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd 'T' hh:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd 'T' hh:mm:ss")
    private LocalDateTime endData;

    private String address;

    private String name;

    private Integer state;

    private String attendPassword;

    private Integer type;

    private Integer alive;

    private Integer templateId;

    private String jobNumber;


}
