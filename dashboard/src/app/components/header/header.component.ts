import { Component, Input } from '@angular/core';
import { MatCardModule} from '@angular/material/card'
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import {MatFormFieldModule} from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import {MatIconModule} from '@angular/material/icon';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { NgFor, NgIf } from '@angular/common';
import { ConfirmDialogComponent, ConfirmDialogModel } from '../confirm-dialog/confirm-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import {MatMenuModule} from '@angular/material/menu';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    MatCardModule,
    MatButtonModule,
    MatToolbarModule,
    MatFormFieldModule,
    FormsModule,
    MatIconModule,
    NgIf,
    NgFor,
    MatMenuModule
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {

  @Input() backButton: boolean = false;
  @Input() backRoute: string = '/';
  @Input() title: string = '';
  @Input() backCheck: boolean = true;

  private readonly LANGUAGES = ["German", "English"];


  public currentLang: string;
  constructor(private router: Router, private authService: AuthService, public dialog: MatDialog, public translate: TranslateService) { 
      this.currentLang = this.translate.currentLang;
  }

  /**
   * Logout the user
   */
  logout(): void {

    const dialogData = new ConfirmDialogModel(this.translate.instant('HEADER.LOGOUT_HEADER'), this.translate.instant('HEADER.LOGOUT_BODY'));

    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: dialogData,
      enterAnimationDuration: 0,
      exitAnimationDuration: 0
    });

    dialogRef.afterClosed().subscribe(dialogResult => {
      if(dialogResult === true) {
        this.authService.logout();
      }
    });

    //this.authService.logout();
  }

  /**
   * Navigate back to the previous page
   */
  goBack(): void {
    if(this.backCheck === true) {
    const dialogData = new ConfirmDialogModel(this.translate.instant('HEADER.BACK_HEADER'), this.translate.instant('HEADER.BACK_BODY'));
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: dialogData,
      enterAnimationDuration: 0,
      exitAnimationDuration: 0
    });

    dialogRef.afterClosed().subscribe(dialogResult => {
      if(dialogResult === true) {
        this.router.navigate([this.backRoute]);
      }
    });
  }
  else {
    this.router.navigate([this.backRoute]);

  }
}

  /**
   * Change the language of the application
   * @param lang The language to change to
   */
  changeLang(lang: string): void {
    this.translate.use(lang);
    this.currentLang = lang;
    localStorage.setItem('locale', lang);
    // window.location.reload();
  }

}
