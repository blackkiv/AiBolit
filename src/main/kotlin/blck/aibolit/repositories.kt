package blck.aibolit

import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.event.EventListener
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class Init(
    private val patientRepository: PatientRepository,
    private val doctorRepository: DoctorRepository
) {

    @EventListener
    fun init(applicationStartedEvent: ApplicationStartedEvent) {
        patientRepository.saveAll(
            listOf(
                Patient(firstname = "Viktor", lastname = "Stepanchuk", age = 19, gender = true),
                Patient(firstname = "Dmitro", lastname = "Karvatskiy", age = 20, gender = true),
            )
        )

        doctorRepository.saveAll(
            listOf(
                Doctor(id = 1, firstname = "Livio", lastname = "Agolini", age = 13, gender = true)
            )
        )
    }
}

interface PatientRepository : JpaRepository<Patient, Long> {

    @Query("select id as id, concat(firstname, ' ', lastname) as name from Patient")
    fun findAllShortInfo(): List<PatientProjection>

    @Query(
        """
        select p.id as id, 
        concat(p.firstname, ' ', p.lastname) as name, 
        count(a) as appointments 
        from Patient p left join Appointment a on a.patient = p 
        group by id, name
    """
    )
    fun findAllShortInfoWithAppointments(): List<PatientWithAppointmentsShortInfoProjection>
}

interface AppointmentRepository : JpaRepository<Appointment, Long> {

    fun countAppointmentByDoctorIdAndAppointmentDateBetweenAndArchivedIsFalse(
        doctorId: Long, dateFrom: LocalDateTime, dateTo: LocalDateTime
    ): Long

    fun findAllByDoctorIdAndArchived(
        doctorId: Long, archived: Boolean
    ): List<Appointment>
}

interface DoctorRepository : JpaRepository<Doctor, Long>
