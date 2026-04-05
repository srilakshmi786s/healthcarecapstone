import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";

import { AuthRoutingModule } from "./auth-routing.module";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { HttpClientModule } from "@angular/common/http";
import { RegistrationComponent } from "./components/registration/registration.component";
import { LoginComponent } from "./components/login/login.component";
import { LogoutComponent } from "./components/logout/logout.component";
import { ForgotPasswordComponent } from "./components/forgot-password/forgot-password.component";
import { ResetPasswordComponent } from "./components/reset-password/reset-password.component";
import { RouterModule } from "@angular/router";

@NgModule({
  declarations: [
    RegistrationComponent,
    LoginComponent,
    LogoutComponent,
    ForgotPasswordComponent,
    ResetPasswordComponent
  ],
  imports: [
    CommonModule,
    AuthRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule
  ],
  exports: [
    LogoutComponent
  ]
})
export class AuthModule {}