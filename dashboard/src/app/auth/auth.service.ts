import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { User } from './user';

@Injectable({
    providedIn: 'root',
})
export class AuthService {

  /**
   * 
   * @returns true if the user is logged in, false otherwise
   */

  isLoggedIn() {
    return localStorage.getItem('username') !== null;
  }

  constructor(
    private router: Router
  ) {}

  /**
   * Logs in the user
   * @param user the user to log in
   */

  login(user: User) {
    if (user.userName !== '' && user.password !== '' ) {
        localStorage.setItem('username', user.userName);
        if(user.userName === 'dispatcher'){
            localStorage.setItem('userrole', 'dispatcher');
            this.router.navigate(['/dispatcher']);
        }
        else{
            localStorage.setItem('userrole', 'calltaker');
            this.router.navigate(['/calltaker']);
        }
    }
  }
  /**
   * Logs out the user
   */

  logout() {
    localStorage.removeItem('username');
    localStorage.removeItem('userrole');
    this.router.navigate(['/login']);
  }
}