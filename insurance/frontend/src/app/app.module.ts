import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {RouterModule, Routes} from '@angular/router'
import {FormsModule} from "@angular/forms"

import { AppComponent } from './app.component';
import { SupporterConsultationComponent } from './supporter-consultation/supporter-consultation.component';
import { ClientConsultationComponent } from './client-consultation/client-consultation.component';
import {ClientConsultationService} from './client-consultation.service';
import {SupporterConsultationService} from './supporter-consultation.service';
import { MessagesComponent } from './messages/messages.component';

const appRoutes: Routes = [
  {path: "client", component: ClientConsultationComponent},
  {path: "support", component: SupporterConsultationComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    SupporterConsultationComponent,
    ClientConsultationComponent,
    MessagesComponent
  ],
  imports: [
    FormsModule,
    BrowserModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [
    ClientConsultationService,
    SupporterConsultationService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
