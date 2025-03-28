package com.example.tjournal.naverAPI;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MarkerDataDto implements IMarkerData {
    private Long id;
    private double lat;
    private double lng;
    private String content;
    private Long boardId;
}
