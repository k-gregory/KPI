import {Component} from '@angular/core';
import {decodeCsv, getControls} from '../csv'
import {newton_second_evenly} from "../newton";
import {ChartDataSets, ChartOptions} from "chart.js";


@Component({
  selector: 'app-newton-second-evenly',
  templateUrl: './newton-second-evenly.component.html',
  styleUrls: ['./newton-second-evenly.component.css']
})
export class NewtonSecondEvenlyComponent {
  error?: string;

  inputFunction?: {x: number, y: number}[];
  controlFunction: {x: number, y: number}[];

  selectedPoint: {x: number, y: number};

  point?: string;
  result?: number;

  colors: any;
  chartDss? : ChartDataSets[] = null;


  constructor() { }

  options : ChartOptions = {
    tooltips: {
      enabled: true
    }
  };

  updateDss(){
    const base : ChartDataSets = {
      label: 'Вхідна функція',
      data: this.inputFunction,
      showLine: true
    };

    const cls = [{
      pointBackgroundColor: 'blue'
    }];
    const res: ChartDataSets[] = [base];

    if(this.controlFunction){
      res.push({
        label: 'Контрольні точки',
        data: this.controlFunction,
      });
      cls.push({
        pointBackgroundColor: 'rgba(255, 0, 0)'
      });
    }

    if(this.selectedPoint){
      console.log("x");
      res.push({
        label: 'Обрана точка',
        data: [this.selectedPoint]
      });
      cls.push({
        pointBackgroundColor: 'rgba(0, 255, 0)'
      });
    }


    this.chartDss = null;
    this.colors = null;
    setTimeout(()=>{
      this.chartDss = res;
      this.colors = cls;
    }, 1)
  }


  inputSelected(csv: string){
    this.result = null;
    this.error = null;
    this.inputFunction = null;

    try{
      this.inputFunction = decodeCsv(csv);
      this.updateDss();
    } catch (e) {
      this.error = e.toString();
    }
  }

  controlSelected(csv: string){
    this.controlFunction = getControls(csv).map(x=>({
      x, y: newton_second_evenly(this.inputFunction, x)
    }));
    console.log(this.controlFunction);
    this.updateDss();
  }

  calc(){
    const x = parseFloat(this.point);
    if(isNaN(x)){
      this.error="Can't parse input point";
      this.selectedPoint = null;
    } else {
      this.error = null;
      this.selectedPoint = {
        x,
        y:newton_second_evenly(this.inputFunction, x),
      };
      this.result = this.selectedPoint.y;
      this.updateDss();
    }
  }

}
