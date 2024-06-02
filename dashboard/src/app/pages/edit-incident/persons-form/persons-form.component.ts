import {Component, ViewChild} from "@angular/core";
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {HeaderComponent} from "../../../components/header/header.component";
import {MatButtonModule} from "@angular/material/button";
import {MatStepperModule} from "@angular/material/stepper";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {MatChipsModule} from "@angular/material/chips";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatListModule} from "@angular/material/list";
import {MatTabsModule} from "@angular/material/tabs";
import {MatIconModule} from "@angular/material/icon";
import {LeafletModule} from "@asymmetrik/ngx-leaflet";
import {MatRadioModule} from "@angular/material/radio";
import {MatMenuModule} from "@angular/material/menu";
import {MatTable, MatTableModule} from "@angular/material/table";
import {MatDividerModule} from "@angular/material/divider";
import {Patient, Sex} from "../../../dtos/incident";


@Component({
  selector: 'persons-form',
  standalone: true,
  imports: [HeaderComponent,
    MatButtonModule,
    MatStepperModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatChipsModule,
    MatSidenavModule,
    MatListModule,
    MatTabsModule,
    MatIconModule,
    LeafletModule,
    MatRadioModule,
    MatMenuModule,
    MatTableModule,
    MatDividerModule],
  templateUrl: './persons-form.component.html',
  styleUrl: '../edit-incident.component.css'
})
export class PersonsFormComponent {

  constructor(private formBuilder: FormBuilder) {
  }

  form = this.formBuilder.group({
    caller: this.formBuilder.group({
      number: [''],
      name: [''],
    }),
    numberOfPatients: ['', Validators.pattern("^[0-9]{0,2}$")],
  })

  // ####################### Patients ####################### //

  @ViewChild(MatTable) table: MatTable<any>;

  // TODO maybe we can also use dynamic forms for the patients


  patients: Patient[] = [];

  addPatient(): void {
    this.patients.push({age: 0, sex: Sex.UNKNOWN});
    this.form.controls.numberOfPatients.patchValue(this.patients.length as unknown as string)
    this.table.renderRows();
  }

  removePatient(patient: Patient): void {
    const index = this.patients.indexOf(patient);
    if (index !== undefined && index !== -1) {
      this.patients.splice(index, 1);
      this.form.controls.numberOfPatients.patchValue(this.patients.length as unknown as string)
      this.table.renderRows();
    }
  }

  numberInputOnly(e: KeyboardEvent) {
    // Allow only numeric input
    const allowedChars = /\d/;
    const key = e.key;

    // Allow special keys like backspace, delete, arrows, etc.
    const allowedSpecialKeys = ['Backspace', 'Delete', 'ArrowLeft', 'ArrowRight', 'Tab', 'Home', 'End'];

    if (!allowedChars.test(key) && !allowedSpecialKeys.includes(key)) {
      e.preventDefault();
    }
  }

  updatePatients() {
    while (this.patients.length < (this.form.value.numberOfPatients as unknown as number)) {
      this.patients.push({age: 0, sex: Sex.UNKNOWN});
    }
    while (this.patients.length > (this.form.value.numberOfPatients as unknown as number)) {
      this.patients.pop();
    }
    this.table.renderRows();
  }
}
