package com.simbest.boot.wfengine.rabbitmq.register;/**
 * @author Administrator
 * @create 2020/2/18 22:40.
 */

/**
 *@ClassName RabbitMQException
 *@Description TODO
 *@Author Administrator
 *@Date 2020/2/18 22:40
 *@Version 1.0
 **/
public class RabbitMQException extends RuntimeException{

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public RabbitMQException(String message) {
        super(message);
    }
}
