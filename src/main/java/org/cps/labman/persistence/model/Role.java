package org.cps.labman.persistence.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Table(name ="roles")
public class Role {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;
    @Column(nullable=false, unique = true)

    String name;

    @ManyToMany(mappedBy = "roles")
    List<User> users = new ArrayList<>();
}
