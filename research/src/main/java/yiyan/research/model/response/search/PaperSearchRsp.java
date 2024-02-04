package yiyan.research.model.response.search;

// PaperSearchRsp.java

import co.elastic.clients.elasticsearch._types.aggregations.HistogramBucket;
import co.elastic.clients.elasticsearch._types.aggregations.MultiTermsBucket;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import jakarta.annotation.Resource;
import lombok.Data;
import yiyan.research.model.domain.WorksInfo;
import yiyan.research.model.esEntity.ESWork;
import yiyan.research.service.WorkService;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaperSearchRsp {
    private List<SourceList> sourceList;
    private List<ConceptList> conceptList;
    private List<PublicYearRange> publicYearRangeList;
    private List<ESWork> results;
    private int hits;

    public PaperSearchRsp(TotalHits total, List<Hit<ESWork>> hits, List<StringTermsBucket> conceptsBuckets, List<StringTermsBucket> sourceBuckets, List<HistogramBucket> publicYearBuckets) {
        this.hits = (int) total.value();
        results = new ArrayList<>();
        sourceList = new ArrayList<>();
        conceptList = new ArrayList<>();
        publicYearRangeList = new ArrayList<>();
        for(Hit<ESWork> hit : hits){
            results.add(hit.source());
        }
        for(StringTermsBucket bucket : conceptsBuckets){
            ConceptList concept = new ConceptList();
            concept.name = bucket.key();
            concept.docCnt = bucket.docCount();
            conceptList.add(concept);
        }
        for(StringTermsBucket bucket : sourceBuckets){
            SourceList source = new SourceList();
            source.name = bucket.key();
            source.docCnt = bucket.docCount();
            sourceList.add(source);
        }
        for(HistogramBucket bucket : publicYearBuckets){
            PublicYearRange type = new PublicYearRange();
            type.key = bucket.key();
            type.docCnt = bucket.docCount();
            publicYearRangeList.add(type);
        }
    }

    public PaperSearchRsp(){}

    @Data
    public static class ConceptList {
        private long docCnt;
        private String name;
    }

    @Data
    public static class SourceList {
        private long docCnt;
        private String name;
    }

    @Data
    public static class PublicYearRange {
        private long docCnt;
        private double key;
    }
}


