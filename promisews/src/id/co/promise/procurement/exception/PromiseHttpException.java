package id.co.promise.procurement.exception;

import java.io.Serializable;

public class PromiseHttpException extends Exception implements Serializable{
	private static final long serialVersionUID = 1L;
    public PromiseHttpException() {
        super();
    }
    public PromiseHttpException(String msg)   {
        super(msg);
    }
    public PromiseHttpException(String msg, Exception e)  {
        super(msg, e);
    }
}
