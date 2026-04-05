import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  public serverName = environment.apiUrl;

  constructor(private http: HttpClient, private authService: AuthService) { }

  private getHttpOptions() {
    const token = this.authService.getToken();
    return {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : ''
      })
    };
  }

  updateDoctorAvailability(doctorId: any, availability: any): Observable<any> {
    const options = this.getHttpOptions();
    return this.http.post(`${this.serverName}/api/doctor/availability?doctorId=${doctorId}&availability=${availability}`, {}, options);
  }

  getAllAppointments(): Observable<any> {
    const options = this.getHttpOptions();
    return this.http.get(`${this.serverName}/api/receptionist/appointments`, options);
  }

  getAppointmentByDoctor(id: any): Observable<any> {
    const options = this.getHttpOptions();
    return this.http.get(`${this.serverName}/api/doctor/appointments?doctorId=${id}`, options);
  }

  getAppointmentByPatient(id: any): Observable<any> {
    const options = this.getHttpOptions();
    return this.http.get(`${this.serverName}/api/patient/appointments?patientId=${id}`, options);
  }

  ScheduleAppointment(details: any): Observable<any> {
    const options = this.getHttpOptions();
    return this.http.post(`${this.serverName}/api/patient/appointment`, details, options);
  }

  ScheduleAppointmentByReceptionist(details: any): Observable<any> {
    const options = this.getHttpOptions();
    return this.http.post(`${this.serverName}/api/receptionist/appointment`, details, options);
  }

  reScheduleAppointment(appointmentId: any, formvalue: any): Observable<any> {
    const options = this.getHttpOptions();
    return this.http.put(`${this.serverName}/api/receptionist/appointment-reschedule/${appointmentId}`, formvalue, options);
  }

  getDoctors(): Observable<any> {
    const options = this.getHttpOptions();
    return this.http.get(`${this.serverName}/api/patient/doctors`, options);
  }

  Login(details: any): Observable<any> {
    return this.http.post(`${this.serverName}/api/user/login`, details, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    });
  }

  registerPatient(details: any): Observable<any> {
    return this.http.post(`${this.serverName}/api/patient/register`, details, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    });
  }

  registerDoctors(details: any): Observable<any> {
    return this.http.post(`${this.serverName}/api/doctors/register`, details, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    });
  }

  registerReceptionist(details: any): Observable<any> {
    return this.http.post(`${this.serverName}/api/receptionist/register`, details, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    });
  }
}
