package ch.jtaf.to;

import java.math.BigInteger;

public class AthleteTO {

    private final BigInteger id;
    private final String lastName;
    private final String firstName;
    private final Integer yearOfBirth;
    private final String gender;
    private final String category;
    private final String club;
    private final BigInteger numberOfResults;

    public AthleteTO(BigInteger id, String lastName, String firstName, Integer yearOfBirth, 
            String gender, String category, String club, BigInteger numberOfResults) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.yearOfBirth = yearOfBirth;
        this.gender = gender;
        this.category = category;
        this.club = club;
        this.numberOfResults = numberOfResults;
    }

    public BigInteger getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public Integer getYearOfBirth() {
        return yearOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public String getCategory() {
        return category;
    }

    public String getClub() {
        return club;
    }

    public BigInteger getNumberOfResults() {
        return numberOfResults;
    }
}
