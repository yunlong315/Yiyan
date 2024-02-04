package yiyan.research.model.response.search;

import yiyan.research.model.domain.openalex.Works;

import java.util.List;

@lombok.Data
public class GetRelatedWorksRsp {
    private List<Paper> papers;

    @lombok.Data
    public static class Paper {
        private String workId;
        private List<Author> author;
        private long citedByCount;
        private String title;
        private String abstractText;
        private long viewCount;

        public Paper(Works works){
            workId = works.getId();
            citedByCount = works.getCitedByCount();
            title = works.getTitle();
            abstractText = works.getAbstracts();
        }
        public Paper(){}
    }

    @lombok.Data
    public static class Author {
        private String id;
        private String displayName;
    }

    public GetRelatedWorksRsp(List<Paper> works){
        papers = works;
    }

    public GetRelatedWorksRsp(){}
}





