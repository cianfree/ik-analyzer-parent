package org.wltea.analyzer.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Arvin
 * @time 2017/1/19 9:29
 */
public class IKAnalyzerTest {

    @Test
    public void testIKAnalyzer() {

        String content = "春天";//"我是中国人， 春天来了";

        System.out.println("标准分词器： " + join(analyzer(new StandardAnalyzer(), content), "\t"));

        System.out.println("IK分词器(细粒度切分算法)： " + join(analyzer(new IKAnalyzer(), content), "\t"));

        System.out.println("IK分词器(智能分词算法)： " + join(analyzer(new IKAnalyzer(true), content), "\t"));


    }

    /**
     * 对内容content使用analyzer进行分词
     *
     * @param analyzer 分词器
     * @param content  内容
     * @return
     */
    public List<String> analyzer(Analyzer analyzer, String content) {
        List<String> result = new ArrayList<>();
        TokenStream ts = analyzer.tokenStream("content", content);
        CharTermAttribute ch = ts.addAttribute(CharTermAttribute.class);
        try {
            ts.reset();
            while (ts.incrementToken()) {
                String word = ch.toString();
                if (null != word && !"".equals(word) && !result.contains(word)) {
                    result.add(word);
                }
            }
            ts.end();
            ts.close();
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        }
        return result;
    }

    /**
     * 连接
     *
     * @param cols
     * @param sep
     * @return
     */
    public String join(Collection<String> cols, String sep) {
        StringBuilder builder = new StringBuilder();
        if (null != cols && !cols.isEmpty()) {
            for (String item : cols) {
                builder.append(item).append(sep);
            }
        }
        return builder.toString().replaceAll(sep + "$", "");
    }
}
