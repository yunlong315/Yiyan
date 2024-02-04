package yiyan.research.model.request.search;

import lombok.Data;

// PaperSearchReq.java

import java.util.List;
import java.util.Objects;

@Data
public class PaperSearchReq {
    /**
     * 概念领域Id，概念领域筛选Id，-1表示不筛选
     */
    private String conceptName;
    /**
     * 检索条件列表
     */
    private List<Conditions> conditions;
    /**
     * 结束年限，筛选结束年限
     */
    private int endYear;
    /**
     * 排序方式，0-相关度；1-发表时间
     */
    private int order;
    /**
     * 页码，从1开始的当前页码
     */
    private int page;
    /**
     * 显示数量，每页内显示的结果数量
     */
    private int resultInPage;
    /**
     * 来源Id，筛选来源Id，-1表示不筛选
     */
    private String sourceName;
    /**
     * 开始年限，筛选开始年限
     */
    private int startYear;

    @Data
    public static class Conditions {
        /**
         * 条件运算符，0-并且；1-或者；2-且非
         */
        private int relation;
        /**
         * 检索词，检索词
         */
        private String text;
        /**
         * 目标类型，0-综合；1-标题；2-摘要；3-作者
         */
        private int type;
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Conditions that = (Conditions) o;
            return relation == that.relation &&
                    type == that.type &&
                    Objects.equals(text, that.text);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaperSearchReq that = (PaperSearchReq) o;
        return endYear == that.endYear &&
                order == that.order &&
                page == that.page &&
                resultInPage == that.resultInPage &&
                startYear == that.startYear &&
                Objects.equals(conceptName, that.conceptName) &&
                Objects.equals(conditions, that.conditions) &&
                Objects.equals(sourceName, that.sourceName);
    }
}


