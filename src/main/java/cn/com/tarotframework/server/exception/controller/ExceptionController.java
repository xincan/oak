package cn.com.tarotframework.server.exception.controller;

import cn.com.tarotframework.exception.TarotBusinessException;
import cn.com.tarotframework.response.TarotCode;
import cn.com.tarotframework.response.TarotResponseResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * copyright (C), 2022, 塔罗牌基础架构
 *
 * @program: tarot-demo
 * @description: 异常测试类
 * @author: Jiang Xincan
 * @date: 2022/5/25 15:34
 * @version: 0.0.1
 **/

@Api(tags = {"异常处理DEMO"})
@Validated
@RestController
@RequestMapping("exception")
@TarotResponseResultBody
public class ExceptionController {

    @ApiOperation(value = "常规异常测试1", httpMethod = "GET", notes = "常规异常测试1（枚举加，文字说明）")
    @GetMapping("demo1")
    public void demo1() {
        throw new TarotBusinessException(TarotCode.BUSINESS_EXCEPTION, "测试普通异常错误【demo1】");
    }

    @ApiOperation(value = "常规异常测试2", httpMethod = "GET", notes = "常规异常测试2（只有文字说明）")
    @GetMapping("demo2")
    public void demo2() {
        throw new TarotBusinessException("测试普通异常错误【demo2】");
    }

    @ApiOperation(value = "常规异常测试3", httpMethod = "GET", notes = "常规异常测试3（自定义状态码，加文字说明）")
    @GetMapping("demo3")
    public void demo3() {
        throw new TarotBusinessException(3001, "测试普通异常错误:自定义状态码，加文字说明【demo3】");
    }

    @ApiOperation(value = "常规异常测试4", httpMethod = "GET", notes = "常规异常测试4（try-cach块，加文字说明）")
    @GetMapping("demo4")
    public void demo4() {
        try {
            int i = 1 / 0;
        }catch (Exception e) {
            throw new TarotBusinessException("测试普通异常错误:自定义状态码，加文字说明【demo4】", e);
        }

    }

}
