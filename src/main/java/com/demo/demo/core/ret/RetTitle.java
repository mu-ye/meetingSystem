package com.demo.demo.core.ret;

import lombok.Data;

/**
 * @author 牟欢
 * @Classname RetTitle
 * @Description TODO
 * @Date 2020-06-17 9:51
 */
@Data
public class RetTitle {
    /**
     * 正常的 title
     */

    private Integer id;

    private Integer alive;

    private String titleName;

    private String titleType;

    private String dataType;

    private String data;

    private String prompt;

    private Integer autoIndex;

    private Integer width;

}
