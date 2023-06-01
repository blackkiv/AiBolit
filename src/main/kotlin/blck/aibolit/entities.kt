package blck.aibolit

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class CreatedDateAuditedEntity(
    @CreatedDate
    @Column(updatable = false)
    var createdAt: LocalDateTime? = null
)

@Entity
class Patient(
    @Id @GeneratedValue var id: Long? = null,
    @Column(nullable = false) var firstname: String,
    @Column(nullable = false) var lastname: String,
    @Column(nullable = false) var age: Int,
    @Column(nullable = false) var gender: Boolean,
    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL]
    ) var medicalHistory: List<MedicalHistory> = emptyList()
) : CreatedDateAuditedEntity()

@Entity
class MedicalHistory(
    @Id @GeneratedValue var id: Long? = null,
    @Column(nullable = false) var data: String
) : CreatedDateAuditedEntity()

@Entity
class Doctor(
    @Id @GeneratedValue var id: Long? = null,
    @Column(nullable = false) var firstname: String,
    @Column(nullable = false) var lastname: String,
    @Column(nullable = false) var age: Int,
    @Column(nullable = false) var gender: Boolean
) : CreatedDateAuditedEntity()

@Entity
class Appointment(
    @Id @GeneratedValue var id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY) var doctor: Doctor,
    @ManyToOne(fetch = FetchType.LAZY) var patient: Patient,
    @Column(nullable = false, updatable = false) var appointmentDate: LocalDateTime,
    var archived: Boolean = false
) : CreatedDateAuditedEntity()
