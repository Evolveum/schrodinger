/*
 * Copyright (c) 2024  Evolveum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.evolveum.midpoint.schrodinger.trainings.first.steps.util.dto;

public class HRApplicationUserDto {

    public static final String FIRST_NAME_FIELD_ID = "firstname";
    public static final String SURNAME_FIELD_ID = "surname";
    public static final String ARTNAME_FIELD_ID = "artname";
    public static final String EMPLOYEE_NUMBER_FIELD_ID = "employeeNumber";
    public static final String LOCALITY_FIELD_ID = "locality";
    public static final String COUNTRY_FIELD_ID = "country";
    public static final String JOB_FIELD_ID = "job";
    public static final String EMP_TYPE_FIELD_ID = "empType";
    public static final String STATUS_FIELD_ID = "status";

    private String firstName;
    private String surname;
    private String artname;
    private String employeeNumber;
    private String locality;
    private String country;
    private String job;
    private String empType;
    private String status;

    public HRApplicationUserDto(String firstName, String surname, String artname, String employeeNumber, String locality,
                                String country, String job, String empType, String status) {
        this.firstName = firstName;
        this.surname = surname;
        this.artname = artname;
        this.employeeNumber = employeeNumber;
        this.locality = locality;
        this.country = country;
        this.job = job;
        this.empType = empType;
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getArtname() {
        return artname;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public String getLocality() {
        return locality;
    }

    public String getCountry() {
        return country;
    }

    public String getJob() {
        return job;
    }

    public String getEmpType() {
        return empType;
    }

    public String getStatus() {
        return status;
    }
}
