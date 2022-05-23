package com.yac.service.module.oss.controller;

import cn.hutool.core.util.ObjectUtil;
import com.amazonaws.services.s3.model.Bucket;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yac.core.common.api.CommonResult;
import com.yac.core.common.api.ResultCode;
import com.yac.core.common.constant.OssConstant;
import com.yac.core.common.constant.YacConstant;
import com.yac.core.oss.core.OssTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;

/**
 * @author xiaoya
 * @qq 278729535
 * @link https://github.com/an0701/ya-java
 * @date : 2022/5/13 16:46
 * @since 0.1.0
 */
@Slf4j
@Api(value = "OssController", tags = "Oss服务")
@RequestMapping("/api/v2/oss")
@RestController
public class OssController {
    @Resource
    private OssTemplate ossTemplate;

    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "获取全部bucket")
    @GetMapping("/bucketList")
    public CommonResult<List<Bucket>> getAllBuckets() {
        List<Bucket> allBuckets = ossTemplate.getAllBuckets();
        return CommonResult.success(allBuckets);
    }

    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "创建bucket")
    @ApiImplicitParam(name = "bucketName", value = "bucket名称", required = true)
    @PostMapping("/bucket")
    public CommonResult<String> createBucket(@RequestParam(value = "bucketName") String bucketName) {
        ossTemplate.createBucket(bucketName);
        return CommonResult.success();
    }

    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "查询bucket信息")
    @ApiImplicitParam(name = "bucketName", value = "bucket名称", required = true)
    @GetMapping("/bucket")
    public CommonResult<Optional<Bucket>> getBucket(@RequestParam(value = "bucketName") String bucketName) {
        Optional<Bucket> bucket = ossTemplate.getBucket(bucketName);
        return CommonResult.success(bucket);
    }

    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "下载")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bucketName", value = "bucket名称", required = true),
            @ApiImplicitParam(name = "objectName", value = "文件名称", required = true)
    })
    @GetMapping("/download")
    public CommonResult<String> download(HttpServletRequest request, HttpServletResponse response,
                                         @RequestParam(value = "bucketName") String bucketName,
                                         @RequestParam(value = "objectName") String objectName) {
        try {
            InputStream inputStream = ossTemplate.getObject(bucketName, objectName);
            if (ObjectUtil.isNull(inputStream)) {
                return CommonResult.failed(ResultCode.FILE_DOES_NOT_EXIST);
            }
//            ServletOutputStream outputStream = null;
            // 2. 告诉浏览器下载的方式以及一些设置
            // 解决文件名乱码问题，获取浏览器类型，转换对应文件名编码格式，IE要求文件名必须是utf-8, firefo要求是iso-8859-1编码
            String agent = request.getHeader(YacConstant.BROWSER_USER_AGENT);
            if (agent.contains(YacConstant.BROWSER_FIREFOX)) {
                objectName = new String(objectName.getBytes(YacConstant.CHARSET_UTF_8), YacConstant.CHARSET_ISO_8859_1);
            } else {
                objectName = URLEncoder.encode(objectName, YacConstant.CHARSET_UTF_8);
            }
            // 设置下载文件的mineType，告诉浏览器下载文件类型
            String mineType = request.getServletContext().getMimeType(objectName);
            response.setContentType(mineType);
            // 设置一个响应头，无论是否被浏览器解析，都下载
            response.setHeader("Content-disposition", "attachment; filename=" + objectName);

            ServletOutputStream outputStream = response.getOutputStream();
            //读取文件流
            int len = 0;
            byte[] buffer = new byte[1024 * 10];
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(ResultCode.FILE_DOWNLOAD_ABNORMAL);
        }
        return null;


    }

    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "获取文件外部链接,7天内有效")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bucketName", value = "bucket名称", required = true),
            @ApiImplicitParam(name = "objectName", value = "文件名称", required = true)
    })
    @GetMapping("/url")
    public CommonResult<String> getObjectURL(@RequestParam(value = "bucketName") String bucketName,
                                             @RequestParam(value = "objectName") String objectName) {
        String objectURL = ossTemplate.getObjectURL(bucketName, objectName, OssConstant.URL_EXPIRES_DAYS);
        return CommonResult.success(objectURL);
    }

    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "文件上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bucketName", value = "bucket名称", required = true),
            @ApiImplicitParam(name = "objectName", value = "文件名称", required = true)
    })
    @PostMapping("/upload")
    public CommonResult<String> putObject(MultipartFile[] files,
                                          @RequestParam(value = "bucketName") String bucketName,
                                          @RequestParam(value = "objectName") String objectName) {
        try {
            for (MultipartFile file : files) {
                String oldName = file.getOriginalFilename();
                String fileType = oldName.substring(oldName.lastIndexOf("."));
                /**
                 * the size of the file in bytes
                 */
                long fileSize = file.getSize();
                String fileId = cn.hutool.core.lang.UUID.randomUUID().toString();
                String index = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                String uniqueName = fileId + index;
                //TODO 保存文件信息到数据库

                ossTemplate.putObject(bucketName, objectName, file.getInputStream());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return CommonResult.failed(ResultCode.FILE_UPLOAD_EXCEPTION);
        }


        return CommonResult.success();
    }

    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "创建bucket")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bucketName", value = "bucket名称", required = true),
            @ApiImplicitParam(name = "objectName", value = "文件名称", required = true)
    })
    @DeleteMapping("/")
    public CommonResult<String> removeObject(@RequestParam(value = "bucketName") String bucketName,
                                             @RequestParam(value = "objectName") String objectName) {
        try {
            //TODO 删除数据库信息
            ossTemplate.removeObject(bucketName, objectName);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return CommonResult.failed(ResultCode.FILE_UPLOAD_EXCEPTION);
    }
}
