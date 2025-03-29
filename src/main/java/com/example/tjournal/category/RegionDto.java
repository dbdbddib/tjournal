package com.example.tjournal.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegionDto {
    private String englishName;
    private String koreanName;
    private double centerLat;
    private double centerLng;



    public static RegionDto from(RegionEnum region) {
        return new RegionDto(
                region.getEnglishName(),
                region.getKoreanName(),
                region.getCenterLat(),
                region.getCenterLng()
        );
    }
}
