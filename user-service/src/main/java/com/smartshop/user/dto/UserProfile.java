package com.smartshop.user.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserProfile {
    String name;
    String email;
    String mobile;
}
