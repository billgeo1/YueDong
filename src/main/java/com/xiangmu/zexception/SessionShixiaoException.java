package com.xiangmu.zexception;

public class SessionShixiaoException extends ServiceException
{
	public SessionShixiaoException() {
        super();
    }

    public SessionShixiaoException(String message) {
        super(message);
    }

    public SessionShixiaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionShixiaoException(Throwable cause) {
        super(cause);
    }

    protected SessionShixiaoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
