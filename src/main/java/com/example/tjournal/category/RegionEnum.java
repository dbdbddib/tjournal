package com.example.tjournal.category;

import lombok.Getter;

// 상수 값은 enum의 인스턴스이자, 하나의 객체로 간주
// 상수 하나하나가 객체이므로 객체 생성시 생성자 호출  RegionEnum(String englishName, String koreanName)
// this는 생성한 현 객체의 필드값
@Getter
public enum RegionEnum {
    SEOUL("seoul", "서울", 37.5665, 126.9780),
    BUSAN("busan", "부산", 35.1796, 129.0756),
    DAEGU("daegu", "대구", 35.8714, 128.6014),
    INCHEON("incheon", "인천", 37.4563, 126.7052),
    GWANGJU("gwangju", "광주", 35.1595, 126.8526),
    DAEJEON("daejeon", "대전", 36.3504, 127.3845),
    ULSAN("ulsan", "울산", 35.5384, 129.3114),
    SEJONG("sejong", "세종", 36.4800, 127.2890),
    GYEONGGI("gyeonggi", "경기", 37.4138, 127.5183),
    GANGWON("gangwon", "강원", 37.7519, 128.8764),
    CHUNGBUK("chungbuk", "충청북도", 36.6357, 127.4914),
    CHUNGNAM("chungnam", "충청남도", 36.5184, 126.8000),
    JEONBUK("jeonbuk", "전북", 35.7175, 127.1441),
    JEONNAM("jeonnam", "전라남도", 34.8161, 126.4629),
    GYEONGBUK("gyeongbuk", "경상북도", 36.4919, 128.8889),
    GYEONGNAM("gyeongnam", "경상남도", 35.2384, 128.6922),
    JEJU("jeju", "제주", 33.4996, 126.5312),
    ALL("all", "전국", 37.3595704, 127.105399);

    private final String englishName;
    private final String koreanName;
    private final double centerLat;
    private final double centerLng;

    RegionEnum(String englishName, String koreanName, double centerLat, double centerLng) {
        this.englishName = englishName;
        this.koreanName = koreanName;
        this.centerLat = centerLat;
        this.centerLng = centerLng;
    }
//
//    public static String fromEnglishName(String englishName) {
//        for (RegionEnum region : RegionEnum.values()) {
//            if (region.getEnglishName().equalsIgnoreCase(englishName)) {
//                return String.valueOf(region);
//            }
//        }
//        throw new IllegalArgumentException("해당 영어 이름을 가진 지역이 없습니다: " + englishName);
//    }
}
