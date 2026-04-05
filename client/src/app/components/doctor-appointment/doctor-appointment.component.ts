import { Component, OnInit } from '@angular/core';
import { HttpService } from '../../services/http.service';

@Component({
  selector: 'app-doctor-appointment',
  templateUrl: './doctor-appointment.component.html',
  styleUrls: ['./doctor-appointment.component.scss']
})
export class DoctorAppointmentComponent implements OnInit {
  appointmentList: any[] = [];

  constructor(public httpService: HttpService) { }

  ngOnInit(): void {
    this.getAppointments();
  }

  getAppointments() {
    const userIdStr = localStorage.getItem('userId');
    if (userIdStr) {
      const userId = parseInt(userIdStr, 10);
      this.httpService.getAppointmentByDoctor(userId).subscribe(
        (data: any) => {
          this.appointmentList = data;
        },
        (error: any) => {
          console.error('Error fetching doctor appointments:', error);
        }
      );
    }
  }
}
