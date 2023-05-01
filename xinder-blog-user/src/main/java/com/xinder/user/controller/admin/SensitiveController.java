package com.xinder.user.controller.admin;

import com.baomidou.mybatisplus.extension.api.R;
import com.xinder.api.bean.Role;
import com.xinder.api.response.RespBean;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import com.xinder.common.util.ContentFilterUtils;
import com.xinder.common.util.SensitiveWordInit;
import com.xinder.common.util.SensitiveWordsUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author Xinder
 * @date 2023-05-01 16:40
 */
@RestController
@RequestMapping("/sensitive")
public class SensitiveController {

    @RequestMapping("/add")
    public RespBean addSensitive(@RequestParam("sensitive") String content) {
        if (content == null){
            return new RespBean("error", "不能为空");
        }
        boolean addFlag = SensitiveWordsUtils.add(content);
        if (addFlag) {
            SensitiveWordsUtils.refresh();
            return new RespBean("success", "添加成功!");
        } else {
            return new RespBean("error", "添加失败!");
        }
    }

    @RequestMapping("/list")
    public DtoResult list() {
        Set<String> wordSet = SensitiveWordsUtils.getInstance().getSensitiveWordInit().getWordSet();
        DtoResult dtoResult = DtoResult.success();
        dtoResult.setData(wordSet);
        return dtoResult;
    }

    @RequestMapping("/delete")
    public RespBean delete(@RequestParam("name") String name) {
        boolean deleteFlag = SensitiveWordsUtils.delete(name);
        if (deleteFlag) {
            SensitiveWordsUtils.refresh();
            return new RespBean("success", "删除成功!");
        } else {
            return new RespBean("error", "删除失败!");
        }
    }
}
