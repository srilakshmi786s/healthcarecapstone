import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpService } from '../../services/http.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  itemForm: FormGroup;
  formModel: any = {};
  showError: boolean = false;
  errorMessage: any;

  constructor(
    public router: Router,
    public httpService: HttpService,
    private formBuilder: FormBuilder,
    private authService: AuthService
  ) {
    this.itemForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  ngOnInit(): void {}

  onLogin() {
    if (this.itemForm.valid) {
      this.httpService.Login(this.itemForm.value).subscribe(
        (response: any) => {
          this.authService.saveToken(response.token);
          this.authService.SetRole(response.role);
          this.authService.saveUserId(response.userId);
          this.router.navigate(['/dashboard']);
          setTimeout(() => {
            window.location.reload();
          }, 500);
        },
        (error: any) => {
          this.showError = true;
          this.errorMessage = error.error?.message || 'Login failed. Please check your credentials.';
        }
      );
    }
  }

  registration() {
    this.router.navigate(['/registration']);
  }
}
