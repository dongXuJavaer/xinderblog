package com.xinder.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinder.api.bean.Article;
import com.xinder.api.bean.History;
import com.xinder.api.response.dto.HistoryListDtoResult;
import com.xinder.api.response.dto.HistorySimpleDtoResult;
import com.xinder.api.response.dto.UserDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import com.xinder.article.feign.UserFeignClient;
import com.xinder.article.mapper.HistoryMapper;
import com.xinder.article.service.HistoryService;
import com.xinder.common.constant.CommonConstant;
import com.xinder.common.util.CookieUtils;
import com.xinder.common.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Xinder
 * @since 2023-03-30
 */
@Service
@Slf4j
public class HistoryServiceImpl extends ServiceImpl<HistoryMapper, History> implements HistoryService {

    @Autowired
    private HistoryMapper historyMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private ArticleServiceImpl articleService;

    @Override
    public Result batchSave(List<History> historyList, Long uid) {
        int i = 0;
        if (!CollectionUtils.isEmpty(historyList)) {
            List<History> oldList = getHistoryListByUid(uid);
            Map<Long, History> map = oldList.stream().collect(Collectors.toMap(History::getAid, Function.identity()));
            // 如果之前有过浏览记录，则更新其时间
            List<History> updateList = new ArrayList<>();
            for (int j = 0; j < historyList.size(); j++) {
                History history = map.get(historyList.get(j).getAid());
                if (history != null) {
                    updateList.add(history);
                    historyList.remove(j);
                    j--;
                }
            }
            transactionTemplate.executeWithoutResult(status -> {
                if (!CollectionUtils.isEmpty(updateList)) {
                    historyMapper.batchUpdate(updateList);
                }
                if (!CollectionUtils.isEmpty(historyList)) {
                    historyMapper.batchSave(historyList, uid);
                }

            });

        }
        return Result.success("添加成功");
    }

    @Override
    public boolean addHistoryToFront(Article article, String cookieDomain) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        List<History> historyList = Util.getHistoryListByCookie();

        History history = new History()
                .setAid(article.getId())
                .setCreateTime(new Date());
        boolean containFlag = false;
        for (int i = 0; i < historyList.size(); i++) {
            // 如果已经浏览过，则只更新时间
            if (historyList.get(i).getAid().equals(history.getAid())) {
                historyList.set(i, history);
                containFlag = true;
                break;
            }
        }
        // 浏览历史里面不包含，则添加到浏览历史
        if (!containFlag) {
            historyList.add(history);
        }
        log.info("cookie中的浏览信息{}", historyList);
        String str = JSON.toJSONString(historyList);
        try {
            str = URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        CookieUtils.addCookie(response, cookieDomain,
                "/", CommonConstant.HTTP_HEADER_HISTORY, str, -1, false);
        return true;
    }

    @Override
    public HistoryListDtoResult getCurrentUserHistoryList() {

        Long uid = userFeignClient.currentUser().getData().getId();
        List<History> historyList = null;
        HistoryListDtoResult dtoResult = DtoResult.dataDtoSuccess(HistoryListDtoResult.class);
        if (uid != null) {
            historyList = getHistoryListByUid(uid);
        } else {
            historyList = Util.getHistoryListByCookie();
        }

        historyList.sort(((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime())));
        List<HistorySimpleDtoResult> dayList = buildListByDay(historyList);

        dtoResult.setList(dayList);
        return dtoResult;
    }

    /**
     * 构建根据如期分类的浏览记录列表
     *
     * @param historyList 当前用户的浏览记录（未登录则为客户端cookie里面的）
     * @return {@link List}<{@link HistorySimpleDtoResult}>
     */
    private List<HistorySimpleDtoResult> buildListByDay(List<History> historyList) {
        List<HistorySimpleDtoResult> dayList = new ArrayList<>();
        Calendar ca = Calendar.getInstance();
        historyList.forEach(item -> {
            Article article = articleService.getArticleById(item.getAid());
            item.setArticle(article);

            ca.setTime(item.getCreateTime());
            String dateStr = "" + ca.get(Calendar.YEAR) + "-" + (ca.get(Calendar.MONTH) + 1) + "-" + ca.get(Calendar.DAY_OF_MONTH);
            System.out.println(dateStr);
            // 当前记录是否已经组合
            boolean containFlag = false;
            for (HistorySimpleDtoResult simpleDtoResult : dayList) {
                if (simpleDtoResult.getDateStr().equals(dateStr)) {
                    simpleDtoResult.getHistoryList().add(item);
                    containFlag = true;
                    break;
                }
            }
            // 如果不存在则当前日期的组合，则新建一个
            if (!containFlag) {
                HistorySimpleDtoResult simpleDtoResult = new HistorySimpleDtoResult()
                        .setDate(item.getCreateTime())
                        .setDateStr(dateStr);
                simpleDtoResult.getHistoryList().add(item);
                dayList.add(simpleDtoResult);
            }
        });

        dayList.sort(((o1, o2) -> o2.getDate().compareTo(o1.getDate())));
        return dayList;
    }

    /**
     * 根据uid获取用户的所有浏览记录
     *
     * @param uid uid
     * @return {@link List}<{@link History}>
     */
    private List<History> getHistoryListByUid(Long uid) {
        LambdaQueryWrapper<History> wrapper = new LambdaQueryWrapper<History>().eq(History::getUid, uid);
        return historyMapper.selectList(wrapper);
    }


}
