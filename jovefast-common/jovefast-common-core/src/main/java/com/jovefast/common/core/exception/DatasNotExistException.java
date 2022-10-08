package com.jovefast.common.core.exception;

/**
 * 数据不存在异常
 */
public class DatasNotExistException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DatasNotExistException(String msg){
        super(msg);
    }

    public DatasNotExistException() {
        super();
    }
}
