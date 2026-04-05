import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { HttpService } from '../../services/http.service';

@Component({
  selector: 'app-receptionist-schedule-appointments',
  templateUrl: './receptionist-schedule-appointments.component.html',
  styleUrls: ['./receptionist-schedule-appointments.component.scss'],
  providers: [DatePipe]
})
export class ReceptionistScheduleAppointmentsComponent implements OnInit {
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

  ngOnInit(): void {}

  onSubmit() {
    if (this.itemForm.valid) {
      const formValue = { ...this.itemForm.value };
      formValue.appointmentTime = this.datePipe.transform(formValue.appointmentTime, 'yyyy-MM-dd HH:mm:ss');

      this.httpService.ScheduleAppointmentByReceptionist(formValue).subscribe(
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
