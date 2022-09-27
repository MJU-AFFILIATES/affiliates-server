package com.example.affiliates.jwt.DTO;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDTO {
    @ApiModelProperty(value = "grantType", example = "bearer")
    private String grantType;
    @ApiModelProperty(value = "accessToken", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI2MDIwMDAwMCIsImF1dGgiOiJST0xFX1VTRVIiLCJleHAiOjE2NjQyNTIzODJ9.0l_QTS7W0LEcmt3mCcHTHDTXvgDyEqP9yMjneY4Ai1tbDTKvYTbX-cpbfBTGgpqLg72pkTGIYrq40Zm8QCUmfQ")
    private String accessToken;
    @ApiModelProperty(value = "refreshToken", example = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NjQ4NTUzODJ9.yvexjm2x0XilfJ-aw8xoOX6-pveeiP_iFsyLOe20U0xOszkRRn_X2nUyw4kGeEThfAFNqg1wtiO1xyY-BAjsEg")
    private String refreshToken;
    @ApiModelProperty(value = "accessTokenExpiresIn", example = "1664252382875")
    private Long accessTokenExpiresIn;

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }
}