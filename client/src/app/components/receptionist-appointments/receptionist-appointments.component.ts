import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { HttpService } from '../../services/http.service';

@Component({
  selector: 'app-receptionist-appointments',
  templateUrl: './receptionist-appointments.component.html',
  styleUrls: ['./receptionist-appointments.component.scss'],
  providers: [DatePipe]
})
export class ReceptionistAppointmentsComponent implements OnInit {
  itemForm: FormGroup;
  formModel: any = {};
  responseMessage: any;
  appointmentList: any[] = [];
  isAdded: boolean = false;

  constructor(
    public httpService: HttpService,
    private formBuilder: FormBuilder,
    private datePipe: DatePipe
  ) {
    this.itemForm = this.formBuilder.group({
      id: ['', Validators.required],
      time: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.getAppointments();
  }

  getAppointments() {
    this.httpService.getAllAppointments().subscribe(
      (data: any) => {
        this.appointmentList = data;
      },
      (error: any) => {
        console.error('Error fetching appointments:', error);
      }
    );
  }

  editAppointment(val: any) {
    this.itemForm.get('id')?.setValue(val.id);
    this.isAdded = true;
  }

  onSubmit() {
    if (this.itemForm.valid) {
      const appointmentId = this.itemForm.value.id;
      const formattedTime = this.datePipe.transform(this.itemForm.value.time, 'yyyy-MM-dd HH:mm:ss');
      const formValue = { time: formattedTime };

      this.httpService.reScheduleAppointment(appointmentId, formValue).subscribe(
        (response: any) => {
          this.responseMessage = 'Appointment rescheduled successfully!';
          this.itemForm.reset();
          this.isAdded = false;
          this.getAppointments();
        },
        (error: any) => {
          this.responseMessage = 'Failed to reschedule appointment.';
        }
      );
    }
  }
}
