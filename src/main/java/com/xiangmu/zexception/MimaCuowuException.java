package com.xiangmu.zexception;

public class MimaCuowuException extends ServiceException
{
	public MimaCuowuException() {
        super();
    }

    public MimaCuowuException(String message) {
        super(message);
    }

    public MimaCuowuException(String message, Throwable cause) {
        super(message, cause);
    }

    public MimaCuowuException(Throwable cause) {
        super(cause);
    }

    protected MimaCuowuException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
