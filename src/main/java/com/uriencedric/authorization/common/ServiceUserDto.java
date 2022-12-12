package com.uriencedric.authorization.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ServiceUserDto {
    private Long id;
    private String name;
    private int refreshTokenValidity;
    private int tokenValidity;
    private String secret;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Create {

        @NotNull(message = "Value cannot be null")
        private String name;

        @NotNull(message = "Value cannot be null")
        private int refreshTokenValidity;

        @NotNull(message = "Value cannot be null")
        private int tokenValidity;

        @NotNull(message = "Value cannot be null")
        private String secret;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Update {
        @NotNull(message = "Value cannot be null")
        private int refreshTokenValidity;
        @NotNull(message = "Value cannot be null")
        private int tokenValidity;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UpdateSecret {
        @NotNull(message = "Value cannot be null")
        private String secret;
    }


}
