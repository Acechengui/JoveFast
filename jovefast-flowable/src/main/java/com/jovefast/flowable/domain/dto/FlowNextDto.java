package com.jovefast.flowable.domain.dto;

import com.jovefast.system.api.domain.SysRole;
import com.jovefast.system.api.domain.SysUser;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 动态人员、组
 * @author Acechengui
 */
@Data
public class FlowNextDto implements Serializable {

    private static final long serialVersionUID = 4355558541906691041L;
    private String type;

    private String vars;

    private String dataType;

    private List<SysUser> userList;

    private List<SysRole> roleList;
}
