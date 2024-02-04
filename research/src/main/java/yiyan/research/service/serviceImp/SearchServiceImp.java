package yiyan.research.service.serviceImp;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.HistogramBucket;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.json.JsonData;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import yiyan.research.mapper.WorkMapper;
import yiyan.research.model.domain.WorksInfo;
import yiyan.research.model.domain.openalex.Works;
import yiyan.research.model.esEntity.ESAuthor;
import yiyan.research.model.esEntity.ESInstitution;
import yiyan.research.model.esEntity.ESWork;
import yiyan.research.model.request.search.AuthorSearchReq;
import yiyan.research.model.request.search.GetRelatedWorksReq;
import yiyan.research.model.request.search.InstSearchReq;
import yiyan.research.model.request.search.PaperSearchReq;
import yiyan.research.model.response.search.*;
import yiyan.research.service.SearchService;
import yiyan.research.service.WorkService;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImp implements SearchService {
    private final String workIndex = "work";
    private final String instIndex = "institution";
    private final String authorIndex = "author";
    @Resource
    ElasticsearchClient elasticsearchClient;
    @Resource
    WorkMapper workMapper;

    @Override
    public PaperSearchRsp searchWorks(PaperSearchReq request) {
        try {
            List<Query> must = new ArrayList<>();
            List<Query> should = new ArrayList<>();
            List<Query> must_not = new ArrayList<>();
            List<Query> must2 = new ArrayList<>();
            getBoolQueryBuilder(request.getConditions(), must, should, must_not);
            if (request.getConceptName() != null) {
                must2.add(MatchQuery.of(m -> m
                        .field("concepts.displayName")
                        .query(request.getConceptName())
                )._toQuery());
            }
            if (request.getSourceName() != null) {
                must2.add(MatchQuery.of(m -> m
                        .field("source.displayName")
                        .query(request.getSourceName())
                )._toQuery());
            }
            if (request.getStartYear() != -1) {
                must2.add(RangeQuery.of(r -> r
                        .field("publicationYear")
                        .gte(JsonData.of(request.getStartYear()))
                )._toQuery());
            }
            if (request.getEndYear() != -1) {
                must2.add(RangeQuery.of(r -> r
                        .field("publicationYear")
                        .lte(JsonData.of(request.getEndYear()))
                )._toQuery());
            }

            int from = (request.getPage()-1) * request.getResultInPage();
            int size = request.getResultInPage();

            SearchResponse<ESWork> response;

            if(request.getOrder() == 1){
                response = elasticsearchClient.search(s -> s
                                .index(workIndex)
                                .from(from)
                                .size(size)
                                .query(q -> q
                                        .bool(b1 -> b1
                                                .must(m -> m
                                                        .bool(b2 -> b2
                                                                .must(must)
                                                                .should(should)
                                                                .mustNot(must_not)))
                                                .must(must2)
                                        )
                                ).sort(so -> so.
                                        field(f -> f
                                                .field("publicationYear")
                                                .order(SortOrder.Desc))
                                ).aggregations("concepts", a -> a
                                        .terms(t -> t
                                                .size(15)
                                                .field("concepts.displayName")))
                                .aggregations("source", a -> a
                                        .terms(t -> t
                                                .size(15)
                                                .field("source.displayName")))
                                .aggregations("publicationYear", a -> a
                                        .histogram(t -> t
                                                .field("publicationYear")
                                                .interval(10.0)
                                                .minDocCount(1)))
                        , ESWork.class
                );
            }
            else if(request.getOrder() == 2){
                response = elasticsearchClient.search(s -> s
                                .index(workIndex)
                                .from(from)
                                .size(size)
                                .query(q -> q
                                        .bool(b1 -> b1
                                                .must(m -> m
                                                        .bool(b2 -> b2
                                                                .must(must)
                                                                .should(should)
                                                                .mustNot(must_not)))
                                                .must(must2)
                                        )
                                ).sort(so -> so.
                                        field(f -> f
                                                .field("citedByCount")
                                                .order(SortOrder.Desc))
                                ).aggregations("concepts", a -> a
                                        .terms(t -> t
                                                .size(15)
                                                .field("concepts.displayName")))
                                .aggregations("source", a -> a
                                        .terms(t -> t
                                                .size(15)
                                                .field("source.displayName")))
                                .aggregations("publicationYear", a -> a
                                        .histogram(t -> t
                                                .field("publicationYear")
                                                .interval(10.0)
                                                .minDocCount(1)))
                        , ESWork.class
                );
            }
            else {
                response = elasticsearchClient.search(s -> s
                                .index(workIndex)
                                .from(from)
                                .size(size)
                                .query(q -> q
                                        .bool(b1 -> b1
                                                .must(m -> m
                                                        .bool(b2 -> b2
                                                                .must(must)
                                                                .should(should)
                                                                .mustNot(must_not)))
                                                .must(must2)
                                        )
                                ).aggregations("concepts", a -> a
                                        .terms(t -> t
                                                .size(15)
                                                .field("concepts.displayName")))
                                .aggregations("source", a -> a
                                        .terms(t -> t
                                                .size(15)
                                                .field("source.displayName")))
                                .aggregations("publicationYear", a -> a
                                        .histogram(t -> t
                                                .field("publicationYear")
                                                .interval(10.0)
                                                .minDocCount(1)))
                        , ESWork.class
                );
            }

            TotalHits total = response.hits().total();
            List<Hit<ESWork>> hits = response.hits().hits();
            List<StringTermsBucket> conceptsBuckets = response.aggregations()
                    .get("concepts")
                    .sterms()
                    .buckets().array();
            List<StringTermsBucket> sourceBuckets = response.aggregations()
                    .get("source")
                    .sterms()
                    .buckets().array();
            List<HistogramBucket> publicYearBuckets = response.aggregations()
                    .get("publicationYear")
                    .histogram()
                    .buckets().array();

            return new PaperSearchRsp(total, hits, conceptsBuckets, sourceBuckets, publicYearBuckets);
        } catch (Exception e) {
            System.out.println("SearchWorks throws a exception");
            e.printStackTrace();
        }
        return null;
    }

    public InstSearchRsp searchInst(InstSearchReq request){
        try {
            int from = (request.getPage()-1) * request.getResultInPage();
            int size = request.getResultInPage();
            SearchResponse<ESInstitution> response = elasticsearchClient.search(s -> s
                            .index(instIndex)
                            .from(from)
                            .size(size)
                            .query(q -> q
                                    .bool(b -> b
                                            .should(getMatchQuery("name", request.getText()))
                                            .should(getMatchQuery("acronyms", request.getText()))
                                    )
                            ).aggregations("country", a -> a
                                    .terms(t -> t
                                            .size(15)
                                            .field("country")))
                            .aggregations("type", a -> a
                                    .terms(t -> t
                                            .size(15)
                                            .field("type")))
                    , ESInstitution.class
            );
            TotalHits total = response.hits().total();
            List<Hit<ESInstitution>> hits = response.hits().hits();
            List<StringTermsBucket> countryBuckets = response.aggregations()
                    .get("country")
                    .sterms()
                    .buckets().array();
            List<StringTermsBucket> typeBuckets = response.aggregations()
                    .get("type")
                    .sterms()
                    .buckets().array();
            return new InstSearchRsp(total, hits, countryBuckets, typeBuckets);
        }catch (Exception e){
            System.out.println("SearchInst throws a exception");
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public AuthorSearchRsp searchAuthor(AuthorSearchReq request) {
        try {
            int from = (request.getPage()-1) * request.getResultInPage();
            int size = request.getResultInPage();
            SearchResponse<ESAuthor> response = elasticsearchClient.search(s -> s
                            .index(authorIndex)
                            .from(from)
                            .size(size)
                            .query(q -> q
                                    .bool(b -> b
                                            .should(getMatchQuery("name", request.getText()))
                                            .should(getMatchQuery("alternatives", request.getText()))
                                    )
                            ).aggregations("institution", a -> a
                                    .terms(t -> t
                                            .size(15)
                                            .field("institution")))
                            .aggregations("hIndex", a -> a
                                    .histogram(t -> t
                                            .field("hindex")
                                            .interval(20.0)
                                            .minDocCount(1)))
                    , ESAuthor.class
            );
            TotalHits total = response.hits().total();
            List<Hit<ESAuthor>> hits = response.hits().hits();
            List<StringTermsBucket> instBuckets = response.aggregations()
                    .get("institution")
                    .sterms()
                    .buckets().array();
            List<HistogramBucket> hIndexBuckets = response.aggregations()
                    .get("hIndex")
                    .histogram()
                    .buckets().array();
            return new AuthorSearchRsp(total, hits, instBuckets, hIndexBuckets);
        }catch (Exception e){
            System.out.println("SearchAuthor throws a exception");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public GetRelatedWorksRsp getRelatedWorks(GetRelatedWorksReq request) {
        List<GetRelatedWorksRsp.Paper> works = new ArrayList<>();
        for(String workId: request.getRelatedWorks()){
            List<GetRelatedWorksRsp.Author> authors = workMapper.getAuthorsOfWork(workId);
            GetRelatedWorksRsp.Paper work = new GetRelatedWorksRsp.Paper(workMapper.getWorkById(workId));
            WorksInfo worksInfo = workMapper.getWorksInfo(workId);
            if(worksInfo == null){
                worksInfo = new WorksInfo(workId);
                workMapper.initWorksInfo(workId);
            }
            work.setAuthor(authors);
            work.setViewCount(worksInfo.getViewCount());
            works.add(work);
        }
        return new GetRelatedWorksRsp(works);
    }

    @Override
    public HotPaperRsp getHotWorks() {
        try {
            SearchResponse<ESWork> response = elasticsearchClient.search(s -> s
                            .index(workIndex)
                            .query(q1 -> q1
                                    .functionScore(f -> f
                                            .query(q2 -> q2
                                                    .matchAll(v -> v))
                                            .functions(f1 -> f1
                                                            .fieldValueFactor(f2 -> f2
                                                                    .field("citedByCount")
                                                                    .modifier(FieldValueFactorModifier.Ln1p)
                                                                    .factor(0.1)
                                                            )
                                                    )
                                            .functions(f3 -> f3
                                                    .randomScore(r->r))
                                            .boostMode(FunctionBoostMode.Multiply)))
                            .size(5)
                    , ESWork.class
            );
            List<Hit<ESWork>> hits = response.hits().hits();
            List<ESWork> works = new ArrayList<>();
            for(Hit<ESWork> hit: hits){
                ESWork work = hit.source();
                WorksInfo info = workMapper.getWorksInfo(work.getId());
                if(info == null)
                {
                    work.setViewCount(0);
                    workMapper.initWorksInfo(work.getId());
                }else
                    work.setViewCount(info.getViewCount());
                works.add(work);
            }
            return new HotPaperRsp(works);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void getBoolQueryBuilder(List<PaperSearchReq.Conditions> conditions, List<Query> must, List<Query> should, List<Query> must_not) {
        for (PaperSearchReq.Conditions condition : conditions) {
            List<Query> queries = new ArrayList<>();
            switch (condition.getType()) {
                case 0 -> {
                    queries.add(getMatchQuery("title", condition.getText()));
                    queries.add(getMatchQuery("abstractText", condition.getText()));
                    queries.add(getMatchQuery("authors.displayName", condition.getText()));
                }
                case 1 -> queries.add(getMatchQuery("title", condition.getText()));
                case 2 -> queries.add(getMatchQuery("abstractText", condition.getText()));
                case 3 -> queries.add(getMatchQuery("authors.displayName", condition.getText()));
            }
            switch (condition.getRelation()) {
                case 0 -> {
                    if(condition.getType() == 0)
                        should.addAll(queries);
                    else
                        must.addAll(queries);
                }
                case 1 -> should.addAll(queries);
                case 2 -> must_not.addAll(queries);
            }
        }
    }

    private Query getMatchQuery(String field, String value) {
        return MatchQuery.of(m -> m
                .field(field)
                .query(value)
                .analyzer("ik_max_word")
        )._toQuery();
    }


}
