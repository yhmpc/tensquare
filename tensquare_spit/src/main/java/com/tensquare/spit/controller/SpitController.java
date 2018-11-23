package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/spit")
public class SpitController {

    @Autowired
    private SpitService spitService;

    @RequestMapping(method = RequestMethod.GET)
    public Result<List<Spit>> findAll() {
        return new Result<>(true, StatusCode.OK, "查询成功", spitService.findAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result<Spit> findOne(@PathVariable String id) {
        return new Result<>(true, StatusCode.OK, "查询成功", spitService.findById(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Spit spit) {
        spitService.add(spit);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result add(@PathVariable String id, @RequestBody Spit spit) {
        spit.set_id(id);
        spitService.update(spit);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String id) {
        spitService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 根据上级ID查询吐槽分页数据
     */
    @RequestMapping(value = "/comment/{parentid}/{page}/{size}", method = RequestMethod.GET)
    public Result findByParentid(@PathVariable String parentid, @PathVariable int page, @PathVariable int size) {
        Page<Spit> pageList = spitService.findByParentid(parentid, page, size);
        return new Result(true, StatusCode.OK, "", new PageResult<Spit>(pageList.getTotalElements(), pageList.getContent()));
    }

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 点赞 --- 防止重复点赞
     */
    @RequestMapping(value = "/thumbup/{id}", method = RequestMethod.PUT)
    public Result updateThumbup(@PathVariable String id) {
        // 判断当前登陆用户是否点过赞
        String userid = "2023"; // 模拟登陆用户ID

        if (redisTemplate.opsForValue().get("thumbup_" + userid + "_" + id) != null) {
            return new Result(false, StatusCode.REP_ERROR, "您已经点过赞了");
        }
        spitService.updateThumbup(id);
        redisTemplate.opsForValue().set("thumbup_" + userid + "_" + id, "1");
        return new Result(true, StatusCode.OK, "点赞成功");
    }
}
