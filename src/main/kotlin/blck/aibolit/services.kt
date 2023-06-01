package blck.aibolit

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

interface AppointmentService {

    fun appointmentsForToday(userId: Long): Long

    fun appointments(userId: Long): List<Appointment>

    fun createAppointment(appointmentDto: AppointmentDto)

    fun archiveOldAppointments(userId: Long)

    @Service
    class Base(
        private val appointmentRepository: AppointmentRepository,
        private val doctorRepository: DoctorRepository,
        private val patientRepository: PatientRepository
    ) : AppointmentService {

        @Transactional(readOnly = true)
        override fun appointmentsForToday(userId: Long): Long =
            appointmentRepository.countAppointmentByDoctorIdAndAppointmentDateBetweenAndArchivedIsFalse(
                userId,
                LocalDate.now().atStartOfDay(),
                LocalDate.now().plusDays(1).atStartOfDay()
            )

        @Transactional(readOnly = true)
        override fun appointments(userId: Long): List<Appointment> =
            appointmentRepository.findAllByDoctorIdAndArchived(userId, false)

        @Transactional
        override fun createAppointment(appointmentDto: AppointmentDto) {
            appointmentRepository.save(
                Appointment(
                    doctor = doctorRepository.getReferenceById(1L),
                    patient = patientRepository.getReferenceById(appointmentDto.patientId!!),
                    appointmentDate = appointmentDto.appointmentDate!!
                )
            )
        }

        @Transactional(readOnly = true)
        override fun archiveOldAppointments(userId: Long) {
            val now = LocalDateTime.now()
            val appointments = appointmentRepository.findAllByDoctorIdAndArchived(userId, false)
            if (appointments.isEmpty()) {
                return
            }
            appointments
                .filter { it.appointmentDate.isBefore(now) }
                .forEach { it.archived = true }
            appointmentRepository.saveAll(appointments)
        }
    }
}

interface PatientService {

    fun getPatientsShortInfo(): List<PatientShortInfo>

    fun getPatients(): List<PatientShortInfoWithAppointments>

    fun getPatient(id: Long): Patient

    fun createPatient(patient: PatientDto)

    fun updateMedicalHistory(id: Long, medicalHistoryDto: MedicalHistoryDto)

    @Service
    class Base(private val patientRepository: PatientRepository) : PatientService {

        @Transactional(readOnly = true)
        override fun getPatientsShortInfo(): List<PatientShortInfo> =
            patientRepository.findAllShortInfo()
                .map { PatientShortInfo(it.getId(), it.getName()) }

        @Transactional(readOnly = true)
        override fun getPatients(): List<PatientShortInfoWithAppointments> =
            patientRepository.findAllShortInfoWithAppointments()
                .map { PatientShortInfoWithAppointments(it.getId(), it.getName(), it.getAppointments()) }

        override fun getPatient(id: Long): Patient =
            patientRepository.getReferenceById(id)

        @Transactional
        override fun createPatient(patient: PatientDto) {
            patientRepository.save(
                Patient(
                    firstname = patient.firstname!!,
                    lastname = patient.lastname!!,
                    age = patient.age!!,
                    gender = patient.gender!!
                )
            )
        }

        @Transactional
        override fun updateMedicalHistory(id: Long, medicalHistoryDto: MedicalHistoryDto) {
            val patient = patientRepository.getReferenceById(id)
            patient.medicalHistory += MedicalHistory(data = medicalHistoryDto.content!!)
            patientRepository.save(patient)
        }
    }
}
