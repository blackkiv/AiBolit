package blck.aibolit

interface PatientProjection {

    fun getId(): Long

    fun getName(): String
}

interface PatientWithAppointmentsShortInfoProjection : PatientProjection {

    fun getAppointments(): Int
}
