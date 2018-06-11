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

  inputDatasets: any;

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

  inputSelected(csv: String){
    this.result = null;
    this.error = null;
    this.inputFunction = null;
    this.inputDatasets = null;

    try{
      this.inputFunction = decodeCsv(csv);
      this.inputDatasets= [
        {
          pointBackgroundColor: 'blue',
          label: 'Вхідна функція',
          data: this.inputFunction,
        }];
    } catch (e) {
      this.error = e.toString();
    }
  }

  calc(){
    this.result = '42';
    /*
    const ctx = this.er.nativeElement.getContext('2d');


    let x = new Chart(ctx, {
      type: 'scatter',
      data: {
        datasets: [{
          label: 'Input function',
          data: this.inputFunction
        }, {
          label: 'Line',
          data: [...this.inputFunction],
          type: 'line'
        }],
        labels: this.inputFunction.map(e=>e.x)
      },
      options: {
        responsive: true,
        onClick: (event1, activeElements) => {
          console.log(event1);
          console.log(x.getElementAtEvent(event1));
        }
      }
    })
    */

    /*
    const mixedChart = new Chart(ctx, {
      type: 'bar',
      data: {
        datasets: [{
          label: 'Bar Dataset',
          data: [10, 20, 30, 40]
        }, {
          label: 'Line Dataset',
          data: [50, 50, 50, 50],

          // Changes this dataset to become a line
          type: 'line'
        }],
        labels: ['January', 'February', 'March', 'April']
      },
      options: {}
    });
    */

    /*
    new Chart(this.er.nativeElement.getContext('2d'), {
      type: 'bar',
      data: {
        labels: ["Red", "Blue", "Yellow", "Green", "Purple", "Orange"],
        datasets: [{
          label: '# of Votes',
          data: [12, 19, 3, 5, 2, 3]
        }]
      },
      options: {
      }
    });
    */
  }

}
