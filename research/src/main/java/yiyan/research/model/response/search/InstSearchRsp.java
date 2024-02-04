package yiyan.research.model.response.search;

import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import lombok.Data;
import yiyan.research.model.esEntity.ESInstitution;

import java.util.ArrayList;
import java.util.List;

@Data
public class InstSearchRsp {
    private List<ESInstitution> results;
    private List<Country> countryList;
    private List<Type> typeList;
    private int hits;

    @Data
    public static class Country {
        private long docCnt;
        private String name;
    }

    @Data
    public static class Type {
        private long docCnt;
        private String name;
    }

    public InstSearchRsp(){}

    public InstSearchRsp(TotalHits total, List<Hit<ESInstitution>> hits, List<StringTermsBucket> countryBuckets, List<StringTermsBucket> typeBuckets) {
        this.hits = (int) total.value();
        results = new ArrayList<>();
        countryList = new ArrayList<>();
        typeList = new ArrayList<>();
        for(Hit<ESInstitution> hit : hits){
            results.add(hit.source());
        }
        for(StringTermsBucket bucket : countryBuckets){
            Country country = new Country();
            country.name = bucket.key();
            country.docCnt = bucket.docCount();
            countryList.add(country);
        }
        for(StringTermsBucket bucket : typeBuckets){
            Type type = new Type();
            type.name = bucket.key();
            type.docCnt = bucket.docCount();
            typeList.add(type);
        }
    }

}
