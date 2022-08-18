package com.web.service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.common.BaseContext;
import com.web.common.CustomException;
import com.web.dao.OrdersDao;
import com.web.domain.*;
import com.web.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrdersServiceImpl extends ServiceImpl<OrdersDao,Orders> implements OrdersService {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private UserService userService;

    @Override
    public void submit(Orders orders) {
        long userId = BaseContext.getId();
        User user = userService.getById(userId);
        //购物车获取地址   用户会有多个地址   所以从当前的取
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if (addressBook==null) throw new CustomException("地址有误，下单失败");
        //从购物车获取菜单
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(shoppingCartLambdaQueryWrapper);
        if(shoppingCartList == null || shoppingCartList.size() == 0) throw new CustomException("购物车为空，不能下单");

        //订单号
        long orderId = IdWorker.getId(); //生成订单号
        orders.setNumber(String.valueOf(orderId));
        //保存到detail
        AtomicInteger amount = new AtomicInteger(0); //总金额

        List<OrderDetail> orderDetailList = shoppingCartList.stream().map(item -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(item, orderDetail, "createTime", "userId");
            orderDetail.setOrderId(orderId);
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue()); //统计购物车金额
            return orderDetail;

        }).collect(Collectors.toList());
        log.info("amount={}",amount);

        //订单表插入数据

        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        save(orders);
        //订单明细表插入数据
        orderDetailService.saveBatch(orderDetailList);

        //清空购物车
        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);
    }
}
