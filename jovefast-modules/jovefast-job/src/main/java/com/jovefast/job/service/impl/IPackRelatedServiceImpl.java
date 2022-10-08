package com.jovefast.job.service.impl;

import com.baomidou.dynamic.datasource.annotation.Slave;
import com.jovefast.common.core.qiwx.WeixinApiConfig;
import com.jovefast.job.domain.dto.ShipiInstructionDTO;
import com.jovefast.job.enums.RobotsTarget;
import com.jovefast.job.mapper.PackRelatedMapper;
import com.jovefast.job.service.IPackRelatedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slave
public class IPackRelatedServiceImpl implements IPackRelatedService {

    @Autowired
    private PackRelatedMapper packRelatedMapper;

    @Override
    public void sendDeliveryInstruction() {
        // 获取发货指令为【已创建】的记录
        List<ShipiInstructionDTO> instruction = packRelatedMapper.getShipiInstructionByStatusOnCreate();
        if (instruction == null || instruction.size() < 1){
            return;
        }
        List<ShipiInstructionDTO> instructionVOS = instruction.stream().filter(item -> {
            return "1".equals(item.getFactoryName());
        }).collect(Collectors.toList());



        StringBuffer sb = new StringBuffer();

        String user = "DengHao|WeiSanTing|LiuMingMing|XiaoJian-p1-BaoZhuang|OuMingHan|WuXingFu-p1-KaiLiao|TanJianMin|HuangGenPing";

        for (ShipiInstructionDTO item:instructionVOS) {
            sb.append("已创建但未处理的发货指令\n")
                    .append("\n指派编码：" + item.getShipNum() + "\n")
                    .append("客户代码：" + item.getCustCode() + "\n")
                    .append("出货日期：" + item.getShipDate() + "\n")
                    .append("指令发送日期：" + item.getLaunchDate() + "\n");
            sendMessageWeChat(sb.toString(),user);
            sb.setLength(0);
        }

    }

    /**
     * 消息推送App
     * @param text 文本信息
     * @param user 推送人
     */
    private void sendMessageWeChat(String text,String user){
        String message = "{\n" +
                "   \"touser\" : \""+user+"\",\n" +
                "   \"msgtype\" : \"text\",\n" +
                "   \"agentid\" : 1000079,\n" +
                "   \"text\" : {\n" +
                "       \"content\" : \""+ text +" \"\n" +
                "   },\n" +
                "}";
        WeixinApiConfig.rotSendInfo(RobotsTarget.MESSAGE_APP_KEY.getValue(), message, 1);
    }
}
