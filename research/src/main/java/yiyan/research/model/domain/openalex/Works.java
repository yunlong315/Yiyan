package yiyan.research.model.domain.openalex;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
public class Works {
    private String id;
    private String doi;
    private String title;
    private String displayName;
    private Integer publicationYear;
    private String publicationDate;
    private String type;
    private Integer citedByCount;
    private Boolean isRetracted;
    private Boolean isParatext;
    private String citedByApiUrl;
    private String abstractInvertedIndex;
    private String abstracts=null;
    public String generateAbstract(){
        String abstracts = abstractInvertedIndex;
        if(abstractInvertedIndex!=null){
            abstractInvertedIndex = abstractInvertedIndex.substring(1, abstractInvertedIndex.length() - 1);
            String[] entries = abstractInvertedIndex.split("], ");
            if(entries.length > 1){
                Map<Integer, String> a = new HashMap<>();
                Arrays.stream(entries)
                        .map(entry -> entry.split(": \\["))
                        .forEach(parts -> {
                            if(parts.length>1){
                                String key = parts[0].trim().substring(1, parts[0].length() - 1);
                                String[] positions = parts[1].replace("]", "").split("\\s*,\\s*");
                                Arrays.stream(positions)
                                        .forEach(position -> a.put(Integer.parseInt(position.trim()), key));
                            }
                        });
                List<Map.Entry<Integer, String>> list = new ArrayList<>(a.entrySet());
                list.sort(Comparator.comparingInt(Map.Entry::getKey));
                abstracts = list.stream()
                        .map(Map.Entry::getValue)
                        .collect(Collectors.joining(" "));
            }
        }
        return abstracts;
    }

    public  String getAbstracts(){
        if(abstracts==null)
            abstracts=generateAbstract();
        return abstracts;
    }

}