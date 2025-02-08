import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {TranslateService} from '@ngx-translate/core';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'respond';

  constructor(public translate: TranslateService) {
    translate.addLangs(['de', 'en']);
    translate.use(translate.getDefaultLang());
  }
}
