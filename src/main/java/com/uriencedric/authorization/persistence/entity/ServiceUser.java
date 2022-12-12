package com.uriencedric.authorization.persistence.entity;

import com.uriencedric.authorization.common.ServiceUserDto;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@EqualsAndHashCode()
public class ServiceUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String secret;

    @Column(columnDefinition = "tinyint(1) default 1")
    private Boolean isSecretRequired;

    private String redirectURI;

    @Column(columnDefinition = "tinyint(1) default 0")
    private Boolean isTechnicalUser;

    @Column(nullable = false)
    private int tokenValidity;

    @Column(nullable = false)
    private int refreshTokenValidity;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "service_user_grants", joinColumns = @JoinColumn(name = "service_user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "service_user_grant_id", referencedColumnName = "id"))
    private Set<Grant> grants = new HashSet<>();

    @OneToMany(mappedBy = "serviceUser", fetch = FetchType.EAGER)
    private List<ServiceUserScope> serviceUserScopes;

    public ServiceUserDto toDto() {
        return ServiceUserDto.builder()
                .id(this.id)
                .name(this.name)
                .secret(this.secret)
                .refreshTokenValidity(this.refreshTokenValidity)
                .tokenValidity(this.tokenValidity)
                .build();
    }
}
