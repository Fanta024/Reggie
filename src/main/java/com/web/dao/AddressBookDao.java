package com.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.web.domain.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookDao extends BaseMapper<AddressBook> {

}
