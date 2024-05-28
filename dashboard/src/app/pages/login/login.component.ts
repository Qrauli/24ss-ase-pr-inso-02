import { Component } from '@angular/core';
import { MatCardModule} from '@angular/material/card'
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import {environment} from "../../../environments/environment";


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatToolbarModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  private dispatcherToken = environment.mockDispatcherToken;
  private calltakerToken = environment.mockCalltakerToken;

  constructor(
    private fb: FormBuilder,         // {3}
  ) {
    this.form = this.fb.group({     // {5}
      userName: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  form: FormGroup;                    // {1}

  /**
   * Login the user by completing OIDC flow
   */
  loginOauth() {
    const urlParams = new URLSearchParams(window.location.search);
    const redirectUri = urlParams.get('redirect_uri');
    const state = urlParams.get('state');

    if (this.form.value.userName === 'dispatcher') {
      window.location.href = `${redirectUri}#token_type=Bearer&id_token=${this.dispatcherToken}&access_token=${this.dispatcherToken}&state=${state}&expires_in=3600`;
    } else if (this.form.value.userName === 'calltaker') {
      window.location.href = `${redirectUri}#token_type=Bearer&id_token=${this.calltakerToken}&access_token=${this.calltakerToken}&state=${state}&expires_in=3600`;
    }
  }

}
