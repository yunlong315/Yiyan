package yiyan.research.service;

import org.springframework.stereotype.Service;
import yiyan.research.model.domain.User;
import yiyan.research.model.domain.openalex.Authors;
import yiyan.research.model.domain.openalex.AuthorsCountsByYear;
import yiyan.research.model.domain.openalex.AuthorsStats;
import yiyan.research.model.entity.CoAuthor;

import java.util.List;

@Service
public interface AuthorService {
    Authors getAuthorById(String authorId);

    int auth(String authorId, String userId);

    List<AuthorsCountsByYear> getAuthorsCountsByYearByAuthorId(String id);

    int updateAuthor(Authors author);

    AuthorsStats getAuthorsStatsByAuthorId(String authorId);

    List<CoAuthor> getCoAuthorList(String authorId);

    void addAuthorWorkCount(String authorId);

    void reduceWorksCount(String authorId);

    Authors getAuthorByUserId(String userId);
}
