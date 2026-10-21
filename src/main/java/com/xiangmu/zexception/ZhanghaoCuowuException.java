package com.xiangmu.zexception;

public class ZhanghaoCuowuException extends ServiceException
{
	public ZhanghaoCuowuException() {
        super();
    }

    public ZhanghaoCuowuException(String message) {
        super(message);
    }

    public ZhanghaoCuowuException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZhanghaoCuowuException(Throwable cause) {
        super(cause);
    }

    protected ZhanghaoCuowuException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
