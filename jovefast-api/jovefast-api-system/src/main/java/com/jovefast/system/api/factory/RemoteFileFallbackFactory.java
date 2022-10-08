package com.jovefast.system.api.factory;

import com.jovefast.system.api.domain.SysFileInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.jovefast.common.core.domain.R;
import com.jovefast.system.api.RemoteFileService;
import com.jovefast.system.api.domain.SysFile;

/**
 * 文件服务降级处理
 * 
 * @author Acechengui
 */
@Component
public class RemoteFileFallbackFactory implements FallbackFactory<RemoteFileService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteFileFallbackFactory.class);

    @Override
    public RemoteFileService create(Throwable throwable)
    {
        log.error("文件服务调用失败:{}", throwable.getMessage());
        return new RemoteFileService()
        {
            @Override
            public R<SysFile> upload(MultipartFile file)
            {
                return R.fail("上传文件失败:" + throwable.getMessage());
            }

            /**
             * 根据文件名删除文件
             *
             * @param filePath 文件信息
             * @return 结果
             */
            @Override
            public R<Boolean> delByPath(String filePath) {
                return R.fail("删除文件失败:" + throwable.getMessage());
            }

            /**
             * 保存系统文件 测试Feign服务调用
             *
             * @param sysFileInfo 系统文件
             * @return 结果
             */
            @Override
            public R<Boolean> saveFile(SysFileInfo sysFileInfo) {
                return R.fail("文件入库失败:" + throwable.getMessage());
            }

            @Override
            public byte[] downloadTemplate(String objectName) {
                return new byte[0];
            }
        };
    }
}
