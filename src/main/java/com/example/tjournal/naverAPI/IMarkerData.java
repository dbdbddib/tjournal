package com.example.tjournal.naverAPI;

public interface IMarkerData {

    double getLat();
    void setLat(double lat);

    double getLng();
    void setLng(double lng);

    String getContent();
    void setContent(String content);

    Long getBoardId();
    void setBoardId(Long boardId);

    default void copyFields(IMarkerData from) {
        if (from == null) {
            return;
        }
        // 기본형은 null 체크 없이 바로 복사
        this.setLat(from.getLat());
        this.setLng(from.getLng());
        if (from.getContent() != null && !from.getContent().isEmpty()) {
            this.setContent(from.getContent());
        }
        if (from.getBoardId() != null) {
            this.setBoardId(from.getBoardId());
        }
    }
}