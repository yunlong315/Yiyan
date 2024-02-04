package yiyan.research.service;

import org.springframework.stereotype.Service;
import yiyan.research.model.domain.openalex.Institutions;
import yiyan.research.model.domain.openalex.InstitutionsCountsByYear;
import yiyan.research.model.domain.openalex.InstitutionsGeo;
import yiyan.research.model.domain.openalex.InstitutionsStats;
import yiyan.research.model.entity.AuthorInInstitution;
import yiyan.research.model.entity.CoIns;

import java.util.List;

@Service
public interface InstitutionService {
    Institutions getInsById(String id);
    InstitutionsGeo getInsGeoById(String id);

    List<InstitutionsCountsByYear> getInsCountByYear(String id);

    List<CoIns> getAuthorCoInsList(String authorId);

    List<AuthorInInstitution> getInstMembers(String id);

    InstitutionsStats getInsStatsById(String insId);

    int getCoInsCountById(String insId);

    List<CoIns> getInsCoInsList(String insId);
}
