package com.jovefast.job.service.impl;

import com.baomidou.dynamic.datasource.annotation.Slave;
import com.jovefast.common.core.qiwx.TokenUtilForWechat;
import com.jovefast.common.core.apiinvoker.messagetype.ApplicationMessage;
import com.jovefast.job.domain.LockMessage;
import com.jovefast.job.enums.RobotsTarget;
import com.jovefast.job.mapper.LockMessageMapper;
import com.jovefast.job.service.ILockMessageService;
import com.jovefast.job.util.ListUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分层分级过数锁定
 * @author Acechengui
 */
@Service
@Slave
public class ILockMessageServiceImpl implements ILockMessageService {

    private static final Logger log = LoggerFactory.getLogger(ILockMessageServiceImpl.class);
    @Autowired
    LockMessageMapper lockMessageMapper;

    @Override
    public void theLockTimeoutPush() {
        //推送对象集合
        List<Map<String, String>> pushObjList = new ArrayList<>();

        //群聊 ”电测过数自主预警“ 推送对象
        Map<String, String> pushObj2 = new HashMap<>(5);
        pushObj2.put("push_the_code","1");
        //"wrfdPQDAAAu0QSR6qbsaZwrDFNbPynEA"
        pushObj2.put("group_id",RobotsTarget.DC_ROBOT_KEY.getValue());
        pushObjList.add(pushObj2);

        //循环推送对象集合
        for(Map<String, String> push : pushObjList){

            //构建查询条件
            Map<String, Object> params = new HashMap<>(5);
            params.put("push_the_code",push.get("push_the_code"));

            //获取待推送的数据
            List<LockMessage> list = lockMessageMapper.getTheLockTimeoutDatas(params);
            if (!CollectionUtils.isEmpty(list)) {

                //获取密钥
                String accessToken = null;
                try {
                    accessToken = new TokenUtilForWechat().getToken(RobotsTarget.APP_IEW.getValue());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //拆分，防止推送时字符数超出被截断
                List<List<LockMessage>> listPages = ListUtil.splistList(list, 1);
                for (List<LockMessage> dbs : listPages) {

                    //将推送成功的id保存下来
                    List<Integer> rkeys = new ArrayList<>();

                    //构建推送文本
                    StringBuilder text = new StringBuilder();
                    for (int di = 0; di < dbs.size(); di++) {
                        text.append("<font color=\"warning\">              工卡超48小时未解锁警报</font>").append("\n");
                        text.append("LOT        :  ").append(dbs.get(di).getLot()).append("\n");
                        text.append("型号        :  ").append(dbs.get(di).getMfg()).append("\n");
                        text.append("工卡号     :  ").append(dbs.get(di).getWo()).append("\n");
                        text.append("锁定原因  :  ").append(dbs.get(di).getLockreason()).append("\n");
                        text.append("锁定时间  :  ").append(dbs.get(di).getLock_date()).append("\n");
                        text.append("触发原因  :  ").append(dbs.get(di).getJudge()).append("\n");
                        text.append("<font color=\"warning\">工卡已锁定</font> <font color=\"info\">").append(dbs.get(di).getTimeout()).append("</font> <font color=\"warning\">小时未解锁，请及时处理。</font>");
                        text.append(di == dbs.size() - 1 ? "" : "\n\n");
                        rkeys.add(dbs.get(di).getRkey());
                    }

                    //推送
                    Integer errorCode = ApplicationMessage.groupChatMarkdownMessageSend(accessToken, push.get("group_id"), text.toString());
                    if (errorCode.equals(0)) {

                        Map<String, Object> updateStatuMap = new HashMap<>();
                        updateStatuMap.put("rkeys", rkeys);

                        //更新数据标识，防止重复推送
                        lockMessageMapper.updateStatus(updateStatuMap);
                    } else {
                        log.error("错误代码 : " + errorCode);
                    }
                }
            }
        }
    }

    @Override
    public void hierarchicalClassificationLockPush() {
        //推送对象集合
        List<Map<String, String>> pushObjList = new ArrayList<>();

        //群聊 ”分层分级过数自主预警“ 推送对象
        Map<String, String> pushObj1 = new HashMap<>(10);
        //数据库表 push_the_code 列，用来标识需要推送到哪一个群
        pushObj1.put("push_the_code","0");
        // "wrfdPQDAAAbmPPwqeCMLYxbGdyICIyAQ"        //群聊ID
        pushObj1.put("group_id",RobotsTarget.FCFJ_ROBOT_KEY.getValue());
        pushObjList.add(pushObj1);

        //群聊 ”电测过数自主预警“ 推送对象
        Map<String, String> pushObj2 = new HashMap<>(5);
        pushObj2.put("push_the_code","1");
        // "wrfdPQDAAAu0QSR6qbsaZwrDFNbPynEA"
        pushObj2.put("group_id",RobotsTarget.DC_ROBOT_KEY.getValue());
        pushObjList.add(pushObj2);

        //循环推送对象集合
        for(Map<String, String> push : pushObjList) {

            //构建查询条件
            Map<String, Object> params = new HashMap<>(5);
            params.put("push_the_code",push.get("push_the_code"));

            //获取待推送的数据
            List<LockMessage> list = lockMessageMapper.getLockMessage(params);
            if (!CollectionUtils.isEmpty(list)) {

                //获取密钥
                String accessToken = null;
                try {
                    accessToken = new TokenUtilForWechat().getToken(RobotsTarget.APP_IEW.getValue());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //拆分，防止推送时字符数超出被截断
                List<List<LockMessage>> listPages = ListUtil.splistList(list, 1);
                for (List<LockMessage> dbs : listPages) {

                    //将推送成功的id保存下来
                    List<Integer> rkeys = new ArrayList<>();

                    //构建推送文本
                    StringBuilder text = new StringBuilder();
                    for (int di = 0; di < dbs.size(); di++) {
                        text.append("LOT        :  ").append(dbs.get(di).getLot()).append("\n");
                        text.append("型号        :  ").append(dbs.get(di).getMfg()).append("\n");
                        text.append("工卡号     :  ").append(dbs.get(di).getWo()).append("\n");
                        text.append("锁定原因  :  ").append(dbs.get(di).getLockreason()).append("\n");
                        text.append("锁定时间  :  ").append(dbs.get(di).getLock_date()).append("\n");
                        text.append("触发原因  :  ").append(dbs.get(di).getJudge());
                        text.append(di == dbs.size() - 1 ? "" : "\n\n");
                        rkeys.add(dbs.get(di).getRkey());
                    }

                    //推送
                    Integer errorCode = ApplicationMessage.groupChatMarkdownMessageSend(accessToken, push.get("group_id"), text.toString());
                    if (errorCode.equals(0)) {

                        Map<String, Object> updateStatuMap = new HashMap<>();
                        updateStatuMap.put("rkeys", rkeys);

                        //更新数据标识，防止重复推送
                        lockMessageMapper.updateStatus(updateStatuMap);
                    } else {
                        log.error("错误代码 : " + errorCode);
                    }
                }
            }
        }
    }
}
