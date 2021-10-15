package org.wxd.agent.tool.io;

/**
 * @author: Troy.Chen(失足程序员, 15388152619)
 * @version: 2021-08-18 15:40
 **/
public interface EConsumer<T> {

    void accept(T t) throws Exception;
}
