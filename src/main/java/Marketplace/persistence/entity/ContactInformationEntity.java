package Marketplace.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contact_information")
public class ContactInformationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "contact_person")
    private String contact_person;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phone_number;

    @OneToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;
}
