import { NgModule } from '@angular/core';

import {
  MatButtonModule,
  MatIconModule,
  MatListModule,
  MatSidenavModule,
  MatTableModule,
  MatToolbarModule
} from "@angular/material";

const modules = [
  MatButtonModule, MatSidenavModule, MatToolbarModule, MatIconModule, MatListModule, MatTableModule
];

@NgModule({
  imports: [...modules],
  exports: [...modules]
})
export class MyMaterialModule { }
