package cn.com.tarotframework.exception;

/**
 * copyright (C), 2022, 塔罗牌基础架构
 *
 * @program: tarot-demo
 * @description: 异常处理
 * @author: Jiang Xincan
 * @date: 2022/5/23 10:52
 * @version: 0.0.1
 **/
public class OakException extends TarotBusinessException {

    public OakException(int i, String s) {
        super(i, s);
    }
}
