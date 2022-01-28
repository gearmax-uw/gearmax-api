package com.uw.gearmax.gearmaxapi.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "major_option")
public class Option implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "option_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "option", nullable = false)
    private String name;

//    @JsonIgnore
//    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<CarOption> carOptions;
}
