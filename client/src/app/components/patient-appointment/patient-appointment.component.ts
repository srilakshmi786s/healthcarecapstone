import { Component, OnInit } from '@angular/core';
import { HttpService } from '../../services/http.service';

@Component({
  selector: 'app-patient-appointment',
  templateUrl: './patient-appointment.component.html',
  styleUrls: ['./patient-appointment.component.scss']
})
export class PatientAppointmentComponent implements OnInit {
  appointmentList: any[] = [];

  constructor(public httpService: HttpService) { }

  ngOnInit(): void {
    this.getAppointments();
  }

  getAppointments() {
    const userIdStr = localStorage.getItem('userId');
    if (userIdStr) {
      const userId = parseInt(userIdStr, 10);
      this.httpService.getAppointmentByPatient(userId).subscribe(
        (data: any) => {
          this.appointmentList = data;
        },
        (error: any) => {
          console.error('Error fetching patient appointments:', error);
        }
      );
    }
  }
}
