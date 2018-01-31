package com.stylefeng.guns.modular.cus.service.impl;

import com.stylefeng.guns.common.persistence.model.Person;
import com.stylefeng.guns.common.persistence.dao.PersonMapper;
import com.stylefeng.guns.modular.cus.service.IPersonService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 格调先生123
 * @since 2018-01-31
 */
@Service
public class PersonServiceImpl extends ServiceImpl<PersonMapper, Person> implements IPersonService {

}
