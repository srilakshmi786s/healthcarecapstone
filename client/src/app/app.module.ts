import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { DoctorAppointmentComponent } from './components/doctor-appointment/doctor-appointment.component';
import { DoctorAvailabilityComponent } from './components/doctor-availability/doctor-availability.component';
import { PatientAppointmentComponent } from './components/patient-appointment/patient-appointment.component';
import { ReceptionistAppointmentsComponent } from './components/receptionist-appointments/receptionist-appointments.component';
import { ReceptionistScheduleAppointmentsComponent } from './components/receptionist-schedule-appointments/receptionist-schedule-appointments.component';
import { ScheduleAppointmentComponent } from './components/schedule-appointment/schedule-appointment.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistrationComponent,
    DashboardComponent,
    DoctorAppointmentComponent,
    DoctorAvailabilityComponent,
    PatientAppointmentComponent,
    ReceptionistAppointmentsComponent,
    ReceptionistScheduleAppointmentsComponent,
    ScheduleAppointmentComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
