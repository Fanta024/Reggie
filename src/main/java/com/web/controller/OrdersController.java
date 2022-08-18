package com.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.web.common.R;
import com.web.domain.Orders;
import com.web.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        ordersService.submit(orders);
        return R.success(null, "下单成功");
    }

    @GetMapping("/page")
    public R<IPage> list(long page, long pageSize) {
        IPage pageInfo = new Page(page, pageSize);
        ordersService.page(pageInfo);
        return R.success(pageInfo, "");
    }

    @PutMapping
    public R<String> updeatStatus(Orders orders) {
        ordersService.updateStatus(orders);
        return R.success(null, "修改成功");
    }
}
