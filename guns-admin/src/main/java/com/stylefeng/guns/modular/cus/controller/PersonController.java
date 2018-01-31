package com.stylefeng.guns.modular.cus.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.common.persistence.model.Person;
import com.stylefeng.guns.modular.cus.service.IPersonService;

/**
 * 人员管理控制器
 *
 * @author 格调先生
 * @Date 2018-01-31 15:22:43
 */
@Controller
@RequestMapping("/person")
public class PersonController extends BaseController {

    private String PREFIX = "/cus/person/";

    @Autowired
    private IPersonService personService;

    /**
     * 跳转到人员管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "person.html";
    }

    /**
     * 跳转到添加人员管理
     */
    @RequestMapping("/person_add")
    public String personAdd() {
        return PREFIX + "person_add.html";
    }

    /**
     * 跳转到修改人员管理
     */
    @RequestMapping("/person_update/{personId}")
    public String personUpdate(@PathVariable Integer personId, Model model) {
        Person person = personService.selectById(personId);
        model.addAttribute("item",person);
        LogObjectHolder.me().set(person);
        return PREFIX + "person_edit.html";
    }

    /**
     * 获取人员管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return personService.selectList(null);
    }

    /**
     * 新增人员管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Person person) {
        personService.insert(person);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除人员管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer personId) {
        personService.deleteById(personId);
        return SUCCESS_TIP;
    }

    /**
     * 修改人员管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Person person) {
        personService.updateById(person);
        return super.SUCCESS_TIP;
    }

    /**
     * 人员管理详情
     */
    @RequestMapping(value = "/detail/{personId}")
    @ResponseBody
    public Object detail(@PathVariable("personId") Integer personId) {
        return personService.selectById(personId);
    }
}
