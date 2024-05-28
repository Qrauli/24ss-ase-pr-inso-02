import {AfterViewInit, Component} from "@angular/core";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
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
import {MatTableModule} from "@angular/material/table";
import {MatDividerModule} from "@angular/material/divider";


@Component({
  selector: 'questions-form',
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
  templateUrl: './questions-form.component.html',
  styleUrl: '../add-incident.component.css'
})
export class QuestionsFormComponent implements AfterViewInit {

  constructor() {
  }

  recommendation = 'C1C0';
  questionaryId = '';

  ngAfterViewInit(): void {
    // TODO service interaction (set recommendation and questionaryId)
  }

  // ####################### Questions ####################### //

  selectedIndex = 0;

  changeIndex(index: number): void {
    this.selectedIndex = index;
  }
}
