import { Component, OnInit } from '@angular/core';
import {trapetion_simple} from "../trapetion";
import {f, l, h} from "../f";

@Component({
  selector: 'app-trapezoid',
  templateUrl: './trapezoid.component.html',
  styleUrls: ['./trapezoid.component.css']
})
export class TrapezoidComponent implements OnInit {
  h: string = "0.0001";
  result?: number = null;

  constructor() { }

  calc(){
    let dist = parseFloat(this.h);
    this.result = trapetion_simple(f, l, h, dist);
  }

  ngOnInit() {
  }

}
