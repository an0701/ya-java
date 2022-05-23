package com.yac.core.common.pojo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yac.core.common.api.ResultDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author xiaoya
 * @qq 278729535
 * @link https://github.com/an0701/ya-java
 * @date : 2022/5/17 14:06
 * @since 0.1.0
 */
@Data
@ApiModel(value = "Oss接口统一调用参数报文")
public class OssResultDTO extends ResultDTO {
    private static final long serialVersionUID = -3076427280459858593L;
//    @ApiModelProperty(value = "数据参数")
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private T data;

}
