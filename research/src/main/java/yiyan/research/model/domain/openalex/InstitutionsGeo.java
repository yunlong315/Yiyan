package yiyan.research.model.domain.openalex;

import lombok.Data;

@Data
public class InstitutionsGeo {
    private String institutionId;
    private String city;
    private String geonamesCityId;
    private String region;
    private String countryCode;
    private String country;
    private Float latitude;
    private Float longitude;
}