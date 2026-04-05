import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { HttpService } from '../../services/http.service';

@Component({
  selector: 'app-schedule-appointment',
  templateUrl: './schedule-appointment.component.html',
  styleUrls: ['./schedule-appointment.component.scss'],
  providers: [DatePipe]
})
export class ScheduleAppointmentComponent implements OnInit {
  doctorList: any[] = [];
  itemForm: FormGroup;
  formModel: any = {};
  responseMessage: any;
  isAdded: boolean = false;

  constructor(
    public httpService: HttpService,
    private formBuilder: FormBuilder,
    private datePipe: DatePipe
  ) {
    this.itemForm = this.formBuilder.group({
      patientId: ['', Validators.required],
      doctorId: ['', Validators.required],
      appointmentTime: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.getPatients();
  }

  getPatients() {
    this.httpService.getDoctors().subscribe(
      (data: any) => {
        this.doctorList = data;
      },
      (error: any) => {
        console.error('Error fetching doctors:', error);
      }
    );
  }

  addAppointment(val: any) {
    const userIdStr = localStorage.getItem('userId');
    if (userIdStr) {
      this.itemForm.get('patientId')?.setValue(parseInt(userIdStr, 10));
      this.itemForm.get('doctorId')?.setValue(val.id);
      this.isAdded = true;
    }
  }

  onSubmit() {
    if (this.itemForm.valid) {
      const formValue = { ...this.itemForm.value };
      formValue.appointmentTime = this.datePipe.transform(formValue.appointmentTime, 'yyyy-MM-dd HH:mm:ss');

      this.httpService.ScheduleAppointment(formValue).subscribe(
        (response: any) => {
          this.responseMessage = 'Appointment scheduled successfully!';
          this.itemForm.reset();
          this.isAdded = false;
        },
        (error: any) => {
          this.responseMessage = 'Failed to schedule appointment.';
        }
      );
    }
  }
}
