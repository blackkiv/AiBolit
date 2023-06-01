package blck.aibolit

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@Controller
class HomeController(
    private val appointmentService: AppointmentService
) {

    @GetMapping("/home")
    fun index(model: Model): String {
        model.addAttribute("username", "blck")
        val appointmentsForToday = appointmentService.appointmentsForToday(1)
        model.addAttribute("appointmentsForToday", appointmentsForToday)
        return "index"
    }

    @GetMapping("/bug-reporting")
    fun bugReporting(model: Model): String {
        model.addAttribute("username", "blck")
        return "bug-reporting";
    }
}

@Controller
class AppointmentController(
    private val appointmentService: AppointmentService,
    private val patientService: PatientService
) {

    @GetMapping("/appointments")
    fun appointments(model: Model): String {
        model.addAttribute("username", "blck")
        model.addAttribute("appointments", appointmentService.appointments(1))
        model.addAttribute(
            "patientsShortInfoForm", patientService.getPatientsShortInfo()
        )
        model.addAttribute("appointmentForm", AppointmentDto())
        return "appointments"
    }

    @PostMapping("/appointment")
    fun createAppointment(
        @ModelAttribute appointment: AppointmentDto,
        bindingResult: BindingResult,
        model: Model
    ): String {
        appointmentService.createAppointment(appointment)
        return "redirect:/appointments"
    }
}

@Controller
class PatientController(
    private val patientService: PatientService
) {

    @GetMapping("/patients")
    fun patients(model: Model): String {
        model.addAttribute("username", "blck")
        model.addAttribute("patients", patientService.getPatients())
        model.addAttribute("patientForm", PatientDto())
        return "patients"
    }

    @GetMapping("/patients/{id}")
    fun patient(@PathVariable("id") id: Long, model: Model): String {
        model.addAttribute("username", "blck")
        model.addAttribute("patient", patientService.getPatient(id))
        model.addAttribute("medicalHistoryForm", MedicalHistoryDto())
        return "patient"
    }

    @PostMapping("/patient")
    fun createPatient(
        @ModelAttribute patient: PatientDto,
        bindingResult: BindingResult,
        model: Model
    ): String {
        patientService.createPatient(patient)
        return "redirect:/patients"
    }

    @PostMapping("/patients/{id}/medical-history")
    fun updateMedicalHistory(
        @PathVariable("id") id: Long,
        @ModelAttribute medicalHistoryDto: MedicalHistoryDto,
        bindingResult: BindingResult,
        model: Model
    ): String {
        patientService.updateMedicalHistory(id, medicalHistoryDto)
        return "redirect:/patients/$id"
    }
}