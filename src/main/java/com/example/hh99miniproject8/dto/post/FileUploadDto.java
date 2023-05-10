package com.example.hh99miniproject8.dto.post;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUploadDto {
    private String url;
}
