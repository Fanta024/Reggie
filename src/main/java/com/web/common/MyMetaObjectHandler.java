package com.web.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("insert公共字段填充"+metaObject.toString());
        metaObject.setValue("createUser",BaseContext.getId());
        metaObject.setValue("updateUser",BaseContext.getId());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("update公共字段填充"+metaObject.toString());
        metaObject.setValue("updateUser",BaseContext.getId());
        metaObject.setValue("updateTime", LocalDateTime.now());
        long id = Thread.currentThread().getId();
        log.info("线程id{}",id);
        log.warn("{}",BaseContext.getId());

    }
}
