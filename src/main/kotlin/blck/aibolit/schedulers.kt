package blck.aibolit

import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class Scheduler(private val appointmentService: AppointmentService) {

    @Scheduled(fixedDelay = 3, timeUnit = TimeUnit.SECONDS)
    fun archiveOldAppointments(event: ApplicationStartedEvent) {
        appointmentService.archiveOldAppointments(1L)
    }
}