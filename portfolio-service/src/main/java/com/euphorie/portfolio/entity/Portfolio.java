package com.euphorie.portfolio.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

import java.util.List;

import com.euphorie.position.entity.Position;


@Entity 
@Table(name="Portfolio")
@Getter
@Setter
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private  String name;
    @Column(nullable = false)
    private BigDecimal cashBalance;
    @OneToMany(mappedBy = "portfolio")
    private List<Position> positions;

}