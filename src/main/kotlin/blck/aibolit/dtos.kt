package blck.aibolit

import java.time.LocalDateTime

class AppointmentDto(
    var patientId: Long? = null,
    var appointmentDate: LocalDateTime? = null
)

class PatientShortInfo(
    val id: Long,
    val name: String
)

class PatientShortInfoWithAppointments(
    val id: Long,
    val name: String,
    val appointments: Int
)

class PatientDto(
    var firstname: String? = null,
    var lastname: String? = null,
    var age: Int? = null,
    var gender: Boolean? = null,
)

class MedicalHistoryDto(
    var content: String? = null
)
