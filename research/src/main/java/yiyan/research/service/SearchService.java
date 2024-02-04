package yiyan.research.service;

import org.springframework.stereotype.Service;
import yiyan.research.model.domain.openalex.Authors;
import yiyan.research.model.esEntity.ESWork;
import yiyan.research.model.request.search.AuthorSearchReq;
import yiyan.research.model.request.search.GetRelatedWorksReq;
import yiyan.research.model.request.search.InstSearchReq;
import yiyan.research.model.request.search.PaperSearchReq;
import yiyan.research.model.response.search.*;

import java.util.List;

@Service
public interface SearchService{

    PaperSearchRsp searchWorks(PaperSearchReq request);

    InstSearchRsp searchInst(InstSearchReq request);

    AuthorSearchRsp searchAuthor(AuthorSearchReq request);

    GetRelatedWorksRsp getRelatedWorks(GetRelatedWorksReq request);

    HotPaperRsp getHotWorks();
}
