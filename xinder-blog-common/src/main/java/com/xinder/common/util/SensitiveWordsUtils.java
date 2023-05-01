package com.xinder.common.util;

import org.elasticsearch.common.inject.Singleton;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 敏感词工具类
 */
public class SensitiveWordsUtils {
    //敏感词过滤器：利用DFA算法  进行敏感词过滤
    private Map sensitiveWordMap = null;
    // 最小匹配规则，如：敏感词库["中国","中国人"]，语句："我是中国人"，匹配结果：我是[中国]人
    public static int minMatchType = 1;
    // 最大匹配规则，如：敏感词库["中国","中国人"]，语句："我是中国人"，匹配结果：我是[中国人]
    public static int maxMatchType = 2;
    // 单例
    private static SensitiveWordsUtils instance = null;

    private SensitiveWordInit sensitiveWordInit;

    // 构造函数，初始化敏感词库
    private SensitiveWordsUtils() {
        sensitiveWordInit = new SensitiveWordInit();
        sensitiveWordMap = sensitiveWordInit.initKeyWord();
    }

    // 提供给获取敏感词汇的使用
    public SensitiveWordInit getSensitiveWordInit() {
        return sensitiveWordInit;
    }

    // 获取单例
    public static SensitiveWordsUtils getInstance() {
        if (null == instance) {
            synchronized (SensitiveWordsUtils.class) {
                if (null == instance) {
                    instance = new SensitiveWordsUtils();
                }
            }
        }
        return instance;
    }

    // 刷新单例对象
    public static void refresh() {
        instance = new SensitiveWordsUtils();
    }

    // 添加敏感词
    public static boolean add(String content) {
        //敏感词库
        File file = new File(SensitiveWordInit.filePath);
        try {
            // 文件是否是文件 和 是否存在
            if (!file.isFile() || !file.exists()) {
                file.createNewFile();
            }

            // 获取文件输出流
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.newLine();
            bufferedWriter.write(content);
            bufferedWriter.flush();
            // 关闭文件流
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 删除敏感词汇
    public static boolean delete(String content) {
        //敏感词库
        File file = new File(SensitiveWordInit.filePath);
        try {
            // 文件是否是文件 和 是否存在
            if (!file.isFile() || !file.exists()) {
                file.createNewFile();
            }
            // 读取文件输入流
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(read);
            String txt = null;
            List<String> list = new ArrayList<>();
            while ((txt = bufferedReader.readLine()) != null) {
                boolean equals = txt.equals(content);
                if (equals) {
                    continue;
                }
                list.add(txt);
            }
            bufferedReader.close();
            read.close();

            // 获取文件输出流
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            for (String str : list) {
                bufferedWriter.write(str);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
            // 关闭文件流
            bufferedWriter.close();
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    // 获取文字中的敏感词
    public Set<String> getSensitiveWord(String txt, int matchType) {
        Set<String> sensitiveWordList = new HashSet<String>();
        for (int i = 0; i < txt.length(); i++) {
            // 判断是否包含敏感字符
            int length = checkSensitiveWord(txt, i, matchType);
            // 存在,加入list中
            if (length > 0) {
                sensitiveWordList.add(txt.substring(i, i + length));
                // 减1的原因，是因为for会自增
                i = i + length - 1;
            }
        }
        return sensitiveWordList;
    }

    // 替换敏感字字符
    public String replaceSensitiveWord(String txt, int matchType,
                                       String replaceChar) {
        String resultTxt = txt;
        // 获取所有的敏感词
        Set<String> set = getSensitiveWord(txt, matchType);
        Iterator<String> iterator = set.iterator();
        String word = null;
        String replaceString = null;
        while (iterator.hasNext()) {
            word = iterator.next();
            //word长度为n 就有n个*
            replaceString = getReplaceChars(replaceChar, word.length());
            //将resultTxt中的敏感词替换为*
            resultTxt = resultTxt.replaceAll(word, replaceString);
        }
        return resultTxt;
    }

    /**
     * 获取替换字符串
     *
     * @param replaceChar
     * @param length
     * @return
     */
    private String getReplaceChars(String replaceChar, int length) {
        String resultReplace = replaceChar;
        for (int i = 1; i < length; i++) {
            resultReplace += replaceChar;
        }

        return resultReplace;
    }

    /**
     * 检查文字中是否包含敏感字符，检查规则如下：<br>
     * 如果存在，则返回敏感词字符的长度，不存在返回0
     *
     * @param txt
     * @param beginIndex
     * @param matchType
     * @return
     */
    public int checkSensitiveWord(String txt, int beginIndex, int matchType) {

        // 敏感词结束标识位：用于敏感词只有1位的情况
        boolean flag = false;
        // 匹配标识数默认为0  作用是检查文本敏感词有多少位
        int matchFlag = 0;
        //敏感词map
        Map nowMap = sensitiveWordMap;
        for (int i = beginIndex; i < txt.length(); i++) {
            //读取文本字符
            char word = txt.charAt(i);
            // 获取指定key
            nowMap = (Map) nowMap.get(word);
            // 存在，则判断是否为最后一个
            if (nowMap != null) {
                // 找到相应key，匹配标识+1
                matchFlag++;
                // 如果为最后一个匹配规则,结束循环，返回匹配标识数
                if ("1".equals(nowMap.get("isEnd"))) {
                    // 结束标志位为true
                    flag = true;
                    // 最小规则，直接返回,最大规则还需继续查找
                    if (SensitiveWordsUtils.minMatchType == matchType) {
                        break;
                    }
                }
            }
            // 不存在，直接返回
            else {
                break;
            }
        }
        if (SensitiveWordsUtils.maxMatchType == matchType) {
            if (matchFlag < 2 || !flag) {        //长度必须大于等于1，为词
                matchFlag = 0;
            }
        }
        if (SensitiveWordsUtils.minMatchType == matchType) {
            if (matchFlag < 2 && !flag) {        //长度必须大于等于1，为词
                matchFlag = 0;
            }
        }
        return matchFlag;
    }

    public boolean checkContainSensitiveWord(String txt, int matchType) {
        for (int i = 0; i < txt.length(); i++) {
            System.out.println(txt.charAt(i));
            int length = checkSensitiveWord(txt, i, matchType);
            System.out.println(length);
            if (length > 0) {
                return true;
            }
        }
        return false;
    }

}
