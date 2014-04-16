package ch.jtaf.entity;

public class AthleteTO {

    private final Long id;
    private final String lastName;
    private final String firstName;
    private final int yearOfBirth;
    private final String gender;
    private final String category;
    private final String club;

    public AthleteTO(Long id, String lastName, String firstName, int yearOfBirth, String gender, String category, String club) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.yearOfBirth = yearOfBirth;
        this.gender = gender;
        this.category = category;
        this.club = club;
    }

    public Long getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getYearOfBirth() {
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

}
