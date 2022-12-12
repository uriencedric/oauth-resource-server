package com.uriencedric.authorization.persistence.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@EqualsAndHashCode(exclude = { "scope" })
public class ServiceUserScope {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "service_user_id")
    ServiceUser serviceUser;
    @ManyToOne
    @JoinColumn(name = "scope_id")
    Scope scope;

    boolean isAutoApprove;
}
