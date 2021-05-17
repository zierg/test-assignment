package com.itechart.assignment.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class Employee {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Integer id;

    @Column(name = "first_name")
    @NotBlank
    String firstName;

    @Column(name = "last_name")
    @NotBlank
    String lastName;

    @Column(name = "middle_name")
    String middleName;

    @Column(name = "passport")
    @NotBlank
    String passport;

    @Column(name = "contact_information")
    String contactInformation;

    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    Date birthday;

    @Column(name = "state")
    @NotNull
    EmployeeState state = EmployeeState.ADDED;
}
