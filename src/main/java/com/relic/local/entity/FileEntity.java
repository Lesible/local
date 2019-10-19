package com.relic.local.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author Relic
 * @desc 文件实体类
 * @date 2019-10-19 15:32
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class FileEntity {

    private String fileName;
    private String url;

}
