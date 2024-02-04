package yiyan.research.service.serviceImp;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import yiyan.research.mapper.AuthorMapper;
import yiyan.research.mapper.InstitutionMapper;
import yiyan.research.model.domain.openalex.Authors;
import yiyan.research.model.domain.openalex.AuthorsCountsByYear;
import yiyan.research.model.domain.openalex.AuthorsStats;
import yiyan.research.model.entity.CoAuthor;
import yiyan.research.service.AuthorService;

import java.util.List;

@Service
public class AuthorServiceImp implements AuthorService {
    @Resource
    private AuthorMapper authorMapper;
    @Resource
    private InstitutionMapper institutionMapper;


    @Override
    public Authors getAuthorById(String authorId) {
        return authorMapper.getAuthorById(authorId);
    }

    @Override
    public int auth(String authorId, String userId){
        return authorMapper.auth(authorId,userId);
    }
    @Override
    public List<AuthorsCountsByYear> getAuthorsCountsByYearByAuthorId(String id) {
        return authorMapper.getAuthorsCountsByYearByAuthorId(id);
    }

    @Override
    public int updateAuthor(Authors author) {

        return authorMapper.updateAuthor(author);
    }
    @Override
    public AuthorsStats getAuthorsStatsByAuthorId(String authorId){
        return authorMapper.getAuthorsStatsByAuthorId(authorId);
    }
    @Override
    public List<CoAuthor> getCoAuthorList(String authorId){
        List<CoAuthor> coAuthorList =  authorMapper.getCoAuthorList(authorId);
       /* for(CoAuthor coAuthor: coAuthorList){
            Authors authors = authorMapper.getAuthorById(coAuthor.getAuthorId());
            if(authors.getDisplayInstitution()!=null&&authors.getDisplayInstitution()!=""){
                coAuthor.setInstitutionName(authors.getDisplayInstitution());
            }else{
                Institutions institutions = institutionMapper.getInstitutionById(authors.getLastKnownInstitution());
                coAuthor.setInstitutionName(institutions.getDisplayName());
                coAuthor.setInstitutionId(authors.getLastKnownInstitution());
            }
        }*/
        return coAuthorList;
    }
    @Override
    public void addAuthorWorkCount(String authorId){
        authorMapper.addAuthorWorkCount(authorId);
    }
    @Override
    public void reduceWorksCount(String authorId){
        authorMapper.reduceWorksCount(authorId);
    }
    @Override
    public Authors getAuthorByUserId(String userId){
        return authorMapper.getAuthorByUserId(userId);
    }
}
