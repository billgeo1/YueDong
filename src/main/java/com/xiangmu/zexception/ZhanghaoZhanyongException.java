package com.xiangmu.zexception;

public class ZhanghaoZhanyongException extends ServiceException
{
	public ZhanghaoZhanyongException() {
        super();
    }

    public ZhanghaoZhanyongException(String message) {
        super(message);
    }

    public ZhanghaoZhanyongException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZhanghaoZhanyongException(Throwable cause) {
        super(cause);
    }

    protected ZhanghaoZhanyongException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
