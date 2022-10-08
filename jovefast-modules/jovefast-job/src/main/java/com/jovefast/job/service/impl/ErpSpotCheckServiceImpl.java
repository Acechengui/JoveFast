package com.jovefast.job.service.impl;

import com.jovefast.common.core.utils.StringUtils;
import com.jovefast.common.datasource.annotation.Slave;
import com.jovefast.job.domain.Data9995;
import com.jovefast.job.mapper.SpotCheckMapper;
import com.jovefast.job.service.IErpSpotCheckService;
import com.jovefast.system.api.RemoteFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Acechengui
 */
@Service
@Slave
public class ErpSpotCheckServiceImpl implements IErpSpotCheckService {

    @Autowired
    private SpotCheckMapper spotCheckMapper;

    @Autowired
    private RemoteFileService remoteFileService;

    /**
     * 删除过期的图片以及数据
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delExpireData() {
        List<Data9995> list = spotCheckMapper.findExpireData9995();
        list.forEach(l->{
            String str1=l.getImagesAddress();
            if(StringUtils.isNotNull(str1)){
                remoteFileService.delByPath(str1);
            }
            String str2=l.getImagesAddress2();
            if(StringUtils.isNotNull(str2)){
                remoteFileService.delByPath(str2);
            }
            String str3=l.getImagesAddress3();
            if(StringUtils.isNotNull(str3)){
                remoteFileService.delByPath(str3);
            }
            String str4=l.getImagesAddress4();
            if(StringUtils.isNotNull(str4)){
                remoteFileService.delByPath(str4);
            }
            String str5=l.getImagesAddress5();
            if(StringUtils.isNotNull(str5)){
                remoteFileService.delByPath(str5);
            }
            String str6=l.getQrImagesAddress();
            if(StringUtils.isNotNull(str6)){
                remoteFileService.delByPath(str6);
            }
        });
        return spotCheckMapper.delExpireData9995() > 0 ;
    }
}
