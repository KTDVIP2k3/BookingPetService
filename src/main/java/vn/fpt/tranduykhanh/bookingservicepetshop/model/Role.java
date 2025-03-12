package vn.fpt.tranduykhanh.bookingservicepetshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "userList"})
public class Role extends BaseEntity{
    @Enumerated(EnumType.STRING)
    private RoleEnum roleName;

    public RoleEnum getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleEnum roleName) {
        this.roleName = roleName;
    }

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
//    @JsonIgnore // Ngăn vòng lặp khi serialize JSON
    private List<User> userList;
}
