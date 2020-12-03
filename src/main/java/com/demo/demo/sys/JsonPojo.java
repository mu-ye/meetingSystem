package com.demo.demo.sys;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @author 牟欢
 * @Classname JsonDemo
 * @Description TODO
 * @Date 2020-06-01 11:08
 */
@Data
public class JsonPojo {
    private String userName;

    /**
     *  SerializedName 注解 json与 Bean 进行匹配； 若json 既存在  emailAddress 又有 email_address，则按最后一个 email_address进行匹配
     */
    @SerializedName(value = "email",alternate = {"emailAddress", "email_address"})
    private String email;
}
