package yiyan.research.model.esEntity;

import lombok.Data;
import lombok.experimental.Accessors;
import yiyan.research.model.domain.openalex.Works;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class ESWork {
    private String id;
    private String title;
    private String abstractText;
    private Integer publicationYear;
    private Integer citedByCount;
    private Integer viewCount;
    private boolean isLike = false;
    private List<Author> authors;
    private List<Concept> concepts;
    private Source source;


    @Data
    public static class Concept {
        private String id;
        private String displayName;
    }

    @Data
    public static class Author {
        private String id;
        private String displayName;
        private String rawAffiliationString;
    }

    @Data
    public static class Source {
        private String id;
        private String displayName;
    }

    public static ESWork parse(Works work, List<Author> authors, List<Concept> concepts, Source source){
        ESWork res = new ESWork();
        res.id = work.getId();
        res.title = work.getTitle();
        res.abstractText = work.getAbstractInvertedIndex();
        res.publicationYear = work.getPublicationYear();
        res.citedByCount = work.getCitedByCount();
        res.authors = authors;
        res.concepts = concepts;
        res.source = source;
        return res;
    }
}