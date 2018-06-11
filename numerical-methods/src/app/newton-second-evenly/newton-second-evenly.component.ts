import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {decodeCsv} from '../csv'
import * as Chart from "chart.js";


@Component({
  selector: 'app-newton-second-evenly',
  templateUrl: './newton-second-evenly.component.html',
  styleUrls: ['./newton-second-evenly.component.css']
})
export class NewtonSecondEvenlyComponent {
  ctx: CanvasRenderingContext2D;
  error?: string;

  inputFunction?: {x: number, y: number}[];

  point?: string;
  result?: string;


  constructor() { }

  opts = {
    elements:{
      line: {
        tension: 0
      }
    }
  };

  @ViewChild("myOwn") er: ElementRef;

  get inputDataset() {
    if(!this.inputFunction) return null;

    const othe = this.inputFunction.map(e=>({
      x: e.x + 5, y: e.y * 2
    }));

    const t = [...othe, ...this.inputFunction];
    console.log(t);

    return [
      {
        pointBackgroundColor: 'blue',
        label: 'Вхідна функція',
        data: this.inputFunction,
        showLine: true
      },
      {
        pointBackgroundColor: 'red',
        label: 'X2',
        data: t,
        showLine: false
      }
    ];
  }


  inputSelected(csv: String){
    this.result = null;
    this.error = null;
    this.inputFunction = null;

    try{
      this.inputFunction = decodeCsv(csv);
    } catch (e) {
      this.error = e.toString();
    }
  }

  calc(){
    this.result = '42';
  }

}
