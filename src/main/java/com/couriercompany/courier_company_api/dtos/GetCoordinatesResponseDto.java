package com.couriercompany.courier_company_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetCoordinatesResponseDto {
    private Double latitude;
    private Double longitude;
}
