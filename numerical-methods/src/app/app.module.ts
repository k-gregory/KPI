import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MyMaterialModule} from "./my-material/my-material.module";
import { InterpolationComponent } from './interpolation/interpolation.component';
import { IntegrationComponent } from './integration/integration.component';
import {RouterModule, Routes} from "@angular/router";
import { NewtonSecondEvenlyComponent } from './newton-second-evenly/newton-second-evenly.component';
import { NewtonFirstComponent } from './newton-first/newton-first.component';
import { TrapezoidComponent } from './trapezoid/trapezoid.component';
import { RombergComponent } from './romberg/romberg.component';
import {routes} from "./routes";
import { FileInputComponent } from './file-input/file-input.component';
import {ChartsModule} from "ng2-charts";
import {FormsModule} from "@angular/forms";

@NgModule({
  declarations: [
    AppComponent,
    InterpolationComponent,
    IntegrationComponent,
    NewtonSecondEvenlyComponent,
    NewtonFirstComponent,
    TrapezoidComponent,
    RombergComponent,
    FileInputComponent,
  ],
  imports: [
    RouterModule.forRoot(routes, {
      enableTracing: false
    }),
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    MyMaterialModule,
    ChartsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
