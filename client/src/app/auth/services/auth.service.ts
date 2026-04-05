import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { Injectable } from "@angular/core";
import { User } from "src/app/mediconnect/models/User";
import { UserRegistrationDTO } from "src/app/mediconnect/models/UserRegistrationDTO";

@Injectable({
  providedIn: "root",
})
export class AuthService {

  // ✅ WILL RESOLVE TO http://localhost:3000
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  private httpOptions = {
    headers: new HttpHeaders({
      "Content-Type": "application/json"
    })
  };

  // ✅ LOGIN
  login(user: Partial<User>): Observable<any> {
    return this.http.post(
      `${this.baseUrl}/api/user/login`,
      user,
      this.httpOptions
    );
  }

  // ✅ REGISTER TARGETED
  registerPatient(user: UserRegistrationDTO): Observable<any> {
    return this.http.post(`${this.baseUrl}/api/patient/register`, user, this.httpOptions);
  }

  registerDoctors(user: UserRegistrationDTO): Observable<any> {
    return this.http.post(`${this.baseUrl}/api/doctors/register`, user, this.httpOptions);
  }

  registerReceptionist(user: UserRegistrationDTO): Observable<any> {
    return this.http.post(`${this.baseUrl}/api/receptionist/register`, user, this.httpOptions);
  }

  getToken(): string | null {
    return localStorage.getItem("token");
  }

  getRole(): string | null {
    return localStorage.getItem("role");
  }

  logout(): void {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("user_id");
    localStorage.removeItem("doctor_id");
    localStorage.removeItem("patient_id");
  }

  // ── Password Reset ──────────────────────────────────────────────────────────

  /** Step 1: submit email to request a reset link. Always returns 200. */
  forgotPassword(username: string): Observable<any> {
    return this.http.post(
      `${this.baseUrl}/api/auth/forgot-password`,
      { username },
      this.httpOptions
    );
  }

  /** Step 2: submit the token from the email + new password. */
  resetPassword(token: string, newPassword: string): Observable<any> {
    return this.http.post(
      `${this.baseUrl}/api/auth/reset-password`,
      { token, newPassword },
      this.httpOptions
    );
  }
}