package com.example.tjournal.category;

import lombok.Getter;

// 상수 값은 enum의 인스턴스이자, 하나의 객체로 간주
// 상수 하나하나가 객체이므로 객체 생성시 생성자 호출  RegionEnum(String englishName, String koreanName)
// this는 생성한 현 객체의 필드값
@Getter
public enum RegionEnum {
    SEOUL("seoul", "서울"),
    BUSAN("busan", "부산"),
    DAEGU("daegu", "대구"),
    INCHEON("incheon", "인천"),
    GWANGJU("gwangju", "광주"),
    DAEJEON("daejeon", "대전"),
    ULSAN("ulsan", "울산"),
    SEJONG("sejong", "세종"),
    GYEONGGI("gyeonggi", "경기"),
    GANGWON("gangwon", "강원"),
    CHUNGBUK("chungbuk", "충청북도"),
    CHUNGNAM("chungnam", "충청남도"),
    JEONBUK("jeonbuk", "전북"),
    JEONNAM("jeonnam", "전라남도"),
    GYEONGBUK("gyeongbuk", "경상북도"),
    GYEONGNAM("gyeongnam", "경상남도"),
    JEJU("jeju", "제주"),
    ALL("all", "전국");

    private final String englishName;
    private final String koreanName;

    RegionEnum(String englishName, String koreanName) {
        this.englishName = englishName;
        this.koreanName = koreanName;
    }

    public static String fromEnglishName(String englishName) {
        for (RegionEnum region : RegionEnum.values()) {
            if (region.getEnglishName().equalsIgnoreCase(englishName)) {
                return String.valueOf(region);
            }
        }
        throw new IllegalArgumentException("해당 영어 이름을 가진 지역이 없습니다: " + englishName);
    }
}
