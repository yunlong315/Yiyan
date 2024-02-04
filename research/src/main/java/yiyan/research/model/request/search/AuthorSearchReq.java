package yiyan.research.model.request.search;

import lombok.Data;

import java.util.Objects;

@Data
public class AuthorSearchReq {
    /**
     * 页码，从1开始的当前页码
     */
    private int page;
    /**
     * 显示数量，每页内显示的结果数量
     */
    private int resultInPage;
    /**
     * 搜索词，如果是句子会自动分词
     */
    private String text;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorSearchReq that = (AuthorSearchReq) o;
        return page == that.page &&
                resultInPage == that.resultInPage &&
                Objects.equals(text, that.text);
    }
}
