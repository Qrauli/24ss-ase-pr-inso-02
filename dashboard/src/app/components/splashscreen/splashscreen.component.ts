import {Component, inject} from '@angular/core';
import {MatButton, MatFabButton} from "@angular/material/button";
import {MatCard, MatCardContent} from "@angular/material/card";
import {Router} from "@angular/router";
import {AsyncPipe, NgIf} from "@angular/common";
import {AuthService} from "../../services/auth/auth.service";

@Component({
  selector: 'app-splashscreen',
  standalone: true,
  imports: [
    MatButton,
    MatCard,
    MatCardContent,
    NgIf,
    MatFabButton,
    AsyncPipe
  ],
  templateUrl: './splashscreen.component.html',
  styleUrl: './splashscreen.component.css'
})
export class SplashscreenComponent {
  authService: AuthService = inject(AuthService);

  isLoggedIn = this.authService.isLoggedIn();

  constructor(
    private router: Router
  ) {
    this.authService.loginOauth();
    setTimeout(() => {
      this.authService.hasRole('dispatcher').then(r => {
        if (r) this.router.navigate(['/dispatcher']);
      });
      this.authService.hasRole('calltaker').then(r => {
        if (r) this.router.navigate(['/calltaker']);
      });
    }, 50);
  }

  /**
   * Trigger the login by redirecting to the identity provider page
   */

  triggerLogin() { this.authService.triggerLogin() };

}
