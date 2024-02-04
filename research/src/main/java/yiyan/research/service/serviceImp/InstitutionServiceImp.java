package yiyan.research.service.serviceImp;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import yiyan.research.mapper.InstitutionMapper;
import yiyan.research.model.domain.openalex.Institutions;
import yiyan.research.model.domain.openalex.InstitutionsCountsByYear;
import yiyan.research.model.domain.openalex.InstitutionsGeo;
import yiyan.research.model.domain.openalex.InstitutionsStats;
import yiyan.research.model.entity.AuthorInInstitution;
import yiyan.research.model.entity.CoIns;
import yiyan.research.service.InstitutionService;

import java.util.List;

@Service
public class InstitutionServiceImp implements InstitutionService {
    @Resource
    private InstitutionMapper institutionMapper;
    @Override
    public Institutions getInsById(String id) {
        return institutionMapper.getInstitutionById(id);
    }
    @Override
    public InstitutionsGeo getInsGeoById(String id) {
        return institutionMapper.getInstitutionGeoById(id);
    }

    @Override
    public List<InstitutionsCountsByYear> getInsCountByYear(String id) {
        return institutionMapper.getInsCountByYear(id);
    }

    @Override
    public List<CoIns> getAuthorCoInsList(String authorId){
        return institutionMapper.getAuthorCoInsList(authorId);
    }

    @Override
    public List<AuthorInInstitution> getInstMembers(String id) {
        return institutionMapper.getInstMembers(id);
    }
    @Override
    public InstitutionsStats getInsStatsById(String insId){
        return institutionMapper.getInsStatsById(insId);
    }

    @Override
    public int getCoInsCountById(String insId){
        return institutionMapper.getCoInsCountById(insId);
    }
    @Override
    public List<CoIns> getInsCoInsList(String insId){
        return institutionMapper.getInsCoInsList(insId);
    }
}
