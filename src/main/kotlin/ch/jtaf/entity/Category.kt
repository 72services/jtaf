package ch.jtaf.entity

import ch.jtaf.entity.Gender.MALE
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity
data class Category(
        @Id @GeneratedValue(strategy = IDENTITY)
        var id: Long? = null,

        var abbreviation: String = "",
        var name: String = "",
        var yearFrom: Int = 1900,
        var yearTo: Int = 9999,
        var gender: Gender = MALE,

        var seriesId: Long? = null
) {
    @OneToMany
    @OrderColumn(name = "position")
    @JoinTable(name = "category_event",
            joinColumns = [(JoinColumn(name = "category_id", referencedColumnName = "id"))],
            inverseJoinColumns = [(JoinColumn(name = "event_id", referencedColumnName = "id"))])
    var events: MutableList<Event> = ArrayList()

    @OneToMany
    @JoinTable(name = "category_athlete",
            joinColumns = [(JoinColumn(name = "category_id", referencedColumnName = "id"))],
            inverseJoinColumns = [(JoinColumn(name = "athlete_id", referencedColumnName = "id"))])
    var athletes: MutableList<Athlete> = ArrayList()
}