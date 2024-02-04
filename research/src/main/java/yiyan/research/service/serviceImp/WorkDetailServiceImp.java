package yiyan.research.service.serviceImp;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import yiyan.research.mapper.WorkDetailMapper;
import yiyan.research.model.domain.Comment;
import yiyan.research.model.domain.WorksInfo;
import yiyan.research.model.domain.openalex.*;
import yiyan.research.model.producer.Producer;
import yiyan.research.service.WorkDetailService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Service
public class WorkDetailServiceImp extends WorkDetailService {
    @Resource
    private WorkDetailMapper workDetailMapper;
    @Resource
    private Producer producer;
    @Override
    public Map<String, Object> getInfo(String workId,String token) throws MalformedURLException {
        Works works=workDetailMapper.getInfo(workId);
        Map<String, Object>a=new HashMap<>();
        a.put("title",works.getTitle());
        WorksAuthorships[] worksAuthorships=workDetailMapper.getWorksAuthorships(workId);
        List<Map<String ,String>>author=new ArrayList<>();
        for(WorksAuthorships w:worksAuthorships){
            String authorId=w.getAuthorId();
            String name=workDetailMapper.getAuthorName(authorId);
            AuthorsStats authorsStats=workDetailMapper.getAuthorsStats(authorId);
            Authors authors=workDetailMapper.getAuthors(authorId);
            Map<String ,String> aa=new HashMap<>();
            aa.put("id",authorId);
            aa.put("name",name);
            aa.put("h_index",String.valueOf(authorsStats.getHIndex()));
            aa.put("papers", String.valueOf(workDetailMapper.getPaper(authorId).length));
            aa.put("citations", String.valueOf(authors.getCitedByCount()));
            aa.put("organization",authors.getDisplayInstitution());
            aa.put("level",authors.getProfessionalTitle());
            author.add(aa);
        }
        a.put("authors",author);
        a.put("citations",works.getCitedByCount());
        WorksInfo info=workDetailMapper.getWorkInfo(workId);
        a.put("views",info!=null?info.getViewCount():0);
        a.put("abstract",works.getAbstracts());

//        RestTemplate restTemplate = new RestTemplate();
//        String translation = restTemplate.getForObject(
//                "https://fanyi-api.baidu.com/api/trans/vip/translate?q="+works.getAbstracts()+"&from=en&to=es&appid="+"20230903001803311"+"&salt=123&sign=YOUR_SIGN",
//                String.class
//        );
//        a.put("translation",translation);
        WorksPrimaryLocations worksPrimaryLocations=workDetailMapper.getWorksPrimaryLocations(workId);
        a.put("previewUrl",worksPrimaryLocations.getPdfUrl());

        a.put("attachment",info!=null?info.getAttachmentUrl():0);
        a.put("openComment",info!=null?info.isCommentAllowed():0);
        WorksLocation[] locations = workDetailMapper.getWorksLocation(workId);
        List<Map<String, String>> originals = new ArrayList<>();
        for (WorksLocation location : locations) {
            Map<String, String> l = new TreeMap<>();
            String link = location.getLandingPageUrl();
            URL url = new URL(link);
            String domain = url.getHost();
            l.put("source", domain);
            l.put("link", link);
            originals.add(l);
        }
        a.put("originals",originals);
        //id 标题 所有作者 被引用数 是否收藏
        List<Map<String, Object>>references=new ArrayList<>();
        WorksReferencedWorks [] worksReferencedWorks=workDetailMapper.getWorksReferencedWorks(workId);
        for(WorksReferencedWorks w:worksReferencedWorks){
            Map<String,Object>m=new HashMap<>();
            m.put("work_id",w.getWorkId());
            info=workDetailMapper.getWorkInfo(w.getWorkId());
            m.put("views",info!=null?info.getViewCount():0);
            works=workDetailMapper.getInfo(workId);
            m.put("work_name",works.getTitle());

            worksAuthorships=workDetailMapper.getWorksAuthorships(workId);
            author=new ArrayList<>();
            for(WorksAuthorships w1 : worksAuthorships){
                String authorId=w1.getAuthorId();
                String name=workDetailMapper.getAuthorName(authorId);
                Map<String ,String> aa=new HashMap<>();
                aa.put("id",authorId);
                aa.put("name",name);
                Authors aaa=workDetailMapper.getAuthors(authorId);
                aa.put("avatar", aaa.getAvatar());
                author.add(aa);
            }
            m.put("authors",author);
            m.put("citations",works.getCitedByCount());


            references.add(m);
            boolean isFavorite=false;
            if(token!=null){
                Map<String, Object> o=producer.syncSend(new HashMap<>(){{
                    put("workId",workId);
                    put("token",token);
                    put("type",4);
                }});
                isFavorite=(boolean) o.get("isFavorite");
            }

            m.put("isFavorite",isFavorite);
        }
        a.put("references",references);

        return a;
    }

    @Override
    public boolean comment(int userId, String content, int parentId, String workId) {
        try {
            WorksInfo worksInfo = workDetailMapper.getWorkInfo(workId);
            if(worksInfo ==null){
                workDetailMapper.insertWorks_info(workId);
            }
            else if(!worksInfo.isCommentAllowed()){
                return false;
            }
            Comment comment=new Comment();
            Date createdAt=new Date();
            comment.setUserId(userId);
            comment.setContent(content);
            comment.setParentId(parentId);
            comment.setWorkId(workId);
            comment.setCreatedAt(createdAt);
            workDetailMapper.insertComment(comment);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteComment(int userId, int commentId,boolean isAdmin) {
        Comment comment=workDetailMapper.getComment(commentId);
        if(comment==null)
            return false;
        if(comment.getUserId()==userId ||isAdmin){
            workDetailMapper.deleteComment(commentId);
            return true;
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> getAllComment(String workId) {
        WorksInfo worksInfo = workDetailMapper.getWorkInfo(workId);
        if(worksInfo ==null){
            workDetailMapper.insertWorks_info(workId);
        }
        else if(!worksInfo.isCommentAllowed()){
            return null;
        }
        Comment[] comments=workDetailMapper.getAllComment(workId);
        List<Map<String, Object>>map=new ArrayList<>();
        for(Comment comment : comments){
            Map<String, Object> c=comment.toDict();
            c.put("reply",workDetailMapper.getAllReply(workId,comment.getId()));
            map.add(c);
        }
        return map;
    }

    public boolean updateCommentPermission(String workId,String authorId){//userId
        WorksAuthorships authorship=workDetailMapper.getAuthorships(workId,authorId);
        if(authorship==null)
            return false;
        WorksInfo worksInfo = workDetailMapper.getWorkInfo(workId);
        if(worksInfo ==null){
            workDetailMapper.insertWorks_info(workId);
            workDetailMapper.updateCommentPermission(workId,false);
        }
        else
            workDetailMapper.updateCommentPermission(workId,!worksInfo.isCommentAllowed());
        return true;
    }

    @Override
    public boolean getCommentPermission(String workId) {
        WorksInfo worksInfo = workDetailMapper.getWorkInfo(workId);
        if(worksInfo ==null){
            workDetailMapper.insertWorks_info(workId);
            return true;
        }
        return worksInfo.isCommentAllowed();
    }

    @Override
    public void updateAttachment(String workId, String url) {
        workDetailMapper.updateAttachment(url,workId);
    }

    public Map<String, Object> getCite(String workId){
        Map<String, Object> map=new HashMap<>();
        String gb="",chicago="",apa="",mla="";
        /*
        作者；
        标题；
        出版年份；
        出版社
         */
        WorksAuthorships[] authorships= workDetailMapper.getWorksAuthorships(workId);
        String[] authorName=new String[authorships.length];
        for(int i=0;i<authorships.length;i++){
            authorName[i]=workDetailMapper.getAuthor(authorships[i].getAuthorId()).getDisplayName();
        }
//
//        String[] authorName=workDetailMapper.joinName(workId);
        Works works=workDetailMapper.getInfo(workId);
        String title=works.getTitle();
        WorksPrimaryLocations primaryLocations=workDetailMapper.getWorksPrimaryLocations(workId);
        int publishYear=works.getPublicationYear();
        String publishDate=works.getPublicationDate();

        Sources sources=workDetailMapper.getSources(workId);
        String publisher="";
        if(sources!=null)
            publisher=sources.getDisplayName();

//        gb= String.join(", ", authorName)+ ". " + title + "[M]. " + publisher + ", " + publishYear + ".";
        gb = String.join(", ", authorName) + ". " + title + ". [" + publisher + "], " + publishYear + ".";
        map.put("GB",gb);

        chicago= String.join(", ", authorName)+ ". " + title + ". " + publisher + ", " + publishYear + ".";
        map.put("Chicago",chicago);

//        apa=String.join(", ", authorName)+" (" + publishYear + "). " + title + ". " + publisher + ".";
        apa = String.join(", ", authorName) + " (" + publishYear + "). " + title + ". " + publisher + ".";
        map.put("APA",apa);

//        mla= String.join(", ", authorName)+". " + title + ". " + publisher + ", " + publishYear + ". " + publishDate + ".";
        mla = String.join(", ", authorName) + ". " + title + ". " + publisher + ", " + publishYear + ". " + publishDate + ".";
        map.put("MLA",mla);

        return map;
    }

    @Override
    public boolean getPermission(String workId, String authorId) {
        return workDetailMapper.getWorksAuthorshipsByAuthorId(workId,authorId)==null ;
    }


}
