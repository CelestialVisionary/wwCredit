package com.wwfinance.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer userType;
    private String mobile;
    private String code;
    private String password;
    private String passwordto;
}
