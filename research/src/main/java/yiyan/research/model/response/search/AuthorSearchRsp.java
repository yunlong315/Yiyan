package yiyan.research.model.response.search;

import co.elastic.clients.elasticsearch._types.aggregations.HistogramBucket;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import lombok.Data;
import yiyan.research.model.esEntity.ESAuthor;
import yiyan.research.model.esEntity.ESInstitution;

import java.util.ArrayList;
import java.util.List;

@Data
public class AuthorSearchRsp {
    private List<ESAuthor> results;
    private List<Institution> instList;
    private List<HIndexRange> hIndexRangeList;
    private int hits;

    public AuthorSearchRsp(TotalHits total, List<Hit<ESAuthor>> hits, List<StringTermsBucket> instBuckets, List<HistogramBucket> hIndexBuckets) {

        this.hits = (int) total.value();
        results = new ArrayList<>();
        instList = new ArrayList<>();
        hIndexRangeList = new ArrayList<>();
        for(Hit<ESAuthor> hit : hits){
            results.add(hit.source());
        }
        for(StringTermsBucket bucket : instBuckets){
            Institution institution = new Institution();
            institution.name = bucket.key();
            institution.docCnt = bucket.docCount();
            instList.add(institution);
        }
        for(HistogramBucket bucket : hIndexBuckets){
            HIndexRange type = new HIndexRange();
            type.key = bucket.key();
            type.docCnt = bucket.docCount();
            hIndexRangeList.add(type);
        }

    }

    public AuthorSearchRsp(){}

    @Data
    public static class Institution {
        private long docCnt;
        private String name;
    }

    @Data
    public static class HIndexRange {
        private long docCnt;
        private double key;
    }

}
